import processing.core.PApplet;

public class Player extends PApplet {
    // This declares necessary variables to determine the position, angle,
    // jumping, falling, and dashing of the player
    public int y, x, width, height, ySpeed, degrees, degreeChange;
    public boolean isJumping, isFalling, isDash;

    // This constructor determines the position and initialized variables for position, angle
    // jumping, falling, and dashing of the player
    public Player(){
        width = 80;
        height = 56;
        y = 450-height/2;
        x = 600-width/2;
        isJumping = false;
        isFalling = false;
        isDash = false;
        ySpeed = 0;
        degrees = 0;
        degreeChange = 0;
    }

    // This method determines the speed and angle of the player when jumping
    // relative to the time that has passed
    public void jump(int elapsed){
        if(isJumping){
            if(ySpeed > 0){
                ySpeed = 0;
            }
            degrees = -45;
            ySpeed -= 1;
        }
    }

    // This method determines the speed and angle ofthe plauer when falling
    // relative to the time that has passed
    public void fall(int elapsed){
        if(isFalling){
            ySpeed += 1;
            degrees+=3;
        }
    }

    // This displays player accounting for any changed to speed and angle
    public void display(PApplet pa){
        if(!isDash){
            y += ySpeed;
        }

        if(degrees >= 90){
            degrees = 90;
        }
        pa.rect(-width/2, -height/2,width,height);
    }
}