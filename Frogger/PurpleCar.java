public class PurpleCar extends Car
{

    protected int coinCounter;
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
         coinCounter = b.getCoinCounter(); 

         if(posCar_x < b.getWidth()){
            if(coinCounter > 0) 
                posCar_x += normalSpeed / coinCounter; // pour éviter de diviser par 0
            else
                posCar_x += normalSpeed * 2; 
         }
         else //limite du terrain
            posCar_x = 0;
         
         this.setPosX(posCar_x);
    }
}
