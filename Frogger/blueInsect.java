public class blueInsect extends FixedGameElement{
    
    public blueInsect(int pos_x, int pos_y){
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "blueInsect.png";
    }

    public String getType(){
        return "blueInsect";
    }

    public void triggerAction(Board board){
        board.incScore(1);
    }
}
