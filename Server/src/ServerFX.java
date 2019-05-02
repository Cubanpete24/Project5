import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ServerFX extends Application{

    /**TEST TEST TEST Where I declare all the buttons, texts, and text fields used for the Server program**/
	Button portButton, onButton;
	Text scoreCard, playerScores;
	TextField portInput;
	int port = 5555;

	Button teleportToGame = new Button("Teleport to Game");
	Stage myStage ;
	Scene startupScene;

	private ServerConnection  conn;
	private TextArea messages = new TextArea();
	private TextArea playerList = new TextArea();


	private Parent createContent() {
		messages.setPrefHeight(250);
		messages.setPrefWidth(200);

		/**UI: Where I initialize all the new buttons, and text fields**/
		portInput = new TextField();
		portInput.setPrefWidth(50);
		portInput.setText("5555");

		scoreCard = new Text("Players");
		scoreCard.setScaleX(2);
		scoreCard.setScaleY(1.8);

		playerScores = new Text("Waiting for players...");
		portButton = new Button("port");
		onButton = new Button("Turn on Server");






		/**When this button is pressed, it changes the port the server is listening to**/
		portButton.setOnAction(event -> {
			Button b = (Button) event.getSource();
			try {
				port = Integer.parseInt(portInput.getText());
				System.out.println("new port: " + port);
			}
			catch(Exception e) {
			}
		});

		teleportToGame.setOnAction(event -> {
			try {

			}
			catch(Exception e) {
			}
		});


    /**This event controls the server on/off button, when you press "Turn on Server", it creates a new Server listening
     * to the port you entered, by default it is 5555. When the button is pressed, the text changes, and when it is
     * pressed again, it closes the window**/
		onButton.setOnAction(event -> {
			try {
				if(onButton.getText().equals("Turn off Server"))
					System.exit(0);
				conn = createServer();
				conn.startConn();
				onButton.setText("Turn off Server");
				buttonThread test = new buttonThread();
				test.start();
			}
			catch(Exception e) {
			}
		});

        /**This is where I organize the layout of the UI with H and VBoxes**/
		HBox textStuff = new HBox(messages, playerList);

		HBox stuff = new HBox(4, onButton);

		HBox portStuff = new HBox(4, portInput, portButton);
		VBox middleStuff = new VBox(1, scoreCard, playerScores);
		middleStuff.setAlignment(Pos.CENTER);

		HBox row = new HBox(70, portStuff, middleStuff, stuff);
		row.setAlignment(Pos.BOTTOM_CENTER);

		VBox root = new VBox(20, messages, row);
		root.setPrefSize(600, 400);
		
		return root;
		
				
		
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	/**method displays the UI on startup**/
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		startupScene = new Scene(createContent());
		primaryStage.setScene(startupScene);
		primaryStage.show();
		//conn.startConn();

	}

	/**PLEASE WORK No matter what, init always runs whenever you open the program, for more control, I opted not to use it **/
	@Override
	public void init() throws Exception{
		//conn.startConn();
	}

	@Override
	public void stop() throws Exception{
		try {
			conn.closeConn();
		}
		catch (Exception e) {
			/**informative error message**/
			System.out.println("network connection never made");

		}
	}

	/**method creates the server**/
	private Server createServer() {
		return new Server(port, data -> {
			Platform.runLater(() -> {
				messages.appendText(data.toString() + "\n");
			});
		});
	}

	/**This thread handles the dynamic UI elements, such as the score, and players currently connected to the server**/
	class buttonThread extends Thread {
				public void run() {
					try {

						/**This code block handles displaying who is currently connected to the server**/
						String inServer;
						int size = conn.clients.size();
						while(true){
							if(conn.updatePlayerList == true){
								inServer = ""; //Clears string so that we can reuse it
								for(int i = 0; i < conn.superClients.size(); i++){ //For loop iterates through entire list of people who are connected
									inServer += conn.superClients.get(i).clientName + ": " + conn.superClients.get(i).score; //concatenates that name followed by a new line
									inServer += "\n";
								}
								conn.updatePlayerList = false;
								playerScores.setText(inServer);
								conn.send("u", conn.superClients);
								conn.send(inServer, conn.superClients);
							}
							this.sleep(1000); //sleep done to give the program a little break, without the sleep there is a nullpointer exception

						}
					}
			catch (Exception e) {
			    /**informative error message**/
				System.out.println("Oops");

			}

		}
	}

}
