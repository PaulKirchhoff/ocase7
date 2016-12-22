/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocase7.loginView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ocase7.CardBox;
import ocase7.Session;
import ocase7.User;
import ocase7.view2.View2;

/**
 *
 * @author PaulsBook
 */
public class LoginView {
    
    Scene loginScene;
    Stage primaryStage;
    TextField userTextField;
    PasswordField pwTextfield;
   // User currentUser;
    
    public LoginView(Stage primaryStage) {
        super();
        this.primaryStage = primaryStage;
    }

    public LoginView() {
    }
    
    public Scene creatLoginView() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("loginView");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(-150, 5, 5, 5));

        Text scenetitle = new Text("Willkommen beim Ocase7 Training");
        scenetitle.getStyleClass().add("sceneTitle");
        scenetitle.setEffect(new InnerShadow(0,1, 1, Color.CRIMSON));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Username:");
        userName.getStyleClass().add("userNameLabel");
        grid.add(userName, 0, 12);

        userTextField = new TextField();
        userTextField.getStyleClass().add("userTextfield");
        grid.add(userTextField, 1, 12);

        Label pw = new Label("Password:");
        pw.getStyleClass().add("pwLabel");
        grid.add(pw, 0, 16);

        pwTextfield = new PasswordField();
        pwTextfield.getStyleClass().add("pwTextfield");
        grid.add(pwTextfield, 1, 16);

        Button btn = new Button("Sign in");
        btn.getStyleClass().add("loginButton");

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 20);

        Text actiontarget = new Text();
        grid.add(actiontarget, 0, 22);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setId("actiontarget");
                actiontarget.setText("Sign in button pressed");

                Session session = new Session();
                User user = new User(session).getUserByLogin(userTextField.getText(), pwTextfield.getText());
//                user = user.getUserByLogin(user.getName(), user.getPassword());
                if(user.getName().equals(userTextField.getText()) && user.getPassword().equals(pwTextfield.getText())) {
                    actiontarget.setText("Herzlich Willkommen " + user.getName());
                    View2 v2 = new View2(primaryStage);
                    //loginScene = v2.createView2Scene();
                    primaryStage.setScene(v2.createView2Scene(user));
                } else {
                    actiontarget.setText("Bitte prüfe nochmal den Namen und das Passwort.");
                }
            }
        });

        loginScene = new Scene(grid,Color.DEEPSKYBLUE);
        loginScene.getStylesheets().add
        (LoginView.class.getResource("/style/style.css").toExternalForm());
        
        return loginScene;
        
    }
    
    
}