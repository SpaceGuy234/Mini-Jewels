/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matchingthreegame;

import javafx.scene.image.Image;
import javafx.scene.control.Button;

/**
 *
 * @author Jonathan
 */
public class SmartButton extends Button{
    
    private int x;
    private int y;
    private Image symbol;
    
    public SmartButton(int x, int y, Image s) {
        super();
        this.symbol = s;
        this.x = x;
        this.y = y;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public void setX(int newX) {
        this.x = newX;
    }
    
    public void setY(int newY) {
        this.y = newY;
    }
}
