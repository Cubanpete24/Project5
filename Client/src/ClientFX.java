

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
import javafx.scene.layout.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static javafx.geometry.Pos.*;
import static javafx.scene.paint.Color.WHITE;


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
	Scene DoorScene1, DoorScene2, DoorScene3, DoorScene4, DoorScene5, DoorScene6, DoorScene7, DoorScene8;
	/**STEP 2: DECLARE A BUTTON FOR YOUR PUZZLE, THIS WILL NOT BE USED IN THE FINAL IMPLEMENTATION BUT WILL GIVE YOU DIRECT ACCESS TO IT SO YOU CAN DEBUG IT**/
	/**ALREADY DONE**/
	MenuItem door1, door2, door3, door4,door5, door6, door7, door8, testGame;
	//Button door1, door2, door3, door4, door5, door6, testGame;
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
		door5.setVisible(true);
		door6.setVisible(true);
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
		door5 = new MenuItem("Door #5");
		door6 = new MenuItem("Door #6");
		door7 = new MenuItem("Door #7");
		door8 = new MenuItem("Door #8");

		testGame = new MenuItem("Test Game");
		//door1 = new Button("Door #1");
		//door2 = new Button("Door #2");
		//door3 = new Button("Door #3");
		//door4 = new Button("Door #4");
		//door5 = new Button("Door #5");
		//door6 = new Button("Door #6");
        //testGame = new Button("Test Game");


        /**THEY ARE INVISIBLE ON STARTUP, AND BECOME VISIBLE ONCE THE USER CONNECTS...THIS DOESN'T REALLY MATTER, BUT IS THERE ANYWAY**/
		door1.setVisible(false);
		door2.setVisible(false);
		door3.setVisible(false);
		door4.setVisible(false);
		door5.setVisible(false);
		door6.setVisible(false);
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
				door5.setVisible(true);
				door6.setVisible(true);
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
		dropMenu.getItems().addAll(door1, door2, door3, door4, door5, door6, door7, door8, testGame);

		HBox Doors = new HBox(10, Score, dropMenu);
		//HBox Doors = new HBox(10, Score, door1, door2, door3, door4, door5, door6, testGame);
		Doors.setAlignment(CENTER);


		/**Unable to implement stage changing in a different thread, so for now, puzzle choosing will be event Driven**/
        testGame.setOnAction(event -> {

            try {
                conn.send("c");
				door1.setVisible(false);
				door2.setVisible(false);
				door3.setVisible(false);
				door4.setVisible(false);
				door5.setVisible(false);
				door6.setVisible(false);
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
				primaryStage.setTitle("Sudoku!");

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

		door5.setOnAction(event -> {
			try {
				DoorScene5 = new Scene(createDoor5(), 900, 500);
				sceneList.add(DoorScene5);
				primaryStage.setScene(DoorScene5);
			}
			catch(Exception e) {
			}
		});

		door6.setOnAction(event -> {

			try {
				DoorScene6 = new Scene(createDoor6(), 900, 500);
				sceneList.add(DoorScene6);
				primaryStage.setScene(DoorScene6);
			}
			catch(Exception e) {
			}
		});

		door7.setOnAction(event -> {

			try {
				DoorScene7 = new Scene(createDoor7(), 900, 500);
				sceneList.add(DoorScene7);
				primaryStage.setScene(DoorScene7);
				primaryStage.setTitle("Pitch: The Game");
			}
			catch(Exception e) {
			}
		});

		door8.setOnAction(event -> {

			try {
				DoorScene8 = new Scene(createDoor8(), 900, 500);
				sceneList.add(DoorScene8);
				primaryStage.setScene(DoorScene8);
				primaryStage.setTitle("¡nʞopnS");

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
		middleRow.setAlignment(CENTER);

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

		Text puzzleTitleCard = new Text("Welcome to Regular Sudoku!!");
		TextField cheatField;

		Button solve = new Button("Solve");
		solve.setAlignment(CENTER);

		Button giveUp = new Button("Give up");
		Button sabotage = new Button("Sabotage");

		BorderPane gamePane = new BorderPane();
		Image bg = new Image("pictures/giphy.gif");
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage bgImg = new BackgroundImage(bg, null, null, null, backgroundSize);
		Background background = new Background(bgImg);



		//background.set
		gamePane.setBackground(background);




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
		s3.setUpperLeft(2,4,99,99);
		s3.setUpperRight(99,99,2,99);
		s3.setLowerLeft(99,99,1,99);
		s3.setLowerRight(99,99,4,2);
        s3.setUpperLeftAnswers(99,99,3,1);
        s3.setUpperRightAnswers(3,1,99,4);
        s3.setLowerLeftAnswers(4,2,99,3);
        s3.setLowerRightAnswers(1,3,99,99);

		sudoku s4 = new sudoku();
		s4.setUpperLeft(99,99,99,99);
		s4.setUpperRight(99,99,1,4);
		s4.setLowerLeft(2,1,99,99);
		s4.setLowerRight(99,99,99,99);
		s4.setUpperLeftAnswers(1,4,3,2);
		s4.setUpperRightAnswers(3,2,99,99);
		s4.setLowerLeftAnswers(99,99,4,3);
		s4.setLowerRightAnswers(4,3,2,1);

		sudoku s5 = new sudoku();
		s5.setUpperLeft(99,99,99,99);
		s5.setUpperRight(3,99,2,1);
		s5.setLowerLeft(99,4,2,1);
		s5.setLowerRight(99,99,99,99);
		s5.setUpperLeftAnswers(1,2,4,3);
		s5.setUpperRightAnswers(99,4,99,99);
		s5.setLowerLeftAnswers(3,99,99,99);
		s5.setLowerRightAnswers(1,2,4,3);





		//To give the illusion that there are randomized puzzles

		//puzzle1.setVisible(false);
		//puzzle2.setVisible(false);
		//puzzle3.setVisible(false);

		sudokuList.add(s1); //Add all possible puzzles to a list of puzzles
		sudokuList.add(s2);
		sudokuList.add(s3);
		sudokuList.add(s4);
		sudokuList.add(s5);


		Collections.shuffle(sudokuList, new Random()); //shuffle the list

        //populates arrayList of VBox's with all the sudoku puzzles, so we can make it visible/invisible. They are all invisible by default
        for(int i = 0; i < sudokuList.size(); i++) {
            sudokuListVBox.add(sudokuList.get(i).makePuzzle());
            sudokuListVBox.get(i).setVisible(false);
        }


		//HBox puzzleHolder = new HBox(50, sudokuListVBox.get(0), sudokuListVBox.get(1), sudokuListVBox.get(2), sudokuListVBox.get(3), sudokuListVBox.get(4));
		HBox puzzleHolder = new HBox(50, sudokuListVBox.get(0));
		HBox buttonHolder = new HBox(50, solve, giveUp);
        VBox everything = new VBox(30, puzzleTitleCard, puzzleHolder, buttonHolder);
        everything.setAlignment(CENTER);
		puzzleHolder.setAlignment(CENTER);
		buttonHolder.setAlignment(CENTER);


		sudokuListVBox.get(0).setVisible(true); //get first puzzle in list and make it visible
        cheatField = sudokuList.get(0).getCheat();


		solve.setOnAction(event -> {

			try {
                /**IMPORTANT THING #1: INCREASE YOUR SCORE**/
                //if((c21.getText().equals("3") && c12.getText().equals("2")) || (c21.getText().equals("2") && c12.getText().equals("3")) || (d13.getText().equals("3") && d32.getText().equals("8")) || (f11.getText().equals("1") && f12.getText().equals("2") && f22.getText().equals("4") && g11.getText().equals("3") && g12.getText().equals("4") && h12.getText().equals("1")) && h21.getText().equals("4") && h22.getText().equals("3")){
                if (sudokuList.get(0).checkAnswers() == true) {
                    conn.score++;
                    Score.setText("Score: " + conn.score); //Updates Score Text on UI
					primaryStage.setTitle("Puzzle Gauntlet");
					//door1.setDisable(true);
					primaryStage.setScene(sceneList.get(0)); //Sets scene back to the primary stage
                    conn.send("w"); //Sends a w to the server to let it know that someone WON
					//primaryStage.setTitle("Puzzle Gauntlet");
					//door1.setDisable(true);


				} else
                    solve.setText("WRONG!!!...Try again!");
            }
			catch(Exception e){
                }

		});

		giveUp.setOnAction(event -> {

			try {
				/**IMPORTANT THING #1: INCREASE YOUR SCORE**/
					primaryStage.setTitle("Puzzle Gauntlet");
					door1.setDisable(true);
					primaryStage.setScene(sceneList.get(0)); //Sets scene back to the primary stage
					door1.setDisable(true);
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

		gamePane.setCenter(everything);
		return gamePane;
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

		choiceHBox.setAlignment(CENTER);
		background.setCenter(choiceHBox);

		top.setAlignment(Pos.BASELINE_CENTER);
		top.setTranslateY(10);
		background.setTop(top);

		background.setBottom(bottom);
		bottom.setAlignment(Pos.BASELINE_CENTER);

		return background;
	}


	/**CHARLY'S PUZZLE HERE**/
	//FIXME: need to apply gui to the buttons, make them red and green
	private Parent createDoor3() {
		Text puzzle = new Text("Click the buttons until they are all green, \nwhen done go to checkWinner to confirm");

		VBox centerScene = new VBox();
		ArrayList<HBox> rowButton = new ArrayList<>();
		//ArrayList<Button> myButton = new ArrayList<>();
		ArrayList<ArrayList<Button>> myButton = new ArrayList<>();
		//ImageView redBox = new ImageView("red.png");
		//redBox.set


		for(ArrayList<Button> elem : myButton){
			elem = new ArrayList<Button>();
		}

		for(int i=0;i<5;i++){//this whole block creates a 5X5 array of buttons and adds them to the scene
			myButton.add(new ArrayList<Button>() );
			rowButton.add(new HBox() );
			for(int j=0;j<5;j++){
				Button temp = new Button("red");//Integer.toString((i*5)+j) );
				Image myImage = new Image("red.jpg");
				ImageView myView = new ImageView(myImage);
				myView.setPreserveRatio(true);
				myView.setFitWidth(50);
				temp.setGraphic(myView);
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
						Image myImage = new Image("green.jpg");
						ImageView myView = new ImageView(myImage);
						myView.setPreserveRatio(true);
						myView.setFitWidth(50);
						ele.setGraphic(myView);
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


		HBox choiceHBox = new HBox(centerScene, checkWinner);
		VBox Door3Box = new VBox(puzzle, choiceHBox);
		BorderPane mainScene = new BorderPane(Door3Box);
		return mainScene;
	}

	public ImageView createPictureOnButton(String dir) {
		Image image = new Image(dir);
		ImageView view = new ImageView(image);
		view.setFitHeight(300);
		view.setFitWidth(300);
		view.setPreserveRatio(true);
		return view;
	}

	private VBox setUpButton(String realAnswer) {

		TextField answer = new TextField();
		answer.setPrefHeight(5);
		answer.setPrefWidth(5);

		Button submitAnswerButton = new Button("Submit Answer");
		Button giveUpButton = new Button("Give up");

		giveUpButton.setOnAction(event -> {
			primaryStage.setScene(sceneList.get(0));
		});


		submitAnswerButton.setOnAction(event -> {
			try {
				String theirAnswer = answer.getText().toUpperCase();
				answer.clear();
				if(theirAnswer.equals(realAnswer)) {
					conn.score++;
					Score.setText("Score: " + conn.score);
					primaryStage.setScene(sceneList.get(0));
					conn.send("w");
				}
				else {
					answer.appendText("Try again. Delete this text to do so");
				}
			}
			catch(Exception e) {
			}
		});

		HBox playerButtonAction = new HBox(submitAnswerButton, giveUpButton);
		VBox playerTotalAction = new VBox(answer, playerButtonAction);
		return playerTotalAction;
	}

	/**KAVEESHA'S PUZZLE HERE**/
	private Parent createDoor4() {
		BorderPane morseGameplay = new BorderPane();
		Text morseCode = new Text("... -- .- ... .... / -... .-. --- ...");
		morseCode.setFill(Color.DEEPPINK);
		morseCode.setScaleX(3);
		morseCode.setScaleY(3);
		morseCode.setTranslateX(370);
		morseCode.setTranslateY(30);

		VBox playerAction = setUpButton("SMASH BROS");
		morseGameplay.setTop(morseCode);
		morseGameplay.setBottom(playerAction);
		morseGameplay.setStyle("-fx-background-color: lightskyblue");

		return morseGameplay;
	}


	private Parent createDoor5() {
		BorderPane binaryGameplay = new BorderPane();
		Text binary = new Text("1101 1010 1011");
		binary.setFill(Color.CRIMSON);
		binary.setScaleX(3);
		binary.setScaleY(3);
		binary.setTranslateX(350);
		binary.setTranslateY(30);
		VBox playerAction = setUpButton("DAB");
		binaryGameplay.setTop(binary);
		binaryGameplay.setBottom(playerAction);
		binaryGameplay.setStyle("-fx-background-color: steelblue");

		return binaryGameplay;
	}

	private Parent createDoor6() {
		Button mathQuestion = new Button();
		mathQuestion.setGraphic(createPictureOnButton("mathPuzzle.jpg"));

		BorderPane mathGameplay = new BorderPane();

		VBox playerAction = setUpButton("51");
		mathGameplay.setTop(mathQuestion);
		mathGameplay.setBottom(playerAction);
		mathGameplay.setStyle("-fx-background-color: palegreen");

		return mathGameplay;
	}


	private Parent createDoor7() {

		//Scoreboard for added legitimacy
		Text yourScore = new Text("You: 4");
		Text opponent1Score = new Text("Opponent 1: 4");
        Text opponent2Score = new Text("Opponent 2: 4");
        Text opponent3Score = new Text("Opponent 3: 4");

		Image cardField1 = new Image("pitch/7D.jpg");
		Image cardField2 = new Image("pitch/JD.jpg");
		Image cardField3 = new Image("pitch/2H.jpg");

		Image cardHand1 = new Image("pitch/3C.jpg");
		Image cardHand2 = new Image("pitch/1C.jpg");
		Image cardHand3 = new Image("pitch/QD.jpg");

		ImageView cfView1, cfView2, cfView3;
		ImageView chView1, chView2, chView3;

		cfView1 = new ImageView(cardField1);
		cfView1.setFitHeight(200);
		cfView1.setFitWidth(80);
		cfView1.setPreserveRatio(true);
		cfView2 = new ImageView(cardField2);
		cfView2.setFitHeight(200);
		cfView2.setFitWidth(80);
		cfView2.setPreserveRatio(true);
		cfView3 = new ImageView(cardField3);
		cfView3.setFitHeight(200);
		cfView3.setFitWidth(80);
		cfView3.setPreserveRatio(true);

		chView1 = new ImageView(cardHand1);
		chView1.setFitHeight(200);
		chView1.setFitWidth(80);
		chView1.setPreserveRatio(true);
		chView2 = new ImageView(cardHand2);
		chView2.setFitHeight(200);
		chView2.setFitWidth(80);
		chView2.setPreserveRatio(true);
		chView3 = new ImageView(cardHand3);
		chView3.setFitHeight(200);
		chView3.setFitWidth(80);
		chView3.setPreserveRatio(true);

		Button cf1 = new Button();
		Button cf2 = new Button();
		Button cf3 = new Button();
		cf1.setGraphic(cfView1);
		cf2.setGraphic(cfView2);
		cf3.setGraphic(cfView3);


		Button ch1 = new Button();
		Button ch2 = new Button();
		Button ch3 = new Button();
		ch1.setGraphic(chView1);
		ch2.setGraphic(chView2);
		ch3.setGraphic(chView3);


		HBox field = new HBox(5, cf1, cf2,cf3);
		HBox yourHand = new HBox(15, ch1, ch2, ch3);
		yourHand.setAlignment(BOTTOM_CENTER);

		Text instructions = new Text("It's the last trick of the game...Everyone is tied...The Trump this round is Diamonds...It is your turn");
		instructions.setFill(WHITE);

		Text uhoh = new Text("You DO remember how to play Pitch, right?");
		uhoh.setFill(WHITE);
        VBox scoreHolder = new VBox(yourScore, opponent1Score, opponent2Score, opponent3Score);
        VBox upperLeftHolder = new VBox(5, scoreHolder, instructions);

		//Background image stuff
        BorderPane gamePane = new BorderPane();
        field.setAlignment(CENTER);
        gamePane.setCenter(field);
        VBox BottomHolder = new VBox(5, uhoh,yourHand);
        //BottomHolder.setAlignment(CENTER);
        BottomHolder.setAlignment(BOTTOM_CENTER);
        gamePane.setBottom(BottomHolder);
		upperLeftHolder.setAlignment(TOP_LEFT);
		//upperLeftHolder.setPadding();
		gamePane.setTop(upperLeftHolder);

		Image bg = new Image("pictures/green.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage bgImg = new BackgroundImage(bg, null, null, null, backgroundSize);
        Background background = new Background(bgImg);

		//Text puzzleTitleCard = new Text("Under Construction!!");
		TextField cheatField;

		Button solve = new Button("Solve");
		//solve.setAlignment(CENTER);

		Button giveUp = new Button("Give up");
		Button sabotage = new Button("Sabotage");





		//background.set
		gamePane.setBackground(background);


		//HBox cardField = new HBox(50);
		//HBox cardHand = new HBox(50, sudokuListVBox.get(0));
		//VBox everything = new VBox(30,field,giveUp);
		//gamePane.setCenter(everything);
		//gamePane.setBottom(yourHand);

		//everything.setAlignment(CENTER);



		ch3.setOnAction(event -> {

			try {
				/**IMPORTANT THING #1: INCREASE YOUR SCORE**/
					conn.score++;
					Score.setText("Score: " + conn.score); //Updates Score Text on UI
					primaryStage.setTitle("Puzzle Gauntlet");
					primaryStage.setScene(sceneList.get(0)); //Sets scene back to the primary stage
					conn.send("p"); //Does the same thing as "w" but it informs everyone in the server that you won a game of Pitch
			}
			catch(Exception e){
			}

		});

		giveUp.setOnAction(event -> {

			try {
				/**IMPORTANT THING #1: INCREASE YOUR SCORE**/
				primaryStage.setTitle("Puzzle Gauntlet");
				door1.setDisable(true);
				primaryStage.setScene(sceneList.get(0)); //Sets scene back to the primary stage
				door1.setDisable(true);
			}
			catch(Exception e){
			}

		});

		return gamePane;
	}

	/**Mean Sudoku**/
	private Parent createDoor8() {
		ArrayList<sudoku> sudokuList = new ArrayList<sudoku>();
		ArrayList<VBox> sudokuListVBox = new ArrayList<VBox>();

		Text puzzleTitleCard = new Text("Welcome to Upside Down Sudoku!!");
		TextField cheatField;

		Button solve = new Button("Solve");
		solve.setAlignment(CENTER);

		Button giveUp = new Button("Give up");
		Button sabotage = new Button("Sabotage");

		BorderPane gamePane = new BorderPane();
		Image bg = new Image("pictures/giphy.gif");
		gamePane.setRotate(180);
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage bgImg = new BackgroundImage(bg, null, null, null, backgroundSize);
		Background background = new Background(bgImg);



		//background.set
		gamePane.setBackground(background);




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
		s3.setUpperLeft(2,4,99,99);
		s3.setUpperRight(99,99,2,99);
		s3.setLowerLeft(99,99,1,99);
		s3.setLowerRight(99,99,4,2);
		s3.setUpperLeftAnswers(99,99,3,1);
		s3.setUpperRightAnswers(3,1,99,4);
		s3.setLowerLeftAnswers(4,2,99,3);
		s3.setLowerRightAnswers(1,3,99,99);

		sudoku s4 = new sudoku();
		s4.setUpperLeft(99,99,99,99);
		s4.setUpperRight(99,99,1,4);
		s4.setLowerLeft(2,1,99,99);
		s4.setLowerRight(99,99,99,99);
		s4.setUpperLeftAnswers(1,4,3,2);
		s4.setUpperRightAnswers(3,2,99,99);
		s4.setLowerLeftAnswers(99,99,4,3);
		s4.setLowerRightAnswers(4,3,2,1);

		sudoku s5 = new sudoku();
		s5.setUpperLeft(99,99,99,99);
		s5.setUpperRight(3,99,2,1);
		s5.setLowerLeft(99,4,2,1);
		s5.setLowerRight(99,99,99,99);
		s5.setUpperLeftAnswers(1,2,4,3);
		s5.setUpperRightAnswers(99,4,99,99);
		s5.setLowerLeftAnswers(3,99,99,99);
		s5.setLowerRightAnswers(1,2,4,3);





		//To give the illusion that there are randomized puzzles

		//puzzle1.setVisible(false);
		//puzzle2.setVisible(false);
		//puzzle3.setVisible(false);

		sudokuList.add(s1); //Add all possible puzzles to a list of puzzles
		sudokuList.add(s2);
		sudokuList.add(s3);
		sudokuList.add(s4);
		sudokuList.add(s5);


		Collections.shuffle(sudokuList, new Random()); //shuffle the list

		//populates arrayList of VBox's with all the sudoku puzzles, so we can make it visible/invisible. They are all invisible by default
		for(int i = 0; i < sudokuList.size(); i++) {
			sudokuListVBox.add(sudokuList.get(i).makePuzzle());
			sudokuListVBox.get(i).setVisible(false);
		}


		//HBox puzzleHolder = new HBox(50, sudokuListVBox.get(0), sudokuListVBox.get(1), sudokuListVBox.get(2), sudokuListVBox.get(3), sudokuListVBox.get(4));
		HBox puzzleHolder = new HBox(50, sudokuListVBox.get(0));
		puzzleHolder.setRotate(15);
		HBox buttonHolder = new HBox(50, solve, giveUp);
		buttonHolder.setRotate(180);
		VBox everything = new VBox(30, buttonHolder, puzzleTitleCard, puzzleHolder);
		everything.setAlignment(CENTER);
		puzzleHolder.setAlignment(CENTER);
		buttonHolder.setAlignment(CENTER);


		sudokuListVBox.get(0).setVisible(true); //get first puzzle in list and make it visible
		cheatField = sudokuList.get(0).getCheat();


		solve.setOnAction(event -> {

			try {
				/**IMPORTANT THING #1: INCREASE YOUR SCORE**/
				//if((c21.getText().equals("3") && c12.getText().equals("2")) || (c21.getText().equals("2") && c12.getText().equals("3")) || (d13.getText().equals("3") && d32.getText().equals("8")) || (f11.getText().equals("1") && f12.getText().equals("2") && f22.getText().equals("4") && g11.getText().equals("3") && g12.getText().equals("4") && h12.getText().equals("1")) && h21.getText().equals("4") && h22.getText().equals("3")){
				if (sudokuList.get(0).checkAnswers() == true) {
					conn.score++;
					Score.setText("Score: " + conn.score); //Updates Score Text on UI
					primaryStage.setTitle("Puzzle Gauntlet");
					//door1.setDisable(true);
					primaryStage.setScene(sceneList.get(0)); //Sets scene back to the primary stage
					conn.send("w"); //Sends a w to the server to let it know that someone WON
					//primaryStage.setTitle("Puzzle Gauntlet");
					//door1.setDisable(true);


				} else
					solve.setText("WRONG!!!...Try again!");
			}
			catch(Exception e){
			}

		});

		giveUp.setOnAction(event -> {

			try {
				/**IMPORTANT THING #1: INCREASE YOUR SCORE**/
				primaryStage.setTitle("Puzzle Gauntlet");
				door1.setDisable(true);
				primaryStage.setScene(sceneList.get(0)); //Sets scene back to the primary stage
				door1.setDisable(true);
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

		//everything.setRotate(180);

		gamePane.setCenter(everything);
		return gamePane;
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
		Scene scene5 = new Scene(createDoor5(), 900, 500);
		Scene scene6 = new Scene(createDoor6(), 900, 500);
		Scene scene7 = new Scene(createDoor7(), 900, 500);
		Scene scene8 = new Scene(createDoor8(), 900, 500);


		Scene finalScene = startUpScene;
		sceneList.add(scene1);
		sceneList.add(scene2);
		sceneList.add(scene3);
		sceneList.add(scene4);
		sceneList.add(scene5);
		sceneList.add(scene6);
		sceneList.add(scene7);
		sceneList.add(scene8);

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
