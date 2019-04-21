import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends ClientConnection {

	private String ip;
	private int port;
	private String name;
	private String hand;
	
	public Client(String ip, int port, Consumer<Serializable> callback, String name) {
		super(callback);
		this.ip = ip;
		this.port = port;
		this.name = name;
	}


	@Override
	protected boolean isServer() {
		return false;
	}

	@Override
	protected String getIP() {
		// TODO Auto-generated method stub
		return this.ip;
	}

	@Override
	protected int getPort() {
		// TODO Auto-generated method stub
		return this.port;
	}

	@Override
	protected String getName() {

		return this.name;
	}

	@Override
	protected String getHand() {

		return this.hand;
	}

}
