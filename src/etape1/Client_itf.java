package etape1;

public interface Client_itf extends java.rmi.Remote {
	Object reduce_lock(int id) throws java.rmi.RemoteException;
	void invalidate_reader(int id) throws java.rmi.RemoteException;
	Object invalidate_writer(int id) throws java.rmi.RemoteException;
}
