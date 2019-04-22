

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;


public class ClientFX extends Application{

	/**Do find out how to add your own puzzles, crtl+f STEP**/

	/**Declare all my buttons and textfields**/
	Button portButton, ipButton, play, connect, quit;
	TextField portInput, ipInput, nameInput;
	int port = 5555;
	String ip = "127.0.0.1";
	String clientName = "";
	boolean updatePlayerList;

	/**STEP 1: CREATE A DOOR BUTTON FOR YOUR PUZZLE**/
	Button door1, door2, door3;
	Scene Door1, Door2, Door3;
	Stage primaryStage ;
	Scene startUpScene;
	ArrayList<Scene> sceneList = new ArrayList<Scene>(); //This will be better as a hashmap
	Text Score = new Text("Score: 0");


	private ClientConnection  conn;
	private TextArea messages = new TextArea();
	private TextArea playerList = new TextArea();



	private Parent createContent() {
		/**This is where I organize the layout and design of the UI**/

		/**This HBox conins the buttons for the rock, paper, scissots etc...*/
		HBox Doors = new HBox(10);
		Doors.setAlignment(Pos.CENTER);

		/**This HBox contains the the button and textfield for the port, this was done so the two could be close together**/
		HBox Port = new HBox(2, portInput, portButton);
		/**This HBox contains the button and textifeld for IP address, done so the two could be close together, and to make it look better**/
		HBox IP = new HBox(2, ipInput, ipButton);
		HBox Name = new HBox(2, nameInput);


		/**HBox contains two other HBoxes, as well as the buttons to connect, play, and quit the game**/
		HBox middleRow = new HBox(5, Port, IP, Name, connect, play, quit);
		middleRow.setAlignment(Pos.CENTER);

		/**HBox contains the scores of each player**/
//		HBox bottomRow = new HBox(60, player1Score, player2Score);
//		bottomRow.setAlignment(Pos.CENTER);

		/**Textbox stuff**/
		Text PlayerText = new Text("Players:");

		VBox PlayerListBox = new VBox(PlayerText, playerList);
		HBox textStuff = new HBox(messages, PlayerListBox);

		/**Finally, VBox contains all the HBoxes**/
		VBox root = new VBox(5, textStuff, Doors, middleRow); //bottomRow is not included as it does not pertain to project 4
		root.setPrefSize(600, 500);
		return root;
	}


	/**Creates the content for the UI**/
	private Parent createStartup() {

		/**initialize buttons and textfields, I initialize the rest in start**/
		messages.setPrefHeight(350);
		messages.setPrefWidth(550);

		playerList.setPrefHeight(350);
		playerList.setPrefWidth(150);

		portInput = new TextField("5555");
		portInput.setPrefWidth(50);

		ipInput = new TextField("127.0.0.1");
		ipInput.setPrefWidth(70);

		nameInput = new TextField("Enter Name");
		nameInput.setPrefWidth(90);

		messages.appendText( "Welcome to Project 5, please enter your port and IP address\nIf you do not have one, the default will be chosen when you press connect\nOnce you enter your name, you will be able to connect\n");
		//disableButtons();
		play.setVisible(false);

		door1 = new Button("Door #1");
		door2 = new Button("Door #2");
		door3 = new Button("Door #3");

		door1.setVisible(false);
		door2.setVisible(false);
		door3.setVisible(false);



		//Generic handler for the client choices
		EventHandler<ActionEvent> buttonSendRPSLS = event -> {
			Button b = (Button) event.getSource();
			String message = b.getText();

			try {
				conn.send(message);
			}
			catch(Exception e) {
			}
		};

		/**changes port to value in Port textfield**/
		portButton.setOnAction(event -> {

			try {
				port = Integer.parseInt(portInput.getText());
				System.out.println("new port: " + port);
			}
			catch(Exception e) {
			}
		});

		/**changes port**/
		portInput.setOnAction(event -> {

			try {
				port = Integer.parseInt(portInput.getText());
				System.out.println("new port: " + port);
			}
			catch(Exception e) {
			}
		});

		/**changes ip address**/
		ipButton.setOnAction(event -> {

			try {
				ip = ipInput.getText();
				System.out.println("new ip: " + ip);
			}
			catch(Exception e) {
			}
		});

		ipInput.setOnAction(event -> {

			try {
				ip = ipInput.getText();
				System.out.println("new ip: " + ip);
			}
			catch(Exception e) {
			}
		});

		nameInput.setOnAction(event -> {
			try {
				//TODO check if name is in use
				//true
				clientName = nameInput.getText();
				nameInput.clear();
				messages.appendText("Your name is now: " + clientName + "\n");
				connect.setVisible(true);
				//false - try again and do not store
			}
			catch(Exception e) {
				// TODO degugging take out
				messages.appendText("error for name");
			}
		});

		/**connects to server**/
		connect.setOnAction(event -> {

			try {
				conn = createClient();


				portButton.setVisible(false);
				ipButton.setVisible(false);
				portInput.setVisible(false);
				ipInput.setVisible(false);
				connect.setVisible(false);
				nameInput.setVisible(false);
				door1.setVisible(true);
				door2.setVisible(true);
				door3.setVisible(true);

				conn.startConn(this.clientName);

				buttonThread test = new buttonThread();
				test.start();

				//root.setPrefSize(600, 200);

			}
			catch(Exception e) {
				messages.appendText("connection could not be started");
			}
		});


		play.setOnAction(event -> {

			try {
				conn.makeCall("Please wait for the other player to decide, in the meantime, you can make your selection");
				conn.hideButtons = false;


				play.setVisible(false);
				//conn.send("Other play will play again");
			}
			catch(Exception e) {
			}
		});


		quit.setOnAction(event -> {

			try {
				conn.send("Quit");
			//System.exit(0);
			}
			catch(Exception e) {
			}

			System.exit(0);

		});

		/**door1 will set the stage to a new scene, we can come up with more creative puzzles**/
		door1.setOnAction(event -> {

			try {
				/**First we initialize the buttons and text/textfields that the puzzle needs**/
				Door1 = new Scene(createPuzzle1(), 900, 500);
				sceneList.add(Door1);
				primaryStage.setScene(Door1);
			}
			catch(Exception e) {
			}

			//
			//System.exit(0);

		});

		/**This is where I organize the layout and design of the UI**/

		/**This HBox conins the buttons for the rock, paper, scissots etc...*/
		HBox Doors = new HBox(10, Score, door1, door2, door3);
		Doors.setAlignment(Pos.CENTER);

		/**This HBox contains the the button and textfield for the port, this was done so the two could be close together**/
		HBox Port = new HBox(2, portInput, portButton);
		/**This HBox contains the button and textifeld for IP address, done so the two could be close together, and to make it look better**/
		HBox IP = new HBox(2,ipInput,ipButton);
		HBox Name = new HBox(2, nameInput);



		/**HBox contains two other HBoxes, as well as the buttons to connect, play, and quit the game**/
		HBox middleRow = new HBox(5, Port, IP, Name, connect, play, quit);
		middleRow.setAlignment(Pos.CENTER);

		/**HBox contains the scores of each player**/
//		HBox bottomRow = new HBox(60, player1Score, player2Score);
//		bottomRow.setAlignment(Pos.CENTER);

		/**Textbox stuff**/
		Text PlayerText = new Text("Players:");

		VBox PlayerListBox = new VBox(PlayerText, playerList);
		HBox textStuff = new HBox(messages, PlayerListBox);

		/**Finally, VBox contains all the HBoxes**/
		VBox root = new VBox(5, textStuff, Doors, middleRow); //bottomRow is not included as it does not pertain to project 4
		root.setPrefSize(600, 500);
		return root;

	}

