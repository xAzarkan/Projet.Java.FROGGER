public class BlueCar extends Car
{
    public BlueCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "blueCar.png";
    }

    public String getCarType(){
        return "blueCar";
    }    

    public void moveCar(Board b) //méthode qui fait bouger la voiture
    {
        posCar_x = this.getPosX();
        posCar_y = this.getPosY();

        if(posCar_x > 0)
        {
            if(b.getFrog().getPosY() == posCar_y) //si le joueur se trouve sur sa bande, la voiture ralenti
                posCar_x -= normalSpeed / 3; //vitesse de base divisée par 3
            else
                posCar_x -= normalSpeed;
        }
        else
        {
            posCar_x = b.getWidth();
        }

        this.setPosX(posCar_x);
        
    }
}
