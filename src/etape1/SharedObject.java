package etape1;

import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {

	public Object obj;
	public int id;
	private int lock;
	/*
	 * 0 NL
	 * 1 RLC
	 * 2 WLC
	 * 3 RLT
	 * 4 WLT
	 * 5 RLT_WLC
	 */

	public SharedObject(int id) {
		this.id = id;
		obj = null;
		lock = 0;
	}

	public SharedObject(Object o) {
		obj = o;
		id = -1;
		lock = 0;
	}

	// invoked by the user program on the client node
	public void lock_read() {
		switch (lock) {
			case 0: // NL
				obj = Client.lock_read(id);
			case 1: // RLC
				lock = 3; // RLT
				break;
			case 2: // WLC
				lock = 5; // RLT_WLC
				break;
		}
	}

	// invoked by the user program on the client node
	public void lock_write() {
		switch (lock) {
			case 0: // NL
			case 1: // RLC
				obj = Client.lock_write(id);
			case 2: // WLC
				lock = 4; // WLT
				break;
		}
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch (lock) {
			case 3: // RLT
				lock = 1; // RLC
				break;
			case 4: // WLT
				lock = 2; // WLC
				break;
		}
	}

	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		switch (lock) {
			case 4: // WLT
				lock = 1; // RLC
				break;
			case 5: // RLT_WLC
				lock = 3; // RLT
		}

		return null;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		switch (lock) {
			case 1: // RLC
			case 3: // RLT
				lock = 0; // NL
				break;
		}
	}

	public synchronized Object invalidate_writer() {
		switch (lock) {
			case 2: // WLC
			case 4: // WLT
			case 5: // RLT_WLC
				lock = 0; // NL
				break;
		}

		return null;
	}

	public int getId() {
		return id;
	}
}
