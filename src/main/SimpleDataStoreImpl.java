package main;

import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.rdf.model.Model;

import interfaces.DataStore;

public class SimpleDataStoreImpl implements DataStore {
	
	public SimpleDataStoreImpl(Model schema) {
		
	}

	@Override
	public List<String> executeSelectQuery(Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean executeAskQuery(Query query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateDatabase(Query query) {
		// TODO Auto-generated method stub
		return false;
	}

}
