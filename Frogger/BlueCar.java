public class BlueCar extends Car
{
    private int posCar_x;
    private int posCar_y;

    public BlueCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "blueCar.png";
    }

    public String getCarType(){
        return "blueCar";
    }    

    public void moveCar(Board b)
    {  
        posCar_x = this.getPosX();
        posCar_y = this.getPosY();

        if(posCar_x > 0)
        {
            if(b.getFrog().getPosY() == posCar_y) //si le joueur se trouve sur sa bande, la voiture ralenti
                posCar_x -= b.getDotSize()/3; //vitesse de base divisée par 3
            else
                posCar_x -= b.getDotSize();
        }
        else
        {
            posCar_x = b.getWidth();
        }

        this.setPosX(posCar_x);
        
    }
}
