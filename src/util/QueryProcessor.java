package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

public class QueryProcessor {
	public final static String FILE_NAME = "C:\\Users\\Mum\\git\\p2p-aware-0.1\\src\\config\\queries.properties";
	protected static final Query eventTypeQuery;
	protected static final Query ruleEventQuery;
	protected static final Query actionQuery;
	protected static final String eventSuperClassQueryPt1;
	protected static final String eventSuperClassQueryPt2;
	protected static final String eventTypeQueryVar;
	protected static final String ruleEventQueryVar;
	protected static final String actionQueryVar;
	protected static final String eventSuperClassQueryVar;

	static {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(FILE_NAME));
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Static query strings could not be initialised due to faulty file connection.");
		}
		eventTypeQuery = createQueryFromString(props.getProperty("eventTypeQuery"));
		ruleEventQuery = createQueryFromString(props.getProperty("ruleEventQuery"));
		actionQuery = createQueryFromString(props.getProperty("actionQuery"));
		eventSuperClassQueryPt1 = props.getProperty("eventSuperClassQueryPt1");
		eventSuperClassQueryPt2 = props.getProperty("eventSuperClassQueryPt2");
		eventTypeQueryVar = props.getProperty("eventTypeQueryVar");
		ruleEventQueryVar = props.getProperty("ruleEventQueryVar");
		actionQueryVar = props.getProperty("actionQueryVar");
		String malformedSuperClassVar = props.getProperty("eventSuperClassQueryVar");
		eventSuperClassQueryVar = malformedSuperClassVar.trim();
	}
	
	public static String runEventTypeQuery(Model event) throws NoSuchElementException {
		ResultSet results = processSelectQuery(executeQuery(eventTypeQuery,event));
		return extractResultVariable(results.next(),eventTypeQueryVar);
	}
	
	public static String runRuleEventQuery(Model rule) throws NoSuchElementException {
		ResultSet results = processSelectQuery(executeQuery(ruleEventQuery,rule));
		return extractResultVariable(results.next(), ruleEventQueryVar);
	}
	
	public static String runActionQuery(Model rule) throws NoSuchElementException {
		ResultSet results = processSelectQuery(executeQuery(actionQuery,rule));
		return extractResultVariable(results.next(), actionQueryVar);
	}
	
	public static List<String> runSuperClassQuery(String eventType, Model schema) {
		Query eventSuperClassQuery = createQueryFromString(eventSuperClassQueryPt1 + eventType + " " + eventSuperClassQueryPt2); //FIX THIS!		
		ResultSet results = processSelectQuery(executeQuery(eventSuperClassQuery,schema));
		List<String> resultsList = new ArrayList<String>();
		while (results.hasNext()) {
			resultsList.add(results.next().getResource(eventSuperClassQueryVar).getURI());
		}
		return resultsList;
	}
	
	public static Query createQueryFromString(String string) {
		return QueryFactory.create(string);
	}
	
	public static QueryExecution executeQuery(Query query, Model model) {
		return QueryExecutionFactory.create(query, model);
	}

	public static Model processConstructQuery(QueryExecution execution) {
		return execution.execConstruct();
	}
	
	public static boolean processAskQuery(QueryExecution execution) {
		return execution.execAsk();
	}

	public static ResultSet processSelectQuery(QueryExecution execution) {
		return execution.execSelect();
	}

	/*
	 * Should we have the facility to check whether only single results are being produced where appropriate (as in old version)?
	 */
	public static String extractResultVariable(QuerySolution queryResult, String variable){
		return queryResult.getResource(variable).getURI();
	}
	
	
}
