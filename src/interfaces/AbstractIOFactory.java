package interfaces;

import java.util.concurrent.BlockingQueue;

public interface AbstractIOFactory {
	BlockingQueue<String> createBlockingQueue();
	OutputStreamHandler createOutputStreamHandler(EventHandler eventHandler, BlockingQueue<String> sharedQueue);
	InputStreamHandler createInputStreamHandler(BlockingQueue<String> sharedQueue);
}
