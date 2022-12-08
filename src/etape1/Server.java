package etape1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server extends UnicastRemoteObject implements Server_itf {

    private HashMap<String, Integer> ids = new HashMap<>();
    private ArrayList<Object> objects = new ArrayList<>();
    private ArrayList<Integer> locks;
    private HashSet<Client_itf> connections;
    /*
     * 0 NL
     * 1 RL
     * 2 WL
     */

    protected Server() throws RemoteException {
        super();
    }

    @Override
    public int lookup(String name) throws RemoteException {
        return ids.get(name);
    }

    @Override
    public void register(String name, int id) throws RemoteException {
        ids.put(name, id);
    }

    @Override
    public int create(Object o) throws RemoteException {
        int id = objects.size();
        objects.add(o);
        return id;
    }

    @Override
    public Object lock_read(int id, Client_itf client) throws RemoteException {
        if (locks.get(id) == 2) { // WL
            connections.forEach((c) -> {
                try {
                    if (!c.equals(client))
                        c.reduce_lock(id);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        connections.add(client);

        locks.set(id, 1); // RL
        return objects.get(id);
    }

    @Override
    public Object lock_write(int id, Client_itf client) throws RemoteException {
        switch (locks.get(id)) {
            case 1: // RL
                connections.forEach((c) -> {
                    try {
                        if (!c.equals(client))
                            c.invalidate_reader(id);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case 2: // WL
                connections.forEach((c) -> {
                    try {
                        if (!c.equals(client))
                            c.invalidate_writer(id);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
        }

        connections.add(client);

        locks.set(id, 2);
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
