public class Bush extends FixedGameElement{
    
    public Bush(int pos_x, int pos_y)
    {
        super(pos_x, pos_y);
    }

    public static String getPathToImage()
    {
        return "gameIcones/bush.png";
    }

    public String getType()
    {
        return "bush";
    }

    public void triggerAction(Board board)
    {
        //pas d'action le joueur ne peut juste pas accéder à la position où se trouve le bush
    }

}
