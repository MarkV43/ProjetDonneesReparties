package etape2;

import etape1.SharedObject_itf;

public interface Sentence_itf extends SharedObject_itf {
	void write(String text);
	String read();

	void lock_read();
}