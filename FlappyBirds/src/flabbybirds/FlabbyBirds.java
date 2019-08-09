package flabbybirds;

        import processing.core.PApplet;
        import processing.core.PImage;
        import processing.core.PFont;
        import java.applet.*;
        import java.net.*;
        import java.io.InputStream;
        import  sun.audio.*;    //import the sun.audio package
        import  java.io.*;
        import java.nio.ByteBuffer;
        import java.nio.ByteOrder;
        import javax.sound.sampled.*;
        import java.util.Random;
        import java.util.ArrayList;
public class FlabbyBirds extends PApplet{
    PImage bg, flabby, flabbyFly1, flabbyFly2, pipe, pipe2;
    int speed = 3, bgX =0, bgY =0, flabbyX = 210, flabbyY = 110, pipeX = 600, pipeY = 0, pipe2X = 600, pipe2Y = 310, counter =0, counter2 = 0, counter3 = 0, timer, score, highScore, numOfObstaclesJumped, startTime;
    float jumpHeight;
    boolean fly = false, count = true, saved = false, firstPipe = true;

    Clip audioClip = null, effect = null;
    PFont font, default1, font2;
    ArrayList<Pipes> pipes = new ArrayList<>();
    float speedIncrease = .3f;
    float rateOfDifficulty = 5;
    float counterRotate = 0.0f;
    int difficultyTime = 0;
    enum GameState{
        RUNNING, GAMEOVER, WAITING
    }
    static GameState currentState;

