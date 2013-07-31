package interfaces;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

public interface EventHandler {
	List<String> handleEvent(Model event);
}
