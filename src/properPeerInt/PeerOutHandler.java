package properPeerInt;

import com.hp.hpl.jena.rdf.model.Model;

public interface PeerOutHandler {
	void post(Model data);
}
