import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends ServerConnection {

	private int port;
	private String hand;
	private String name;
	
	public Server(int port, Consumer<Serializable> callback) {
		super(callback);
		// TODO Auto-generated constructor stub
		this.port = port;
	}


	@Override
	protected int getPort() {
		// TODO Auto-generated method stub
		return port;
	}

}
