package tests;

import static org.junit.Assert.*;

import java.util.List;

import main.SchemaFactory;

import org.junit.BeforeClass;
import org.junit.Test;

import util.QueryProcessor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class QueryProcessorTest {
	private static Model rule1 = null;
	private static Model rule2 = null;
	private static Model rule3 = null;
	private static Model rule4 = null;
	private static Model rule5 = null;
	private static Model updateEv;
	private static Model createEv;
	private static Model deleteEv;
	private static Model documentEv;
	private static Model evEv;
	private static Model schemaMod = SchemaFactory.createSchema("C:\\Users\\Mum\\git\\protoNetwork\\src\\stuff\\schema.rdf");
	
	@BeforeClass
	public static void setUpModels() {
		rule1 = ModelFactory.createDefaultModel();
		rule2 = ModelFactory.createDefaultModel();
		rule3 = ModelFactory.createDefaultModel();
		rule4 =  ModelFactory.createDefaultModel();
		rule5 =  ModelFactory.createDefaultModel();
		
		Resource ruleEvent1 = rule1.createResource("http://www.rules.org/ruleEvent1");
		Resource eventClass = schemaMod.getResource("http://www.model.org/event");
		ruleEvent1.addProperty(RDF.type, eventClass);
		Resource action1 = rule1.createResource("http://www.rules.org/action1");
		Property hasAction = schemaMod.getProperty("http://www.model.org/hasAction");
		ruleEvent1.addProperty(hasAction, action1);
		
		Resource ruleEvent2 = rule2.createResource("http://www.rules.org/ruleEvent2");
		Resource memberEventClass = schemaMod.getResource("http://www.model.org/memberEvent");
		ruleEvent2.addProperty(RDF.type, memberEventClass);
		Resource action2 = rule2.createResource("http://www.rules.org/action2");
		ruleEvent2.addProperty(hasAction,action2);
		
		Resource ruleEvent3 = rule3.createResource("http://www.rules.org/ruleEvent3");
		Resource updateClass = schemaMod.getResource("http://www.model.org/update");
		ruleEvent3.addProperty(RDF.type, updateClass);
		Resource action3 = rule3.createResource("http://www.rules.org/action3");
		ruleEvent3.addProperty(hasAction,action3);
		
		Resource ruleEvent4 = rule4.createResource("http://www.rules.org/ruleEvent4");
		Resource documentEventClass = schemaMod.getResource("http://www.model.org/documentEvent");
		ruleEvent4.addProperty(RDF.type, documentEventClass);
		Resource action4 = rule4.createResource("http://www.rules.org/action4");
		ruleEvent4.addProperty(hasAction,action4);
		
		Resource ruleEvent5 = rule5.createResource("http://www.rules.org/ruleEvent5");
		Resource joinEvent = schemaMod.getResource("http://www.model.org/join");
		ruleEvent5.addProperty(RDF.type, joinEvent);
		Resource action5 = rule5.createResource("http://www.rules.org/action5");
		ruleEvent5.addProperty(hasAction,action5);
		
		updateEv = ModelFactory.createDefaultModel();
		Resource event1 = updateEv.createResource("http://www.test.org/event1");
		event1.addProperty(RDF.type,updateClass);
		Resource peer1 = updateEv.createResource("http://www.test.org/peer1");
		Property occurredAt = schemaMod.getProperty("http://www.model.org/occurredAt");
		event1.addProperty(occurredAt, peer1);
		
		createEv = ModelFactory.createDefaultModel();
		Resource event2 = createEv.createResource("http://www.test.org/event2");
		event2.addProperty(RDF.type,schemaMod.getResource("http://www.model.org/create"));
		Resource peer2 = createEv.createResource("http://www.test.org/peer2");
		event2.addProperty(occurredAt, peer2);
		
		deleteEv = ModelFactory.createDefaultModel();
		Resource event3 = deleteEv.createResource("http://www.test.org/event3");
		event3.addProperty(RDF.type,schemaMod.getResource("http://www.model.org/delete"));
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
	}

	@Test
	public void testSuperClassFinder() {
		List<String> eventSupers = QueryProcessor.runSuperClassQuery("event", schemaMod);
		List<String> memberSupers = QueryProcessor.runSuperClassQuery("memberEvent", schemaMod);
		List<String> documentSupers = QueryProcessor.runSuperClassQuery("documentEvent", schemaMod);
		List<String> updateSupers = QueryProcessor.runSuperClassQuery("update", schemaMod);
		List<String> leaveSupers = QueryProcessor.runSuperClassQuery("leave", schemaMod);
		assertTrue(eventSupers.size() == 1);
		assertTrue(memberSupers.size() == 2);
		assertTrue(documentSupers.size() == 2);
		assertTrue(updateSupers.size() == 3);
		assertTrue(leaveSupers.size() == 3);
		assertTrue(eventSupers.contains("http://www.model.org/event"));
		assertTrue(memberSupers.contains("http://www.model.org/memberEvent") && memberSupers.contains("http://www.model.org/event"));
		assertTrue(documentSupers.contains("http://www.model.org/event") && documentSupers.contains("http://www.model.org/documentEvent"));
		assertTrue(updateSupers.contains("http://www.model.org/event") && updateSupers.contains("http://www.model.org/documentEvent") && updateSupers.contains("http://www.model.org/update"));
		assertTrue(leaveSupers.contains("http://www.model.org/event") && leaveSupers.contains("http://www.model.org/memberEvent") && leaveSupers.contains("http://www.model.org/leave"));
	}
	
	@Test
	public void testRuleEventQuery() {
		 assertEquals(QueryProcessor.runRuleEventQuery(rule1),"http://www.model.org/event");
		 assertEquals(QueryProcessor.runRuleEventQuery(rule2),"http://www.model.org/memberEvent");
		 assertEquals(QueryProcessor.runRuleEventQuery(rule3),"http://www.model.org/update");
		 assertEquals(QueryProcessor.runRuleEventQuery(rule4),"http://www.model.org/documentEvent");
		 assertEquals(QueryProcessor.runRuleEventQuery(rule5),"http://www.model.org/join");
	}
	
	@Test
	public void testEventTypeQuery() {
		assertEquals(QueryProcessor.runEventTypeQuery(evEv),"http://www.model.org/event");
		assertEquals(QueryProcessor.runEventTypeQuery(createEv),"http://www.model.org/create");
		assertEquals(QueryProcessor.runEventTypeQuery(deleteEv),"http://www.model.org/delete");
		assertEquals(QueryProcessor.runEventTypeQuery(documentEv),"http://www.model.org/documentEvent");
		assertEquals(QueryProcessor.runEventTypeQuery(updateEv),"http://www.model.org/update");
	}
	
	@Test
	public void testActionQuery() {
		assertEquals(QueryProcessor.runActionQuery(rule1),"http://www.rules.org/action1");
		assertEquals(QueryProcessor.runActionQuery(rule2),"http://www.rules.org/action2");
		assertEquals(QueryProcessor.runActionQuery(rule3),"http://www.rules.org/action3");
		assertEquals(QueryProcessor.runActionQuery(rule4),"http://www.rules.org/action4");
		assertEquals(QueryProcessor.runActionQuery(rule5),"http://www.rules.org/action5");
	}

}
