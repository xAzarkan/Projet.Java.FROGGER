public class Pill extends FixedGameElement{
    
    public Pill(int pos_x, int pos_y)
    {
        super(pos_x, pos_y);
    }
    public static String getPathToImage()
    {
        return "gameIcones/pill.png";
    }

    public String getType()
    {
        return "pill";
    }

    public void triggerAction(Board board)
    {
        this.setPosX(board.getVoid_x()); //on met la pillule dans le "vide" (on ne la voit plus sur le board)
        this.setPosY(board.getVoid_y());
        board.setInvincibleModeActivated(); //activation du mode invincible
    }
}
