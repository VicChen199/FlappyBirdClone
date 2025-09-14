/*
Flappy Bird Clone

This is a clone of the game Flappy Bird with a twist - the ability to dash! 
You can now dash through the pipes rather than just jumping through them.
However, the opening between the pipes gets even smaller now.
 */

// These imports set up Processing
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import java.util.ArrayList;

public class Runner extends PApplet{

    // Declares necessary variables to manage the display, timing, and win/lose conditions
    Player user;
    PImage userPhoto, backgroundPhoto, pipeUpPhoto, pipeDownPhoto, startLogoTop, tellUser1, tellUser2, tellUser3, tellUser4;
    long currentJumpTime, startJumpTime, startPipeTime, currentPipeTime, startDashTime, currentDashTime;
    int elapsedJumpTime, elapsedPipeTime, score, best = 0, elapsedDashTime;
    boolean gameStarted, gameOver;
    PFont mono;
    ArrayList<Pipe> pipes;

    public static void main(String[] args) {
        PApplet.main("Runner", args);
    }

    public void setup() {

        // Declares and initializes necessary variables manage the score,
        // win/lose conditions, and the user's inputs.
        score = 0;
        gameStarted = false;
        gameOver = false;
        pipes = new ArrayList<Pipe>();
        user = new Player();

        // Declares and initialized the different parts of the display (images and font)
        mono = createFont("src/FlappyBirdRegular-9Pq0.ttf", 128);
        userPhoto = loadImage("src/Images/FlappyBirdV2.png");
        userPhoto.resize(80,56);

        backgroundPhoto = loadImage("src/Images/background.png");
        backgroundPhoto.resize(1200,900);

        pipeUpPhoto = loadImage("src/Images/image.png");
        pipeUpPhoto.resize(100,900);

        pipeDownPhoto = loadImage("src/Images/183-1831473_flappy-bird-pipe-png-flappy-bird-pipe-transparent.png");
        pipeDownPhoto.resize(100,900);

        startLogoTop = loadImage("src/Images/text-1718069936146.png");
        startLogoTop.resize(1000, 150);

        tellUser1 = loadImage("src/Images/text-1717948023243.png");
        tellUser1.resize(400,60);
        tellUser2 = loadImage("src/Images/text-1718069796908.png");
        tellUser2.resize(400,60);
        tellUser3 = loadImage("src/Images/text-1718070121998.png");
        tellUser3.resize(400,60);
        tellUser4 = loadImage("src/Images/text-1718070129102.png");
        tellUser4.resize(400,60);
    }

    // Manages the size of the game
    public void settings() {
        size(1200, 900);
    }

