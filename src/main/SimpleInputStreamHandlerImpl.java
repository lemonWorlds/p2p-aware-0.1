package main;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import interfaces.InputStreamHandler;

public class SimpleInputStreamHandlerImpl implements InputStreamHandler {
	private DataInputStream stream = null;
	private BlockingQueue<String> sharedQueue;
	
	public SimpleInputStreamHandlerImpl(BlockingQueue<String> sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		if (stream == null) {
			Thread.currentThread().interrupt();
			System.out.println("InputStream was not set!");
		}
		try {
			while (true) {
				sharedQueue.add(getInputData(stream));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void setInputStream(InputStream in) {
		stream = new DataInputStream(in);
	}
	
	private String getInputData(DataInputStream stream) throws IOException {		
		int size = stream.readInt();
		byte[] data = new byte[size];
		stream.read(data,0,size);
		return new String(data);
	}

}
