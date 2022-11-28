package etape1;

public class Sentence implements java.io.Serializable {
	String 		data;
	public Sentence() {
		data = "";
	}
	
	public void write(String text) {
		data = text;
	}
	public String read() {
		return data;	
	}
	
}