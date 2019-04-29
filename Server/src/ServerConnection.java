import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class ServerConnection {

	ConnThread connthread = new ConnThread(); //Connthread is what listens for clients to connect, once they do, a new thread inside connThread called ClientThread handles teir gameplay
	private Consumer<Serializable> callback; //Have no idea what this does but it prints stuff to the textbox

	//int scores[] = {0,0}; //keeps track of the scores
	ArrayList<ClientThread> clients = new ArrayList<>(); //This arrayList keeps track of who is connected to the server
	ArrayList<ClientThread> inGame = new ArrayList<>(); //This arrayList keeps track of who is connected to the server
	ArrayList<ClientThread> superClients = new ArrayList<>(); //This arrayList keeps track of who is connected to the server

	/**The ClientThread Object contains lots of info, like player number, score, name (in this case name of thread), etc**/
	boolean gameOver; //lets us know when tell the UI to ask the user to play again or start a new round


	/**  Every time the client wants to send something to the server, it must be in order of (STRING, INT, INT)  **/
	/**  and this method also sends the end of round message to both players  **/


	public ServerConnection(Consumer<Serializable> callback) {
		synchronized(this) {
			this.callback = callback;
			connthread.setDaemon(true);
		}
		//System.out.println("server goes thru here 1"); //This print statement was for debugging purposes

	}

	public void startConn() throws Exception{
		connthread.start();
		System.out.println("server goes thru here 2"); //debugging purposes

	}

	/**This method sends the same message to everyone in an instance of a game**/
	public void send(Serializable data, ArrayList<ClientThread> clients) throws Exception{
		synchronized(this) {

			for (int i = 0; i < clients.size(); i++) {

				clients.get(i).out.writeObject(data);
			}
		}

		//System.out.println("server goes thru here 3"); //debugging purposes
	}

	public void closeConn() throws Exception{
		//connthread.socket.close();
		for(int i = 0; i < clients.size(); i++){
			clients.get(i).socket.close();
		}
		System.out.println("server goes thru here 4");

	}

	abstract protected int getPort();

	/** The idea is: your network connection is in its own thread. The accept method needs to be in an infinite
	 loop to keep accepting clients. The sockets returned by accept need to go on their own thread. **/

	class ConnThread extends Thread{
		ConnThread(){}

		public void run() {
			if(clients.size() < 2)
				callback.accept("Server awaiting connection");
			else
				callback.accept("2 players are in the arena, we can be ready to play");

			try{
				ServerSocket server = new ServerSocket(getPort());
				while(true) {
					ClientThread t1 = new ClientThread(server.accept());
					clients.add(t1);
					superClients.add(t1);
					t1.start();
					callback.accept("Checking name " + t1.clientName);
				}

			}
			catch(Exception e) {
				callback.accept("connection Closed");
			}
		}
	}


	class ClientThread extends Thread {

		String clientName = "";
		private Socket socket;
		private ObjectOutputStream out;
		String data;
		String hand;
		int score;
		int playerNumber; //0 = player1.......1 = player2
		boolean alreadyPlayed = false; //Lets us know if the client already threw something, so their choice isn't counted twice
		boolean demandsCoaching = false;
		ArrayList<String> playHistory = new ArrayList<String>();
		ClientThread(Socket s) {
			this.socket = s;
		}


		public void run() {

			try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

				callback.accept("Connection has been established: Current number of players " + clients.size());
				this.out = out;
				socket.setTcpNoDelay(true);
				//when the game ends, the list of players is shown to the client again
				while (true) {
					data = (String) in.readObject();

					//Check for what input the user is sending to manipulate
					if( (data.equals("Change name")) ){
						data = (String) in.readObject(); //contains the name of client
						this.clientName = data;
					}
					else if((data.equals("w"))){
						callback.accept(clientName + " won the puzzle");
					}
					/**c will be sent when a player connects, but for now, it is only sent when we press the test game button**/
					else if((data.equals("c"))){
						callback.accept(clientName + " has entered the test realm");//Edit later
						if(clients.size() < 2) {
							callback.accept("Server awaiting 3 more players...But to test a build closer to the final game, have another person connect and press Test Game");
						}
						else if(clients.size() < 3) {
							callback.accept("Server awaiting 2 more players...But for now");
							send("g", clients); //Delete this later

						}
						else if(clients.size() < 4)
							callback.accept("Server awaiting 1 more player...");
						else if(clients.size() == 4) {
							send("g", clients);
						}
					}
					//this appends what happened during the game to the server gui
					else {
						callback.accept(clientName + " Says: " + data);
					}
				}
			} catch (Exception e) {
				callback.accept("connection Closed");
				System.out.println("We are removing " + this.getName() + "\nAfter remove...\n");
				clients.remove(this);
				superClients.remove(this);
				for(int i = 0; i < clients.size(); i++){
					System.out.println(clients.get(i).getName() + "\n");

				}
			}
		}

	}

}

