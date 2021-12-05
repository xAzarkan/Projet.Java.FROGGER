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
    private int levelNumber = 0;

    private ArrayList<FixedGameElement> fixedGameElementList;
    private ArrayList<Car> carList;

    private Frog frog = new Frog(pos_x, pos_y);

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean nextLevel = false;

    private Timer timer;

    private Image coinImage;
    private Image insectImage;
    private Image frogImage;

    private HashMap<String, ImageIcon> fixedGameElementImageMap;
    private HashMap<String, ImageIcon> carImageMap;

    private int score = 0;
    private int void_x = -1 * B_WIDTH; //position du "vide"
    private int void_y = -1 * B_HEIGHT; //je vais y mettre les objets que je souhaite faire disparaitre

    private int[][] roadForLevel = {{0,0,0,1,1,1,0,0,1,1,1,1,0,0,1,1}, //niveau 0 --> les 0 représentent les routes, les 1 représentent les espaces
                                    {0,0,0,0,1,1,0,0,0,0,1,1,1,0,0,1},  //niveau 1
                                    {0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,1}}; //niveau 2

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.pink);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        fixedGameElementImageMap = new HashMap<String, ImageIcon>();
        carImageMap = new HashMap<String, ImageIcon>();

        ImageIcon iic = new ImageIcon(Coin.getPathToImage()); //image du coin
        //coinImage = iic.getImage();
        fixedGameElementImageMap.put("coin", iic); //je n'utilise pas la méthode getType() car ce n'est pas une classe static

        ImageIcon iii = new ImageIcon(Insect.getPathToImage()); //image de la pomme (voir dans Apple.java)
        //insectImage = iii.getImage();
        fixedGameElementImageMap.put("insect", iii);

        ImageIcon iiredcar = new ImageIcon(RedCar.getPathToImage());
        carImageMap.put("redCar", iiredcar);
        //System.out.println("width redcar" + iiredcar.getIconWidth());

        ImageIcon iibluecar = new ImageIcon(BlueCar.getPathToImage());
        carImageMap.put("blueCar", iibluecar);

        ImageIcon iiorangecar = new ImageIcon(OrangeCar.getPathToImage());
        carImageMap.put("orangeCar", iiorangecar);

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

        for(int i = 0; i < roadCounter; i++){
            if(roadForLevel[levelNumber][i] % 2 == 0)
            {
                posCar_x = getRandomPositionCarX();

                if(carList.size() % 2 == 0)
                {
                    carList.add(new BlueCar(posCar_x, posCar_y));
                }
                else if(carList.size() % 3 == 0)
                {
                    carList.add(new RedCar(posCar_x, posCar_y));
                } 
                else
                {
                    carList.add(new OrangeCar(posCar_x, posCar_y));
                }
                
            }
            posCar_y += DOT_SIZE; //on avance ligne par ligne   
        }

        //---FIN PLACEMENT ALEATOIRE----//

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private int getRandomPositionCarX()
    {
        int randomPositionCarX = (int) (Math.random() * B_WIDTH); //on génère une position n'importe où sur la largeur
        return (randomPositionCarX);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        initRoad(g);

        doDrawing(g);
    }

    public int getDotSize()
    {
        return DOT_SIZE;
    }

    private void initRoad(Graphics g)
    {
        //placement des routes (dépend du levelNumber)
        for(int i = 0; i < roadForLevel[0].length; i++)
        {
            if(roadForLevel[levelNumber][i] % 2 == 0)
            {
                g.setColor(Color.gray);
                g.fillRect(roadPos_x, roadPos_y + (i*DOT_SIZE), roadWidth, roadHeight); //insertion des voies
    
                g.setColor(Color.black);
                g.drawRect(roadPos_x - 1, roadPos_y + (i*DOT_SIZE), roadWidth + 1, roadHeight); //bordure des voies
            }
        }    
    }
    
    private void doDrawing(Graphics g) {

        //fixedGameElementList.add(frog);

        if(inGame) {

            if(nextLevel)
            {
                if(levelNumber == 2) //si j'arrive au dernier niveau fin du jeu
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
                    //int a = elem.hashCode();
                    g.drawImage(fixedGameElementImageMap.get(elem.getType()).getImage(), elem.getPosX(), elem.getPosY(), this);
                }
    
                for(Car elem : carList){
                    elem.moveCar(this);
                    g.drawImage(carImageMap.get(elem.getCarType()).getImage(), elem.getPosX(), elem.getPosY(), this);
                }
                
                frog.setPosX_Y(pos_x, pos_y);
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
        initGame();
        
    }

    private void clearRoad(Graphics g)
    {
        g.clearRect(0, 0, B_WIDTH, B_HEIGHT);
    }

    public int getPosFroggerX()
    {
        return pos_x;
    }

    public int getPosFroggerY()
    {
        return pos_y;
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

    private void checkCollision() {

        Rectangle frogRec = frog.getBounds();

        /* System.out.println("pos y " + frog.getPosY()); */
        
        for(Car elem: carList){

            Rectangle elemRec = elem.getBounds();

            if(frogRec.intersects(elemRec)) //collision entre la grenouille et une voiture
            {
                /*
                System.out.println("largeur " + frogRec.getWidth());
            
                System.out.println("hauteur " + frogRec.getHeight());

                System.out.println("COLLISION"); */
                elem.triggerAction(this);
            }
        }

        if (!inGame || nextLevel) 
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
