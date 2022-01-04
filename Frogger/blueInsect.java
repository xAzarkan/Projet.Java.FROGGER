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
        this.setPosX(board.getVoid_x()); //on déplace coin ou l'insecte dans le "vide" pour la faire disparaitre de l'écran
        this.setPosY(board.getVoid_y());
        board.incScore(1);
    }
}
