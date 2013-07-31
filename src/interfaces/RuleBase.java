package interfaces;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

public interface RuleBase {
	void addRules(Model... rules);
	List<String> matchEventToRules(Model event);
}
