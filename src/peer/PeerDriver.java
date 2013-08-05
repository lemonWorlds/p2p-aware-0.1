package peer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import main.SchemaFactory;

public class PeerDriver {
	private Model schema = SchemaFactory.createSchema("C:\\Users\\Mum\\git\\protoNetwork\\src\\stuff\\schema.rdf");
	private Model updateEv;
	private Model createEv;
	private Model deleteEv;
	private Model documentEv;
	private Model evEv;
	private Model memEv;
	private Model joinEv;
	private Model leaveEv;

	public static void main(String[] args) throws Exception {
		new PeerDriver().launch();
	}

	private void launch() throws Exception {
		initEvents();
		Socket sock = new Socket("127.0.0.1",6000);
		ClientInputHandler inHandler = new ClientInputHandler(sock.getInputStream());
		DataOutputStream dout = new DataOutputStream(sock.getOutputStream());
		new Thread(inHandler).start();
		for (int i = 0; i<1; i++) {
			//writeBytesToStream(getBytesFromModel(createEv), dout);
			writeBytesToStream(getBytesFromModel(updateEv), dout);
			//writeBytesToStream(getBytesFromModel(memEv),dout);
			Thread.sleep(i*1000);
		}
		while (true) {
			
		}
	}
	
	private byte[] getBytesFromModel(Model model) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		model.write(baos, "RDF/XML");
		return baos.toByteArray();
	}
	
	private void writeBytesToStream(byte[] arr, DataOutputStream dout) {
		try {
			dout.writeInt(arr.length);
			dout.write(arr,0,arr.length);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void initEvents() {
		Resource eventClass = schema.getResource("http://www.model.org/event");
		Resource memberEventClass = schema.getResource("http://www.model.org/memberEvent");
		Resource updateClass = schema.getResource("http://www.model.org/update");
		Resource documentEventClass = schema.getResource("http://www.model.org/documentEvent");
		Resource joinEvent = schema.getResource("http://www.model.org/join");
		Resource leaveEvent = schema.getResource("http://www.model.org/leave");
		Resource create = schema.getResource("http://www.model.org/create");
		Resource delete = schema.getResource("http://www.model.org/delete");
		
		updateEv = ModelFactory.createDefaultModel();
		Resource event1 = updateEv.createResource("http://www.test.org/event1");
		event1.addProperty(RDF.type,updateClass);
		Resource peer1 = updateEv.createResource("http://www.test.org/peer1");
		Property occurredAt = schema.getProperty("http://www.model.org/occurredAt");
		Property appliedTo = schema.getProperty("http://www.model.org/appliedTo");
		Resource doc1 = updateEv.createResource("http://www.mydoc.com/doc1");
		Resource documentClass = schema.getProperty("http://www.model.org/document");
		doc1.addProperty(RDF.type, documentClass);
		event1.addProperty(appliedTo, doc1);
		event1.addProperty(occurredAt, peer1);
		
		createEv = ModelFactory.createDefaultModel();
		Resource event2 = createEv.createResource("http://www.test.org/event2");
		event2.addProperty(RDF.type,create);
		Resource doc2 = createEv.createResource("http://www.mydoc.com/doc2");
		doc2.addProperty(RDF.type, documentClass);
		event2.addProperty(appliedTo, doc2);
		Resource peer2 = createEv.createResource("http://www.test.org/peer2");
		event2.addProperty(occurredAt, peer2);
		
		deleteEv = ModelFactory.createDefaultModel();
		Resource event3 = deleteEv.createResource("http://www.test.org/event3");
		event3.addProperty(RDF.type,delete);
		Resource peer3 = deleteEv.createResource("http://www.test.org/peer3");
		event3.addProperty(occurredAt, peer3);
		
		documentEv = ModelFactory.createDefaultModel();
		Resource event4 = documentEv.createResource("http://www.test.org/event4");
		event4.addProperty(RDF.type,documentEventClass);
		Resource peer4 = documentEv.createResource("http://www.test.org/peer4");
		event4.addProperty(occurredAt, peer4);
		
		evEv = ModelFactory.createDefaultModel();
		Resource event5 = evEv.createResource("http://www.test.org/event5");
		event5.addProperty(RDF.type,eventClass);
		Resource peer5 = evEv.createResource("http://www.test.org/peer5");
		event5.addProperty(occurredAt, peer5);
		
		memEv = ModelFactory.createDefaultModel();
		Resource event6 = memEv.createResource("http://www.test.org/event6");
		event6.addProperty(RDF.type,memberEventClass);
		event6.addProperty(appliedTo,doc2);
		Resource peer6 = memEv.createResource("http://www.test.org/peer6");
		event6.addProperty(occurredAt, peer6);	
		
		joinEv = ModelFactory.createDefaultModel();
		Resource event7 = joinEv.createResource("http://www.test.org/event7");
		event7.addProperty(RDF.type,joinEvent);
		Resource peer7 = joinEv.createResource("http://www.test.org/peer7");
		event7.addProperty(occurredAt, peer7);	
		
		leaveEv = ModelFactory.createDefaultModel();
		Resource event8 = leaveEv.createResource("http://www.test.org/event8");
		event8.addProperty(RDF.type,leaveEvent);
		Resource peer8 = leaveEv.createResource("http://www.test.org/peer8");
		event8.addProperty(occurredAt, peer8);
	}

}
