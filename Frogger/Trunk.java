public class Trunk
{
    private int pos_x;
    private int pos_y;


    public Trunk(int pos_x, int pos_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    public static String getPathToImage(){
        return "gameIcones/trunk.png";
    }

    public String getType(){
        return "trunk";
    }

    public int getPosX(){
        return pos_x;
    }
    
    public int getPosY(){
        return pos_y;
    }
    
    public void setPosX(int new_pos){
        pos_x = new_pos;
    }
    
    public void setPosY(int new_pos){
        pos_y = new_pos;
    }
    
}
