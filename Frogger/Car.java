public abstract class Car
{
    protected int pos_x;
    protected int pos_y;
    protected int posCar_x;
    protected int posCar_y;
    protected int indexOfRoad;
    protected int normalSpeed  = 20;
    
    protected int imgWidth = 25; //largeur de ma voiture 
    protected int imgHeight = 20; //hauteur de ma voiture

    public Car(int pos_x, int pos_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    public int getPosX(){
        return pos_x;
    }
    
    public int getPosY(){
        return pos_y;
    }
    
    public void setPosX(int new_pos){
        pos_x = new_pos;
    }
    
    public void setPosY(int new_pos){
        pos_y = new_pos;
    }

    public void setIndexOfRoad(int index){
        indexOfRoad = index;
    }
    public int getIndexOfRoad(){
        return indexOfRoad;
    }

    public abstract void moveCar(Board b);

    public abstract String getCarType();    

    public void triggerAction(Board board){
        board.setInGameToFalse(); //peu importe la voiture que je touche --> game over
    }

    public int getImgWidth(){
        return imgWidth;
    }

    public int getImgHeight(){
        return imgHeight;
    } 
}
