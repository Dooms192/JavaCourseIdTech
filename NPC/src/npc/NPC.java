package npc;
import processing.core.PApplet;
import processing.core.PFont;

public class NPC extends PApplet {
    PFont font;
    boolean showText = false;

    public static void main(String[] args) {
        PApplet.main("npc.NPC");
    }

    public void setup() {
        font = createFont("Arial", 16, true);
    }

    public void settings() {
        size(600, 500);
    }

    public void draw() {
        background(255, 255, 255);
        textFont(font, 50);
        if (showText) {
            fill(200, 100, 0);
            text("Hello World!", 15, 50);
        }
    }

    public void keyPressed() {
        if (key == 'w') {
            showText = true;
        }
    }

    public void keyReleased() {
        if (key == 'w') {
            showText = false;
        }
    }

}
