package properPeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import main.SchemaFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import properPeerInt.PeerEventHandler;

import properPeerInt.PeerOutHandler;

public class SimplePeerEventHandler implements PeerEventHandler {
	private List<String> documentList = new ArrayList<String>();
	private PeerOutHandler peerOut;
	private Model schema = SchemaFactory.createSchema("H:\\newWorkspace\\protoNetwork\\src\\stuff\\schema.rdf");
	
	public SimplePeerEventHandler(PeerOutHandler peerOut) {
		this.peerOut = peerOut;
	}
	
	@Override
	public void start() {
		userIntro();
		while (true) {
			peerActionReader();
		}
	}

	private void userIntro() {
		System.out.println("Welcome to the PeerAware network!");
		System.out.println("To begin, please enter your current documents: (Enter 0 when you have finished)");
		String userInput = null;
		BufferedReader bufferedReader = null;
		while (true) {
			bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			try {
				userInput = bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (userInput.equals("0")) {
				break;
			} 
			documentList.add(userInput);
		}/*
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		System.out.println("Document entry is complete. You may now enter the network.");
	}
	
	private void peerActionReader() {
		System.out.println("Which document would you like to update (enter the appropriate integer): ");
		for (int i = 1; i <= documentList.size(); i++) {
			System.out.println("(" + i + ")" + " " + documentList.get(i-1));
		}
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		Integer userInput = null;
		try {
			userInput = Integer.parseInt(bufferedReader.readLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}/*
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		postEventModel(documentList.get(userInput));
	}

	private void postEventModel(String documentURI) {
		Model event = ModelFactory.createDefaultModel();
		Resource eventURI = event.createResource("http://www.peer.org/event1");
		eventURI.addProperty(RDF.type, schema.getResource("http://www.model.org/update"));
		Resource me = event.createResource("http://www.me.org/me");
		eventURI.addProperty(schema.getProperty("http://www.model.org/occurredAt"),me);
		Resource doc = event.createResource(documentURI);
		eventURI.addProperty(schema.getProperty("http://www.model.org/appliedTo"),doc);
		peerOut.post(event);
	}

}
