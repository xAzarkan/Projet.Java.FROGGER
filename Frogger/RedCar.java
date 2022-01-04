public class RedCar extends Car
{
    private int posFrog_Y;
    private int[] roadTab;
    private int road;

    public RedCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "redCar.png";
    }

    public String getCarType(){
        return "redCar";
    }

    public void moveCar(Board board)
    { //LA VOITURE ROUGE VA TOUJOURS DE GAUCHE A DROITE

        minLimitGame_X = board.getBoardMin_X();
        maxLimitGame_X = board.getBoardMax_X();

        posFrog_Y = board.getFrog().getPosY();

        roadTab = board.getRoadTab();
        road = 0;

        posCar_x = getPosX();
        posCar_y = getPosY();
        
        indexOfRoad = getIndexOfRoad(); //voie sur laquelle se trouve la voiture

        int aboveTheFrog = posFrog_Y - normalSpeed;
        int underTheFrog = posFrog_Y + normalSpeed;

        int underTheCar = roadTab[indexOfRoad + 1];
        int aboveTheCar = roadTab[indexOfRoad - 1];

        //----GESTION DE LA VOITURE SUR L'AXE Y--------//

        //CONDITION : si au dessus du frogger se trouve la voiture et que en dessous de la voiture il y a une route (== 0) alors 
        if( (aboveTheFrog == posCar_y) && (underTheCar == road) ) 
        {
            posCar_y += normalSpeed; //LA VOITURE VA VERS LE BAS
            indexOfRoad += 1; //la voiture change de route
        }
        //CONDITION : si en dessous du frogger se trouve la voiture et que au dessus de la voiture il y a une route (==0) alors 
        else if( (underTheFrog == posCar_y) && (aboveTheCar == road) ) 
        {
            posCar_y -= normalSpeed; //LA VOITURE VA VERS LE HAUT
            indexOfRoad -= 1; //la voiture change de route
        } 

        this.setPosY(posCar_y); //on enregistre la nouvelle position de la voiture sur l'axe y
        this.setIndexOfRoad(indexOfRoad);

        //----GESTION DE LA VOITURE SUR L'AXE X--------//

        if(posCar_x < maxLimitGame_X) //tant que la voiture n'est pas encore tout à droite
            posCar_x += normalSpeed;      
        else //la voiture est arrivée tout à droite (il faut la remettre au tout début)
            posCar_x = minLimitGame_X;
        

        this.setPosX(posCar_x); //on enregistre la nouvelle position de la voiture sur l'axe x
    }

}
