public class RedCar extends Car
{
    private int posCar_x, posCar_y;

    public RedCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "redCar.png";
    }

    public String getCarType(){
        return "redCar";
    }

    public void moveCar(Board b)
    {
        int posFrogY = b.getFrog().getPosY();
        posCar_x = this.getPosX();
        posCar_y = this.getPosY();

        if(posCar_x < b.getWidth())
        {
            posCar_x += b.getDotSize();      
        }
        else
        {
            posCar_x = 0;
        }

        this.setPosX(posCar_x);
    }

}
