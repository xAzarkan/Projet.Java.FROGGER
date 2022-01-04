public class OrangeCar extends Car
{
    private int slow;
    private int randomSpeed;

    public OrangeCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "orangeCar.png";
    }

    public String getCarType(){
        return "orangeCar";
    }     

    public void moveCar(Board board)
    {
        //VOITURE ORANGE VA TOUJOURS DE DROITE A GAUCHE
        //accelere et ralenti au hasard 

        minLimitGame_X = board.getBoardMin_X();
        maxLimitGame_X = board.getBoardMax_X();
        
        slow = 0;
        randomSpeed = (int)(Math.random() * 2); 
        
        posCar_x = getPosX(); //récupère la position sur l'axe x de la voiture

        if(posCar_x > minLimitGame_X)
        {
            if(randomSpeed == slow) 
            {//si le chiffre randomSpeed vaut 0 alors on divise la vitesse par 3 pour aller plus lentement
                posCar_x -= normalSpeed/3;
            }
            else
            {//sinon on va plus vite (on multiplie la vitesse normale par 2)
                posCar_x -= normalSpeed*2;
            }
        }
        else
        { // on arrive à la limite donc on remet la voiture au début
            posCar_x = maxLimitGame_X;
        }

        this.setPosX(posCar_x); //enregistre la nouvelle position de la voiture
    }
}
