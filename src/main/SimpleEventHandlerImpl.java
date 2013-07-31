package main;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

import interfaces.DataStore;
import interfaces.EventHandler;
import interfaces.RuleBase;

public class SimpleEventHandlerImpl implements EventHandler {
	
	public SimpleEventHandlerImpl(RuleBase base, DataStore store, Model schema) {
		
	}

	@Override
	public List<String> handleEvent(Model event) {
		// TODO Auto-generated method stub
		return null;
	}

}
