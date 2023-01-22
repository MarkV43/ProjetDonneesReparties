package etape3;

public interface SharedObject_itf {
	void lock_read();

	void lock_write();

	void unlock();

	int getId();
}