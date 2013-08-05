package main;

import java.io.IOException;

import org.apache.commons.lang3.StringEscapeUtils;

import interfaces.DataStore;
import interfaces.EventHandler;
import interfaces.RuleBase;
import interfaces.Server;

import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class Runner {
	private Model schema;
	public static void main(String[] args) {
		new Runner().launch(3000, "H:\\newWorkspace\\protoNetwork\\src\\stuff\\schema.rdf");
	}

	private void launch(int port,String fileName) {
		/*
		 * For later optimisation tests, check performance of multiple
		 * schema model objects versus a single schema model object 
		 */
		schema = SchemaFactory.createSchema(fileName);
		RuleBase ruleBase = new SimpleRuleBaseImpl(schema);
		addRules(ruleBase);
		DataStore dataStore = new SimpleDataStoreImpl();
		addData(dataStore);
		EventHandler eventHandler = new SimpleEventHandlerImpl(ruleBase,dataStore,schema);
		Server server = new SimpleServerImpl(eventHandler, new SimpleConcreteIOFactory(), port);
		try {
			server.listen();
		} catch (IOException ex) {
			System.out.println("Could not bind server socket. Cannot listen for peer communication.");
		}
	}

	private void addData(DataStore dataStore) {
		Model peers = ModelFactory.createDefaultModel();
		Resource peer1 = peers.createResource("www.peers.com/peer1");
		Resource peerClass = schema.getResource("http://www.model.org/peer");
		peer1.addProperty(RDF.type, peerClass);
		Resource peer2 = peers.createResource("www.peers.com/peer2");
		peer2.addProperty(RDF.type,peerClass);
		Resource peer3 = peers.createResource("www.peers.com/peer3");
		peer3.addProperty(RDF.type,peerClass);
		Resource peer4 = peers.createResource("www.peers.com/peer4");
		peer4.addProperty(RDF.type,peerClass);
		Resource document1 = peers.createResource("http://www.mydoc.com/doc1");
		document1.addProperty(RDF.type, schema.getResource("http://www.model.org/document"));
		Bag docBag = peers.createBag("www.bag.net/bag1");
		docBag.add(peer3);
		docBag.add(peer1);
		docBag.add(peer2);
		document1.addProperty(schema.getProperty("http://www.model.org/heldBy"), docBag);
		

		Resource document2 = peers.createResource("http://www.mydoc.com/doc2");
		document2.addProperty(RDF.type, schema.getResource("http://www.model.org/document"));
		Bag docBag2 = peers.createBag("www.aBag.org/bag2");
		docBag2.add(peer3);
		document2.addProperty(schema.getProperty("http://www.model.org/heldBy"), docBag2);

		
		Resource document3 = peers.createResource("http://www.mydoc.com/doc3");
		document3.addProperty(RDF.type, schema.getResource("http://www.model.org/document"));
		Bag docBag3 = peers.createBag("www.aBag.org/bag3");
		docBag3.add(peer2);
		docBag3.add(peer1);
		document3.addProperty(schema.getProperty("http://www.model.org/heldBy"), docBag3);


		dataStore.addData(peers);
	}

	private void addRules(RuleBase ruleBase) {
		Model rules = ModelFactory.createDefaultModel();
		Resource ruleEvent1 = rules.createResource("http://www.rules.org/ruleEvent1");
		Resource eventClass = schema.getResource("http://www.model.org/event");
		ruleEvent1.addProperty(RDF.type, eventClass);
		String a1 = "<action><class>properPeer.DummyAction</class><method>doAction</method><peerData>none</peerData></action>";
		String act1 = StringEscapeUtils.escapeXml(a1);
		Resource action1 = rules.createResource(act1);
		Property hasAction = schema.getProperty("http://www.model.org/hasAction");
		ruleEvent1.addProperty(hasAction, action1);
		
		
		Model rules1 = ModelFactory.createDefaultModel();
		Resource ruleEvent2 = rules1.createResource("http://www.rules.org/ruleEvent2");
		Resource create = schema.getResource("http://www.model.org/create");
		ruleEvent2.addProperty(RDF.type, create);
		String a2 = "<action><class>properPeer.AnotherDummy</class><method>dummyAct</method><peerData>document</peerData></action>";
		String act2 = StringEscapeUtils.escapeXml(a2);
		Resource action2 = rules1.createResource(act2);
		ruleEvent2.addProperty(hasAction,action2);
		
		
		Model rules2 = ModelFactory.createDefaultModel();
		Resource ruleEvent3 = rules2.createResource("http://www.rules.org/ruleEvent3");
		Resource member = schema.getResource("http://www.model.org/memberEvent");
		ruleEvent3.addProperty(RDF.type, member);
		String a3 = "<action><class>properPeer.DummyAction</class><method>doAction</method><peerData>document</peerData></action>";
		String act3 = StringEscapeUtils.escapeXml(a3);
		Resource action3 = rules2.createResource(act3);
		ruleEvent3.addProperty(hasAction,action3);
		
		
		Model rules3 = ModelFactory.createDefaultModel();
		Resource ruleEvent4 = rules3.createResource("http://www.rules.org/ruleEvent4");
		Resource documentEventClass = schema.getResource("http://www.model.org/documentEvent");
		ruleEvent4.addProperty(RDF.type, documentEventClass);
		String a4 = "<action><class>properPeer.AnotherDummy</class><method>dummyAct</method><peerData>all</peerData></action>";
		String act4 = StringEscapeUtils.escapeXml(a4);
		Resource action4 = rules3.createResource(act4);
		ruleEvent4.addProperty(hasAction,action4);
		
		
		ruleBase.addRules(rules,rules1,rules2,rules3);
	}
}
