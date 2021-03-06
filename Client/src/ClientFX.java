

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
<<<<<<< HEAD
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
||||||| merged common ancestors
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
=======
import javafx.scene.layout.*;
>>>>>>> willDeleteThisBranchter
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;




public class ClientFX extends Application{



	/**Do find out how to add your own puzzles, crtl+f STEP**/

	/**Declare all my buttons and textfields**/
	Button portButton, ipButton, play, connect, quit;
	TextField portInput, ipInput, nameInput;
	MediaPlayer mediaPlayer;
	int port = 5555;
	String ip = "127.0.0.1";
	String clientName = "";
	boolean updatePlayerList;

	/**STEP 1: DECLARE A SCENE FOR YOUR PUZZLE, EACH SCENE IS IT'S OWN PUZZLE**/
	/**ALREADY DONE**/
	Scene DoorScene1, DoorScene2, DoorScene3, DoorScene4;
	/**STEP 2: DECLARE A BUTTON FOR YOUR PUZZLE, THIS WILL NOT BE USED IN THE FINAL IMPLEMENTATION BUT WILL GIVE YOU DIRECT ACCESS TO IT SO YOU CAN DEBUG IT**/
	/**ALREADY DONE**/
	MenuItem door1, door2, door3, door4, testGame;
	Stage primaryStage ; //THIS IS THE STAGE THAT DETERMINES WHAT THE USE IS CURRENTLY LOOKING AT
	Scene startUpScene; //THIS IS THE SCENE THAT YOU SEE ON STARTUP
	ArrayList<Scene> sceneList = new ArrayList<Scene>(); //Might be better as a hashmap but for now, its an arrayList
	Text Score = new Text("Score: 0");




	private ClientConnection  conn;
	private TextArea messages = new TextArea();
	private TextArea playerList = new TextArea();
	int sceneNum = 0;

	void enableButtons(){
		door1.setVisible(true);
		door2.setVisible(true);
		door3.setVisible(true);
		door4.setVisible(true);
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

		/**STEP 3: INITIALIZE YOUR DOOR BUTTONS**/
		/**ALREADY DONE**/
		door1 = new MenuItem("Door #1");
		door2 = new MenuItem("Door #2");
		door3 = new MenuItem("Door #3");
		door4 = new MenuItem("Door #4");
        testGame = new MenuItem("Test Game");


        /**THEY ARE INVISIBLE ON STARTUP, AND BECOME VISIBLE ONCE THE USER CONNECTS...THIS DOESN'T REALLY MATTER, BUT IS THERE ANYWAY**/
		door1.setVisible(false);
		door2.setVisible(false);
		door3.setVisible(false);
		door4.setVisible(false);
        testGame.setVisible(false);

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
				// TODO debugging take out
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
				door4.setVisible(true);
				testGame.setVisible(true);

				conn.startConn(this.clientName);
				/**COMMENTING OUT THIS THREAD FOR NOW UNTIL IT IS BETTER OPTIMIZED, FOR NOW NO DYNAMIC UI ELEMENTS ON THE CLIENT SIDE**/
				buttonThread test = new buttonThread();

				test.start();

				conn.send("Why isn't this working");



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

		/**This HBox contains the buttons for the rock, paper, scissors etc...*/
		/**STEP 3.5: ADD BUTTON TO HBOX OF DOOR BUTTONS**/
		/**ALREADY DONE**/
		//create a drop down menu

		MenuButton dropMenu = new MenuButton("Puzzles");
		dropMenu.getItems().addAll(door1, door2, door3, door4, testGame);

		HBox Doors = new HBox(10, Score, dropMenu);
		Doors.setAlignment(Pos.CENTER);


		/**Unable to implement stage changing in a different thread, so for now, puzzle choosing will be event Driven**/
        testGame.setOnAction(event -> {

            try {
                conn.send("c");
				door1.setVisible(false);
				door2.setVisible(false);
				door3.setVisible(false);
				door4.setVisible(false);
				testGame.setVisible(false);
            }
            catch(Exception e) {
            }


        });



		/**STEP 4: CREATE THE EVENT FOR YOUR BUTTON, THAT WILL INITIALIZE IT AND CHANGE THE STAGE TO YOUR PUZZLE**/
		/**THIS MOST LIKELY WON'T BE USED IN THE FINAL VERSION, BUT IS DONE SO EVERYONE HAS A WAY TO DEBUG THEIR PUZZLES**/
		/**ALREADY DONE**/




		/**door1 will set the stage to a new scene, we can come up with more creative puzzles**/
		door1.setOnAction(event -> {

			try {
				//door1.setVisible(false); Comment this out once program is done
				DoorScene1 = new Scene(createDoor1(), 900, 500); //We create the scene
				sceneList.add(DoorScene1); //We add the scene to an arrayList of Scenes so we can access it later
				primaryStage.setScene(DoorScene1); //We display the scene
			}
			catch(Exception e) {
			}


		});

		/**ADRIAN'S DOOR HERE**/
		door2.setOnAction(event -> {

			try {
				DoorScene2 = new Scene(createDoor2(), 900, 500);
				sceneList.add(DoorScene2);
				primaryStage.setScene(DoorScene2);
			}
			catch(Exception e) {
			}


		});

		/**CHARLY'S DOOR HERE**/
		door3.setOnAction(event -> {

			try {
				DoorScene3 = new Scene(createDoor3(), 900, 500);
				sceneList.add(DoorScene3);
				primaryStage.setScene(DoorScene3);
			}
			catch(Exception e) {
			}


		});

		/**KAVEESHA'S DOOR HERE**/
		door4.setOnAction(event -> {

			try {
				DoorScene4 = new Scene(createDoor4(), 900, 500);
				sceneList.add(DoorScene4);
				primaryStage.setScene(DoorScene4);
			}
			catch(Exception e) {
			}


		});

		/**This is where I organize the layout and design of the UI**/



		/**This HBox contains the the button and textfield for the port, this was done so the two could be close together**/
		HBox Port = new HBox(2, portInput, portButton);
		/**This HBox contains the button and textifeld for IP address, done so the two could be close together, and to make it look better**/
		HBox IP = new HBox(2,ipInput,ipButton);
		HBox Name = new HBox(2, nameInput);



		/**HBox contains two other HBoxes, as well as the buttons to connect, play, and quit the game**/
		HBox middleRow = new HBox(5, Port, IP, Name, connect, play, quit);
		middleRow.setAlignment(Pos.CENTER);

		/**HBox contains the scores of each player**/

		/**Textbox stuff**/
		Text PlayerText = new Text("Players: ");
		//playerList.setText("Oops...buttonThread \nis deactivated...\nSo I don't work");

		VBox PlayerListBox = new VBox(PlayerText, playerList);
		HBox textStuff = new HBox(messages, PlayerListBox);

		/**Finally, VBox contains all the HBoxes**/
		VBox root = new VBox(5, textStuff, Doors, middleRow); //bottomRow is not included as it does not pertain to project 4
		root.setPrefSize(600, 500);
		return root;

	}

