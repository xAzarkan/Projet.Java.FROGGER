import java.awt.Rectangle;

public class Frog
{
    private int pos_x;
    private int pos_y;
    private int imgHeight = 20;
    private int imgWidth = 20;

    public Frog(int pos_x, int pos_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }
    
    public int getPosX(){
        return pos_x;
    }
    
    public int getPosY(){
        return pos_y;
    }
    
    public void setPosX_Y(int new_posX, int new_posY){
        pos_x = new_posX;
        pos_y = new_posY;
    }
    
    public String getType(){
        return "frog";
    }

    public static String getPathToImage(){
        return "frogger2.png";
    }

    public Rectangle getBounds() {
        return new Rectangle(this.getPosX(), this.getPosY(), imgWidth, imgHeight);
    }
}
