public class yellowInsect extends FixedGameElement{

    public yellowInsect(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }
    
    public static String getPathToImage(){
        return "yellowInsect.png";
    }

    public String getType(){
        return "yellowInsect";
    }

    public void triggerAction(Board board){
        board.incScore(5); //augmente le score de 5 (dans Board)
    }
    
}