	/**STEP 5: CREATE THE ACTUAL PUZZLE**/
	/** TO DO**/

	private Parent createDoor1() {
		ArrayList<sudoku> sudokuList = new ArrayList<sudoku>();
		ArrayList<VBox> sudokuListVBox = new ArrayList<VBox>();
		Button choice1 = new Button("Press me to win the puzzle");
		Button choice2 = new Button("Press me to do nothing");
		Button choice3 = new Button("Press me to do nothing");
		Text puzzleTitleCard = new Text("Challenge 1: Lightning Sudoku!");
		TextField cheatField;


		sudoku s1 = new sudoku();
		s1.setUpperLeft(4,3,1,2);
		s1.setUpperRight(99,99,3,99);
		s1.setLowerLeft(99,99,2,1);
		s1.setLowerRight(2, 99,99,99);
        s1.setUpperLeftAnswers(4,3,1,2);
        s1.setUpperRightAnswers(1,2,99,4);
        s1.setLowerLeftAnswers(3,4,99,99);
        s1.setLowerRightAnswers(99, 1,4,3);



		sudoku s2 = new sudoku();
		s2.setUpperLeft(99,3,4,99);
		s2.setUpperRight(4,99,99,2);
		s2.setLowerLeft(1,99,99,2);
		s2.setLowerRight(99,3,1,99);
        s2.setUpperLeftAnswers(2,99,99,1);
        s2.setUpperRightAnswers(99,1,3,99);
        s2.setLowerLeftAnswers(99,4,3,99);
        s2.setLowerRightAnswers(2, 99,99,4);


		//Puzzle 4 with Sudoku object
		sudoku s3 = new sudoku();
		s3.setUpperLeft(1,2,3,4);
		s3.setUpperRight(1,2,3,4);
		s3.setLowerLeft(1,2,3,4);
		s3.setLowerRight(1,2,3,4);
        s3.setUpperLeftAnswers(1,2,3,4);
        s3.setUpperRightAnswers(1,2,3,4);
        s3.setLowerLeftAnswers(1,2,3,4);
        s3.setLowerRightAnswers(1,2,3,4);



		Button solve = new Button("Solve");
		solve.setAlignment(Pos.CENTER);

		//To give the illusion that there are randomized puzzles

		//puzzle1.setVisible(false);
		//puzzle2.setVisible(false);
		//puzzle3.setVisible(false);

		sudokuList.add(s1); //Add all possible puzzles to a list of puzzles
		sudokuList.add(s2);
		sudokuList.add(s3);
		Collections.shuffle(sudokuList, new Random()); //shuffle the list

        //populates arrayList of VBox's with all the sudoku puzzles, so we can make it visible/invisible. They are all invisible by default
        for(int i = 0; i < sudokuList.size(); i++) {
            sudokuListVBox.add(sudokuList.get(i).makePuzzle());
            sudokuListVBox.get(i).setVisible(false);
        }


		HBox puzzleHolder = new HBox(50, sudokuListVBox.get(0), sudokuListVBox.get(1));
		VBox everything = new VBox(puzzleTitleCard, puzzleHolder, solve);
		sudokuListVBox.get(0).setVisible(true); //get first puzzle in list and make it visible
        cheatField = sudokuList.get(0).getCheat();


		solve.setOnAction(event -> {

			try {
                /**IMPORTANT THING #1: INCREASE YOUR SCORE**/
                //if((c21.getText().equals("3") && c12.getText().equals("2")) || (c21.getText().equals("2") && c12.getText().equals("3")) || (d13.getText().equals("3") && d32.getText().equals("8")) || (f11.getText().equals("1") && f12.getText().equals("2") && f22.getText().equals("4") && g11.getText().equals("3") && g12.getText().equals("4") && h12.getText().equals("1")) && h21.getText().equals("4") && h22.getText().equals("3")){
                if (sudokuList.get(0).checkAnswers() == true) {
                    conn.score++;
                    Score.setText("Score: " + conn.score); //Updates Score Text on UI
                    primaryStage.setScene(sceneList.get(0)); //Sets scene back to the primary stage
                    conn.send("w"); //Sends a w to the server to let it know that someone WON
                } else
                    solve.setText("WRONG!!!...Try again!");
            }
			catch(Exception e){
                }

		});

        cheatField.setOnAction(event -> {

            try {
                if(cheatField.getText().equals("c"))
                    sudokuList.get(0).cheat();
            }
            catch(Exception e) {
            }

        });


		return everything;
	}

