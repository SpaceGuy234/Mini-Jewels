/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matchingthreegame;

import javafx.scene.control.Button;

/**
 *
 * @author Jonathan
 */
public class SmartButton extends Button{
    
    private final int x;
    private final int y;
    
    public SmartButton(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
}
