package properPeer;

public class AnotherDummy {
	public void dummyAct(String[] peerIps) {
		System.out.println("You are calling AnotherDummy with dummyAct using the params");
		for (String next: peerIps) {
			System.out.println(next);
		}
	}
}
