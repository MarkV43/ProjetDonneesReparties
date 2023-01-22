package etape3;

public interface Server_itf extends java.rmi.Remote {
	int lookup(String name) throws java.rmi.RemoteException;

	void register(String name, int id) throws java.rmi.RemoteException;

	int create(Object o) throws java.rmi.RemoteException;

	Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException;

	Object lock_write(int id, Client_itf client) throws java.rmi.RemoteException;
}
