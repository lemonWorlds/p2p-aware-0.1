package main;

import interfaces.DataStore;
import interfaces.EventHandler;
import interfaces.RuleBase;
import interfaces.Server;

import com.hp.hpl.jena.rdf.model.Model;

public class Runner {
	public static void main(String[] args) {
		new Runner().launch(Integer.parseInt(args[0]), args[1]);
	}

	private void launch(int port,String fileName) {
		/*
		 * For later optimisation tests, check performance of multiple
		 * schema model objects versus a single schema model object 
		 */
		Model schema = SchemaFactory.createSchema(fileName);
		RuleBase ruleBase = new SimpleRuleBaseImpl(schema);
		DataStore dataStore = new SimpleDataStoreImpl(schema);
		EventHandler eventHandler = new SimpleEventHandlerImpl(ruleBase,dataStore,schema);
		Server server = new SimpleServerImpl(eventHandler, new SimpleConcreteIOFactory(), port);
		server.listen();
	}
}
