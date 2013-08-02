package main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import interfaces.EventHandler;
import interfaces.OutputStreamHandler;

public class SimpleOutputStreamHandlerImpl implements OutputStreamHandler {
	private EventHandler eventHandler;
	private BlockingQueue<String> sharedQueue;
	private DataOutputStream stream = null;
	
	public SimpleOutputStreamHandlerImpl(EventHandler eventHandler, BlockingQueue<String> sharedQueue) {
		this.eventHandler = eventHandler;
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		if (stream == null) {
			Thread.currentThread().interrupt();
			System.out.println("OutputStream was not set!");
		}
		while (true) {
			try {
				Model event = getModelFromString(sharedQueue.take());
				List<String> toSend = eventHandler.handleEvent(event);
				sendList(toSend);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public void setOutputStream(OutputStream out) {
		stream = new DataOutputStream(out);
	}

	private Model getModelFromString(String data) {
		Model model = ModelFactory.createDefaultModel();
		return model.read(new StringReader(data),null);
	}
	
	private void sendList(List<String> list) {
		try {
			for (int i = 0; i < list.size();i++) {
				byte[] arr = list.get(i).getBytes();
				stream.writeInt(arr.length);
				stream.write(arr,0,arr.length);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println("List not sent!");
		}
	}
}
