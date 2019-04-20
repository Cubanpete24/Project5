import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    int sceneWidth = 750;
    int sceneHeight = 750;
    int localPort = 5332;
    String localIP = "127.0.0.1";//local port
    Client myClient = new Client();
    TextArea clientText = new TextArea();
    String greeting = "Client> ";


    @Override
    public void start(Stage primaryStage) throws Exception{
        //myClient.init(localIP,localport);
        BorderPane scene = new BorderPane();
        Text title = new Text("Test Client");
        TextField userIP = new TextField();
        TextField userPort = new TextField();
        Button userButton = new Button("submit");
        HBox userInput = new HBox();
        VBox centerCol = new VBox();


        userIP.setText(localIP);
        userPort.setText(Integer.toString(localPort) );
        userInput.getChildren().addAll(userIP,userPort, userButton);
        centerCol.getChildren().addAll(title, userInput);
        scene.setCenter(centerCol);
        scene.setBottom(clientText);

        userButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String tempIP = userIP.getText();
                Integer tempPort = Integer.valueOf(userPort.getText() );
                if(myClient.init(tempIP,tempPort) ){
                    clientText.appendText(greeting+"client port open\n");
                    clientOpen(primaryStage);
                }
                else{
                    clientText.appendText(greeting + "failed conneciton\n");
                }

            }
        });



        primaryStage.setTitle("Client");
        clientText.appendText(greeting+"\n");
        primaryStage.setScene(new Scene(scene,sceneWidth, sceneHeight) );
        primaryStage.show();
    }

    public void clientOpen(Stage primaryStage){
        BorderPane scene = new BorderPane();
        Text title = new Text("Client connected");
        TextField userString = new TextField();
        Button userSubmit = new Button("server");
        HBox userInput = new HBox();
        VBox centerCol = new VBox();

        userInput.getChildren().addAll(userString,userSubmit);
        centerCol.getChildren().addAll(title,userInput);
        scene.setCenter(centerCol);
        scene.setBottom(clientText);

        userSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String temp = "server:"+userString.getText();
                myClient.sendData(temp);
                userString.clear();
            }
        });

        primaryStage.setScene( new Scene(scene, sceneWidth,sceneHeight));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
