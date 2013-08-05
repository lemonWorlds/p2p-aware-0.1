package properPeer;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import properPeerInt.ActionHandler;
import properPeerInt.PeerEventHandler;
import properPeerInt.PeerInHandler;
import properPeerInt.PeerOutHandler;

public class PeerRunner {

	public static void main(String[] args) throws Exception {
		new PeerRunner().launch("127.0.0.1",3000);
	}

	private void launch(String ip, int port) throws Exception {
		BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<String>();
		Socket socket = new Socket(ip,port);
		ActionHandler actionHandler = new SimpleActionHandler(sharedQueue);
		PeerInHandler peerIn = new SimplePeerInHandler(sharedQueue,socket.getInputStream());
		PeerOutHandler peerOut = new SimplePeerOutHandler(socket.getOutputStream());
		PeerEventHandler peerEventHandler = new SimplePeerEventHandler(peerOut);
		new Thread(actionHandler).start();
		new Thread(peerIn).start();
		peerEventHandler.start();
	}

}
