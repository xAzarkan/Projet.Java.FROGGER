public class Coin extends FixedGameElement
{
    public Coin(int pos_x, int pos_y) {
        
        super(pos_x, pos_y);
    }
    public static String getPathToImage(){
        return "coin.png";
    }

    public String getType(){
        return "coin";
    }

    public void triggerAction(Board board){
        board.incScore(1); //augmente le score de 1 (dans Board)
        board.decreaseCoinAmount(); //diminie le nombre coin de 1
    }
}
