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
	boolean updatePlayerList = false;

	int countPlayAgain = 0;
	String winnerName = "";
	int winnerScore = 0;

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
	public void send(Serializable data, ArrayList<ClientThread> clients) {
		synchronized(this) {

			for (int i = 0; i < clients.size(); i++) {
				try {
					clients.get(i).out.writeObject(data);
				}
				catch(Exception E){
					//do nothing
				}
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
	public int computeWinnerIndex() {
		int index = 0;
		int max = 0;
		for(int whichPlayer = 0; whichPlayer < clients.size(); whichPlayer++) {
			if(clients.get(whichPlayer).score > max) {
				max = clients.get(whichPlayer).score;
				index = whichPlayer;
			}
 		}
		return index;
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
					if(clients.size() < 5) {
						ClientThread t1 = new ClientThread(server.accept());
						clients.add(t1);
						superClients.add(t1);
						t1.start();
						callback.accept("Checking name " + t1.clientName);
					}
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
				updatePlayerList = true;
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
						this.score++;
						updatePlayerList = true;
					}
					else if((data.equals("p"))){
						callback.accept(clientName + " won a game of Pitch");
						String message = clientName + " has won a game of Pitch";
						send(message, clients);
						this.score++;
						updatePlayerList = true;
					}
					else if((data.equals("l"))){
						callback.accept(clientName + " lost a game of Pitch");
						String message = "Hey everyone, " + clientName + " forgot how to play Pitch";
						send(message, clients);
						this.score--;
						updatePlayerList = true;
					}

					else if((data.equals("increment play again variable"))) {
						countPlayAgain++;
						if(countPlayAgain == 1) {
							callback.accept("waiting for 3 more people to play again");
							send("waiting for 3 more people to play again", clients);

						}
						if(countPlayAgain == 2) {
							callback.accept("waiting for 2 more people to play again");
							send("waiting for 2 more people to play again", clients);

						}
						if(countPlayAgain == 3) {
							callback.accept("waiting for 1 more person to play again");
							send("waiting for 1 more person to play again", clients);

						}
						if(countPlayAgain == 4) {
							//resets all of the clients scores to zero
							for(int i = 0; i < clients.size(); i++) {
								clients.get(i).score = 0;
							}
							updatePlayerList = true;
							countPlayAgain = 0;
							send("play again", clients);
						}
					}
                    else if((data.equals("c"))){
                        updatePlayerList = true;
                        if(clients.size() < 2) {
                            callback.accept("Server awaiting 3 more players...");
							send("waiting for 3 more people to join", clients);

						}
                        else if(clients.size() < 3) {
                            callback.accept("Server awaiting 2 more players...");
							send("waiting for 2 more people to join", clients);


						}
                        else if(clients.size() < 4) {
							callback.accept("Server awaiting 1 more player...");
							send("waiting for 1 more person to join", clients);
						}
                        else if(clients.size() == 4) {
                            send("g", clients);
							send("On your mark...get set...Go! The one to complete the most puzzles in 3 minutes wins!", clients);

						}
                    }
					else if((data.equals("calculate winner"))) {
						callback.accept("calculating the winner...");
						int indexOfWinner = computeWinnerIndex();
						winnerName = clients.get(indexOfWinner).clientName;
						winnerScore = clients.get(indexOfWinner).score;
						callback.accept("The winner was:  " + winnerName + " with a score of " + winnerScore);
						send("The winner was " + winnerName + " with score: " + winnerScore, clients);
					}
					//this appends what happened during the game to the server gui
					else {
						callback.accept(clientName + " Says: " + data);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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

