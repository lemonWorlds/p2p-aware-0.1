package util;

import java.util.HashMap;
import java.util.Map;

public class SparqlUtils {
	
	private static final Map<String,String> SPARQL_ESCAPE_SEARCH_REPLACEMENTS = new HashMap<>();
	
	static {
		SPARQL_ESCAPE_SEARCH_REPLACEMENTS.put("\t", "\\t");
		SPARQL_ESCAPE_SEARCH_REPLACEMENTS.put("\n", "\\n");
		SPARQL_ESCAPE_SEARCH_REPLACEMENTS.put("\r", "\\r");
		SPARQL_ESCAPE_SEARCH_REPLACEMENTS.put("\b", "\\b");
		SPARQL_ESCAPE_SEARCH_REPLACEMENTS.put("\f", "\\f");
		SPARQL_ESCAPE_SEARCH_REPLACEMENTS.put("\"", "\\\"");
		SPARQL_ESCAPE_SEARCH_REPLACEMENTS.put("'", "\\'");
		SPARQL_ESCAPE_SEARCH_REPLACEMENTS.put("\\", "\\\\");
	}

	public static String escape(String string) {
			
			StringBuffer bufOutput = new StringBuffer(string);
			for (int i = 0; i < bufOutput.length(); i++) {
				String replacement = SPARQL_ESCAPE_SEARCH_REPLACEMENTS.get("" + bufOutput.charAt(i));
				if(replacement!=null) {
					bufOutput.deleteCharAt(i);
					bufOutput.insert(i, replacement);
					// advance past the replacement
					i += (replacement.length() - 1);
				}
			}
			return bufOutput.toString();
		}

}
