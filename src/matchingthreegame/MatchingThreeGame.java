/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matchingthreegame;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Jonathan
 */
public class MatchingThreeGame extends Application implements EventHandler<ActionEvent> {

    //Necessary Variable to get the GUI to show
    Stage window;
    Scene scene;

    Image symbol;

    @Override
    //Set up the grid and allow the realHandle() method to be called on click
    public void start(Stage primaryStage) throws IOException {
        //Set the stage here.
        window = primaryStage;
        window.setTitle("Match Three");

        //Create the Grid and it's boundaries and padding
        GridPane theGrid = new GridPane();
        theGrid.setPadding(new Insets(10, 10, 10, 10));
        theGrid.setHgap(3);
        theGrid.setVgap(3);

        int rows = 1;
        int columns = 1;

        //2D array to store the buttons in each cell
        SmartButton[][] buttonGrid = new SmartButton[rows][columns];

        //Random number generator here to randomly assign symbol to each cell. TEMPORARY

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //Create a SmartButton.  It knows its location.
                SmartButton button = assignFirstRowCell(j, i, buttonGrid);
                System.out.println(button.getX());
                System.out.println(button.getY());
                //Set image
                button.setGraphic(new ImageView(button.getImage()));
                //Set the coordinates
                theGrid.add(button, j, i);
                //Add the button to the 2D array
                buttonGrid[j][i] = button;

                //If button clicked
                button.setOnAction(e -> {
                    realHandle(e, button, buttonGrid, rows, columns);
                });
            }
        }

        //Scene itself.  Size of the window here
        scene = new Scene(theGrid, 600, 500);
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


    public SmartButton assignFirstRowCell(int x, int y, SmartButton[][] buttonGrid) {
        
        //Create an arrayList of all of the possible values that the cell can be
        //For example, if two adjacent cells are the green symbol, remove green from
        //The array
        ArrayList<Pair> possibleSymbols = new ArrayList<>(7);
        //Add all the possible names to the arrayList
        possibleSymbols.add(new Pair<String, Image>("bluerune", new Image("/bluerune.png", 40, 40, true, true)));
        possibleSymbols.add(new Pair<String, Image>("drop", new Image("/drop.png", 40, 40, false, true)));
        possibleSymbols.add(new Pair<String, Image>("greenswirl", new Image("/greenswirl.png", 40, 40, true, true)));
        possibleSymbols.add(new Pair<String, Image>("minisakura", new Image("/minisakura.png", 40, 40, true, true)));
        possibleSymbols.add(new Pair<String, Image>("man", new Image("/man.png", 40, 40, true, true)));
        possibleSymbols.add(new Pair<String, Image>("twindragons", new Image("/twin_dragons.png", 40, 40, true, true)));
        possibleSymbols.add(new Pair<String, Image>("sunflower", new Image("/sunflower.png", 40, 40, true, true)));
        //Generate the random number and then assign the corresponding image
        Random generator = new Random();
        int randomInt;
        String pictureName = ""; 
        if (x == 0 || x == 1 ) {
            randomInt = generator.nextInt(7);
            if (randomInt == 0) {
                //Import the picture that will be assigned to the tile.
                symbol = new Image("/bluerune.png", 40, 40, true, true);
                pictureName = "bluerune";
            }
            if (randomInt == 1) {
                //Import the picture that will be assigned to the tile.
                symbol = new Image("/drop.png", 40, 40, false, true);
                pictureName = "drop";
            }
            if (randomInt == 2) {
                //Import the picture that will be assigned to the tile.
                symbol = new Image("/greenswirl.png", 40, 40, true, true);
                pictureName = "greenswirl";
            }
            if (randomInt == 3) {
                //Import the picture that will be assigned to the tile.
                symbol = new Image("/minisakura.png", 40, 40, true, true);
                pictureName = "minisakura";
            }
            if (randomInt == 4) {
                //Import the picture that will be assigned to the tile.
                symbol = new Image("/man.png", 40, 40, true, true);
                pictureName = "man";
            }
            if (randomInt == 5) {
                //Import the picture that will be assigned to the tile.
                symbol = new Image("/twin_dragons.png", 40, 40, true, true);
                pictureName = "twindragons";
            }
            if (randomInt == 6) {
                //Import the picture that will be assigned to the tile.
                symbol = new Image("/sunflower.png", 40, 40, true, true);
                pictureName = "sunflower";
            }
            return new SmartButton(x, y, symbol, pictureName);
        }
        else {
            if (buttonGrid[x-2][y].getName().equals(buttonGrid[x-1][y].getName())) {
                
            }
        }
        return null;
    }
    //This variable remembers the SmartButton object that was chosen on the
    //Previous turn.  This is done so that the highlighting can be removed when
    //The second tile is chosen.
    SmartButton rememberMe = new SmartButton(-1, -1, null, "");

    //The realHandle method.  This is what is called when a button is pressed.
    //It needs to be able to recognize when only one button has been selected.
    //And when the second button to swap with has been selected.
    public void realHandle(ActionEvent event, SmartButton button, SmartButton[][] buttonGrid, int rows, int columns) {
        System.out.println(button.getName());
        //Checks to see if it is time for a switch, or if another needs to be clicked.
        int buttonsSelected = numButtonsSelected(rememberMe, button);
        //With this number, have the correct response.  If the number returned is 1
        //Then nothing happens.  The user must pick a second option.  Highlight the tile chosen.
        //If the number returned is 0, then it's time to try and switch the tiles.
        if (buttonsSelected == 1) {
            rememberMe = button;
            rememberMe.setStyle("-fx-border-color: red");
        } else {
            rememberMe.setStyle("-fx-border-color: transparent");
            button.setStyle("-fx-border-color: grey");
            button.setStyle("-fx-focus-color: transparent");
            //Check adjacency.  Make sure that the tiles are next to each other and not diagonal
            //If this is true, continue swapping.  If not true, nothing happens.
            int rMX = rememberMe.getX();    //rememberMeX
            int rMY = rememberMe.getY();    //rememberMeY
            int bX = button.getX();         //buttonX
            int bY = button.getY();         //buttonY

            //This method call here finds all the neighbors of button
            SmartButton[] neighbors = new SmartButton[4];
            neighbors = getNeighbors(buttonGrid, button, rows, columns);

            //Using the method call above, we will see if rememberMe is in neighbors[]
            //This boolean tests adjacency.  It checks to make sure that the x OR the y
            //Is one away in either direction.  There are four tests because there are four
            //Possible tests of adjacency.
            boolean isAdj = false;
            for (int i = 0; i < 4; i++) {
                if ((neighbors[i].getX() == rememberMe.getX()) && (neighbors[i].getY() == rememberMe.getY())) {
                    isAdj = true;
                }
            }
            System.out.println(isAdj);
            if (isAdj == false) {
                //Do something to indicate illegal move
            } else {
                //Check to see if there is a match in any direction
                //If so, switch
                switchImages(rememberMe, button);
                //If not, do nothing, choose two more
            }
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

    public int numButtonsSelected(SmartButton rememberMe, SmartButton button) {
        //Check to see if same button is clicked here.  If so, make nothing happen
        if ((rememberMe.getX() == button.getX()) && (rememberMe.getY() == button.getY())) {
            return buttonCount;
        } //After making sure if they are different buttons, increment how many buttons are
        //Currently active so it can be determined if matches need to be checked for.
        else {
            buttonCount++;
            if (buttonCount == 1) {
                return buttonCount;
            }
            buttonCount = 0;
            return buttonCount;
        }
    }

    //This method is called at the beginning of the game to make sure that
    //No rows of three or greater are present at the start
    //THIS METHOD MAY BE USELESS.  COMMENT OUT FOR NOW
    /*
    public void removeInitialPairs(SmartButton[][] buttonGrid, int rows, int columns) {
        SmartButton[] neighbors;
        int count = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++){
                searchForThrees(buttonGrid, buttonGrid[j][i], rows, columns);
            }           
        }
        System.out.println(count/2);
    }
     */
    //This method compiles a list of all of the neighbors around it.
    //The corners and edges are special cases, which is why we need a whole method.
    public SmartButton[] getNeighbors(SmartButton[][] buttonGrid, SmartButton button, int rows, int columns) {
        SmartButton[] neighbors = new SmartButton[4];
        SmartButton buttonToAdd;
        //This variable will change once every time a value is added so we know where to add the next value.
        int addHere = 0;
        //This is checking for the left neighbor.
        //Check to make sure that there is a neighbor on the left side, as in
        //The tile is not on the left most wall.
        if (button.getX() != 0) {
            //Add the tile on the left side, as in one fewer X and the same Y.
            buttonToAdd = buttonGrid[button.getX() - 1][button.getY()];
            neighbors[addHere] = buttonToAdd;
            addHere++;
        }
        //Check to see if tile is on right most wall
        if (button.getX() != (rows - 1)) {
            //Add the tile on the right side, as in one greater X and the same Y.
            buttonToAdd = buttonGrid[button.getX() + 1][button.getY()];
            neighbors[addHere] = buttonToAdd;
            addHere++;
        }
        //Check to see if tile is on top
        if (button.getY() != 0) {
            //Add the tile on the top, as in same X and one fewer Y.
            buttonToAdd = buttonGrid[button.getX()][button.getY() - 1];
            neighbors[addHere] = buttonToAdd;
            addHere++;
        }
        //Chect to see if tile is on bottom
        if (button.getY() != (columns - 1)) {
            //Add the tile below the button, as in same X and one greater Y.
            buttonToAdd = buttonGrid[button.getX()][button.getY() + 1];
            neighbors[addHere] = buttonToAdd;
            addHere++;
        }

        //For every empty value in the array, fill it with a nonsensical SmartButton
        //Which we will realize later in the program we won't use
        if (addHere == 2) {
            neighbors[addHere] = new SmartButton(-1, -1, null, "");
            addHere++;
        }
        if (addHere == 3) {
            neighbors[addHere] = new SmartButton(-1, -1, null, "");
        }

        return neighbors;

    }

    public void switchImages(SmartButton rememberMe, SmartButton button) {
        //rememberMe.getGraphic();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
