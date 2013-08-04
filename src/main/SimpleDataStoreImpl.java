package main;

import java.util.ArrayList;
import java.util.List;

import util.QueryProcessor;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import interfaces.DataStore;

public class SimpleDataStoreImpl implements DataStore {
	public Model dataStore = ModelFactory.createDefaultModel();

	@Override
	public List<String> executeSelectQuery(Query query,String variable) {
		QueryExecution execution = QueryProcessor.executeQuery(query, dataStore);
		ResultSet results = QueryProcessor.processSelectQuery(execution);
		List<String> selected = new ArrayList<String>();
		while (results.hasNext()) {
			selected.add(results.next().get(variable).toString());
		}
		return selected;
	}

	@Override
	public boolean executeAskQuery(Query query,String variable) {
		return QueryProcessor.processAskQuery(QueryProcessor.executeQuery(query,dataStore));
	}

	@Override
	public void updateDatabase(Query query,String variable) {
		QueryProcessor.processConstructQuery(QueryProcessor.executeQuery(query, dataStore));
	}
	
	@Override
	public void addData(Model data) {
		dataStore.add(data);
	}
	
	@Override
	public Model getDS() {
		return dataStore;
	}
}
