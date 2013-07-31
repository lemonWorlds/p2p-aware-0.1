package interfaces;

import java.util.List;

import com.hp.hpl.jena.query.Query;

public interface DataStore {
	List<String> executeSelectQuery(Query query);
	boolean executeAskQuery(Query query);
	boolean updateDatabase(Query query);
}
