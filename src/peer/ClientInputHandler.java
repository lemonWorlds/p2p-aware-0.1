package peer;


	import java.io.DataInputStream;
	import java.io.IOException;
	import java.io.InputStream;

	public class ClientInputHandler implements Runnable {
		private DataInputStream stream = null;

		public ClientInputHandler(InputStream in) {
			stream = new DataInputStream(in);
		}
		
		@Override
		public void run() {
				try {
					while (true) {
						System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
						System.out.println(getInputData(stream));
						System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
					} 
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		}
		
		private String getInputData(DataInputStream stream) throws IOException {		
			int size = stream.readInt();
			byte[] data = new byte[size];
			stream.read(data,0,size);
			return new String(data);
		}

	}
