public class BlueInsect extends FixedGameElement{
    
    public BlueInsect(int pos_x, int pos_y){
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "gameIcones/blueInsect.png";
    }

    public String getType(){
        return "blueInsect";
    }

    public void triggerAction(Board board){
        int valueOfInsect = 1;

        this.setPosX(board.getVoid_x()); //on déplace coin ou l'insecte dans le "vide" pour la faire disparaitre de l'écran
        this.setPosY(board.getVoid_y());
        board.incScore(valueOfInsect);
    }
}
