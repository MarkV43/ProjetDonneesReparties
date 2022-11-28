package etape1;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	public Client() throws RemoteException {
		super();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
	}
	
	// lookup in the name server
	public static SharedObject lookup(String name) {
		return null;
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		// Demander au serveur la création de l'objet
		// Retrouver cet objet
		return null;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
		return null;
	}

	// request a write lock from the server
	public static Object lock_write (int id) {
		return null;
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		return null;
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		return null;
	}
}
