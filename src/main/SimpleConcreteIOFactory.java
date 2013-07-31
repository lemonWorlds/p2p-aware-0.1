package main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import interfaces.AbstractIOFactory;
import interfaces.EventHandler;
import interfaces.InputStreamHandler;
import interfaces.OutputStreamHandler;

public class SimpleConcreteIOFactory implements AbstractIOFactory {

	@Override
	public BlockingQueue<String> createBlockingQueue() {
		return new LinkedBlockingQueue<String>();
	}

	@Override
	public OutputStreamHandler createOutputStreamHandler(EventHandler eventHandler, BlockingQueue<String> sharedQueue) {
		return new SimpleOutputStreamHandlerImpl(eventHandler, sharedQueue);
	}

	@Override
	public InputStreamHandler createInputStreamHandler(BlockingQueue<String> sharedQueue) {
		return new SimpleInputStreamHandlerImpl(sharedQueue);
	}

}
