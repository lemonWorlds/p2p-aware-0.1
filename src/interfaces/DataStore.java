package interfaces;

import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.rdf.model.Model;

public interface DataStore {
	List<String> executeSelectQuery(Query query, String variable);
	boolean executeAskQuery(Query query, String variable);
	void updateDatabase(Query query, String variable);
	void addData(Model data);
	Model getDS();
}
