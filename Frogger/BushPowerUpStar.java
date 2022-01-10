public class BushPowerUpStar extends FixedGameElement
{
    public BushPowerUpStar(int pos_x, int pos_y)
    {
        super(pos_x, pos_y);
    }
    public static String getPathToImage()
    {
        return "gameIcones/bushpowerup.png";
    }

    public String getType()
    {
        return "bushpowerupstar";
    }

    public void triggerAction(Board board)
    { //cette étoile permet de marcher sur les buissons pendant tout un niveau (se désactive une fois mort)
        this.setPosX(board.getVoid_x()); //on met la pillule dans le "vide" (on ne la voit plus sur le board)
        this.setPosY(board.getVoid_y());
        board.setWalkOverBushMode(); 
    }
}
