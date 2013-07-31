package main;

import java.util.concurrent.BlockingQueue;

import interfaces.EventHandler;
import interfaces.OutputStreamHandler;

public class SimpleOutputStreamHandlerImpl implements OutputStreamHandler {
	
	public SimpleOutputStreamHandlerImpl(EventHandler eventHandler, BlockingQueue<String> sharedQueue) {
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
