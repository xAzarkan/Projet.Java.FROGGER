public class redInsect extends FixedGameElement
{
    public redInsect(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }
    
    public static String getPathToImage(){
        return "redInsect.png";
    }

    public String getType(){
        return "redInsect";
    }

    public void triggerAction(Board board){
        board.incScore(2); //augmente le score de 2 (dans Board)
    }
}
