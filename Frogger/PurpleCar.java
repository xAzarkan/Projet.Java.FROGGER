public class PurpleCar extends Car
{
    private int posCar_x;

    public PurpleCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }


    public String getCarType(){
        return "purpleCar";
    }  
    
    public void moveCar(Board b)
    {
         posCar_x = this.getPosX();

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

    public void triggerAction(Board board){
        board.setInGameToFalse(); //peu importe la voiture que je touche --> game over
    }
}