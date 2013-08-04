package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import interfaces.AbstractIOFactory;
import interfaces.EventHandler;
import interfaces.InputStreamHandler;
import interfaces.OutputStreamHandler;
import interfaces.Server;

public class SimpleServerImpl implements Server {
	private EventHandler eventHandler;
	private AbstractIOFactory factory;
	private int port;
	private ServerSocket ss;
	
	public SimpleServerImpl(EventHandler eventHandler, AbstractIOFactory factory, int port) {
		this.eventHandler = eventHandler;
		this.factory = factory;
		this.port = port;
	}

	@Override
	public void listen() throws IOException {
		ss = new ServerSocket(port);
		while (true) {
			try {
				System.out.println("Waiting for a new peer connection...");
				Socket socket = ss.accept();
				System.out.println("New peer connection accepted from " + socket.getInetAddress().toString());
				BlockingQueue<String> sharedQueue = factory.createBlockingQueue();
				OutputStreamHandler out = factory.createOutputStreamHandler(eventHandler, sharedQueue);
				InputStreamHandler in = factory.createInputStreamHandler(sharedQueue);
				out.setOutputStream(socket.getOutputStream());
				in.setInputStream(socket.getInputStream());
				new Thread(in).start();
				new Thread(out).start();
			} catch (IOException ex) {
				System.out.println("An incoming peer connection failed.");
			}
		}
	}

	@Override
	public void stopListening() throws IOException {
		ss.close();
	}

}
