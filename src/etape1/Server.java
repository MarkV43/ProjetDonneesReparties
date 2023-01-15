package etape1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Server extends UnicastRemoteObject implements Server_itf {

    private HashMap<String, Integer> ids = new HashMap<>();
    private ArrayList<Object> objects = new ArrayList<>();
    // private HashMap<Integer> locks = new HashMap<>();
    private HashMap<Client_itf, ArrayList<Integer>> connections = new HashMap<>();
    /*
     * This is our new lock
     * 0 NL
     * 1 RL
     * 2 WL
     */
    private static String[] LOCK_NAMES = {
        "NL",
        "RL",
        "WL",
    };

    protected Server() throws RemoteException {
        super();
    }

    @Override
    public int lookup(String name) throws RemoteException {
        Integer val = ids.get(name);
        if (val == null) {
            return -1;
        }
        return val;
    }

    @Override
    public void register(String name, int id) throws RemoteException {
        ids.put(name, id);
    }

    @Override
    public int create(Object o) throws RemoteException {
        int id = objects.size();
        objects.add(o);
        for (var list : connections.values()) {
            list.add(0); // NL
        }
        return id;
    }

    // TODO: Remove this
    private void showStates(int id) {
        var list = new ArrayList<>(connections.keySet());

        list.sort((a, b) -> {
            try {
                return a.getName().compareTo(b.getName());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        list.forEach((x) -> {
            try {
                var name = x.getName();
                var lock = connections.get(x).get(id);
                var lock_name = LOCK_NAMES[lock];

                System.out.println(name + " = " + lock_name);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Object lock_read(int id, Client_itf client) throws RemoteException {
        connections.forEach((conn, locks) -> {
            if (conn.equals(client))
                return;

            try {
                switch (locks.get(id)) {
                    case 1: // RL
                        // do nothing
                        break;
                    case 2: // WL
                        var obj = conn.reduce_lock(id);

                        if (obj != null) {
                            objects.set(id, obj);
                        }

                        locks.set(id, 1); // RL

                        break;
                    default:
                        break;
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        if (!connections.containsKey(client)) {
            int size = objects.size();
            ArrayList<Integer> locks = new ArrayList<>(Collections.nCopies(size, 0));
            locks.set(id, 1); // RL
            connections.put(client, locks);
        } else {
            connections.get(client).set(id, 1);
        }

        showStates(id);

        return objects.get(id);
    }

    @Override
    public Object lock_write(int id, Client_itf client) throws RemoteException {
        connections.forEach((conn, locks) -> {
            if (conn.equals(client))
                return;

            try {
                switch (locks.get(id)) {
                    case 1: // RL
                        conn.invalidate_reader(id);
                        break;
                    case 2: // WL
                        var obj = conn.invalidate_writer(id);

                        if (obj != null) {
                            objects.set(id, obj);
                        }

                        break;
                }

                locks.set(id, 0); // NL
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        if (!connections.containsKey(client)) {
            int size = objects.size();
            ArrayList<Integer> locks = new ArrayList<>(Collections.nCopies(size, 0));
            locks.set(id, 2); // RL
            connections.put(client, locks);
        } else {
            connections.get(client).set(id, 2);
        }

        showStates(id);

        return objects.get(id);
    }

    public static void main(String[] args) {
        try {
            if (args.length != 2 && args.length != 3) {
                throw new IllegalArgumentException("Usage : java Server <name> <port> [<host>]");
            }
            String name = args[0];
            int port = Integer.parseInt(args[1]);
            String host;
            if (args.length == 3) {
                host = args[2];
            } else {
                host = "//localhost";
            }

            Server server = new Server();

            Registry reg = LocateRegistry.createRegistry(port);

            var URL = host + ':' + port + '/' + name;

            Naming.rebind(URL, server);

            System.out.println("Le serveur est prêt...\nNom : " + name + "\nPort : " + port + "\nHost : " + host);
        } catch (RemoteException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
