package etape3;

public class Sentence_stub extends SharedObject implements Sentence_itf {

	public Sentence_stub(int id) {
		super(id);
	}

	public Sentence_stub(Object o) {
		super(o);
	}

	public void write(String text) {
		Sentence s = (Sentence) obj;
		s.write(text);
	}

	public String read() {
		Sentence s = (Sentence) obj;
		return s.read();
	}

}