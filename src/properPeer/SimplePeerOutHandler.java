package properPeer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.hp.hpl.jena.rdf.model.Model;

import properPeerInt.PeerOutHandler;

public class SimplePeerOutHandler implements PeerOutHandler {
	private DataOutputStream stream;
	
	public SimplePeerOutHandler(OutputStream out) {
		stream = new DataOutputStream(out);
	}
	
	@Override
	public void post(Model data) {
		writeBytesToStream(getBytesFromModel(data),stream);
	}
	
	private byte[] getBytesFromModel(Model model) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		model.write(baos, "RDF/XML");
		return baos.toByteArray();
	}
	
	private void writeBytesToStream(byte[] arr, DataOutputStream dout) {
		try {
			dout.writeInt(arr.length);
			dout.write(arr,0,arr.length);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
