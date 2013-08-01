package tests;

import static org.junit.Assert.*;

import java.util.List;

import main.SchemaFactory;

import org.junit.Test;

import util.QueryProcessor;

import com.hp.hpl.jena.rdf.model.Model;

public class QueryProcessorTest {

	@Test
	public void testSuperClassFinder() {
		Model schema = SchemaFactory.createSchema("H:\\newWorkspace\\protoNetwork\\src\\stuff\\schema.rdf");
		schema.write(System.out, "N-TRIPLES");
		//List<String> eventSupers = QueryProcessor.runSuperClassQuery("event", schema);
		List<String> memberSupers = QueryProcessor.runSuperClassQuery("memberEvent", schema);
		List<String> documentSupers = QueryProcessor.runSuperClassQuery("documentEvent", schema);
		List<String> updateSupers = QueryProcessor.runSuperClassQuery("update", schema);
		List<String> leaveSupers = QueryProcessor.runSuperClassQuery("leave", schema);
		//assertTrue(eventSupers.size() == 1);
	}

}
