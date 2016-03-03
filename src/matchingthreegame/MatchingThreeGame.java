/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matchingthreegame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    Runnable r;
    Thread t;

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
    
    
    public ArrayList<Pair<String, Image>> makeList() {
        //The arrayList
        ArrayList<Pair<String, Image>> possibleSymbols = new ArrayList<>(7);
        possibleSymbols.add(new Pair<String, Image>("drop", new Image("/drop.png", 40, 40, false, true)));
        possibleSymbols.add(new Pair<String, Image>("greenswirl", new Image("/greenswirl.png", 40, 40, true, true)));
        possibleSymbols.add(new Pair<String, Image>("minisakura", new Image("/minisakura.png", 40, 40, true, true)));
        possibleSymbols.add(new Pair<String, Image>("man", new Image("/man.png", 40, 40, true, true)));
        possibleSymbols.add(new Pair<String, Image>("twindragons", new Image("/twin_dragons.png", 40, 40, true, true)));
        possibleSymbols.add(new Pair<String, Image>("sunflower", new Image("/sunflower.png", 40, 40, true, true)));
        
        return possibleSymbols;
    }
    public SmartButton initializeGrid(int x, int y, SmartButton[][] buttonGrid) {

        //Create an arrayList of all of the possible values that the cell can be
        //For example, if two adjacent cells are the green symbol, remove green from
        
        //Add all the possible names to the arrayList
        
        ArrayList<Pair<String, Image>> possibleSymbols = makeList();
        //Generate the random number and then assign the corresponding image
        Random generator = new Random();
        int randomInt;
        String pictureName = "";
        Image image;
        if ((x == 0 || x == 1) && (y == 0 || y == 1)) {
            //Do nothing here, just a place holder
        } else if ((!(x == 0 || x == 1)) && (y == 0 || y == 1)) {

            if (buttonGrid[x - 2][y].getName().equals(buttonGrid[x - 1][y].getName())) {
                String nameToRemoveX = buttonGrid[x - 1][y].getName();
                for (int i = 0; i < possibleSymbols.size(); i++) {
                    if (possibleSymbols.get(i).getKey().equals(nameToRemoveX)) {
                        possibleSymbols.remove(i);
                    }

                }
            }
        } else if ((x == 0 || x == 1) && (!(y == 0 || y == 1))) {

            if (buttonGrid[x][y - 2].getName().equals(buttonGrid[x][y - 1].getName())) {
                String nameToRemoveY = buttonGrid[x][y - 1].getName();
                for (int j = 0; j < possibleSymbols.size(); j++) {
                    if (possibleSymbols.get(j).getKey().equals(nameToRemoveY)) {
                        possibleSymbols.remove(j);
                    }
                }
            }
        } else {
            if (buttonGrid[x - 2][y].getName().equals(buttonGrid[x - 1][y].getName())) {
                String nameToRemoveX = buttonGrid[x - 1][y].getName();
                for (int i = 0; i < possibleSymbols.size(); i++) {
                    if (possibleSymbols.get(i).getKey().equals(nameToRemoveX)) {
                        possibleSymbols.remove(i);
                    }
                }
            }
            if (buttonGrid[x][y - 2].getName().equals(buttonGrid[x][y - 1].getName())) {
                String nameToRemoveY = buttonGrid[x][y - 1].getName();
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
        } 
        else {
            rememberMe.setStyle("-fx-border-color: transparent");
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
                //X-Checker-First Button
                ArrayList<SmartButton> firstX = switchImagesCheckHorizontal(buttonGrid, button, rememberMe, rows, columns);
                //Y-Checker-First Button
                ArrayList<SmartButton> firstY = switchImagesCheckVertical(buttonGrid, button, rememberMe, rows, columns);
                //X-Checker-Second Button
                ArrayList<SmartButton> secondX = switchImagesCheckHorizontal(buttonGrid, rememberMe, button, rows, columns);
                //Y-Checker-Second Button
                ArrayList<SmartButton> secondY = switchImagesCheckVertical(buttonGrid, rememberMe, button, rows, columns);
                //If one of these booleans is true, that means that there is three in a row in some direction.
                //Create a boolean to encompass all of these
                boolean match = (firstX.size() >= 3) || (firstY.size() >= 3) || (secondX.size() >= 3) || (secondY.size() >= 3);
                if (match) {
                    simpleSwitch(buttonGrid, rememberMe, button);
                    ArrayList<SmartButton> noDuplicates = getListOfRemoved(firstX, firstY, secondX, secondY);
                    remove(noDuplicates, buttonGrid);
                }
                else {
                     //If we can indicate that the move didn't work here, that would be fantastic
                }
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

    public ArrayList<SmartButton> switchImagesCheckHorizontal(SmartButton[][] buttonGrid, SmartButton button1, SmartButton button2, int rows, int columns) {
        ArrayList<SmartButton> potentialNeighbors = new ArrayList<>();
        potentialNeighbors = getNeighbors(buttonGrid, button1, rows, columns);
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
            if ((buttonGrid[button1.getX() - 1][button1.getY()].getName().equals(button2.getName())) && !(button1.getX() - 1 == forbiddenX && button1.getY() == forbiddenY)) {
                if (button1.getX() > 1) {
                    if (buttonGrid[button1.getX() - 2][button1.getY()].getName().equals(button2.getName())) {
                        //The index countX is used because this is how many tiles we have with the same symbol
                        sameSymbolX.add(countX, buttonGrid[button1.getX() - 2][button1.getY()]);
                        countX++;
                    }
                }
                sameSymbolX.add(countX, buttonGrid[button1.getX() - 1][button1.getY()]);
                countX++;
            }
        }

        //Now add the original button flipped, the one we call button
        sameSymbolX.add(countX, buttonGrid[button1.getX()][button1.getY()]);
        countX++;

        //Check to the right here.  Reuse code from above mostly
        if (button1.getX() < columns - 1) {
            if ((buttonGrid[button1.getX() + 1][button1.getY()].getName().equals(button2.getName())) && !(button1.getX() + 1 == forbiddenX && button1.getY() == forbiddenY)) {
                if (button1.getX() < columns - 2) {
                    if (buttonGrid[button1.getX() + 2][button1.getY()].getName().equals(button2.getName())) {
                        //The index countX is used because this is how many tiles we have with the same symbol
                        sameSymbolX.add(countX, buttonGrid[button1.getX() + 2][button1.getY()]);
                        countX++;
                    }
                }
                sameSymbolX.add(countX, buttonGrid[button1.getX() + 1][button1.getY()]);
                countX++;
            }
        }

        //If countX equals at least 3, then we have a match!  They need to be removed
        //Return that we have three in a row to allow the next method which removes code
        //To happen.
        return sameSymbolX;
    }

    public ArrayList<SmartButton> switchImagesCheckVertical(SmartButton[][] buttonGrid, SmartButton button1, SmartButton button2, int rows, int columns) {
        ArrayList<SmartButton> potentialNeighbors = new ArrayList<>();
        potentialNeighbors = getNeighbors(buttonGrid, button1, rows, columns);
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
            if ((buttonGrid[button1.getX()][button1.getY() - 1].getName().equals(button2.getName())) && !(button1.getX() == forbiddenX && button1.getY() - 1 == forbiddenY)) {
                if (button1.getY() > 1) {
                    if (buttonGrid[button1.getX()][button1.getY() - 2].getName().equals(button2.getName())) {
                        //The index countX is used because this is how many tiles we have with the same symbol
                        sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY() - 2]);
                        countY++;
                    }
                }
                sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY() - 1]);
                countY++;
            }
        }

        //Now add the original button flipped, the one we call button
        sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY()]);
        countY++;

        //Check to the bottom here.  Reuse code from above mostly
        if (button1.getY() < rows - 1) {
            if ((buttonGrid[button1.getX()][button1.getY() + 1].getName().equals(button2.getName())) && !((button1.getX() == forbiddenX) && (button1.getY() + 1 == forbiddenY))) {
                if (button1.getY() < rows - 2) {
                    if (buttonGrid[button1.getX()][button1.getY() + 2].getName().equals(button2.getName())) {
                        //The index countX is used because this is how many tiles we have with the same symbol
                        sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY() + 2]);
                        countY++;
                    }
                }
                sameSymbolY.add(countY, buttonGrid[button1.getX()][button1.getY() + 1]);
                countY++;
            }
        }

        //If countX equals at least 3, then we have a match!  They need to be removed
        //Return that we have three in a row to allow the next method which removes code
        //To happen.
        return sameSymbolY;
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

    public ArrayList<SmartButton> getListOfRemoved(ArrayList<SmartButton> firstX, ArrayList<SmartButton> firstY,
            ArrayList<SmartButton> secondX, ArrayList<SmartButton> secondY) {
        ArrayList<SmartButton> removeThese = new ArrayList<>();
        ArrayList<SmartButton> noDuplicates = new ArrayList<>();
        int countFirst = 0;
        int countSecond = 0;
        if (firstX.size() >= 3) {
            for (SmartButton x : firstX) {
                removeThese.add(x);
            }
            countFirst++;
        }
        if (firstY.size() >= 3) {
            for (SmartButton x : firstY) {
                removeThese.add(x);
            }
            countFirst++;
        }
        if (secondX.size() >= 3) {
            for (SmartButton x : secondX) {
                removeThese.add(x);
            }
            countSecond++;
        }
        if (secondY.size() >= 3) {
            for (SmartButton x : secondY) {
                removeThese.add(x);
            }
            countSecond++;
        }
        boolean dupe = false;
        if ((countFirst == 2) || (countSecond == 2)) {
            for (SmartButton s : removeThese) {
                dupe = false;
                for (SmartButton b : noDuplicates) {
                    if (b == s) {
                        dupe = true;
                    }
                }
                if (dupe == false) {
                    noDuplicates.add(s);
                }
            }
        }
        else {
            if (firstX.size() >= 3) {
                for (SmartButton b : firstX) {
                    noDuplicates.add(b);
                }
            }
            if (firstY.size() >= 3) {
                for (SmartButton b : firstY) {
                    noDuplicates.add(b);
                }
            }
            if (secondX.size() >= 3) {
                for (SmartButton b : secondX) {
                    noDuplicates.add(b);
                }
            }
            if (secondY.size() >= 3) {
                for (SmartButton b : secondY) {
                    noDuplicates.add(b);
                }
            }
            for (SmartButton s : noDuplicates) {
                s.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
                //s.setStyle("-fx-border-color: green");
            }
        }
        
        return noDuplicates;
    }
    
    FadeTransition ft;
    
    public void remove(ArrayList<SmartButton> noDuplicates, SmartButton[][]buttonGrid) {
        for (SmartButton s : noDuplicates) {
            
            ft = new FadeTransition(Duration.millis(1000), s);
            ft.setFromValue(0.2);
            ft.setToValue(1.0);
            ft.play();
        }
        boolean isHor = checkForHorizontal(noDuplicates);
        boolean isVert = checkForVertical(noDuplicates);
        if (isHor && (!isVert)) {
            //Check to see if there is a match horizontally.  If so, use this method
            assignNewValueHorizontal(noDuplicates, buttonGrid);
        }
        else if ((!isHor) && isVert) {
            //Check to see if there is a match vertically.  If so, use this method.
            assignNewValueVertical(noDuplicates, buttonGrid);
        }
        else {
            //Match both vertically and horizontally. 
            ArrayList<SmartButton> horizontalIncludingDupe = new ArrayList<>();
            ArrayList<SmartButton> verticalNoDupe = new ArrayList<>();
            int commonY = getCommonY(noDuplicates);
            if (commonY != -1) {
                for (int i = 0; i < noDuplicates.size(); i++) {
                    if (noDuplicates.get(i).getY() == commonY) {
                        horizontalIncludingDupe.add(noDuplicates.get(i));
                        noDuplicates.remove(i);
                        assignNewValueHorizontal(horizontalIncludingDupe, buttonGrid);
                    }
                }
            }
            verticalNoDupe = noDuplicates;
            assignNewValueVertical(verticalNoDupe, buttonGrid);
            
        }
            
        //}
    }
    
    public boolean checkForHorizontal(ArrayList<SmartButton> noDuplicates) {
        int horCount = 0;
        for (SmartButton s : noDuplicates) {
            for (SmartButton b: noDuplicates) {
                if (b.getY() == s.getY()) {
                    horCount++;
                }
            }
            if (horCount >= 3) {
                return true;
            }
            horCount = 0;
        }
        return false;
    }
    
    public boolean checkForVertical(ArrayList<SmartButton> noDuplicates) {
        int vertCount = 0;
        for (SmartButton s : noDuplicates) {
            for (SmartButton b: noDuplicates) {
                if (b.getX() == s.getX()) {
                    vertCount++;
                }
            }
            if (vertCount >= 3) {
                return true;
            }
            vertCount = 0;
        }
        return false;
    }
    
    ArrayList<Pair<String, Image>> possibleSymbols = makeList();
    Random generator = new Random();
    int randomInt;
    String pictureName = "";
    Image image;
    int lowestY = -1;
    int newY;
    
    public void assignNewValueHorizontal(ArrayList<SmartButton> noDuplicates, SmartButton[][]buttonGrid) {
        //This variable will count how many buttons there are in the array in the same column
        //We need this variable because we need to know how far everything has to fall to fill
        //In the holes made by the removal
        //for (SmartButton s : noDuplicates) {
        for (SmartButton s : noDuplicates) { 
            for (int i = s.getY(); i >= 0; i--) {
                if (i - 1 > -1) {
                    int theX = s.getX();
                    buttonGrid[s.getX()][i].setName(buttonGrid[s.getX()][i-1].getName());
                    buttonGrid[s.getX()][i].setImage(buttonGrid[s.getX()][i-1].getImage());
                    buttonGrid[s.getX()][i].setGraphic(new ImageView(buttonGrid[s.getX()][i-1].getImage()));
                }
                else {
                    randomInt = generator.nextInt(possibleSymbols.size());
                    buttonGrid[s.getX()][i].setImage(possibleSymbols.get(randomInt).getValue());
                    buttonGrid[s.getX()][i].setName(possibleSymbols.get(randomInt).getKey());
                    buttonGrid[s.getX()][i].setGraphic(new ImageView(buttonGrid[s.getX()][i].getImage()));
                }
            }
        }
    }
    
    public void assignNewValueVertical(ArrayList<SmartButton> noDuplicates, SmartButton[][]buttonGrid){
        lowestY = -1;
        for (int b = 0; b < noDuplicates.size(); b++) {
            newY = noDuplicates.get(b).getY();
            if (newY > lowestY) {
                lowestY = newY;
            }
        }
        SmartButton theButton;
        SmartButton giverButton; //Called this because this button "gives" it's information to theButton.
        int permanentX = noDuplicates.get(0).getX();
        System.out.println(lowestY);
        for (int i = lowestY; i-noDuplicates.size() >= 0; i--) {
            theButton = buttonGrid[permanentX][i];
            giverButton = buttonGrid[permanentX][i-noDuplicates.size()];
            theButton.setName(giverButton.getName());
            theButton.setImage(giverButton.getImage());
            theButton.setGraphic(new ImageView(giverButton.getImage()));
        }
        for (int j = (noDuplicates.size() - 1); j >= 0; j--) {
            theButton = buttonGrid[permanentX][j];
            randomInt = generator.nextInt(possibleSymbols.size());
            theButton.setImage(possibleSymbols.get(randomInt).getValue());
            theButton.setName(possibleSymbols.get(randomInt).getKey());
            theButton.setGraphic(new ImageView(theButton.getImage()));
        }
        
    }
    
    public int getCommonY(ArrayList<SmartButton> noDuplicates) {
        int occurance = 0;
        for (int i = 0; i < noDuplicates.size(); i++) {
            int valueToCheck = noDuplicates.get(i).getY();
            for (int j = 0; j < noDuplicates.size(); j++) {
                if (noDuplicates.get(j).getY() == valueToCheck){
                    occurance++;
                }
            }
            if (occurance >= 3) {
                return valueToCheck;
            }
        }
        //This return statement should never happen;
        return -1;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
