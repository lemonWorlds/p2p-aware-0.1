package properPeer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;


import properPeerInt.PeerInHandler;

public class SimplePeerInHandler implements PeerInHandler {
	private BlockingQueue<String> sharedQueue;
	private DataInputStream stream;
	public SimplePeerInHandler(BlockingQueue<String> sharedQueue, InputStream in) {
		this.sharedQueue = sharedQueue;
		stream = new DataInputStream(in);
	}

	@Override
	public void run() {
		while (true) {
			try {
				sharedQueue.add(getInputData(stream));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String getInputData(DataInputStream stream) throws IOException {		
		int size = stream.readInt();
		byte[] data = new byte[size];
		stream.read(data,0,size);
		return new String(data);
	}

}
