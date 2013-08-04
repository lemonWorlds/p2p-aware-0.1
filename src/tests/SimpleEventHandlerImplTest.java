package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import interfaces.DataStore;
import interfaces.EventHandler;
import interfaces.RuleBase;

import main.SchemaFactory;
import main.SimpleDataStoreImpl;
import main.SimpleEventHandlerImpl;
import main.SimpleRuleBaseImpl;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class SimpleEventHandlerImplTest {
	private RuleBase base;
	private DataStore store;
	private EventHandler evHand;
	private Model schema = SchemaFactory.createSchema("C:\\Users\\Mum\\git\\protoNetwork\\src\\stuff\\schema.rdf");
	private Model rules;
	private Model rules1;
	private Model rules2;
	private Model rules3;
	private Model peers;
	private Model updateEv;
	@Before
	public void setUp() throws Exception {
		base = new SimpleRuleBaseImpl(schema);
		store = new SimpleDataStoreImpl();
		evHand = new SimpleEventHandlerImpl(base, store,schema);
		
		rules = ModelFactory.createDefaultModel();
		Resource ruleEvent1 = rules.createResource("http://www.rules.org/ruleEvent1");
		Resource eventClass = schema.getResource("http://www.model.org/event");
		ruleEvent1.addProperty(RDF.type, eventClass);
		String a1 = "<action><stuff>hi</stuff><peerData>none</peerData></action>";
		String act1 = StringEscapeUtils.escapeXml(a1);
		Resource action1 = rules.createResource(act1);
		Property hasAction = schema.getProperty("http://www.model.org/hasAction");
		ruleEvent1.addProperty(hasAction, action1);
		
		rules1 = ModelFactory.createDefaultModel();
		Resource ruleEvent2 = rules1.createResource("http://www.rules.org/ruleEvent2");
		Resource memberEventClass = schema.getResource("http://www.model.org/memberEvent");
		ruleEvent2.addProperty(RDF.type, memberEventClass);
		String a2 = "<action><stuff>hello</stuff><peerData>none</peerData></action>";
		String act2 = StringEscapeUtils.escapeXml(a2);
		Resource action2 = rules1.createResource(act2);
		ruleEvent2.addProperty(hasAction,action2);
		
		rules2 = ModelFactory.createDefaultModel();
		Resource ruleEvent3 = rules2.createResource("http://www.rules.org/ruleEvent3");
		Resource updateClass = schema.getResource("http://www.model.org/update");
		ruleEvent3.addProperty(RDF.type, updateClass);
		String a3 = "<action><stuff>nooooo</stuff><peerData>document</peerData></action>";
		String act3 = StringEscapeUtils.escapeXml(a3);
		Resource action3 = rules2.createResource(act3);
		ruleEvent3.addProperty(hasAction,action3);
		
		rules3 = ModelFactory.createDefaultModel();
		Resource ruleEvent4 = rules3.createResource("http://www.rules.org/ruleEvent4");
		Resource documentEventClass = schema.getResource("http://www.model.org/documentEvent");
		ruleEvent4.addProperty(RDF.type, documentEventClass);
		String a4 = "<action><stuff>tom</stuff><peerData>all</peerData></action>";
		String act4 = StringEscapeUtils.escapeXml(a4);
		Resource action4 = rules3.createResource(act4);
		ruleEvent4.addProperty(hasAction,action4);
		
		peers = ModelFactory.createDefaultModel();
		Resource peer1 = peers.createResource("www.peers.com/peer1");
		Resource peerClass = schema.getResource("http://www.model.org/peer");
		peer1.addProperty(RDF.type, peerClass);
		Resource peer2 = peers.createResource("www.peers.com/peer2");
		peer2.addProperty(RDF.type,peerClass);
		Resource peer3 = peers.createResource("www.peers.com/peer3");
		peer3.addProperty(RDF.type,peerClass);
		Resource document1 = peers.createResource("http://www.mydoc.com/doc1");
		document1.addProperty(RDF.type, schema.getResource("http://www.model.org/document"));
		Bag docBag = peers.createBag("www.bag.net/bag1");
		docBag.add(peer3);
		docBag.add(peer1);
		docBag.add(peer2);
		document1.addProperty(schema.getProperty("http://www.model.org/heldBy"), docBag);
		
		updateEv = ModelFactory.createDefaultModel();
		Resource event1 = updateEv.createResource("http://www.test.org/event1");
		event1.addProperty(RDF.type,updateClass);
		Resource myPeer = updateEv.createResource("http://www.test.org/peer1");
		Property occurredAt = schema.getProperty("http://www.model.org/occurredAt");
		Resource doc1 = updateEv.createResource("http://www.mydoc.com/doc1");
		doc1.addProperty(RDF.type, schema.getResource("http://www.model.org/document"));
		event1.addProperty(schema.getProperty("http://www.model.org/appliedTo"),doc1);
		event1.addProperty(occurredAt, myPeer);
		
		
		base.addRules(rules,rules1,rules2,rules3);
		Model data = store.getDS();
		data.add(peers);
	}

	@Test
	public final void testInsertPeers() throws Exception {
		File testFile = new File("C:\\Users\\Mum\\git\\p2p-aware-0.1\\src\\tests\\test.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(testFile);
		List<String> peers = new ArrayList<String>();
		peers.add("1.4.55.2");
		peers.add("33.4.55.1");
		peers.add("12.352.33.5");
		SimpleEventHandlerImpl eh = (SimpleEventHandlerImpl) evHand;
		eh.insertPeers(doc, peers);
		String docAsStr = eh.transformDocumentToString(doc);
		System.out.println(docAsStr);
		String escaped = StringEscapeUtils.escapeXml(docAsStr);
		String unescaped = eh.unescapeXMLString(escaped);
		System.out.println(unescaped);
	} 
	
	@Test
	public void testGetRequired() throws Exception {
		File testFile = new File("C:\\Users\\Mum\\git\\p2p-aware-0.1\\src\\tests\\test.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(testFile);
		SimpleEventHandlerImpl eh = (SimpleEventHandlerImpl) evHand;
		String required = eh.getRequiredData(doc);
		assertEquals(required,"all");
	}
	

	
	@Test
	public void testHandle() throws Exception {
		System.out.println("***********************************************\n" +
			               	"*********************************************");
		
		List<String> result = evHand.handleEvent(updateEv);	
		
		for (String next: result) {
			System.out.println(next);
		}
		System.out.println("***********************************************\n" +
               	"*********************************************");
	}
}
