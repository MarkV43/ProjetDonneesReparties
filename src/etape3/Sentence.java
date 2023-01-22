package etape3;

public class Sentence implements java.io.Serializable {
	String data;

	public Sentence() {
		data = "";
	}

	public Sentence(String txt) {
		data = txt;
	}

	public void write(String text) {
		data = text;
	}

	public String read() {
		return data;
	}

}