	/**ADRIAN'S PUZZLE HERE**/
	Button createImage(String s){
		Button b = new Button();
		Image i = new Image(s);
		ImageView v = new ImageView(i);
		v.setFitHeight(200);
		v.setFitWidth(210);
		v.setPreserveRatio(true);
		b.setGraphic(v);
		//b.setDisable(true);

		return b;
	}

	private Parent createDoor2() {
		BorderPane background = new BorderPane();
		background.setStyle("-fx-background-color: #654321;");

		//Create four pictures to display
		Button clue1 = createImage("pictures/road.jpg");
		Button clue2 = createImage("pictures/horses.jpg");
		Button clue3 = createImage("pictures/horseintheback.jpg");
		Button clue4 = createImage("pictures/billyray.png");
		Button adrianCheck = new Button("Check Answer");
		Button playMusic = new Button("Play Music");
		Button adrianQuit = new Button("Quit Puzzle");



		Text instructions = new Text("Guess the song by the pictures!");
		instructions.setScaleX(3);
		instructions.setScaleY(3);


		TextField answerField = new TextField("Enter Answer Here");
		answerField.setPrefWidth(200);

		playMusic.setOnAction(event -> {
			String path = "/Users/adrianzavala/Desktop/Project5/Client/src/sounds/OldTownRoad.mp3";
			Media media = new Media(new File(path).toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
		});

		adrianCheck.setOnAction(event -> {
			try {
				//get input for the textfield
				String adrianPuzzle = answerField.getText();
				answerField.clear();

				//make input lowercase to check for correctness
				adrianPuzzle = adrianPuzzle.toLowerCase();

				//Check if input is correct
				if( adrianPuzzle.equals("old town road")){
					conn.score++;
					Score.setText("Score: " + conn.score);
					primaryStage.setScene(sceneList.get(0));
					conn.send("w"); // not updating the clients scoreboard
					door2.setDisable(true);
				}
				else{
					answerField.setText("Wrong Answer");
				}
			}
			catch(Exception e) {

			}

		});

		adrianQuit.setOnAction(event -> {
			primaryStage.setScene(sceneList.get(0));
			door2.setDisable(true);
		});


		HBox choiceHBox = new HBox(5, clue1, clue2, clue3, clue4);
		VBox top = new VBox(5, instructions);
		HBox bottom = new HBox(5, answerField, playMusic, adrianCheck, adrianQuit);

		choiceHBox.setAlignment(Pos.CENTER);
		background.setCenter(choiceHBox);

		top.setAlignment(Pos.BASELINE_CENTER);
		top.setTranslateY(10);
		background.setTop(top);

		background.setBottom(bottom);
		bottom.setAlignment(Pos.BASELINE_CENTER);

		return background;
	}

	/**CHARLY'S PUZZLE HERE**/
	private Parent createDoor3() {
//		Button choice1 = new Button("Press me to win the puzzle");
//		Button choice2 = new Button("Press me to do nothing");
//		Button choice3 = new Button("Press me to do nothing");
		Text puzzle = new Text("Click the buttons until they are all green, \nwhen done go to checkWinner to confirm");

		VBox centerScene = new VBox();
		ArrayList<HBox> rowButton = new ArrayList<>();
		//ArrayList<Button> myButton = new ArrayList<>();
		ArrayList<ArrayList<Button>> myButton = new ArrayList<>();
		for(ArrayList<Button> elem : myButton){
			elem = new ArrayList<Button>();
		}

		for(int i=0;i<5;i++){//this whole block creates a 5X5 array of buttons and adds them to the scene
			myButton.add(new ArrayList<Button>() );
			rowButton.add(new HBox() );
			for(int j=0;j<5;j++){
				Button temp = new Button("red");//Integer.toString((i*5)+j) );
				temp.setPrefSize(50,50);

				myButton.get(i).add(temp);
				rowButton.get(i).getChildren().addAll(myButton.get(i).get(j) );
				//rowButton.get(myButton.get(i).get(j) );
			}
			centerScene.getChildren().addAll(rowButton.get(i) );
		}

		myButton.get(0).get(0).setOnAction(new EventHandler<ActionEvent>() {//button that changes them to green
			@Override
			public void handle(ActionEvent event) {
				for(ArrayList<Button> elem: myButton){
					for(Button ele : elem){
						ele.setText("green");
					}
				}
			}
		});

		Button checkWinner = new Button("Check Winner");
		checkWinner.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(ArrayList<Button> elem : myButton){
					for(Button ele : elem){
						if(!ele.getText().equals("green") ){
							return;
						}
					}

					conn.score++;
					Score.setText("Score: "+conn.score );
					primaryStage.setScene(sceneList.get(0) );
				}
			}
		});

