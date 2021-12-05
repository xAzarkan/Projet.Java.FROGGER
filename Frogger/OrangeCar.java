public class OrangeCar extends Car
{
    private int posCar_x;

    public OrangeCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "orangeCar.png";
    }

    public String getCarType(){
        return "orangeCar";
    }     

    public void moveCar(Board b)
    {
        //accelere et ralenti au hasard
        int slow = 0;
        
        int randomSpeed = (int) (Math.random() * 2);
        
        posCar_x = this.getPosX();

        if(posCar_x > 0)
        {
            if(randomSpeed == slow)
            {
                posCar_x -= b.getDotSize()/3;
            }
            else
            {
                posCar_x -= b.getDotSize()*2;
            }
        }
        else
        {
            posCar_x = b.getWidth();
        }

        this.setPosX(posCar_x);
    }
    
    public void triggerAction(Board board){
        board.setInGameToFalse(); //peu importe la voiture que je touche --> game over
    }
}
