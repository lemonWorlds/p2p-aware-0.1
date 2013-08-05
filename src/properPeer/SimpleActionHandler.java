package properPeer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import properPeerInt.ActionHandler;

public class SimpleActionHandler implements ActionHandler {
	private BlockingQueue<String> sharedQueue;
	private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	private final String CLASS_TAG = "class";
	private final String METHOD_TAG = "method";
	private final String PARAM_TAG = "peerIP";
	
	public SimpleActionHandler(BlockingQueue<String> sharedQueue) {
		this.sharedQueue = sharedQueue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				executeAction(sharedQueue.take());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Document createDocument(String action) throws Exception {
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputStream in = new ByteArrayInputStream(action.getBytes());
	    Document document = builder.parse(in);
		return document;
	}
	
	private void executeAction(String action) throws Exception {
		Document document = createDocument(action);
		String[] params = getParams(document);
		String methodName = getMethodName(document);
		String className = getClassName(document);
		Class<?> aClass = Class.forName(className);
		Object obj = aClass.newInstance();
		Class[] argTypes = new Class[] { String[].class };
		Method method = aClass.getMethod(methodName, argTypes);
		method.invoke(obj, (Object)params);
	}
	
	private String getClassName(Document document) {
		return document.getElementsByTagName(CLASS_TAG).item(0).getTextContent();
	}

	private String getMethodName(Document document) {
		return document.getElementsByTagName(METHOD_TAG).item(0).getTextContent();
	}

	private String[] getParams(Document document) {
		NodeList nodeList = document.getElementsByTagName(PARAM_TAG);
		String[] params = new String[nodeList.getLength()]; 
		for (int i = 0; i < nodeList.getLength(); i++) {
			params[i] = nodeList.item(i).getTextContent();
		}
		return params;
	}
}
