package imagetest;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageTest extends PApplet {
    PImage character;
    PImage bg;
    public static void main(String[] args) {
        PApplet.main("imagetest.ImageTest");
    }
    public void setup(){
        character = loadImage("Images/Character.png");
        bg = loadImage("Images/bg.png");
    }
    public void settings(){
        size(300,300);
    }
    public void draw(){
        background(bg);
        character.resize(300,300);
        image(character,0,0);
    }
}
