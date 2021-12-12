import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
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

    private int roadWidth = B_WIDTH;
    private int roadHeight = DOT_SIZE;
    private int roadPos_x = 0;
    private int roadPos_y = DOT_SIZE;

    private int pos_x;
    private int pos_y;

    private int posCar_x = 0; 
    private int posCar_y = 0;
    
    private int coinCounter;
    private int insectCounter;
    private int roadCounter;
    private int lastLevel = 2;
    private int levelNumber = 0;
    private int blueCarCounter = 0;
    private int trunkNumber = 0;

    private ArrayList<FixedGameElement> fixedGameElementList;
    private ArrayList<Car> carList;

    private static final Color WATER_COLOR = new Color(51,204,255);
    private static final Color GRASS_COLOR = new Color(0,153,0);

    private Frog frog = new Frog(pos_x, pos_y);
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

    private int score = 0;
    private int void_x = -1 * B_WIDTH; //position du "vide"
    private int void_y = -1 * B_HEIGHT; //je vais y mettre les objets que je souhaite faire disparaitre

    private int[][] roadForLevel = {{0,0,0,1,0,0,1,1,0,0,0,0,2,2,0,0,0,1},  //niveau 0 --> les 0 représentent les routes, les 1 représentent les espaces, les 2 représentent l'eau
                                    {0,0,0,0,1,1,0,0,0,0,1,1,1,0,0,1,0,1},  //niveau 1
                                    {0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,1,0,1}}; //niveau 2
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

        ImageIcon iii = new ImageIcon(Insect.getPathToImage()); //image de la pomme (voir dans Apple.java)
        fixedGameElementImageMap.put("insect", iii);

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

    private void initGame() {

        pos_x = GAME_BEGINNING_X;
        pos_y = GAME_BEGINNING_Y;

        //posCar_x = DOT_SIZE;
        posCar_y = roadPos_y;

        coinCounter = 3 + levelNumber; //nombre de coins
        insectCounter = 2; // 2 insectes au début du jeu
        roadCounter = roadForLevel[0].length - 1; //nombre de colonnes du tableau

        fixedGameElementList = new ArrayList<FixedGameElement>();
        carList = new ArrayList<Car>();

        //---PLACEMENT ALEATOIRE DES 3 COINS----//
        for(int i = 0; i < coinCounter ; i++){
            fixedGameElementList.add(new Coin(getRandomCoordinate(), getRandomCoordinate()));
        }

        //---PLACEMENT ALEATOIRE DES 2 INSECTES---//

        for(int i = 0; i < insectCounter ; i++){
            fixedGameElementList.add(new Insect(getRandomCoordinate(), getRandomCoordinate()));
        }

        //---PLACEMENT DES VOITURES SUR LA ROUTE---//

        int carListSize = carList.size();

        for(int i = 0; i < roadCounter; i++){
            posCar_x = getRandomPositionCarX();
            if(roadForLevel[levelNumber][i] == 0){ //donc je dois placer une voiture car il y'a une route
                if(carListSize % 2 == 0 && blueCarCounter < 2)
                {
                    carList.add(new BlueCar(posCar_x, posCar_y));
                    blueCarCounter += 1;
                }
                else if(carListSize % 2 == 0)
                    carList.add(new PurpleCar(posCar_x, posCar_y));
                
                else if(carListSize % 3 == 0)
                    carList.add(new RedCar(posCar_x, posCar_y));
                
                else
                    carList.add(new OrangeCar(posCar_x, posCar_y));
                 
                carListSize = carList.size();
            }
            else if(roadForLevel[levelNumber][i] == 2)
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

    public int getTrunkNumber()
    { //va servir pour la classe Trunk
        return trunkNumber;
    }

    private int getRandomPositionCarX()
    {
        //on génère une position n'importe où sur la largeur --> pour que les voitures ne démarrent pas toutes au même endroit
        int randomPositionCarX = (int) (Math.random() * B_WIDTH); 
        return (randomPositionCarX);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        initLevel(g);

        doDrawing(g);
    }

    public int getDotSize()
    {
        return DOT_SIZE;
    }

    private void initLevel(Graphics g)
    {
        //placement des routes (dépend du levelNumber)
        for(int i = 0; i < roadForLevel[0].length; i++)
        {
            if(roadForLevel[levelNumber][i] == 0) //route
            {
                g.setColor(Color.gray);
                g.fillRect(roadPos_x, roadPos_y + (i*DOT_SIZE), roadWidth, roadHeight); //insertion des voies
    
                g.setColor(Color.black);
                g.drawRect(roadPos_x - 1, roadPos_y + (i*DOT_SIZE), roadWidth + 1, roadHeight); //bordure des voies
            }

            else if(roadForLevel[levelNumber][i] == 2) //rivière
            {
                g.setColor(WATER_COLOR);
                g.fillRect(roadPos_x, roadPos_y + (i*DOT_SIZE), roadWidth, roadHeight); //insertion des voies
            }
            
        }    
    }

    private void doDrawing(Graphics g) {

        if(inGame) {

            if(nextLevel)
            {
                if(levelNumber == lastLevel) //si j'arrive au dernier niveau fin du jeu
                {
                    endGame(g);
                }
                else
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
            gameOver(g);
        }        
    }

    public void setInGameToFalse()
    {
        inGame = false;
    }

    private void goToNextLevel()
    {
        
        System.out.println("Level " + levelNumber + " completed !");
        System.out.println("Your score : " + score);
        nextLevel = false;
        levelNumber += 1;
        insectCounter = 3;
        roadCounter = 0;
        blueCarCounter = 0;
        trunkNumber = 0;
        initGame();
    }

    private void clearRoad(Graphics g)
    {
        g.clearRect(0, 0, B_WIDTH, B_HEIGHT);
    }
    public Frog getFrog()
    {
        return frog;
    }
    private void endGame(Graphics g) {

        String msg = "Super ! Vous avez fini le jeu :)";
        Font small = new Font("Helvetica", Font.BOLD, 22);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.black);
        g.fillRect(0, 0, B_WIDTH, B_HEIGHT);
        g.setColor(Color.green);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.black);
        g.fillRect(0, 0, B_WIDTH, B_HEIGHT);
        g.setColor(Color.red);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkGameElementCollision() {

        for(FixedGameElement elem: fixedGameElementList){
            if ((pos_x == elem.getPosX()) && (pos_y == elem.getPosY())){
                elem.setPosX(void_x); //on déplace la coin dans le "vide" pour la faire disparaitre de l'écran
                elem.setPosY(void_y); //on déplace la coin dans le "vide" pour la faire disparaitre de l'écran

                elem.triggerAction(this);

                System.out.println(coinCounter);
                System.out.println(score);
            }
        }
           
    }

    public int getCoinCounter()
    {
        return coinCounter;
    }

    public void incScore(int valueToIncrease)
    {
        score += valueToIncrease;
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
            if(pos_y - DOT_SIZE < 0)//arrivé en haut donc vérif si tous les coins ont été récup avant de passer au niveau suivant
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

        if (downDirection && pos_y+DOT_SIZE < B_HEIGHT) {
            pos_y += DOT_SIZE;
        }
    }

    public void checkCollision() {

        frogRec.setBounds(frog.getPosX(), frog.getPosY(), frog.getImgWidth(), frog.getImgHeight());

        for(Car elem : carList){

            elemRec.setBounds(elem.getPosX(), elem.getPosY(), elem.getImgWidth(), elem.getImgHeight());

            if(frogRec.intersects(elemRec)) //collision entre la grenouille et un elem de type car
            {
                elem.triggerAction(this);
            }
        }

        if (!inGame || nextLevel) //si fin du jeu ou niveau suivant
        {
            timer.stop();
        }
    }
    
    private int getRandomCoordinate() {

        int randomCoordinate = (int) (Math.random() * RAND_POS);
        return ((randomCoordinate * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkGameElementCollision();
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
