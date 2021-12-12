public class PurpleCar extends Car
{
    private int posCar_x;

    public PurpleCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "purpleCar.png";
    }

    public String getCarType(){
        return "purpleCar";
    }  
    
    public void moveCar(Board b)
    {
         posCar_x = this.getPosX();
         int coinCounter = b.getCoinCounter(); 

         if(posCar_x < b.getWidth()){
            if(coinCounter > 0) // pour éviter de diviser par 0
                posCar_x += b.getDotSize()/coinCounter; 
            else
                posCar_x += b.getDotSize()*2; 
         }
         else //limite du terrain
            posCar_x = 0;
         

         this.setPosX(posCar_x);
    }
}
