package etape1;

import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {

	public Object obj;

	public SharedObject(Object o) {
		obj = o;
	}
	
	// invoked by the user program on the client node
	public void lock_read() {
	}

	// invoked by the user program on the client node
	public void lock_write() {
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		return null;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
	}

	public synchronized Object invalidate_writer() {
		return  null;
	}
}
