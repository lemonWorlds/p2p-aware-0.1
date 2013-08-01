package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class QueryProcessor {
	public final static String FILE_NAME = "H:\\newWorkspace\\p2p-aware-0.1\\src\\config\\queries.properties";
	private static final Query eventTypeQuery;
	private static final Query ruleEventQuery;
	private static final Query actionQuery;
	private static final String eventSuperClassQueryPt1;
	private static final String eventSuperClassQueryPt2;
	private static final String eventTypeQueryVar;
	private static final String ruleEventQueryVar;
	private static final String actionQueryVar;
	private static final String eventSuperClassQueryVar;

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
		eventSuperClassQueryVar = props.getProperty("eventSuperClassQueryVar");
	}
	
	public static String runEventTypeQuery(Model event) {
		ResultSet results = processSelectQuery(executeQuery(eventTypeQuery,event));
		return extractResultVariable(results.next(),eventTypeQueryVar);
	}
	
	public static String runRuleEventQuery(Model rule) {
		ResultSet results = processSelectQuery(executeQuery(ruleEventQuery,rule));
		return extractResultVariable(results.next(), ruleEventQueryVar);
	}
	
	public static String runActionQuery(Model rule) {
		ResultSet results = processSelectQuery(executeQuery(actionQuery,rule));
		return extractResultVariable(results.next(), actionQueryVar);
	}
	
	public static List<String> runSuperClassQuery(String eventType, Model schema) {
		Query eventSuperClassQuery = createQueryFromString(eventSuperClassQueryPt1 + eventType + " " + eventSuperClassQueryPt2); //FIX THIS!
		//TESTING 
		System.out.println(eventSuperClassQueryPt1 + eventType + " " + eventSuperClassQueryPt2);
		
		ResultSet results = processSelectQuery(executeQuery(eventSuperClassQuery,schema));
		List<String> resultsList = new ArrayList<String>();
		while (results.hasNext()) {
			System.out.println("in loop");
			QuerySolution result = results.next();
			Iterator<String> iter = result.varNames();
			while (iter.hasNext()) {
				System.out.println(iter.next() + "!");
			}
			System.out.println(result);
			System.out.println(eventSuperClassQueryVar);
			Resource res = result.getResource(eventSuperClassQueryVar);
			System.out.println(res);
			resultsList.add(res.getURI());
			//resultsList.add(results.next().getResource(eventSuperClassQueryVar).getURI());
		}
		return resultsList;
	}
	
	private static Query createQueryFromString(String string) {
		return QueryFactory.create(string);
	}
	
	private static QueryExecution executeQuery(Query query, Model model) {
		return QueryExecutionFactory.create(query, model);
	}
/*	
	private static Model processConstructQuery(QueryExecution execution) {
		return execution.execConstruct();
	}
	
	private static boolean processAskQuery(QueryExecution execution) {
		return execution.execAsk();
	}
*/	
	private static ResultSet processSelectQuery(QueryExecution execution) {
		return execution.execSelect();
	}
	/*
	 * Should we have the facility to check whether only single results are being produced where appropriate (as in old version)?
	 */
	private static String extractResultVariable(QuerySolution queryResult, String variable){
		return queryResult.getResource(variable).getURI();
	}
}
