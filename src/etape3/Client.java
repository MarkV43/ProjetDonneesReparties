package etape3;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Client extends UnicastRemoteObject implements Client_itf {

	private static Server_itf server;
	private static Client_itf client;

	public static String name;

	private static HashMap<Integer, SharedObject> objects = new HashMap<>();

	public Client() throws RemoteException {
		super();
	}

	

	///////////////////////////////////////////////////
	// Interface to be used by applications
	///////////////////////////////////////////////////

	public static boolean hasSingleton() {
		return client != null;
	}

	@Override
    public String getName() throws RemoteException {
        return Irc.myName;
    }



    // initialization of the client layer
	public static void init() {
		try {
			server = (Server_itf) Naming.lookup("//localhost:8080/Server");
			if (client == null) {
				client = new Client();
			}
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			throw new RuntimeException(e);
		}
	}

	// lookup in the name server
	public static <T extends SharedObject> T lookup(String name, Class<T> class1) {
		try {
			int id = server.lookup(name);

			if (id == -1) {
				return null;
			}

			return lookupId(id, class1);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T extends SharedObject> T lookupId(int id, Class<T> class1) {
		try {
			T so = class1.getDeclaredConstructor().newInstance(id);

			System.out.println(so.getId());

			objects.put(id, so);

			return so;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		try {

			server.register(name, so.getId());

		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	// creation of a shared object
	public static <T extends SharedObject> T create(Object o, Class<T> class1) {
		// Demander au serveur la création de l'objet
		// Retrouver cet objet
		try {

			int id = server.create(o);

			var obj = class1.getDeclaredConstructor().newInstance(id);

			objects.put(id, obj);

			return obj;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static SharedObject findId(int id) {
		return objects.get(id);
	}

	public static SharedObject createStub(Object obj, int id) {
		SharedObject so = null;

		try {
			Class<?> class1 = Class.forName(obj.getClass().getName() + "_stub");

			java.lang.reflect.Constructor<?> constructor = 
				class1.getConstructor(new Class[] { int.class, Object.class });

			so = (SharedObject) constructor.newInstance(new Object[] { id, obj });

			objects.put(so.getId(), so);
		} catch (Exception e) {
			new RuntimeException(e);
		}

		return so;
	}

	/////////////////////////////////////////////////////////////
	// Interface to be used by the consistency protocol
	////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
		try {
			return server.lock_read(id, client);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	// request a write lock from the server
	public static Object lock_write(int id) {
		try {
			return server.lock_write(id, client);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		SharedObject obj = Client.objects.get(id);

		System.out.println(id + " : " + Client.objects.containsKey(id));

		return obj.reduce_lock();
	}

	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {		
		SharedObject obj = Client.objects.get(id);

		obj.invalidate_reader();
	}

	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		SharedObject obj = Client.objects.get(id);

		return obj.invalidate_writer();
	}
}
