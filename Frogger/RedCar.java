public class RedCar extends Car
{
    private int posFrog_Y;

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
        posFrog_Y = b.getFrog().getPosY();

        int[] roadTab = b.getRoadTab();
        int road = 0;

        posCar_x = this.getPosX();
        posCar_y = this.getPosY();
        
        indexOfRoad = this.getIndexOfRoad();

        //CONDITION : si au dessus du frogger se trouve la voiture et que en dessous de la voiture il y a une route (==0) alors 
        if( (posFrog_Y - normalSpeed == posCar_y) && (roadTab[indexOfRoad + 1] == road) ) //LA VOITURE DOIT ALLER VERS LE BAS
        {
            posCar_y += normalSpeed;
            indexOfRoad += 1;
        }
        //CONDITION : si en dessous du frogger se trouve la voiture et que au dessus de la voiture il y a une route (==0) alors 
        else if( (posFrog_Y + normalSpeed == posCar_y) && (roadTab[indexOfRoad - 1] == road) ) //LA VOITURE DOIT ALLER VERS LE HAUT
        {
            posCar_y -= normalSpeed;
            indexOfRoad -= 1;
        } 
            this.setPosY(posCar_y);
            this.setIndexOfRoad(indexOfRoad);

        if(posCar_x < b.getWidth())
            posCar_x += normalSpeed;      
        else
            posCar_x = 0;
        

        this.setPosX(posCar_x);   
    }

}
