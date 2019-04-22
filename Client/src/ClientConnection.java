import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class ClientConnection {
	
	ConnThread connthread = new ConnThread();
	private Consumer<Serializable> callback;
	boolean hideButtons = false; //This flag lets the UI know to hide the Rock/Paper/Scissors buttons
	boolean inWaitingRoom = true; //Lets us know if player is currently in game or not
	int playerNumber; //initializes to 0,
	String clientName = "";
	boolean updatePlayerList = false;
	String playerList = "";
	int score;



	
	public ClientConnection(Consumer<Serializable> callback) {
		this.callback = callback;
		connthread.setDaemon(true); //Don't know what this does but it must be important

	}

	public void makeCall(String data){
		callback.accept(data);
	}
	
	public void startConn(String cname) throws Exception{
		//Store the name we receive from client
		this.clientName = cname;

		//startConn is called by FXNet.java, the thread is where most the work is done
		connthread.start();
	}
	
	public void send(String data) throws Exception{
		connthread.out.writeObject(data);
	}

	public void sendChallenge(String data) throws Exception{
		connthread.out.writeObject("I wish to challenge someone!"); //This sends a message to the server to execute the if branch that handles challenges
		connthread.out.writeObject(data); //This sends the actual player the user wishes to challenge
	}

	
	public void closeConn(){
		try {
			connthread.socket.close();
		}
		catch(Exception e) {
			System.out.println("No connection to a server was ever made");
		}
	}
	
	abstract protected boolean isServer();
	abstract protected String getIP();
	abstract protected int getPort();
	abstract protected String getName();
	abstract protected String getHand();



	class ConnThread extends Thread{
		private Socket socket;
		private ObjectOutputStream out;

		/**This is where most the work is done**/
		public void run() {
			try(
					Socket socket = new Socket(getIP(), getPort()); //client
					ObjectOutputStream out = new ObjectOutputStream( socket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
					callback.accept("You are about to enter the Puzzle Gauntlet");


				this.socket = socket;
				this.out = out;
				socket.setTcpNoDelay(true);
				//Sends the client to the server
				try {
					this.out.writeObject("Change name");
					this.out.writeObject(clientName);
				}
				catch (Exception e){
					callback.accept("cannot write the name. Try again");
				}


				/**When the client recieves either of these messages, the playerNumber they are assigned changes**/
				while(true) {
					Serializable data = (Serializable) in.readObject();
					if(data.equals("Skidaddle Skidoodle you are in a game") ){
						inWaitingRoom = false;
					}
					else if(data.equals("Update your playerlist") ) {
						playerList = (String) in.readObject();
						updatePlayerList = true;
					}
					else
						callback.accept(data);
				}
			}
			catch(Exception e) {
					callback.accept("connection to server lost, please restart client");
			}
		}


	}


	
}	

