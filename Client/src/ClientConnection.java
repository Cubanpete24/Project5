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
	boolean gameStart = false;
	boolean gameEnd = false;
	boolean sudokuGameOn;
	int jerkFactor = 1100; //Used to determine how fast the sudoku puzzle moves around
	Timer playerTime = new Timer();
	boolean makeDropDownVisible = false;

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
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());){
				callback.accept("You are about to enter the Puzzle Gauntlet\n\n4 players enter, and compete to see who can complete all the puzzles first\nFor now, you should have direct access to your puzzle so that you can debug it, but in the final game\nthe door buttons will be invisible, until 4 players are connected.");


				this.socket = socket;
				this.out = out;
				socket.setTcpNoDelay(true);
				//Sends the client to the server
				try {
					this.out.writeObject("Change name");
					this.out.writeObject(clientName);
					this.out.writeObject("c");

				}
				catch (Exception e){
					callback.accept("cannot write the name. Try again");
				}


				/**When the client recieves either of these messages, the playerNumber they are assigned changes**/
				while(true) {
					Serializable data = (Serializable) in.readObject();
					if(data.equals("g") ){
						callback.accept("Server has notified client to start game");
						gameStart = true;
						playerTime.start();
					}
					else if(data.equals("u") ) {
						//playerList is a string
						playerList = (String) in.readObject();
						updatePlayerList = true;
					}
					else if(data.equals("exit") ){
						System.out.println("Client has received string to exit");
						//return;
					}
					else if(data.equals("play again")) {
						callback.accept("Server has notified client to start game again!");
						playerTime = new Timer();
						makeDropDownVisible = true;
						playerTime.start();
					}
					else
						callback.accept(data);
				}
			}
			catch(Exception e) {
					callback.accept("connection to server lost, please restart client");
			}
		}//end run method
	}// end ConnectionThread

	class Timer extends Thread {
		String updateString = "test";
//		boolean timeExpired = false;
		int startingSecond = 180;//3 minutes, 180 seconds seconds;

		int counter = startingSecond;
		int mins;
		int seconds;

		Timer(){
			mins = counter / 60;
			seconds = counter - (mins * 60);
			updateString = "Rem min:" + mins + " sec:" + seconds;
		}
//		public boolean timeUp(){
//			return this.timeExpired;
//		}
		public String getString(){
			return this.updateString;
		}

		public void setTime(int setTime) {
			this.counter = setTime;
		}


		public void run(){
			counter = startingSecond;
			//int mins;
			//int seconds;
			callback.accept("Countdown from " + startingSecond + " to " + 0);
			try {
				Thread.sleep(1000);
			}
			catch (Exception E) {
				System.out.println("Could not stop for 1 second");
			}

			System.out.println("Countdown from "+ startingSecond+ " to "+ 0);
			while(counter>0) {
				mins = counter / 60;
				seconds = counter - (mins * 60);

				//System.out.println("Remaining Min:" + mins + " sec:" + seconds);
				callback.accept("Remaining Min:" + mins + " sec:" + seconds);
				//updateString = "Rem min:" + mins + " sec:" + seconds;
				try {
					counter--;
					Thread.sleep(1000);
				} catch (Exception E) {
					callback.accept("could not count down by one second");
				}

			}
			callback.accept("Time is up!");
			gameEnd = true;
			System.out.println("Time is up!");
		}//run
	}//end timer class
}	

