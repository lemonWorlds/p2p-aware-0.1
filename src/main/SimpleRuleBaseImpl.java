package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import util.QueryProcessor;

import com.hp.hpl.jena.rdf.model.Model;

import interfaces.RuleBase;

public class SimpleRuleBaseImpl implements RuleBase {
	private Model schema;
	private Map<String, Set<String>> ruleSet = new HashMap<>();
	
	public SimpleRuleBaseImpl(Model schema) {
		this.schema = schema;
	}

	@Override
	public void addRules(Model... rules) {
		for (Model next: rules) {
			String eventType = null;
			String action = null;
			try {
				action = QueryProcessor.runActionQuery(next);
				eventType = QueryProcessor.runRuleEventQuery(next);
			} catch (NoSuchElementException ex) {
				System.out.println("ERROR: The rule model was not well-formed; could not be added to set.");
				return;
			}
			if (!ruleSet.containsKey(eventType)) {
				Set<String> emptySet = new HashSet<>();
				emptySet.add(action);
				ruleSet.put(eventType, emptySet);
			} else {
				Set<String> existingSet = ruleSet.get(eventType);
				existingSet.add(action);
			}
		}
		
	}

	@Override
	public List<String> matchEventToRules(String event) {
		List<String> superClasses = QueryProcessor.runSuperClassQuery(event, schema);
		List<String> actions = new ArrayList<>();
		for (String next: superClasses) {
			Set<String> eventActions = ruleSet.get(next);
			if (eventActions != null) {
				actions.addAll(eventActions);
			}
		}
		return actions;
	}

}