    public void draw() {

        // If the user hits the pipe, then the game ends
        // Determined by examining the edges of the bird end up where the pipe is
        for(Pipe pipe: pipes){
            if( ( (user.x > pipe.x)  && (user.x < pipe.x + pipe.width) ) || ( (user.x + user.width > pipe.x) && (user.x + user.width < pipe.x + pipe.width) ) ){
                if(user.y < pipe.yUp + pipe.height){
                    gameOver = true;
                }
                if(user.y + user.height > pipe.yDown){
                    gameOver = true;
                }
            }
        }

        if(!gameOver){

            // Determines when the bird starts to fall after a jump is initiated
            // aka. the downward half of the jump
            if(user.isJumping){
                currentJumpTime = System.currentTimeMillis();
                elapsedJumpTime = (int)(currentJumpTime - startJumpTime);

                if(pipes.size() > 0){
                    if(!(pipes.get(0).isDash) && elapsedJumpTime >= 180){
                        user.isJumping = false;
                        user.isFalling = true;
                    }
                }
                else{
                    if(elapsedJumpTime >= 180){
                        user.isJumping = false;
                        user.isFalling = true;
                    }
                }
            }

            // Determines when the dash is over after a dash is initiated
            for(Pipe pipe: pipes) {
                if (pipe.isDash) {
                    currentDashTime = System.currentTimeMillis();
                    elapsedDashTime = (int) (currentDashTime - startDashTime);

                    if (elapsedDashTime >= 150) {
                        user.isJumping = false;
                        user.isFalling = true;
                        user.isDash = false;
                        user.degreeChange = 0;
                        for(Pipe a: pipes){
                            a.isDash = false;
                        }
                    }
                }
            }

            // Adds new pipes to the game
            if(gameStarted){
                currentPipeTime = System.currentTimeMillis();
                elapsedPipeTime = (int)(currentPipeTime - startPipeTime);
                if(elapsedPipeTime >= (int)(Math.random() * (3000-1500+1) + 1500)){
                    pipes.add(new Pipe(score));
                    startPipeTime = currentPipeTime;
                }
            }
        }
        else{
            user.isJumping = false;
            user.isFalling = true;
        }

        // If the bird touches the floor or ceiling, the game ends
        if(user.y + user.height > 825 || user.y < 0){
            gameOver = true;
            if(user.y + user.height > 825){
                user.isFalling = false;
            }
        }

        // Tells the Player class to adjust the speed and angle of the bird
        // depending on the time that passed since the jump was initiated
        user.jump(elapsedJumpTime);
        user.fall(elapsedJumpTime);

        // Adds the background to the game
        image(backgroundPhoto,0,0);

        // This moves the position of the pipes and tracks the score
        score = 0;
        for(Pipe pipe: pipes){
            if(!gameOver){
                pipe.move();
            }
            if(user.x > pipe.x + pipe.width){
                score++;
            }

            // This displays the pipes
            noStroke();
            noFill();
            pipe.display(this);
            image(pipeUpPhoto, pipe.x, pipe.yUp);
            image(pipeDownPhoto, pipe.x, pipe.yDown);
        }

        // This displays the bird with the correct angle
        noStroke();
        noFill();
        pushMatrix();
        translate(user.x + user.width/2, user.y + user.height/2);
        user.display(this);
        rotate(radians(user.degrees));
        image(userPhoto, -user.width/2,-user.height/2);
        popMatrix();

        // This displays the home screen
        if(!gameStarted){
            image(startLogoTop,100,100);
            image(tellUser1, 75, 375);
            image(tellUser2, 125,450);
            image(tellUser3, 675, 375);
            image(tellUser4, 750, 450);
        }

        // This displays the score
        else if(!gameOver){
            textFont(mono);
            fill(255,255,255);
            textSize(150);
            text(score, 580, 200);
        }

        // This displays the Game Over screen
        if(gameOver){
            if(best < score){
                best = score;
            }
            // fail display
            fill(221, 194, 111);
            rect(350, 250, 500, 400);

            fill(255, 255, 255);
            textFont(mono);
            textSize(125);
            text("GAME OVER", 390,360);

            fill(255, 255, 255);
            textFont(mono);
            textSize(100);
            text("SCORE: " + score, 390,450);

            fill(255, 255, 255);
            textFont(mono);
            textSize(100);
            text("BEST: " + best, 390,540);

            fill(255, 255, 255);
            textFont(mono);
            textSize(100);
            text("'R' to Reset", 390,630);
        }
    }

    public void keyPressed(){

        // This initialized the timer for the jump
        if(key == 'j'){
            if(!gameOver){
                user.isJumping = true;
                user.isFalling = false;
                startJumpTime = System.currentTimeMillis();
                if(!gameStarted){
                    gameStarted = true;
                    startPipeTime = System.currentTimeMillis();
                }
            }
        }

        // This resets the game
        if(key == 'r'){
            setup();
        }

        // This stops a jump if there was one and starts the timing of the dash
        if(key == 'k'){
            if(!gameOver){
                if(pipes.isEmpty()){
                    user.isJumping = false;
                    user.isFalling = true;
                }
                else{
                    user.isJumping = false;
                    user.isFalling = false;
                    user.isDash = true;
                    user.degreeChange = 0;
                    startDashTime = System.currentTimeMillis();
                    for(Pipe pipe: pipes){
                        pipe.isDash = true;
                    }
                }
                if(!gameStarted){
                    gameStarted = true;
                    startPipeTime = System.currentTimeMillis();
                }
            }
        }
    }
}