public abstract class Car
{
    protected int posCar_x;
    protected int posCar_y;
    protected int indexOfRoad;
    protected int normalSpeed = 20; //valeur du DOT_SIZE
    protected int maxLimitGame_X;
    protected int minLimitGame_X;
    //_____ DIMENSIONS FIXES ______// --> toutes les voitures possèdent les mêmes dimensions d'image
    protected int imgWidth = 25; //largeur de ma voiture
    protected int imgHeight = 20; //hauteur de ma voiture

    public Car(int posCar_x, int posCar_y) {
        this.posCar_x = posCar_x;
        this.posCar_y = posCar_y;
    }

    public int getPosX(){
        return posCar_x;
    }
    
    public int getPosY(){
        return posCar_y;
    }
    
    public void setPosX(int new_pos){
        posCar_x = new_pos;
    }
    
    public void setPosY(int new_pos){
        posCar_y = new_pos;
    }

    public void setIndexOfRoad(int index){
        indexOfRoad = index;
    }
    public int getIndexOfRoad(){ //index de la bande où se trouve la voiture concernée
        return indexOfRoad;
    }

    public void triggerAction(Board board)
    { // action si le frogger touche une des voitures
        board.setGameOverOrNot(); //peu importe la voiture que je touche
    }

    public int getImgWidth(){
        //retourne la largeur de l'image de la voiture
        return imgWidth;
    }

    public int getImgHeight(){
        //retourne la hauteur de l'image de la voiture
        return imgHeight;
    } 

    public abstract void moveCar(Board board);

    public abstract String getCarType();    
}
