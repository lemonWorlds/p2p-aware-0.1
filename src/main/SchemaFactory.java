package main;


import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

public class SchemaFactory {
	//Going to not use a singleton to begin with; see what happens.
	public static Model createSchema(String fileName) {
		InputStream in = FileManager.get().open(fileName);
		Model model = ModelFactory.createDefaultModel();
		model.read(in,null);
		Reasoner r = ReasonerRegistry.getRDFSReasoner();
		r.setParameter(ReasonerVocabulary.PROPsetRDFSLevel, ReasonerVocabulary.RDFS_SIMPLE);
		InfModel infModel = ModelFactory.createInfModel(r,model);
		return infModel;
	}
}
