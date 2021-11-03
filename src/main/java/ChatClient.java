 

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**

*

* @author MTITCOMB

*/
public class ChatClient extends Application
{ 

  //private TextArea messages = new TextArea();
  private final VBox chatBox = new VBox(5);
  private ClientServer clientServer;
  VBox root;
  private String userName;


  private Pane createContent(){
    chatBox.setPrefHeight(550);
    chatBox.setPrefWidth(550);
    Label lbl = new Label("Welcome to te chatroom " + userName);
    lbl.setStyle(
      "  -fx-text-fill:white;\n" +
      "  -fx-pref-height:20px;\n" +
      "  -fx-pref-width:550px; ");
    lbl.setAlignment(Pos.CENTER_RIGHT);
    
    chatBox.getChildren().add(lbl);
    ScrollPane scrollPane = new ScrollPane();

    scrollPane.setFitToWidth(true);
    scrollPane.setPrefHeight(550);
    scrollPane.setPrefWidth(550);
    scrollPane.setContent(chatBox);

    chatBox.setStyle("-fx-background-color:#333333;"
        + "-fx-min-height:400px;\n" +
        "  -fx-min-width:200px;");
    TextField input = new TextField();
    
    input.setText("please type in username");

    input.setOnAction(event -> {
      String message = input.getText();
      input.clear();
      recordMessage(message, true);
      try
      {
        clientServer.send(message);
      } catch (Exception ex)
      {
        recordMessage("Failed to Send Message" + "\n", true);
      }
    });
    root = new VBox(20, scrollPane, input);
    Pane pane = new Pane();
    root.setPrefSize(600,600);
    pane.getChildren().addAll(root);

    return pane;
  }

 

  private ClientServer createClientServer(){
    return new ClientServer("127.0.0.1", 55555, userName, data -> {
      //putting your fx element inside the platform
      Platform.runLater( ()-> {
        recordMessage(data.toString(), false);
       });
   });
  }

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    primaryStage.setScene(new Scene(createIndex(primaryStage), 600, 600));
    //primaryStage.setScene(new Scene(createContent(), 600, 600));
    primaryStage.show();
  }

 
  public static void main(String[] args)
  {
    launch(args);
  }

  public void recordMessage(String message, boolean own){
    Label lbl = new Label(message);
    if(own){
      lbl.setAlignment(Pos.CENTER_RIGHT);
      lbl.setStyle(
      "  -fx-text-fill:white;\n" +
      "  -fx-pref-height:20px;\n" +
      "  -fx-pref-width:550px; ");
    }else{

      lbl.setAlignment(Pos.CENTER_LEFT);
      lbl.setStyle(
      "  -fx-text-fill:white;\n" +
      "  -fx-pref-height:20px;\n" +
      "  -fx-pref-width:550px; ");
    }

    chatBox.getChildren().add(lbl);
  }

 
  public Parent createIndex(Stage primaryStage)throws Exception{
    Label lbl = new Label("Please insert name");
    TextField nameInput = new TextField();
    Button btn = new Button();

    btn.setOnAction((ActionEvent event) -> {
      userName = nameInput.getText();
      primaryStage.setScene(new Scene(createContent()));
      try{
        clientServer = createClientServer();
        clientServer.startConnection();
      }catch(Exception e){
      }
      });
    VBox indexVBox = new VBox(10, lbl, nameInput,btn);

    return indexVBox;

  }

}