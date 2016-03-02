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

        int rows = 7;
        int columns = 7;

        //2D array to store the buttons in each cell
        SmartButton[][] buttonGrid = new SmartButton[rows][columns];

        //Random number generator here to randomly assign symbol to each cell. TEMPORARY

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                //Create a SmartButton.  It knows its location.  This uses the initizli
                SmartButton button = initializeGrid(j, i, buttonGrid);
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


    public SmartButton initializeGrid(int x, int y, SmartButton[][] buttonGrid) {
        
        //Create an arrayList of all of the possible values that the cell can be
        //For example, if two adjacent cells are the green symbol, remove green from
        //The array
        ArrayList<Pair<String, Image>> possibleSymbols = new ArrayList<>(7);
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
        Image image;
        if ((x == 0 || x == 1) && (y == 0 || y ==1)) {
            //Do nothing here, just a place holder
        }
        else if ((!(x == 0 || x == 1)) && (y ==0 || y == 1)){
            
            if (buttonGrid[x-2][y].getName().equals(buttonGrid[x-1][y].getName())) {
                String nameToRemoveX = buttonGrid[x-1][y].getName();
                for (int i = 0; i < possibleSymbols.size(); i++) {
                    if (possibleSymbols.get(i).getKey().equals(nameToRemoveX)) {
                        possibleSymbols.remove(i);
                    }
                    
                }
            }
        }
        
        else if ((x == 0 || x == 1) && (!(y ==0 || y == 1))) {
            
            if (buttonGrid[x][y-2].getName().equals(buttonGrid[x][y-1].getName())) {
                String nameToRemoveY = buttonGrid[x][y-1].getName();
                for (int j = 0; j < possibleSymbols.size(); j++) {
                    if (possibleSymbols.get(j).getKey().equals(nameToRemoveY)) {
                        possibleSymbols.remove(j);
                    }
                }
            }
        }
            
        else {
            if (buttonGrid[x-2][y].getName().equals(buttonGrid[x-1][y].getName())) {
            String nameToRemoveX = buttonGrid[x-1][y].getName();
            for (int i = 0; i < possibleSymbols.size(); i++) {
                if (possibleSymbols.get(i).getKey().equals(nameToRemoveX)) {
                    possibleSymbols.remove(i);
                }   
            }
        }
            else if (buttonGrid[x][y-2].getName().equals(buttonGrid[x][y-1].getName())) {
                String nameToRemoveY = buttonGrid[x][y-1].getName();
                for (int j = 0; j < possibleSymbols.size(); j++) {
                    if (possibleSymbols.get(j).getKey().equals(nameToRemoveY)) {
                        possibleSymbols.remove(j);
                    }
                }      
            }
        }
            
        randomInt = generator.nextInt(possibleSymbols.size());
        pictureName = possibleSymbols.get(randomInt).getKey();
        image = possibleSymbols.get(randomInt).getValue();
        return new SmartButton(x, y, image, pictureName);
    }
    
    
    //This variable remembers the SmartButton object that was chosen on the
    //Previous turn.  This is done so that the highlighting can be removed when
    //The second tile is chosen.
    SmartButton rememberMe = new SmartButton(-1, -1, null, "");

    //The realHandle method.  This is what is called when a button is pressed.
    //It needs to be able to recognize when only one button has been selected.
    //And when the second button to swap with has been selected.
    public void realHandle(ActionEvent event, SmartButton button, SmartButton[][] buttonGrid, int rows, int columns) {
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
            ArrayList<SmartButton> neighbors = new ArrayList<>();
            neighbors = getNeighbors(buttonGrid, button, rows, columns);

            //Using the method call above, we will see if rememberMe is in neighbors[]
            //This boolean tests adjacency.  It checks to make sure that the x OR the y
            //Is one away in either direction.  There are four tests because there are four
            //Possible tests of adjacency.
            boolean isAdj = false;
            for (int i = 0; i < 4; i++) {
                if ((neighbors.get(i).getX() == rememberMe.getX()) && (neighbors.get(i).getY() == rememberMe.getY())) {
                    isAdj = true;
                }
            }
            if (isAdj == false) {
                //Do something to indicate illegal move
            } else {
                //Check to see if there is a match in any direction
                
                //This method is called twice, once checking for button, once checking for rememberMe, because
                //It doesn't matter the order in which they are switched
                System.out.println("X-Checker-First Button");
                switchImagesCheckHorizontal(buttonGrid, button, rememberMe, rows, columns);
                System.out.println("Y-Checker-First Button");
                switchImagesCheckVertical(buttonGrid, button, rememberMe, rows, columns);
                System.out.println("X-Checker-Second Button");
                switchImagesCheckHorizontal(buttonGrid, rememberMe, button, rows, columns);
                System.out.println("Y-Checker-Second Button");
                switchImagesCheckVertical(buttonGrid, rememberMe, button, rows, columns);
                //If so, switch
                simpleSwitch(buttonGrid, rememberMe, button);
                
                //System.out.println("\n\n");
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

    //This method compiles a list of all of the neighbors around it.
    //The corners and edges are special cases, which is why we need a whole method.
    public ArrayList<SmartButton> getNeighbors(SmartButton[][] buttonGrid, SmartButton button, int rows, int columns) {
        ArrayList<SmartButton> neighbors = new ArrayList<>();
        SmartButton buttonToAdd;
        //This variable will change once every time a value is added so we know where to add the next value.
        int addHere = 0;
        //This is checking for the left neighbor.
        //Check to make sure that there is a neighbor on the left side, as in
        //The tile is not on the left most wall.
        if (button.getX() != 0) {
            //Add the tile on the left side, as in one fewer X and the same Y.
            buttonToAdd = buttonGrid[button.getX() - 1][button.getY()];
            neighbors.add(addHere, buttonToAdd);
            addHere++;
        }
        //Check to see if tile is on right most wall
        if (button.getX() != (rows - 1)) {
            //Add the tile on the right side, as in one greater X and the same Y.
            buttonToAdd = buttonGrid[button.getX() + 1][button.getY()];
            neighbors.add(addHere, buttonToAdd);
            addHere++;
        }
        //Check to see if tile is on top
        if (button.getY() != 0) {
            //Add the tile on the top, as in same X and one fewer Y.
            buttonToAdd = buttonGrid[button.getX()][button.getY() - 1];
            neighbors.add(addHere, buttonToAdd);
            addHere++;
        }
        //Chect to see if tile is on bottom
        if (button.getY() != (columns - 1)) {
            //Add the tile below the button, as in same X and one greater Y.
            buttonToAdd = buttonGrid[button.getX()][button.getY() + 1];
            neighbors.add(addHere, buttonToAdd);
            addHere++;
        }

        //For every empty value in the array, fill it with a nonsensical SmartButton
        //Which we will realize later in the program we won't use
        if (addHere == 2) {
            neighbors.add(addHere, new SmartButton(-1, -1, null, ""));
            addHere++;
        }
        if (addHere == 3) {
            neighbors.add(addHere, new SmartButton(-1, -1, null, ""));
        }

        return neighbors;

    }

    public void switchImagesCheckHorizontal(SmartButton[][] buttonGrid, SmartButton button1, SmartButton button2, int rows, int columns) {
        ArrayList<SmartButton> potentialNeighbors = new ArrayList<>();
        potentialNeighbors = getNeighbors(buttonGrid, button1, rows, columns);
        System.out.println(button1.getX());
        //These values are EXCEEDINGLY IMPORTANT, because they keep the ORIGINAL PLACEMENT
        //From affecting the neighbors.
        int forbiddenX = button2.getX();
        int forbiddenY = button2.getY();
        //Check horizontally.  This means look at different x values but keep y values constant
        int remX = button1.getX();
        //This variable keeps track of how many in a row there are. 
        int countX = 0;
        //This array below keeps track of all horizontally adjacent cells with the same symbol
        ArrayList<SmartButton> sameSymbolX = new ArrayList<>();
        
        //Check to the left here
        if (button1.getX() > 0) {
            if ((buttonGrid[button1.getX()-1][button1.getY()].getName().equals(button2.getName())) && !(
                    button1.getX()-1 == forbiddenX && button1.getY() == forbiddenY)) {
                if (button1.getX() > 1) {
                    if (buttonGrid[button1.getX()-2][button1.getY()].getName().equals(button2.getName())) {
                        //The index countX is used because this is how many tiles we have with the same symbol
                        sameSymbolX.add(countX, buttonGrid[button1.getX()-2][button1.getY()]);
                        countX++;
                    }
                }
                sameSymbolX.add(countX, buttonGrid[button1.getX()-1][button1.getY()]);
                countX++;
            }
        }
        
        //Now add the original button flipped, the one we call button
        sameSymbolX.add(countX, buttonGrid[button1.getX()][button1.getY()]);
        countX++;
        
        //Check to the right here.  Reuse code from above mostly
        if (button1.getX() < columns - 1) {
            if ((buttonGrid[button1.getX()+1][button1.getY()].getName().equals(button2.getName())) && !(
                    button1.getX()+1 == forbiddenX && button1.getY() == forbiddenY)) {
                if (button1.getX() < columns - 2) {
                    if (buttonGrid[button1.getX()+2][button1.getY()].getName().equals(button2.getName())) {
                        //The index countX is used because this is how many tiles we have with the same symbol
                        sameSymbolX.add(countX, buttonGrid[button1.getX()+2][button1.getY()]);
                        countX++;
                    }
                }
                sameSymbolX.add(countX, buttonGrid[button1.getX()+1][button1.getY()]);
                countX++;
            }
        }
        
        
        for (int i = 0; i < sameSymbolX.size(); i++) {
            System.out.println(sameSymbolX.get(i).getX() + ", " + sameSymbolX.get(i).getY());
        }
        
        //If countX equals at least 3, then we have a match!  They need to be removed
    }
    
    public void switchImagesCheckVertical(SmartButton[][] buttonGrid, SmartButton button1, SmartButton button2, int rows, int columns) {
        ArrayList<SmartButton> potentialNeighbors = new ArrayList<>();
        potentialNeighbors = getNeighbors(buttonGrid, button1, rows, columns);
        System.out.println(button1.getX());
        //These values are EXCEEDINGLY IMPORTANT, because they keep the ORIGINAL PLACEMENT
        //From affecting the neighbors.
        int forbiddenX = button2.getX();
        int forbiddenY = button2.getY();
        //Check horizontally.  This means look at different x values but keep y values constant
        int remY = button1.getY();
        //This variable keeps track of how many in a row there are. 
        int countY = 0;
        //This array below keeps track of all horizontally adjacent cells with the same symbol
        ArrayList<SmartButton> sameSymbolY = new ArrayList<>();
        
        //Check to the top here
        if (button1.getY() > 0) {
            if ((buttonGrid[button1.getX()][button1.getY()-1].getName().equals(button2.getName())) && !(
                    button1.getX() == forbiddenX && button1.getY()-1 == forbiddenY)) {
                if (button1.getY() > 1) {
                    if (buttonGrid[button1.getX()][button1.getY()-2].getName().equals(button2.getName())) {
                        //The index countX is used because this is how many tiles we have with the same symbol
                        sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY()-2]);
                        countY++;
                    }
                }
                sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY()-1]);
                countY++;
            }
        }
        
        //Now add the original button flipped, the one we call button
        sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY()]);
        countY++;
        
        //Check to the bottom here.  Reuse code from above mostly
        if (button1.getY() < rows - 1) {
            if ((buttonGrid[button1.getX()][button1.getY()+1].getName().equals(button2.getName())) && !(
                    (button1.getX() == forbiddenX) && (button1.getY()+1 == forbiddenY))) {
                if (button1.getY() < rows - 2) {
                    if (buttonGrid[button1.getX()][button1.getY()+2].getName().equals(button2.getName())) {
                        //The index countX is used because this is how many tiles we have with the same symbol
                        sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY()+2]);
                        countY++;
                    }
                }
                sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY()+1]);
                countY++;
            }
        }
        
        
        for (int i = 0; i < sameSymbolY.size(); i++) {
            System.out.println(sameSymbolY.get(i).getX() + ", " + sameSymbolY.get(i).getY());
        }
        
        //If countX equals at least 3, then we have a match!  They need to be removed
    }
    
    public void simpleSwitch(SmartButton[][] buttonGrid, SmartButton button1, SmartButton button2) {
        Image button1Image = button1.getImage();
        Image button2Image = button2.getImage();
        String button1Name = button1.getName();
        String button2Name = button2.getName();
        button1.setImage(button2Image);
        button2.setImage(button1Image);
        button1.setName(button2Name);
        button2.setName(button1Name);
        button1.setGraphic(new ImageView(button2Image));
        button2.setGraphic(new ImageView(button1Image));
        
    }

    public static void main(String[] args) {
        launch(args);
    }

}
