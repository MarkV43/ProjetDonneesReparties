package etape3;

public interface Sentence_itf extends SharedObject_itf {
	void write(String text);
	String read();

	void lock_read();
}