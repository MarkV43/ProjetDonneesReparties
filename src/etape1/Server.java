package etape1;

import java.rmi.RemoteException;

public class Server implements Server_itf {
    @Override
    public int lookup(String name) throws RemoteException {

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
}
