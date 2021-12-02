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

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;
    private final int GAME_BEGINNING_X = B_WIDTH/2;
    private final int GAME_BEGINNING_Y = B_HEIGHT - DOT_SIZE;

    private int pos_x;
    private int pos_y;
    
    private int coinCounter;
    private int insectCounter;
    private int levelNumber = 1;
    //private ArrayList<Coin> coinList ;
    private ArrayList<FixedGameElement> fixedGameElementList;

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

    private int score = 0;
    private int void_x = -1 * B_WIDTH; //position du "vide"
    private int void_y = -1 * B_HEIGHT; //je vais y mettre les objets que je souhaite faire disparaitre

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        fixedGameElementImageMap = new HashMap<String, ImageIcon>();

        ImageIcon iic = new ImageIcon(Coin.getPathToImage()); //image du coin
        //coinImage = iic.getImage();
        fixedGameElementImageMap.put("coin", iic); //je n'utilise pas la méthode getType() car ce n'est pas une classe static

        ImageIcon iii = new ImageIcon(Insect.getPathToImage()); //image de la pomme (voir dans Apple.java)
        //insectImage = iii.getImage();
        fixedGameElementImageMap.put("insect", iii);

        ImageIcon iif = new ImageIcon("head.png");
        frogImage = iif.getImage();
    }

    private void initGame() {

        pos_x = GAME_BEGINNING_X;
        pos_y = GAME_BEGINNING_Y;

        coinCounter = 2 + levelNumber; //nombre de coins
        insectCounter = 2; // 2 insectes au début du jeu
        fixedGameElementList = new ArrayList<FixedGameElement>();

        //---PLACEMENT ALEATOIRE DES 3 COINS----//
        for(int i = 0; i < coinCounter ; i++){
            fixedGameElementList.add(new Coin(getRandomCoordinate(), getRandomCoordinate()));
        }

        //---PLACEMENT ALEATOIRE DES 2 INSECTES---//

        for(int i = 0; i < insectCounter ; i++){
            fixedGameElementList.add(new Insect(getRandomCoordinate(), getRandomCoordinate()));
        }

        //---FIN PLACEMENT ALEATOIRE DES 3 POMMES----//

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            if(nextLevel)
            {
                goToNextLevel();
            }

            for (FixedGameElement elem : fixedGameElementList) { //on affiche les coins
                g.drawImage(fixedGameElementImageMap.get(elem.getType()).getImage(), elem.getPosX(), elem.getPosY(), this);
            }

            g.drawImage(frogImage, pos_x, pos_y, this);

            Toolkit.getDefaultToolkit().sync();


        }
        else {
            gameOver(g);
        }        
    }

    private void goToNextLevel()
    {
        System.out.println("Level " + levelNumber + " completed !");
        System.out.println("Your score : " + score);
        nextLevel = false;
        levelNumber += 1;
        coinCounter = 5;
        insectCounter = 3;
        initGame();
    }

    private void gameOver(Graphics g) {

        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkGameElementCollision() {

        for(FixedGameElement elem: fixedGameElementList){
            if ((pos_x == elem.getPosX()) && (pos_y == elem.getPosY())){
                elem.setPosX(void_x); //on déplace la coin dans le "vide" pour la faire disparaitre de l'écran
                elem.setPosY(void_y); //on déplace la coin dans le "vide" pour la faire disparaitre de l'écran

                elem.triggerAction(this);

                //score += 1; //le score augemente à chaque fois qu'on attrape une pièce
                //coinCounter -= 1; //une pièce en moins à chaque pièce mangée

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

        if (leftDirection) {
            pos_x -= DOT_SIZE;
        }

        if (rightDirection) {
            pos_x += DOT_SIZE;
        }

        if (upDirection) {
            pos_y -= DOT_SIZE;
        }

        if (downDirection) {
            pos_y += DOT_SIZE;
        }
    }

    private void checkCollision() {

        if (pos_y >= B_HEIGHT) {
            pos_y -= DOT_SIZE; //je reste à ma place (mur invisible)
        }

        if (pos_y < 0) {//arrivé en haut donc vérif si tous les coins ont été récup avant de passer au niveau suivant
            if(coinCounter == 0)
            {
                nextLevel = true; //on passe au niveau 0 car plus aucune pièce
            }
            else
            {
                pos_y += DOT_SIZE;
            }
        }

        if (pos_x >= B_WIDTH) {
            pos_x -= DOT_SIZE;
        }

        if (pos_x < 0) {
            pos_x += DOT_SIZE;
        }
        
        if (!inGame || nextLevel) {
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
