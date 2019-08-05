package tictactoe;

import processing.core.PApplet;

public class TicTacToe extends PApplet {
    int cols = 3;
    int rows = 3;
    int h;
    int w;

    GridSquare[][] board;
    int totalTurns = 0;

    enum GameState {
        OVER,
        RUNNING
    }

    static GameState currentState;

    public static void main(String[] args) {
        PApplet.main("tictactoe.TicTacToe");
    }

    public void setup() {
        w = width / cols;
        h = height / rows;
        board = new GridSquare[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = new GridSquare(r * w, c * h, w, h);
            }
        }
        currentState = GameState.RUNNING;

    }

    public void settings() {
        size(300, 300);
    }

    public void draw() {
        background(255);
        switch (currentState) {
            case OVER:
                drawRunning();
                drawGameOver();
                break;
            case RUNNING:
                drawRunning();
                break;
        }
    }

    void drawGameOver() {
        fill(51, 204, 255);
        noStroke();
        rect(80, 120, 140, 60);
        fill(255); // 255
        textAlign(CENTER);
        text("Game Over!", width / 2, 140);
        if (winner == 0) {
            text("O Wins", width / 2, 160);
        }
        if (winner == 1) {
            text("X Wins", width / 2, 160);
        }
        if (totalTurns == 9) {
            text("Draw", width / 2, 160);
        }
    }

    void drawRunning() {
        background(255);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                stroke(0);
                noFill();
                rect(i * w, j * h, w, h);
                board[i][j].drawTurn();
            }
        }
    }

    public void mousePressed() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (board[i][j].state == -1) {
                    totalTurns = board[i][j].onClick(mouseX, mouseY, totalTurns);
                    checkWin(i, j, board[i][j].state);
                }
            }
        }
    }

    int winner = -1;

    public void checkWin(int x, int y, int turn) {
        int colWin = 0;
        int rowWin = 0;
        int diag1Win = 0;
        int diag2Win = 0;

        for (int next = 0; next < 3; next++) {
            if (board[x][next].state == turn) {
                colWin++;
            }
            if (board[next][y].state == turn) {
                rowWin++;
            }
            if (board[next][next].state == turn) {
                diag1Win++;
            }
            if (board[next][2 - next].state == turn) {
                diag2Win++;
            }
        }
        if (colWin == 3 || rowWin == 3 || diag1Win == 3 || diag2Win == 3) {
            winner = turn;
            if (winner != -1) {
                currentState = GameState.OVER;
            }
        }
        if (totalTurns == 9) {
            currentState = GameState.OVER;
        }
    }

    public class GridSquare {
        public float x;
        public float y;
        public float w;
        public float h;
        public int state;

        public GridSquare(float tempX, float tempY, float tempW, float tempH) {
            x = tempX;
            y = tempY;
            w = tempW;
            h = tempH;
            state = -1;
        }

        public void drawTurn() {
            if (state == 0) {
                //Draw O
                ellipse(x + w / 2, y + h / 2, w, h);
            }
            if (state == 1) {
                //Draw X
                line(x, y, x + w, y + h);
                line(x + w, y, x, y + h);
            }
        }

        int onClick(int clickedX, int clickedY, int turn) {
            if (clickedX > x && clickedX < x + w && clickedY > y && clickedY < y + h) {
                state = turn % 2;
                return turn + 1;
            }
            return turn;
        }
    }
}