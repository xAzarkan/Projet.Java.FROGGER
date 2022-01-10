public class BlueCar extends Car
{
    private int posFrog_y;

    public BlueCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "gameIcones/blueCar.png";
    }

    public String getCarType(){
        return "blueCar";
    }    

    public void moveCar(Board board)
    { //LA VOITURE BLEUE RALENTIT LORSQUE LE FROG SE TROUVE SUR LA MÊME VOIE
        //LA VOITURE BLEUE VA TOUJOURS DE DROITE A GAUCHE

        minLimitGame_X = board.getBoardMin_X();
        maxLimitGame_X = board.getBoardMax_X();

        posCar_x = getPosX();
        posCar_y = getPosY();

        posFrog_y = board.getFrog().getPosY();

        if(posCar_x > minLimitGame_X) //la voiture n'est pas encore arrivée tout à gauche
        {
            if(posFrog_y == posCar_y) //si le joueur se trouve sur sa bande, la voiture ralentit
                posCar_x -= normalSpeed / 3; //vitesse de base divisée par 3
            else
                posCar_x -= normalSpeed; //sinon vitesse normale
        }
        else 
        {//la voiture est arrivée tout à gauche donc il faut la remettre tout à droite
            posCar_x = maxLimitGame_X;
        }

        this.setPosX(posCar_x);
        
    }
}
