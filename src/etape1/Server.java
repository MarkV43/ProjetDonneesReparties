package etape1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements Server_itf {


    protected Server() throws RemoteException {
        super();
    }

    @Override
    public int lookup(String name) throws RemoteException {
        return -1;
    }

    @Override
    public void register(String name, int id) throws RemoteException {

    }

    @Override
    public int create(Object o) throws RemoteException {
        return 0;
    }

    @Override
    public Object lock_read(int id, Client_itf client) throws RemoteException {
        return null;
    }

    @Override
    public Object lock_write(int id, Client_itf client) throws RemoteException {
        return null;
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

            var URL = host +  ':' + port + '/' + name;

            Naming.rebind(URL, server);

            System.out.println("Le serveur est prêt...\nNom : " + name + "\nPort : " + port + "\nHost : " + host);
        } catch (RemoteException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