//		choice1.setOnAction(event -> {
//
//			try {
//				conn.score++;//increments score
//				Score.setText("Score: " + conn.score);//set text
//				primaryStage.setScene(sceneList.get(0));//call scene
//			}
//			catch(Exception e) {
//			}
//
//		});

		HBox choiceHBox = new HBox(centerScene, checkWinner);
		VBox Door3Box = new VBox(puzzle, choiceHBox);
		BorderPane mainScene = new BorderPane(Door3Box);
		return mainScene;
	}

	/**KAVEESHA'S PUZZLE HERE**/
	private Parent createDoor4() {
		Button choice1 = new Button("Press me to win the puzzle");
		Button choice2 = new Button("Press me to do nothing");
		Button choice3 = new Button("Press me to do nothing");
		Text puzzle = new Text("what is the airspeed of an unladen swallow");

		choice1.setOnAction(event -> {

			try {
				conn.score++;
				Score.setText("Score: " + conn.score);
				primaryStage.setScene(sceneList.get(0));
			}
			catch(Exception e) {
			}

		});

		HBox choiceHBox = new HBox(10, choice1, choice2, choice3);
		VBox Door4Box = new VBox(puzzle, choiceHBox);

		return Door4Box;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
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
		Scene scene1 = new Scene(createDoor1(), 900, 500);
		Scene scene2 = new Scene(createDoor2(), 900, 500);
		Scene scene3 = new Scene(createDoor3(), 900, 500);
		Scene scene4 = new Scene(createDoor4(), 900, 500);
		Scene finalScene = startUpScene;
		sceneList.add(scene1);
		sceneList.add(scene2);
		sceneList.add(scene3);
		sceneList.add(scene4);
		sceneList.add(finalScene);

		primaryStage.setScene(startUpScene);
		primaryStage.setTitle("Puzzle Gauntlet");
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


/**Buttonthread handles all the dynamic stuff, like scores and whether or not a client can connect to the server**/
	class buttonThread extends Thread {
		public void run() {
			try {
				int numCheck = 0;
				while(true){
					if(conn.updatePlayerList == true){
						playerList.setText(conn.playerList);
						conn.updatePlayerList = false;
					}
					if(numCheck != conn.score){
						numCheck = conn.score;
						Score.setText("Score: " + conn.score);

					}
					if(conn.gameStart == true) {
						//sceneNum++;
						System.out.println("Sync");
						//primaryStage.setScene(sceneList.get(0));
						enableButtons();
						conn.gameStart = false;
					}
					this.sleep(1000);

				}
			}
			catch (Exception e) {
				System.out.println("Oops in buttonThread");

			}

		}
	}

}
