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
        int DOT_SIZE = b.getDotSize();
        int posFrogY = b.getFrog().getPosY();

        int[] roadTab = b.getRoadTab();

        //System.out.println("posFrogY : " + posFrogY);
        posCar_x = this.getPosX();
        posCar_y = this.getPosY();
        //System.out.println("posCar_y : " + posCar_y);
        
        int indexOfRoad = this.getIndexOfRoad();

        //System.out.println(roadTab[indexOfRoad]);

        //CONDITION : si au dessus du frogger se trouve la voiture et que en dessous de la voiture il y a une route (==0) alors 
        if( (posFrogY - DOT_SIZE == posCar_y) && (roadTab[indexOfRoad + 1] == 0) ) //LA VOITURE DOIT ALLER VERS LE BAS
        {
            //System.out.println("EN DESSOUS IL Y A UNE ROUTE DONC OKLM : " + (roadTab[indexOfRoad + 1]));
            posCar_y += b.getDotSize();
            indexOfRoad += 1;
            //indexOfRoad += 1;
            
        }
        //CONDITION : si en dessous du frogger se trouve la voiture et que au dessus de la voiture il y a une route (==0) alors 
        else if( (posFrogY + DOT_SIZE == posCar_y) && (roadTab[indexOfRoad - 1] == 0) ) //LA VOITURE DOIT ALLER VERS LE HAUT
        {
            posCar_y -= b.getDotSize();
            indexOfRoad -= 1;
        } 
            this.setPosY(posCar_y);
            this.setIndexOfRoad(indexOfRoad);

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
