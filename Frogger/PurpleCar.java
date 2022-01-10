public class PurpleCar extends Car
{
    private int coinCounter;

    public PurpleCar(int pos_x, int pos_y) {
        super(pos_x, pos_y);
    }

    public static String getPathToImage(){
        return "gameIcones/purpleCar.png";
    }

    public String getCarType(){
        return "purpleCar";
    }  
    
    public void moveCar(Board board)
    {
        //LA VOITURE MAUVE VA TOUJOURS DE GAUCHE A DROITE

        minLimitGame_X = board.getBoardMin_X();
        maxLimitGame_X = board.getBoardMax_X();

        posCar_x = getPosX();
        coinCounter = board.getCoinCounter(); //récupère le nombre de pièces restantes

        if(posCar_x < maxLimitGame_X) //si la voiture n'est pas encore arrivée tout à droite
        {
            if(coinCounter > 0) // pour éviter de diviser par 0
                posCar_x += normalSpeed / coinCounter; 
                //si coinCounter vaut 3 alors la voiture est très lente car on divise la vitesse initiale par 3
                //quand je vais prendre une pièce il en restera 2 donc la vitesse initiale sera divisée par 2 ce qui veut dire que la voiture sera un peu plus rapide. etc etc etc
            else //quand il ne reste plus aucune pièce, vitesse normale multipliée par 2 car je ne peux pas divisée un nombre par 0
                posCar_x += normalSpeed * 2; 
        }
        else //la voiture est arrivée tout à droite
            posCar_x = minLimitGame_X;
        
        this.setPosX(posCar_x); //j'enregistre la nouvelle position de la voiture sur l'axe x
    }
}
