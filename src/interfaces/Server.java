package interfaces;

import java.io.IOException;

public interface Server {
	void listen() throws IOException;
	void stopListening() throws IOException;
}
