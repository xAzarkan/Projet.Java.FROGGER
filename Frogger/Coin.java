public class Coin extends FixedGameElement
{
    public Coin(int pos_x, int pos_y) {
        
        super(pos_x, pos_y);
    }
    public static String getPathToImage(){
        return "gameIcones/coin.png";
    }

    public String getType(){
        return "coin";
    }

    public void triggerAction(Board board){
        int valueOfCoin = 1;
        this.setPosX(board.getVoid_x()); //on déplace coin ou l'insecte dans le "vide" pour la faire disparaitre de l'écran
        this.setPosY(board.getVoid_y());
        board.incScore(valueOfCoin); //augmente le score de 1 (dans Board)
        board.decreaseCoinAmount(); //diminie le nombre coin de 1
    }
}
