import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    int sceneWidth = 750;
    int sceneHight = 750;
    int localPort = 5332;
    Server hostServer = new Server();
    TextArea serverText = new TextArea();
    String greeting = "Server> ";


    @Override
    public void start(Stage primaryStage) throws Exception{
        serverInit(primaryStage);
    }

    public void serverInit(Stage primaryStage){
        BorderPane scene = new BorderPane();
        Text title = new Text("Test Server");
        TextField userPort = new TextField();
        Button userButton = new Button("submit");
        HBox userInput = new HBox();
        VBox centerCol = new VBox();
        //int localPort = 5332;

        userPort.setText(Integer.toString(localPort) );
        userInput.getChildren().addAll(userPort, userButton);
        centerCol.getChildren().addAll(title, userInput);
        scene.setCenter(centerCol);
        scene.setBottom(serverText);

        userButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer temp = Integer.valueOf( userPort.getText() );
                userPort.clear();
                if(hostServer.init(temp) ){
                    serverText.appendText(greeting+"server open\n");
                    serverOpen(primaryStage);
                }
                else{
                    serverText.appendText(greeting+"failed connection\n");
                }
            }
        });

        primaryStage.setTitle("Server");
        serverText.appendText(greeting+"\n");
        primaryStage.setScene(new Scene(scene, sceneWidth, sceneHight));
        primaryStage.show();
    }
    public void serverOpen(Stage primaryStage){
        BorderPane scene = new BorderPane();
        scene.setBottom(serverText);
        Text title = new Text("Server Open: ");
        VBox centerCol = new VBox();

        centerCol.getChildren().addAll(title);
        scene.setCenter(centerCol );

        primaryStage.setScene(new Scene(scene, sceneWidth, sceneHight) );
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