    public static void main(String[] args) {
        PApplet.main("flabbybirds.FlabbyBirds");
        String[] fontList = PFont.list(); printArray(fontList);
    }
    public void setup() {
        bg = loadImage("Images/hi.png");
        flabbyFly1 = loadImage("Images/flabby3.png");
        flabbyFly2 = loadImage("Images/flabby1.png");
        flabby = loadImage("Images/flabby2.png");
        pipe = loadImage("Images/pipe.png");
        pipe2 = loadImage("Images/pipe2.png");
        currentState = GameState.WAITING;
        startTime = millis();
        flabby.resize(50, 50);
        flabbyFly1.resize(50, 50);
        flabbyFly2.resize(50, 50);
        pipe.resize(50, 420); //300
        pipe2.resize(50, 420); //300
        font = createFont("Impact", 40);
        font2 = createFont("Impact", 20);
        default1 = createFont("Arial", 25);
        highScore = loadHighScore();



        try {

            File audioFile = new File("sounds/sfx_wing.wav");

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            effect = (Clip) AudioSystem.getLine(info);

            effect.open(audioStream);
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {

            File audioFile = new File("sounds/sfx_wing.wav");

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(Clip.class, format);

            audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.open(audioStream);
            audioClip.start();

        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }
    public void settings(){
        size(420, 420);
    }
    public void draw(){

        switch (currentState){
            case RUNNING:
                timer = (millis() - startTime) / 1000;
                drawBackground();
                //drawPipes();
                drawNewPipes();
                drawScore();
                drawHighScore();
                drawFlabby();
                increaseDifficulty();
                counter++;
                break;
            case GAMEOVER:
                GameOver();
                break;
            case WAITING:
                drawBackground();
                drawTitle();
                drawFlabby();
                break;

        }

    }
    public void drawBackground(){
        imageMode(CORNER);
        image(bg, bgX, bgY);
        if (currentState == GameState.RUNNING) {
            image(bg, bgX + bg.width, 0);
            bgX -= speed;
            if (bgX <= (bg.width * -1)) {
                bgX = 0;
            }
        }
    }
    public void drawFlabby(){
        imageMode(CENTER);
        if (flabbyY < 10){
            flabbyY = 10;
        }

        if (currentState == GameState.WAITING){
            image(flabby, flabbyX, flabbyY);
        }

        if (fly){
            if(counter2 - counter > 8) {
                image(flabbyFly1, flabbyX, flabbyY);
            }
            if(counter2 - counter <= 8){
                image(flabbyFly2, flabbyX, flabbyY);
            }

            if(counter2 == counter){
                fly = false;
            }

            if(counter3 == counter){
                effect.stop();
                effect.setMicrosecondPosition(0);
            }

        }
        else {
            image(flabby, flabbyX, flabbyY);
        }

        if (flabbyY <= 420 && currentState != GameState.WAITING){
            counterRotate++;
            translate(width/2-flabby.width/2, height/2-flabby.height/2);
            rotate(counterRotate*TWO_PI/64);
            translate(-flabby.width/2,-flabby.height/2);
            jumpHeight += 0.4f;
            flabbyY += jumpHeight;
        }
        if (flabbyY >= 360){
            score = numOfObstaclesJumped;
            if (score > highScore){
                highScore = score;
                currentState = GameState.GAMEOVER;
            }
            if (score < highScore){
                currentState = GameState.GAMEOVER;
            }
        }
       /*if((abs(flabbyX-pipeX) < 40) && abs(flabbyY-pipes.get(0).topbound)< 80 ) {
            score = numOfObstaclesJumped;
            if (score > highScore){
                highScore = score;
                currentState = GameState.GAMEOVER;
            }
            if (score < highScore){
                currentState = GameState.GAMEOVER;
            }
        }
        if((abs(flabbyX-pipe2X) < 40) && abs(flabbyY-pipes.get(0).bottombound)< 80 ) {
            score = numOfObstaclesJumped;
            if (score > highScore){
                highScore = score;
                currentState = GameState.GAMEOVER;
            }
            if (score < highScore){
                currentState = GameState.GAMEOVER;
            }
        }*/
        if ((currentState == GameState.RUNNING) && (abs(flabbyX - pipeX) < 20) && (flabbyY <= pipes.get(0).topbound+420 || flabbyY >= pipes.get(0).bottombound)){
            score = numOfObstaclesJumped;
            if (score > highScore){
                highScore = score;
                currentState = GameState.GAMEOVER;
            }
            if (score < highScore){
                currentState = GameState.GAMEOVER;
            }
        }

    }

    public void mousePressed(){

        if(currentState == GameState.RUNNING){
            Clip audioClip = null;
            if(audioClip!= null){audioClip.stop();}
            fly = true;
            counter2 = counter + 16;
            counter3 = counter + 7;

            jumpHeight = -8;
            flabbyY += jumpHeight;


            effect.start();



        }

        if (currentState == GameState.WAITING){
            currentState = GameState.RUNNING;
        }
        if(currentState == GameState.GAMEOVER){
            reset();
            currentState = GameState.WAITING;
        }
    }
    public void drawPipes() {
        pipeX -= speed;
        pipe2X -= speed;
        imageMode(CENTER);
        image(pipe, pipeX, 0);
        imageMode(CENTER);
        image(pipe2, pipe2X, pipe2Y);
        if (pipeX < 0) {
            //numOfObstaclesJumped += 1;
            count = true;
            pipeX = 420;
        }
        if (pipeX + 50 < flabbyX && count){
            count = false;
            numOfObstaclesJumped += 1;
        }
        if (pipe2X < 0) {
            pipe2X = 420;
        }
    }
    public void drawScore(){
        fill(255,255,255);
        textAlign(CENTER);
        text("Score: " + numOfObstaclesJumped, width -70, 30);
    }

    public void drawHighScore(){
        fill(255,255,255);
        textAlign(CENTER);
        text("HighScore: " + highScore, 65, 30);
    }

    public void GameOver(){
        score = numOfObstaclesJumped;
        fill(255, 190, 190);
        noStroke();
        rect(width / 2 - 125, height / 2 - 80, 250, 160);
        fill(255, 100, 100);
        textAlign(CENTER);
        textFont(default1);

        if (saved == false) {
            saveHighScore();
            saved = true;
        }
        highScore = loadHighScore();
        text("Game Over!", width / 2, height / 2 - 50);
        text("Your Score " + score, width / 2, height - 225);
        text("High Score " + highScore, width / 2, height - 190);
        text("Click To Try Again", width / 2, height -150);

    }
    public void reset(){
        speed = 3;
        bgX = 0;
        score = 0;
        numOfObstaclesJumped = 0;
        flabbyX = 210;
        flabbyY = 110;
        pipeX = 420;
        pipeY = 0;
        pipe2X = 420;
        pipe2Y = 320;
        jumpHeight = 0;
        count = true;
        saved = false;
        firstPipe = true;
        pipes.clear();
    }
    public void increaseDifficulty() {
        if (timer % rateOfDifficulty == 0 && difficultyTime != timer) {
            println("Increase");
            speed += speedIncrease;
            difficultyTime = timer;
        }
    }
    public void drawTitle(){
        fill(255, 191, 0);
        textAlign(CENTER);
        textFont(font);
        text("Flabby Birds!", width / 2, height / 2 - 30);
        textFont(font2);
        text("Click To Start!", width / 2, height - 210);
    }

    public void saveHighScore(){
        int oldscore = loadHighScore();
        if(highScore>oldscore) {
            try {
                FileOutputStream out = new FileOutputStream("save.txt");

                int len = Integer.toString(highScore).length();
                byte data[] = new byte[len];
                //System.out.println("\n");
                for (int i = 0; i < len; i++) {
                    data[i] = (byte) (highScore >>> i * 8);
                    //System.out.print(data[i]);
                }
                out.write(highScore);
                out.close();
            } catch (FileNotFoundException e) {File file = new File("save.txt");}
            catch (IOException e) {}
        }
    }
    public void newObstacle(){
        if (firstPipe){
            Pipes newPipe = new Pipes(254, -296);
            pipes.add(newPipe);
            firstPipe = false;
        }
        else {
            Random generator = new Random();
            int bottom = (generator.nextInt(250)+100); //(401) +100
            int top = bottom - 550; //-550
            System.out.println("New Pipe: " + bottom + " " + top);
            Pipes newPipe = new Pipes(bottom, top);
            pipes.add(newPipe);
        }
    }
    public void drawNewPipes(){
        if (pipes.size() ==0 && firstPipe){
            newObstacle();
        }
        if (pipeX <= 0){
            newObstacle();
            pipeX = 600;
            pipe2X = 600;
            count = true;
            pipes.remove(0);
        }
        Pipes pipe1 = pipes.get(0);
        pipeX -=speed;
        pipe2X -=speed;
        //imageMode(CENTER);
        image(pipe, pipeX, pipe1.topbound);
        //imageMode(CENTER);
        image(pipe2, pipe2X, pipe1.bottombound); //bottom
        if (pipeX + 50 < flabbyX && count){
            count = false;
            numOfObstaclesJumped += 1;
        }
    }

    public int loadHighScore(){
        File save = new File("save.txt");
        int oldhigh = 0;
        try (FileInputStream in = new FileInputStream("save.txt")){
            byte[] content = new byte[(int)save.length()];
            in.read(content);
            //System.out.println(content[0]);
            //final ByteBuffer bb = ByteBuffer.wrap(content);
            //bb.order(ByteOrder.LITTLE_ENDIAN);
            oldhigh = content[0];
            //System.out.println(oldhigh);
            in.close();
        }
        catch(IOException e){
            return oldhigh;
        }

        return oldhigh;
    }

    public class Pipes{
        int bottombound, topbound;

        public Pipes(int bottom, int top){
            this.bottombound = bottom;
            this.topbound = top;
        }
    }

    public void junk(){
        int[][][][][][][][] inventory = new int[10][5][9][70][10][5][9][70]; // new int[object][variation]
        inventory[0][3][1][0][6][2][10][1] = 10;
        println(inventory[0][3][1][0][6][2][10][1]);
    }
}
