/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matchingthreegame;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Jonathan
 */
public class MatchingThreeGame extends Application implements EventHandler<ActionEvent>{
    
    Stage window;
    Scene scene;
    
    @Override
    //Set up the grid and allow the realHandle() method to be called on click
    public void start(Stage primaryStage) {
        //Set the stage here.
        window = primaryStage;
        window.setTitle("Match Three");
        
        //Create the Grid and it's boundaries and padding
        GridPane theGrid = new GridPane();
        theGrid.setPadding(new Insets(10, 10, 10, 10));
        theGrid.setHgap(3);
        theGrid.setVgap(3);
        
        int rows = 12;
        int columns = 12;
        
        //This may not be necessary
        //SmartButton[][] buttonGrid = new SmartButton[rows][columns];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //Create a SmartButton.  It knows its location.
                SmartButton button = new SmartButton(j, i);
                //Set the text on the button
                button.setText(j + ", " + i);
                //Set the coordinates
                theGrid.add(button, j, i);
                //Add the button to the 2D array
                
                //If button clicked
                button.setOnAction(e -> {
                    realHandle(e, button);
                });
            }
        }
        
        
        
        //Scene itself.  Size of the window here
        scene = new Scene(theGrid, 700, 400);
        //Show the scene
        window.setScene(scene);
        //Show everything
        window.show();
        
    }
    
    //The handle method.  This is the required method because of implementing
    //Event Handler.  It doesn't have the parameters we need, so it will not be
    //used.  The method realHandle() will be created.
    @Override
    public void handle(ActionEvent event) {
        System.out.println("Won't use this method");       
    }
    
    
    //This variable remembers the SmartButton object that was chosen on the
    //Previous turn.  This is done so that the highlighting can be removed when
    //The second tile is chosen.
    SmartButton rememberMe;
    
    //The realHandle method.  This is what is called when a button is pressed.
    //It needs to be able to recognize when only one button has been selected.
    //And when the second button to swap with has been selected.
    
    public void realHandle(ActionEvent event, SmartButton button){
        //Checks to see if it is time for a switch, or if another needs to be clicked.
        int buttonsSelected = numButtonsSelected();
        //With this number, have the correct response.  If the number returned is 1
        //Then nothing happens.  The user must pick a second option.  Highlight the tile chosen.
        //If the number returned is 0, then it's time to try and switch the tiles.
        if (buttonsSelected == 1) {
            rememberMe = button;
            rememberMe.setStyle("-fx-border-color: red");
        }
        else {
            rememberMe.setStyle("-fx-border-color: transparent");
            button.setStyle("-fx-border-color: grey");
            button.setStyle("-fx-focus-color: transparent");
            //Check adjacency.  Make sure that the tiles are next to each other and not diagonal
            //If this is true, continue swapping.  If not true, nothing happens.
            int rMX = rememberMe.getX();    //rememberMeX
            int rMY = rememberMe.getY();    //rememberMeY
            int bX = button.getX();         //buttonX
            int bY = button.getY();         //buttonY
            //This boolean tests adjacency.  It checks to make sure that the x OR the y
            //Is one away in either direction.  There are four tests because there are four
            //Possible tests of adjacency.
            boolean isAdj = ((rMX - bX == 1) && (rMY - bY == 0)) ||
                            ((rMX - bX == -1) && (rMY - bY == 0)) ||
                            ((rMX - bX == 0) && (rMY - bY == 1)) ||
                            ((rMX - bX == 0) && (rMY - bY == -1));
            System.out.println(isAdj);
        }
    }
    
    //The method called numButtonsSelected.  This is a vitally important method.
    //It checks to see if one or two buttons are currently selected.  Why? Because
    //If the first button is selected, nothing should happen.  However, if the second
    //Button is being selected, then the two need to be switched.  This method
    //Checks to see if the switch needs to take place, or if one more button
    //Needs to be selected
    //
    //The variable is used to see how many are currently clicked
    int buttonCount = 0;
    public int numButtonsSelected() {
        buttonCount++;
        if (buttonCount == 1) {
            return buttonCount;
        }
        buttonCount = 0;
        return buttonCount;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
