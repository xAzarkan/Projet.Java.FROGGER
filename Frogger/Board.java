import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Rectangle;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 400;
    private final int B_HEIGHT = 400;
    private final int DOT_SIZE = 20;
    private final int RAND_POS = 19;
    private final int DELAY = 140;
    private final int GAME_BEGINNING_X = B_WIDTH/2;
    private final int GAME_BEGINNING_Y = B_HEIGHT - DOT_SIZE;
    private final int LIMIT_TO_NEXT_LEVEL = DOT_SIZE;

    private int roadWidth = B_WIDTH;
    private int roadHeight = DOT_SIZE;
    private int roadPos_x = 0;
    private int roadPos_y = 0;
   

    private int pos_x;
    private int pos_y;

    private int posCar_x = 0; 
    private int posCar_y = 0;
    private int posCoin_x;
    private int posCoin_y;
    private int posInsect_x;
    private int posInsect_y;
    
    private int coinCounter;
    private int roadCounter;
    private int lastLevel = 2;
    private int levelNumber = 0;
    private int blueCarCounter = 0;
    private int trunkNumber = 0;

    private ArrayList<FixedGameElement> fixedGameElementList;
    private ArrayList<Car> carList;
    private int carListSize;

    private static final Color WATER_COLOR = new Color(51,204,255);
    private static final Color GRASS_COLOR = new Color(0,153,0);
    private static final Color ROAD_COLOR = Color.gray;
    private static final Color BORDER_ROAD_COLOR = Color.black;
    private static final Color GAME_OVER_COLOR = Color.red;
    private static final Color SUCCESS_ENDGAME_COLOR = Color.green;

    private Frog frog;
    private Rectangle elemRec;
    private Rectangle frogRec;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean nextLevel = false;

    private Timer timer;

    private HashMap<String, ImageIcon> fixedGameElementImageMap;
    private HashMap<String, ImageIcon> carImageMap;

    private int currentScore = 0;
    private int highScore = 0;
    
    private int void_x = -1 * B_WIDTH; //position du "vide"
    private int void_y = -1 * B_HEIGHT; //je vais y mettre les objets que je souhaite faire disparaitre

    private int road = 0;
    private int grass = 1;
    private int water = 2;

    private int[][] roadForLevel = {{1,1,0,0,0,1,0,0,1,1,0,0,0,0,2,2,0,0,0,1},  //niveau 0 --> les 0 représentent les routes, les 1 représentent les espaces, les 2 représentent l'eau
                                    {1,1,0,0,0,0,1,1,0,2,2,0,1,1,1,0,0,0,0,1},  //niveau 1
                                    {1,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,1,0,1}}; //niveau 2
    public Board() {   
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(GRASS_COLOR);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        fixedGameElementImageMap = new HashMap<String, ImageIcon>();
        carImageMap = new HashMap<String, ImageIcon>();

        ImageIcon iic = new ImageIcon(Coin.getPathToImage()); //image du coin
        fixedGameElementImageMap.put("coin", iic); //je n'utilise pas la méthode getType() car ce n'est pas une classe static

        ImageIcon iiredinsect = new ImageIcon(redInsect.getPathToImage()); //type 1 : insecte rouge
        fixedGameElementImageMap.put("redInsect", iiredinsect);

        ImageIcon iiblueinsect = new ImageIcon(blueInsect.getPathToImage()); //type 2 : insecte bleu
        fixedGameElementImageMap.put("blueInsect", iiblueinsect);

        ImageIcon iiyellowinsect = new ImageIcon(yellowInsect.getPathToImage()); //type 1 : insecte jaune
        fixedGameElementImageMap.put("yellowInsect", iiyellowinsect);

        ImageIcon iiredcar = new ImageIcon(RedCar.getPathToImage());
        carImageMap.put("redCar", iiredcar);

        ImageIcon iibluecar = new ImageIcon(BlueCar.getPathToImage());
        carImageMap.put("blueCar", iibluecar);

        ImageIcon iiorangecar = new ImageIcon(OrangeCar.getPathToImage());
        carImageMap.put("orangeCar", iiorangecar);

        ImageIcon iipurplecar = new ImageIcon(PurpleCar.getPathToImage());
        carImageMap.put("purpleCar", iipurplecar);

        ImageIcon iitrunk = new ImageIcon(Trunk.getPathToImage());
        carImageMap.put("trunk", iitrunk);

        ImageIcon iifrog = new ImageIcon(Frog.getPathToImage());
        fixedGameElementImageMap.put("frog", iifrog);
    }

    private void initGame() { //initialisation du jeu

        frog = new Frog(pos_x, pos_y);

        pos_x = GAME_BEGINNING_X;
        pos_y = GAME_BEGINNING_Y;

        posCar_y = roadPos_y;

        coinCounter = 3 + levelNumber; //nombre de coins (+ le numéro de level)
        roadCounter = roadForLevel[0].length - 1; //nombre de colonnes du tableau

        fixedGameElementList = new ArrayList<FixedGameElement>();
        carList = new ArrayList<Car>();

        //---PLACEMENT ALEATOIRE DES 3 COINS----//
        for(int i = 0; i < coinCounter ; i++){
            posCoin_x = getRandomCoordinate();
            posCoin_y = getRandomCoordinate();

            if(posCoin_y == 0) //pour éviter d'aller dans la zone où se trouve les pv et le score
            {
                posCoin_y += DOT_SIZE;
            }

            fixedGameElementList.add(new Coin(posCoin_x, posCoin_y));
        }

        //---PLACEMENT ALEATOIRE DES 3 INSECTES---//

        for(int i = 0; i < 3; i++)
        {
            posInsect_x = getRandomCoordinate();
            posInsect_y = getRandomCoordinate();

            if(posInsect_y == 0)
            {
                posInsect_y += DOT_SIZE;
            }

            if(i == 0)    
                fixedGameElementList.add(new redInsect(posInsect_x, posInsect_y));
            else if(i == 1)
                fixedGameElementList.add(new blueInsect(posInsect_x, posInsect_y));
            else
                fixedGameElementList.add(new yellowInsect(posInsect_x, posInsect_y));
        }
        
    

        //---PLACEMENT DES VOITURES SUR LA ROUTE---//

        carListSize = carList.size();

        for(int i = 0; i < roadCounter; i++){
            posCar_x = getRandomPositionCarX();
            if(roadForLevel[levelNumber][i] == road){ //donc je dois placer une voiture car il y'a une route
                if(carListSize % 2 == 0 && blueCarCounter < 2)
                {
                    carList.add(new BlueCar(posCar_x, posCar_y));
                    blueCarCounter += 1;
                }
                else if(carListSize % 2 == 0) //expliquer la logique
                    carList.add(new PurpleCar(posCar_x, posCar_y));
                
                else if(carListSize % 3 == 0)
                {
                    RedCar redCar = new RedCar(posCar_x, posCar_y);
                    carList.add(redCar);
                    redCar.setIndexOfRoad(i); //je lui donne l'emplacement dans le tableau de la route où la voiture rouge se trouve
                }
                else
                    carList.add(new OrangeCar(posCar_x, posCar_y));
                 
                carListSize = carList.size();
            }
            else if(roadForLevel[levelNumber][i] == water)
            { //je dois placer un tronc d'arbre car je suis sur la riviere
                    Trunk trunk = new Trunk(posCar_x, posCar_y);
                    carList.add(trunk);
                    trunk.setTrunkNumber(trunkNumber);
                    trunkNumber += 1;
            }
            posCar_y += DOT_SIZE; //on avance ligne par ligne
        }
        //---FIN PLACEMENT ALEATOIRE----//

        //---CREATION DES RECTANGLE POUR LE FROG ET LES VOITURES---//
        frogRec = new Rectangle(frog.getPosX(), frog.getPosY(), frog.getImgWidth(), frog.getImgHeight());

        for(Car elem: carList){
            elemRec = new Rectangle(elem.getPosX(), elem.getPosY(), elem.getImgWidth(), elem.getImgHeight());
        }

        timer = new Timer(DELAY, this);
        timer.start();
    }

    // --- GETTEUR DU BOARD POUR LES AUTRES CLASSES --- //

    public int getTrunkNumber()
    { //va servir pour la classe Trunk
        return trunkNumber;
    }

    public int[] getRoadTab()
    {
        int[] roadTab = new int[roadForLevel[0].length];

        for(int col = 0; col < roadForLevel[0].length; col++)
        {
            roadTab[col] = roadForLevel[levelNumber][col];
        }
        return roadTab;
    }

    public Frog getFrog()
    {
        return this.frog;
    }

    public int getCoinCounter()
    {
        return coinCounter;
    }

    public int getDotSize()
    {
        return DOT_SIZE;
    }

    // ------------------------------------------------ //
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        initLevel(g);
        
        doDrawing(g);
    }

    private void initLevel(Graphics g) //Initialisation des niveaux (routes, etc...)
    {
        //placement des routes (dépend du levelNumber)

        for(int i = 0; i < roadForLevel[0].length; i++)
        {
            if(i == 0)
            {
                g.setColor(Color.black);
                g.fillRect(roadPos_x, roadPos_y + (i*DOT_SIZE), roadWidth, roadHeight);
            }
            else if(roadForLevel[levelNumber][i] == road) //route
            {
                g.setColor(ROAD_COLOR);
                g.fillRect(roadPos_x, roadPos_y + (i*DOT_SIZE), roadWidth, roadHeight); //insertion des voies
    
                g.setColor(BORDER_ROAD_COLOR);
                g.drawRect(roadPos_x - 1, roadPos_y + (i*DOT_SIZE), roadWidth + 1, roadHeight); //bordure des voies
            }
            else if(roadForLevel[levelNumber][i] == water) //rivière
            {
                g.setColor(WATER_COLOR);
                g.fillRect(roadPos_x, roadPos_y + (i*DOT_SIZE), roadWidth, roadHeight); //insertion des voies
            }
            //---------- banniere avec les pv et le score ----------//
            
            //------------------------------------------------------//
        }    
    }

    private void doDrawing(Graphics g) {

        if(inGame) {

            if(nextLevel)
            {
                if(levelNumber == lastLevel) //si j'arrive au dernier niveau fin du jeu
                {
                    endGame(g, "Super ! Vous avez fini le jeu :)", SUCCESS_ENDGAME_COLOR);
                }
                else //sinon on va au niveau suivant
                {
                    clearRoad(g);
                    goToNextLevel();
                }
            }
            else
            {
                for (FixedGameElement elem : fixedGameElementList) {
                    g.drawImage(fixedGameElementImageMap.get(elem.getType()).getImage(), elem.getPosX(), elem.getPosY(), this);
                }
    
                for(Car elem : carList){
                    elem.moveCar(this);
                    g.drawImage(carImageMap.get(elem.getCarType()).getImage(), elem.getPosX(), elem.getPosY(), this);
                }
                
                frog.setPosX(pos_x);
                frog.setPosY(pos_y);
                g.drawImage(fixedGameElementImageMap.get(frog.getType()).getImage(), frog.getPosX(), frog.getPosY(), this);
                
                Toolkit.getDefaultToolkit().sync();
            }
        }
        else {
            clearRoad(g);
            endGame(g, "Game over", GAME_OVER_COLOR);
        }        
    }

    public void setInGameToFalse()
    {
        inGame = false;
    }

    private void goToNextLevel()
    {
        System.out.println("Level " + levelNumber + " completed !");
        System.out.println("Your score : " + currentScore);
        nextLevel = false;
        levelNumber += 1;
        roadCounter = 0;
        blueCarCounter = 0;
        trunkNumber = 0;
        initGame();
    }

    private void clearRoad(Graphics g)
    {
        g.clearRect(0, 0, B_WIDTH, B_HEIGHT);
    }
    
    private void endGame(Graphics g, String messageToShow, Color color) {

        Font small = new Font("Helvetica", Font.BOLD, 22);
        FontMetrics metr = getFontMetrics(small);

        String messageFinalScore = "Your final score : " + currentScore;

        g.setColor(Color.black);
        g.fillRect(0, 0, B_WIDTH, B_HEIGHT); //0 et 0 = début du rectangle
        g.setColor(color);
        g.setFont(small);
        g.drawString(messageToShow, (B_WIDTH - metr.stringWidth(messageToShow)) / 2, B_HEIGHT / 2);
        int espaceEntreMessages = 50;
        g.drawString(messageFinalScore, (B_WIDTH - metr.stringWidth(messageFinalScore)) / 2, B_HEIGHT / 2 + espaceEntreMessages);

    }

    public void incScore(int valueToIncrease)
    {
        currentScore += valueToIncrease;
    }

    public void decreaseCoinAmount()
    {
        coinCounter -= 1;
    }

    private void move() {

        if (leftDirection && pos_x - DOT_SIZE >= 0) {
            pos_x -= DOT_SIZE;
        }

        if (rightDirection && pos_x + DOT_SIZE < B_WIDTH) {
            pos_x += DOT_SIZE;
        }

        if (upDirection) {
            if(pos_y - DOT_SIZE < LIMIT_TO_NEXT_LEVEL)//arrivé en haut donc vérif si tous les coins ont été récup avant de passer au niveau suivant
            {
                if(coinCounter == 0)
                {
                    nextLevel = true; //on passe au niveau suivant car plus aucune pièce
                }
            }
            else //on est pas encore tout en haut donc move normal
            {
                pos_y -= DOT_SIZE;
            }
        }

        if (downDirection && pos_y + DOT_SIZE < B_HEIGHT) {
            pos_y += DOT_SIZE;
        }
    }

    //--------- checkCollision ------//

    private void checkCollision() {

        for(FixedGameElement elem: fixedGameElementList){
            if ((pos_x == elem.getPosX()) && (pos_y == elem.getPosY())){
                elem.setPosX(void_x); //on déplace la coin dans le "vide" pour la faire disparaitre de l'écran
                elem.setPosY(void_y); //on déplace la coin dans le "vide" pour la faire disparaitre de l'écran

                elem.triggerAction(this);
            }
        }

        //--- rectangle collision ---//

        frogRec.setBounds(frog.getPosX(), frog.getPosY(), frog.getImgWidth(), frog.getImgHeight());

        for(Car elem : carList){

            elemRec.setBounds(elem.getPosX(), elem.getPosY(), elem.getImgWidth(), elem.getImgHeight());

            if(frogRec.intersects(elemRec)) //collision entre la grenouille et un elem de type car
            {
                elem.triggerAction(this);
            }
        }

        //--- end rectangle collision ---//

        if (!inGame || nextLevel) //si fin du jeu ou niveau suivant
        {
            timer.stop();
        }
    }

    //--------- fin checkCollision ------//

    // -------- getRandomCoordinate / Position ------- //
    
    private int getRandomCoordinate() { //position random pour les élements fixes
        int randomCoordinate = (int) (Math.random() * RAND_POS);
        return ((randomCoordinate * DOT_SIZE));
    }

    private int getRandomPositionCarX() 
    {//on génère une position n'importe où sur la largeur --> pour que les voitures ne démarrent pas toutes au même endroit   
        int randomPositionCarX = (int)(Math.random() * B_WIDTH); 
        return (randomPositionCarX);
    }

    // ------- FIN RANDOM -------- //

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) 
        {
            checkCollision();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                leftDirection = true;
                rightDirection = false;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_RIGHT) {
                rightDirection = true;
                leftDirection = false;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_UP){
                upDirection = true;
                downDirection = false;
                rightDirection = false;
                leftDirection = false;
            }

            if (key == KeyEvent.VK_DOWN) {
                downDirection = true;
                upDirection = false;
                rightDirection = false;
                leftDirection = false;
            }
            move();
        }
           
    }
}
