package pacman;

import processing.core.PApplet;

import java.awt.*;

public class PacMan extends PApplet{
    int posX = 20;
    int posY = 40;
    public static void main(String[] args) {
        PApplet.main("pacman.PacMan");
    }
    public void draw(){
        drawPlayArea();
    }
    public void setup(){

    }
    public void settings(){
        size(700,800);
    }
    public void drawPlayArea(){
        background(0,0,0);
        rect(posX, posY, 50, 700);
        rect(posX, posY, 640, 40);
        rect(width - posX - 70, posY, 50, 700);
        rect(posX, height - posY - 60, 640, 40);
    }
}
