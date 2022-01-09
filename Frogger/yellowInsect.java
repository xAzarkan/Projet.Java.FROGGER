public class YellowInsect extends FixedGameElement{

    public YellowInsect(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }
    
    public static String getPathToImage(){
        return "yellowInsect.png";
    }

    public String getType(){
        return "yellowInsect";
    }

    public void triggerAction(Board board){
        this.setPosX(board.getVoid_x()); //on déplace coin ou l'insecte dans le "vide" pour la faire disparaitre de l'écran
        this.setPosY(board.getVoid_y());
        board.incScore(5); //augmente le score de 5 (dans Board)
    }
    
}
