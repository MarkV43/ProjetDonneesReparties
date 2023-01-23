package etape3;

import java.awt.*;
import java.awt.event.*;

public class Irc extends Frame {
	public TextArea text;
	public TextField data;
	Sentence_itf sentence;
	SharedObject reference;
	static String myName;

	public static void main(String[] argv) {

		if (argv.length != 1) {
			System.out.println("java Irc <name>");
			return;
		}
		myName = argv[0];

		// initialize the system
		Client.init();

		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		Sentence_itf o1 = (Sentence_itf) Client.lookup("IRC-1", Sentence_stub.class);
		if (o1 == null) {
			o1 = (Sentence_itf) Client.create(new Sentence(), Sentence_stub.class);
			Client.register("IRC-1", o1);
		}

		SharedObject o2 = Client.lookup("IRC-2", SharedObject.class);
		if (o2 == null) {
			o2 = (SharedObject) Client.create(o1, SharedObject.class);
			Client.register("IRC-2", o2);
		}

		// create the graphical part
		new Irc(o1, o2);
	}

	public Irc(Sentence_itf s, SharedObject ref) {

		setLayout(new FlowLayout());

		text = new TextArea(10, 60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);

		data = new TextField(60);
		add(data);

		Button write_button = new Button("write");
		write_button.addActionListener(new writeListener(this));
		add(write_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListener(this));
		add(read_button);

		setSize(470, 300);
		text.setBackground(Color.black);
		setVisible(true);

		sentence = s;
		reference = ref;
	}
}

class readListener implements ActionListener {
	Irc irc;

	public readListener(Irc i) {
		irc = i;
	}

	public void actionPerformed(ActionEvent e) {

		// lock the object in read mode
		irc.reference.lock_read();

		Sentence_itf sentence = (Sentence_itf) irc.reference.obj;
		sentence.lock_read();

		// invoke the method
		String s = sentence.read();

		sentence.unlock();

		// unlock the object
		irc.reference.unlock();

		// display the read value
		irc.text.append(s + "\n");
	}
}

class writeListener implements ActionListener {
	Irc irc;

	public writeListener(Irc i) {
		irc = i;
	}

	public void actionPerformed(ActionEvent e) {

		// get the value to be written from the buffer
		String s = irc.data.getText();

		irc.reference.lock_read();

		Sentence_itf sentence = (Sentence_itf) irc.reference.obj;
		sentence.lock_write();

		// invoke the method
		sentence.write(Irc.myName + " wrote " + s);
		irc.data.setText("");

		sentence.unlock();

		// unlock the object
		irc.reference.unlock();
	}
}
