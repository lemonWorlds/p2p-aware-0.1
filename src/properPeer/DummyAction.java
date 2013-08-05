package properPeer;

public class DummyAction {
	public void doAction(String[] args) {
		System.out.println("DoAction method called with these params: ");
		for (String param: args) {
			System.out.println(param);
		}
	}
}
