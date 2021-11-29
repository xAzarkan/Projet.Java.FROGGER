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

    private int pos_x;
    private int pos_y;
    
    private int coinCounter;
    private int insectCounter;
    //private ArrayList<Coin> coinList ;
    private ArrayList<FixedGameElement> fixedGameElementList;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;

    private Image coinImage;
    private Image insectImage;
    private Image head;

    private HashMap<String, ImageIcon> fixedGameElementImageMap;

    private int score;
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

        ImageIcon iih = new ImageIcon("head.png");
        head = iih.getImage();
    }

    private void initGame() {

        score = 0; // le score est égal à 0 à l'initialisation du jeu

        pos_x = B_WIDTH/2;
        pos_y = B_HEIGHT/2;
        
        coinCounter = 3; //nombre de pommes
        insectCounter = 2; // 2 insectes au début du jeu
        fixedGameElementList = new ArrayList<FixedGameElement>();
        
        int coin_x;
        int coin_y;

        //---PLACEMENT ALEATOIRE DES 3 POMMES----//
        for(int i = 0; i < coinCounter ; i++){
            fixedGameElementList.add(new Coin(getRandomCoordinate(), getRandomCoordinate()));
        }

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

            for(FixedGameElement elem: fixedGameElementList){ //on affiche les coins
                g.drawImage(fixedGameElementImageMap.get(elem.getType()).getImage(), elem.getPosX(), elem.getPosY(), this);
            }      
            
            g.drawImage(head, pos_x, pos_y, this);

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
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
            inGame = false;
        }

        if (pos_y < 0) {
            inGame = false;
        }

        if (pos_x >= B_WIDTH) {
            inGame = false;
        }

        if (pos_x < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }
    
    private int getRandomCoordinate() {

        int r = (int) (Math.random() * RAND_POS);
        return ((r * DOT_SIZE));
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

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            move();
        }
    }
}
