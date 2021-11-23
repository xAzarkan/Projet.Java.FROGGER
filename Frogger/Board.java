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

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private int pos_x;
    private int pos_y;
    
    private int appleCounter;
    private ArrayList<Apple> appleList ;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;

    private Image appleImage;
    private Image head;

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

        ImageIcon iia = new ImageIcon(Apple.getPathToImage()); //image de la pomme (voir dans Apple.java)
        appleImage = iia.getImage();

        ImageIcon iih = new ImageIcon("head.png");
        head = iih.getImage();
    }

    private void initGame() {
        
        pos_x = B_WIDTH/2;
        pos_y = B_HEIGHT/2;
        
        appleCounter = 3; //nombre de pommes
        appleList = new ArrayList<Apple>();
        
        int apple_x;
        int apple_y;

        //---PLACEMENT ALEATOIRE DES 3 POMMES----//
        for(int i = 0; i < appleCounter ; i++){
            apple_x = getRandomCoordinate();
            apple_y = getRandomCoordinate();
            appleList.add(new Apple(apple_x, apple_y)); //ajout dans la liste de Apple
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

            for(Apple app: appleList){ //on affiche les pommes
                g.drawImage(appleImage, app.getPosX(), app.getPosY(), this);
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

    private void checkApple() {

        for(Apple app: appleList){ //on verifie si l'on touche une des 3 pommes en parcourant la liste de pommes
            if ((pos_x == app.getPosX()) && (pos_y == app.getPosY())){
                app.setPosX(getRandomCoordinate()); //on genere alors une nouvelle pomme à un nouvel emplacement
                app.setPosY(getRandomCoordinate()); //on genere alors une nouvelle pomme à un nouvel emplacement
            }
        }    
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

            checkApple();
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
