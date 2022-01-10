public class RedInsect extends FixedGameElement
{
    public RedInsect(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }
    
    public static String getPathToImage(){
        return "gameIcones/redInsect.png";
    }

    public String getType(){
        return "redInsect";
    }

    public void triggerAction(Board board){
        int valueOfInsect = 2;

        this.setPosX(board.getVoid_x()); //on déplace coin ou l'insecte dans le "vide" pour la faire disparaitre de l'écran
        this.setPosY(board.getVoid_y());
        board.incScore(valueOfInsect); //augmente le score de 2 (dans Board)
    }
}
