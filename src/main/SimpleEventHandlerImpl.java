package main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.QueryProcessor;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Resource;

import interfaces.DataStore;
import interfaces.EventHandler;
import interfaces.RuleBase;

public class SimpleEventHandlerImpl implements EventHandler {
	private RuleBase base;
	private DataStore store;
	private int NAMESPACE_PREFIX_SIZE = 21;
	private String ROOT = "action";
	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private Model schema;
	private final String ALL_PEERS_QUERY = "PREFIX ex: <http://www.model.org/> " +
											     "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
											     "SELECT ?p " +
											     "WHERE {" +
											      "      ?p rdf:type ex:peer . " +
											      "}";
	private final String FIND_EVENT_DOCUMENT = "PREFIX ex: <http://www.model.org/> " +
		     								   "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
		     								   "SELECT ?doc " +
		     								   "WHERE {" +
		     								   "       ?ev ex:appliedTo ?doc . " +
		     								   "}";
	
	
	public SimpleEventHandlerImpl(RuleBase base, DataStore store, Model schema) {
		this.base = base;
		this.store = store;
		this.schema = schema;
	}

	@Override
	public List<String> handleEvent(Model event) throws Exception { //LOOKED AT
		/*
		 * Get list of applicable actions from ruleBase
		 */
		String eventURI = QueryProcessor.runEventTypeQuery(event);
		List<String> actions = base.matchEventToRules(eventURI.substring(NAMESPACE_PREFIX_SIZE));
		
		/*
		 * For each action, modify it using data from the store and add it to completedAction list
		 */
		List<String> completedActions = new ArrayList<String>();
		for (String action: actions) {
			completedActions.add(modifyWithDataStore(action,event));
		}
		
		return completedActions;
	}

	public String modifyWithDataStore(String action, Model event) throws Exception { //LOOKED AT
		/*
		 * First escape String so it is valid xml and parse it into an XML Document
		 */
		String xmlAction = unescapeXMLString(action);
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputStream in = new ByteArrayInputStream(xmlAction.getBytes());
	    Document document = builder.parse(in);
	    
	    /*
	     * Check what data needs to be added and add it. Then convert document to String and return.
	     */
	    String required = getRequiredData(document);
	    switch (required) {
	    	case "all":
				addAllPeers(document);
				break;
			case "none":
				break;
			case "document":
				addAllPeersWithDocument1(document,event);
				break;
			default:
				throw new IllegalArgumentException();
	    }
	    String completedAction = transformDocumentToString(document);
		return completedAction;
	}

	private void addAllPeersWithDocument1(Document document, Model event) {
		Query query = QueryProcessor.createQueryFromString(FIND_EVENT_DOCUMENT);
		QueryExecution execution = QueryProcessor.executeQuery(query, event);
		ResultSet result = execution.execSelect();
		Resource doc = result.next().getResource("doc");
		addAllPeersWithDocument(document, doc);
	}

	public String transformDocumentToString(Document document) { //LOOKED AT
		StringWriter sw = null; 
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
	        sw = new StringWriter();
	        transformer.transform(new DOMSource(document), new StreamResult(sw));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sw.toString();
	}

	public void addAllPeersWithDocument(Document document, Resource required) {
		Model data = store.getDS();
		NodeIterator iter = data.listObjectsOfProperty(required, schema.getProperty("http://www.model.org/heldBy"));
		Bag bag = data.getBag(iter.next().asResource());
		NodeIterator iterBag = bag.iterator();
		List<String> peerIps = new ArrayList<String>();
		while (iterBag.hasNext()) {
			peerIps.add(iterBag.next().asResource().getURI());
		}
		insertPeers(document,peerIps);
	}

	public void addAllPeers(Document document) { //LOOKED AT
		Query query = QueryProcessor.createQueryFromString(ALL_PEERS_QUERY);
		QueryExecution execution = QueryProcessor.executeQuery(query, store.getDS());
		ResultSet peers = execution.execSelect();
		List<String> peerIps = new ArrayList<String>();
		while (peers.hasNext()) {
			peerIps.add(peers.next().getResource("p").getURI());
		}
		insertPeers(document,peerIps);
	}

	public String getRequiredData(Document document) { //LOOKED AT
		NodeList nodeList = document.getElementsByTagName("peerData");
		Node peerData = nodeList.item(0);
		String required = peerData.getTextContent(); //Need some kind of delete redundant data function
		return required;
	}

	public String unescapeXMLString(String action) { //LOOKED AT
		return StringEscapeUtils.unescapeXml(action);
	}
	
	public void insertPeers(Document document, List<String> peers) { //LOOKED AT
		Node root = document.getElementsByTagName(ROOT).item(0);
		for (String next: peers) {
			Element peer = document.createElement("peerIP");
			peer.appendChild(document.createTextNode(next));
			root.appendChild(peer);
		}
	}
	
}
