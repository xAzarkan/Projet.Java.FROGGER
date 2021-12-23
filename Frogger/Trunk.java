public class Trunk extends Car
{
    private int trunkNumber;
    
    public Trunk(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "trunk.png";
    }

    public String getCarType(){
        return "trunk";
    }

    public void setTrunkNumber(int trunkNumber)
    {
        this.trunkNumber = trunkNumber;
    }

    public int getTrunkNumber()
    {
        return this.trunkNumber;
    }

    public void moveCar(Board b)
    {  
        posCar_x = this.getPosX();

        if(trunkNumber % 2 == 0)
        {
            if(posCar_x > 0)
            {
                posCar_x -= b.getDotSize()/3;
            }
            else
            {
                posCar_x = b.getWidth();
            }
        }
        else
        {
            if(posCar_x < b.getWidth())
            {
                posCar_x += b.getDotSize()/3;
            }
            else
            {
                posCar_x = 0;
            }
        }   
    
        this.setPosX(posCar_x);    
    }

    @Override
    public void triggerAction(Board board){
        //le frogger va se poser sur le tronc (même pos_x que le tronc)
        posCar_x = this.getPosX();
        board.getFrog().setPosX(posCar_x);
    }
}
