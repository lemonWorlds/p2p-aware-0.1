package interfaces;

import java.io.OutputStream;

public interface OutputStreamHandler extends Runnable {
	void setOutputStream(OutputStream out);
}
