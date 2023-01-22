package etape3;

import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {

	public Object obj;
	public int id;
	public int lock;
	/*
	 * 0 NL
	 * 1 RLC
	 * 2 WLC
	 * 3 RLT
	 * 4 WLT
	 * 5 RLT_WLC
	 */

	 private static String[] LOCK_NAMES = {
		"NL",
		"RLC",
		"WLC",
		"RLT",
		"WLT",
		"RLT_WLC"
	 };

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
		var tmp = lock;
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

		System.out.println("lock_read\n" + LOCK_NAMES[tmp] + " -> " + LOCK_NAMES[lock] + "\n");
	}

	// invoked by the user program on the client node
	public void lock_write() {
		var tmp = lock;
		switch (lock) {
			case 0: // NL
			case 1: // RLC
				obj = Client.lock_write(id);
			case 2: // WLC
				lock = 4; // WLT
				break;
		}
		System.out.println("lock_write\n" + LOCK_NAMES[tmp] + " -> " + LOCK_NAMES[lock] + "\n");
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		var tmp = lock;
		switch (lock) {
			case 3: // RLT
				lock = 1; // RLC
				break;
			case 4: // WLT
			case 5: // RLT_WLC
				lock = 2; // WLC
				break;
		}
		System.out.println("unlock\n" + LOCK_NAMES[tmp] + " -> " + LOCK_NAMES[lock] + "\n");
	}

	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		var tmp = lock;
		switch (lock) {
			case 2: // WLC
			case 4: // WLT
				lock = 1; // RLC
				break;
			case 5: // RLT_WLC
				lock = 3; // RLT
		}

		System.out.println("reduce_lock\n" + LOCK_NAMES[tmp] + " -> " + LOCK_NAMES[lock] + "\n");

		return obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		var tmp = lock;
		switch (lock) {
			case 1: // RLC
			case 3: // RLT
				lock = 0; // NL
				break;
		}
		System.out.println("invalidate_reader\n" + LOCK_NAMES[tmp] + " -> " + LOCK_NAMES[lock] + "\n");
	}

	public synchronized Object invalidate_writer() {
		var tmp = lock;
		switch (lock) {
			case 2: // WLC
			case 4: // WLT
			case 5: // RLT_WLC
				lock = 0; // NL
				break;
		}

		System.out.println("invalidate_writer\n" + LOCK_NAMES[tmp] + " -> " + LOCK_NAMES[lock] + "\n");

		return obj;
	}

	public int getId() {
		return id;
	}
}
