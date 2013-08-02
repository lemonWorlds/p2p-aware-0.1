package interfaces;

import java.io.InputStream;

public interface InputStreamHandler extends Runnable {
	void setInputStream(InputStream in);
}