	private Parent createPuzzle1() {
		Button choice1 = new Button("Press me to win the puzzle");
		Button choice2 = new Button("Press me to do nothing");
		Button choice3 = new Button("Press me to do nothing");
		Text puzzle = new Text("what is the airspeed of an unladen swallow");

		choice1.setOnAction(event -> {

			try {
				/**First we initialize the buttons and text/textfields that the puzzle needs**/
				Door1 = new Scene(createPuzzle1(), 900, 500);
				sceneList.add(Door1);
				conn.score++;
				primaryStage.setScene(sceneList.get(0));
			}
			catch(Exception e) {
			}

			//
			//System.exit(0);

		});


		/**THis looks a lot liek a quiz but I think we can get more creative**/
		HBox choiceHBox = new HBox(10, choice1, choice2, choice3);
		VBox Door1Box = new VBox(puzzle, choiceHBox);



		return Door1Box;
	}

	/**I never used this method but it can be used in conjunction with the metod disableButtons() below**/

	/**I never used this method but it disables all the buttons, functionality is identical to removeButtons() below**/

	/**The best way to make sure the user doesn't break your program is deny them that privlege
	 * This method removes the Rock, paper, scissors buttons whenever a player reaches 3 points
	 */

	/**This method brings the buttons back after a user decides to play again**/

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	public ImageView createPictureOnButton(String dir) {
		Image image = new Image(dir);
		ImageView view = new ImageView(image);
		view.setFitHeight(70);
		view.setFitWidth(70);
		view.setPreserveRatio(true);
		return view;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		portButton = new Button("Port");
		ipButton = new Button("ip address");
		play = new Button("New Game");
		quit = new Button("Quit");

		connect = new Button("connect");
		connect.setVisible(false);
		startUpScene = new Scene(createStartup(), 900, 500);
		sceneList.add(startUpScene);
		primaryStage.setScene(startUpScene);
		this.primaryStage = primaryStage;
		primaryStage.show();

	}
	
	@Override
	public void init() throws Exception{
		//conn.startConn();
	}
	
	@Override
	public void stop() {
		try {
			conn.closeConn();
		}
		catch(Exception e){
			System.out.println("No connection to a server was ever made");
		}
	}

	
	private Client createClient() {
		return new Client(ip, port, data -> {
			Platform.runLater(()->{
				messages.appendText(data.toString() + "\n");
			});
		}, "Paul");
	}

/**BUttonthread handles all the dynamic stuff, like scores and whether or not a client can connect to the server**/
	class buttonThread extends Thread {
		public void run() {
			try {
				int numCheck = 0;
				while(true){
					if(conn.updatePlayerList = true){
						playerList.setText(conn.playerList);
						conn.updatePlayerList = false;
					}
					if(numCheck != conn.score){
						numCheck = conn.score;
						Score.setText("Score: " + conn.score);

					}
					this.sleep(1000);

				}
			}
			catch (Exception e) {
				System.out.println("Oops");

			}

		}
	}

}
