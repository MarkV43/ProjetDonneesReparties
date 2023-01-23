package etape2;

public class Sentence_stub extends SharedObject implements Sentence_itf, java.io.Serializable {
	public Sentence_stub (int id, Object obj) {
		super(id, obj);
	}

	public void write(java.lang.String arg0) {
		Sentence o = (Sentence) obj;
		o.write(arg0);
	}

	public java.lang.String read() {
		Sentence o = (Sentence) obj;
		return o.read();
	}
}