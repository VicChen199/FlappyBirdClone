import processing.core.PApplet;

public class Pipe extends PApplet{

    // This declares the necessary variables for the pipe's position, size,
    // and what happens during a dash
    public int yDown, x, yUp, hole, holeHigh, holeLow, yDownHigh, yDownLow, width, height;
    public boolean isDash;

    // Constructor that determines the position of the pipe based on the score
    public Pipe(int score){
        isDash = false;
        width = 100;
        height = 900;
        holeLow = 150;
        yDownHigh = 700;

        if(score <= 13){
            hole = 230 - (score * 10);
        }
        else{
            hole = 100;
        }
        yDownLow = 200 + hole;

        yDown = (int)(Math.random() * (yDownHigh - yDownLow + 1) + yDownLow);
        yUp = yDown - hole - height;
        x = 1200;
    }

    // Moves the pipe based on if a dash was initiated or not
    public void move(){
        if(isDash){
            x-=20;
        }
        else{
            x -= 4;
        }
    }

    // Displays the pipe
    public void display(PApplet pa){
        pa.rect(x, yDown, width, height);
        pa.rect(x, yUp, width, height);
    }
}