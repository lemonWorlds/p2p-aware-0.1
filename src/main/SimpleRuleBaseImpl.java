package main;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

import interfaces.RuleBase;

public class SimpleRuleBaseImpl implements RuleBase {
	private Model schema = null;
	
	public SimpleRuleBaseImpl(Model schema) {
		this.schema = schema;
	}

	@Override
	public void addRules(Model... rules) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> matchEventToRules(Model event) {
		// TODO Auto-generated method stub
		return null;
	}

}
