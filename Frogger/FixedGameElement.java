public abstract class FixedGameElement
{
    //classe abstraite --> classe ne pouvant pas être instanciée
    // regroupe les éléments en communs des classes
    private int pos_x;
    private int pos_y;
    private int imgWidth = 20;
    private int imgHeight = 20;

    public FixedGameElement(int pos_x, int pos_y) {
        this.pos_x = pos_x ;
        this.pos_y = pos_y ;
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

    public int getImgWidth(){
        return imgWidth;
    }

    public int getImgHeight(){
        return imgHeight;
    }

    public abstract String getType();

    public abstract void triggerAction(Board board);

}
