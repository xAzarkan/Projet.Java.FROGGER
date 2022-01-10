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
import java.io.*;


public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 400;
    private final int B_HEIGHT = 400;
    private final int DOT_SIZE = 20;
    private final int RAND_POS = 19;
    private final int DELAY = 140;
    private final int GAME_BEGINNING_X = B_WIDTH/2;
    private final int GAME_BEGINNING_Y = B_HEIGHT - DOT_SIZE;
    private final int LIMIT_TO_NEXT_LEVEL = DOT_SIZE;
    private final int MAX_BUSH_LINE = 4;
    private final int POSITION_SCORE_BAR = 0;
    private final int MAX_BLUECAR_PER_LEVEL = 2;
    private final int INVINCIBLE_MODE_SPEED = 10;
    private final int MIN_LIMIT_GAME_X = 0;
    private final int MAX_LIMIT_GAME_X = B_WIDTH;
    private final int MAX_LIMIT_GAME_Y = B_HEIGHT;

    private int roadWidth = B_WIDTH;
    private int roadHeight = DOT_SIZE;
    private int roadPos_x = 0;
    private int roadPos_y = 0;

    private int invincibleModeDuration = 10; 

    private int pos_x;
    private int pos_y;

    private int posCar_x = 0; 
    private int posCar_y = 0;

    private int posCoin_x;
    private int posCoin_y;

    private int posInsect_x;
    private int posInsect_y;

    private int posHeart_x;
    private int posHeart_y;

    private int posBush_x;
    private int posBush_y;

    private int posPill_x;
    private int posPill_y;

    private int posGreenStar_x;
    private int posGreenStar_y;

    private int posTrunk_x;
    private int posTrunk_y;

    private boolean invincibleMode;
    private long startInvincibleMode;
    private long current_time;

    private int coinCounter;
    private int roadCounter;
    private int lastLevel = 2;
    private int levelNumber = 0;
    private int blueCarCounter = 0;
    private int spaceBetweenTrunk = DOT_SIZE * 8;
    private int numberOfInsects;

    private int trunkNumbers = 3;
    private int riverNumber = 2;
    private int numberOfTrunks = 3;

    private ArrayList<FixedGameElement> fixedGameElementList;
    private ArrayList<Trunk> trunkList;
    private ArrayList<Car> carList;
    private int[] trunkPositionTabX;
    private int[] riverPositionTabY;
    private int carListSize;

    private static final Color WATER_COLOR = new Color(51,204,255);
    private static final Color GRASS_COLOR = new Color(0,153,0);
    private static final Color ROAD_COLOR = Color.gray;
    private static final Color BORDER_ROAD_COLOR = Color.black;
    private static final Color SUCCESS_ENDGAME_COLOR = Color.green;

    private Frog frog;
    private Rectangle elemRec;
    private Rectangle frogRec;

    private boolean canWalkOverBush;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean nextLevel = false;

    private Timer timer;

    private HashMap<String, ImageIcon> fixedGameElementImageMap;
    private HashMap<String, ImageIcon> carImageMap;
    private HashMap<String, ImageIcon> frogImageMap;

    private int currentScore = 0;
    private int highScore = 0; //enregistre le plus gros score de la session
    
    private int void_x = -1 * B_WIDTH; //position du "vide"
    private int void_y = -1 * B_HEIGHT; //je vais y mettre les objets que je souhaite faire disparaitre

    private int road = 0;
    private int grass = 1;
    private int water = 2;

    private int remainingLife = 3;

    private int[][] roadForLevel = {{1,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,1,0,1},  //niveau 0 --> les 0 représentent les routes, les 1 représentent les espaces, les 2 représentent l'eau
                                    {1,1,0,0,0,1,0,0,1,1,0,0,0,0,2,2,0,0,0,1}, //niveau 1
                                    {1,1,0,0,0,0,1,1,0,2,2,0,0,1,1,0,1,0,0,1} }; //niveau 2

    private ImageIcon iifrog;
    private ImageIcon iiinvinciblefrog;
                                                       
    public Board() {   
        initBoard();
    }
    
    private void initBoard() {
        inGame = true;

        addKeyListener(new TAdapter());
        setBackground(GRASS_COLOR);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        getHighScore();
        initGame();
    }

    private void loadImages() {

        fixedGameElementImageMap = new HashMap<String, ImageIcon>();
        carImageMap = new HashMap<String, ImageIcon>();
        frogImageMap = new HashMap<String, ImageIcon>();

        ImageIcon iic = new ImageIcon(Coin.getPathToImage()); //image du coin
        fixedGameElementImageMap.put("coin", iic); //je n'utilise pas la méthode getType() car ce n'est pas une classe static

        ImageIcon iiredinsect = new ImageIcon(RedInsect.getPathToImage()); //type 1 : insecte rouge
        fixedGameElementImageMap.put("redInsect", iiredinsect);

        ImageIcon iiblueinsect = new ImageIcon(BlueInsect.getPathToImage()); //type 2 : insecte bleu
        fixedGameElementImageMap.put("blueInsect", iiblueinsect);

        ImageIcon iiyellowinsect = new ImageIcon(YellowInsect.getPathToImage()); //type 1 : insecte jaune
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
        fixedGameElementImageMap.put("trunk", iitrunk);

        iifrog = new ImageIcon(Frog.getPathToImage());
        frogImageMap.put("frog", iifrog);

        iiinvinciblefrog = new ImageIcon(Frog.getPathToImageInvincible());

        ImageIcon iiheart = new ImageIcon("gameIcones/heart.png");
        fixedGameElementImageMap.put("heart", iiheart);

        ImageIcon iirock = new ImageIcon(Bush.getPathToImage());
        fixedGameElementImageMap.put("bush", iirock);

        ImageIcon iipill = new ImageIcon(Pill.getPathToImage());
        fixedGameElementImageMap.put("pill", iipill);

        ImageIcon iigreenstar = new ImageIcon(BushPowerUpStar.getPathToImage());
        fixedGameElementImageMap.put("bushpowerupstar", iigreenstar);
    }

    private void initGame() { //initialisation du jeu

        frog = new Frog(pos_x, pos_y);

        canWalkOverBush = false;
        invincibleMode = false;

        pos_x = GAME_BEGINNING_X;
        pos_y = GAME_BEGINNING_Y;

        posCar_y = roadPos_y;

        coinCounter = 3 + levelNumber; //nombre de coins (+ le numéro de level)
        roadCounter = roadForLevel[0].length - 1; //nombre de colonnes du tableau
        numberOfInsects = 3;

        fixedGameElementList = new ArrayList<FixedGameElement>();
        carList = new ArrayList<Car>();
        trunkList = new ArrayList<Trunk>();
        trunkPositionTabX = new int[trunkNumbers];
        riverPositionTabY = new int[riverNumber]; 
        
        //---PLACEMENT DES TRONCS D'ARBRES---//

        posTrunk_x = DOT_SIZE; //position des 2 premiers tronc sur l'axe x

        int cptRiver = 0;
        int numberOfRivers = riverPositionTabY.length;

        for(int trunkNumber = 0; trunkNumber < numberOfTrunks; trunkNumber++)
        {   
            trunkPositionTabX[trunkNumber] = posTrunk_x;

            posTrunk_y = 0;

            for(int i = 0; i < roadCounter; i++)
            {
                if(roadForLevel[levelNumber][i] == water)
                { //je dois placer un tronc d'arbre car je suis sur la riviere
                    trunkList.add(new Trunk(posTrunk_x, posTrunk_y));
                    //----JE DEFINIS L'EMPLACEMENT DES 2 RIVIERES---//
                    if(cptRiver < numberOfRivers)
                    {
                        riverPositionTabY[cptRiver] = posTrunk_y; //stock la position sur y dans le tableau de position
                        cptRiver++;
                    }
                    //-----------------------------------------------//
                }
                posTrunk_y += DOT_SIZE;
            }
            posTrunk_x += spaceBetweenTrunk; //espace entre les troncs sur l'axe x
        }

        //---PLACEMENT DES BUISSONS---//

        posBush_x = 0;
        posBush_y = 0;

        for(int i = 0; i < roadCounter; i++)
        {   
            if(roadForLevel[levelNumber][i] == grass && !positionOfScoreBar(posBush_y)) //si on se trouve sur l'herbe ET que l'on ne se trouve pas dans la ligne de score/vie/highScore
            {
                for(int bushNumber = 0; bushNumber < MAX_BUSH_LINE; bushNumber++) //4 buissons sur chaque "ligne" de grass
                {
                    posBush_x = getRandomCoordinate();
                    fixedGameElementList.add(new Bush(posBush_x, posBush_y));
                }
            }

            posBush_y += DOT_SIZE;
        }
         
        //---PLACEMENT ALEATOIRE DES 3 COINS----//
        for(int i = 0; i < coinCounter ; i++){
            posCoin_x = getRandomCoordinate();
            posCoin_y = getRandomCoordinate();

            while(positionOfScoreBar(posCoin_y) || positionOfRiver(posCoin_y) || positionOfBush(posCoin_x, posCoin_y))
            {
                posCoin_y = getRandomCoordinate();
            } //pour éviter d'aller dans la zone où se trouve les pv et le score

            fixedGameElementList.add(new Coin(posCoin_x, posCoin_y));
        }

        //---PLACEMENT DES 3 types d'INSECTE AU HASARD---//

        for (int insectType = 0; insectType < numberOfInsects; insectType++)
        {
            posInsect_x = getRandomCoordinate();
            posInsect_y = getRandomCoordinate();

            while(positionOfScoreBar(posInsect_y) || positionOfRiver(posInsect_y) || positionOfBush(posInsect_x, posInsect_y))
            {
                posInsect_y = getRandomCoordinate();
            }
            if(insectType == 0)
                fixedGameElementList.add(new YellowInsect(posInsect_x, posInsect_y));
            else if(insectType == 1)
                fixedGameElementList.add(new RedInsect(posInsect_x, posInsect_y));
            else
                fixedGameElementList.add(new BlueInsect(posInsect_x, posInsect_y));  
        }

        //---PLACEMENT DE LA PILLULE---//

        posPill_x = getRandomCoordinate();
        posPill_y = getRandomCoordinate();

        while(positionOfScoreBar(posPill_y) || positionOfRiver(posPill_y) || positionOfBush(posPill_x, posPill_y))
        {
            posPill_y = getRandomCoordinate();
        }

        fixedGameElementList.add(new Pill(posPill_x, posPill_y));

        //---PLACEMENT DU POWER UP PERMETTANT DE MARCHER SUR LES BUISSONS---//

        posGreenStar_x = getRandomCoordinate();
        posGreenStar_y = getRandomCoordinate();

        while(positionOfScoreBar(posGreenStar_y) || positionOfRiver(posGreenStar_y) || positionOfBush(posGreenStar_x, posGreenStar_y))
        {
            posGreenStar_y = getRandomCoordinate();
        }

        fixedGameElementList.add(new BushPowerUpStar(posGreenStar_x, posGreenStar_y));

        //---PLACEMENT DES VOITURES SUR LA ROUTE---//
        carListSize = carList.size();

        for(int index = 0; index < roadCounter; index++){
            posCar_x = getRandomPositionCarX();
            if(roadForLevel[levelNumber][index] == road){ //donc je dois placer une voiture car il y'a une route
                if(carListSize % 2 == 0 && blueCarCounter < MAX_BLUECAR_PER_LEVEL) //2 voitures bleues par level
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
                    redCar.setIndexOfRoad(index); //je lui donne l'emplacement dans le tableau de la route où la voiture rouge se trouve
                }
                else
                    carList.add(new OrangeCar(posCar_x, posCar_y));
                 
                carListSize = carList.size();
            }
            posCar_y += DOT_SIZE; //on avance ligne par ligne
        }
        //---FIN PLACEMENT ----//

        //---CREATION DES RECTANGLE POUR LE FROG, LES VOITURES ET LES ELEMENTS FIXES---//
        frogRec = new Rectangle(frog.getPosX(), frog.getPosY(), frog.getImgWidth(), frog.getImgHeight());

        for(Car elem: carList){
            elemRec = new Rectangle(elem.getPosX(), elem.getPosY(), elem.getImgWidth(), elem.getImgHeight());
        }

        for(FixedGameElement elem: fixedGameElementList)
        {
            elemRec = new Rectangle(elem.getPosX(), elem.getPosY(), elem.getImgWidth(), elem.getImgHeight());
        }

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private boolean positionOfRiver(int positionOnY)
    {//verifie si positionOnY correspond à la position d'une riviere
        if(positionOnY == riverPositionTabY[0] || positionOnY == riverPositionTabY[1])
            return true;
        
        return false;
    }

    private boolean positionOfScoreBar(int positionOnY)
    { //verifie si positionOnY correspond à la ScoreBar
        if(positionOnY == POSITION_SCORE_BAR)
            return true;

        return false;
    }


    public void setInvincibleModeActivated()
    {//active le mode invincible (on vient de manger la pillule)
        invincibleMode = true;
        setFrogColorToInvincible();
        startInvincibleMode = System.currentTimeMillis(); //début du mode invincible (on enregistre le début)
    }

    public void setFrogColorToNormal()
    {// le frogger reprend sa couleur normale
        frogImageMap.put("frog", iifrog); 
    }

    public void setFrogColorToInvincible()
    {// le frogger prend la couleur voulue lorsqu'il devient invincible
        frogImageMap.put("frog", iiinvinciblefrog); 
    }

    // --- GETTEUR DU BOARD POUR LES AUTRES CLASSES --- //

    public int[] getRoadTab()
    { //retourne un tableau contenant le niveau actuel(voie, herbe et eau)
        int[] roadTab = new int[roadForLevel[0].length];

        for(int col = 0; col < roadForLevel[0].length; col++)
        {
            roadTab[col] = roadForLevel[levelNumber][col];
        }
        return roadTab;
    }

    public int getBoardMax_X()
    { 
        return MAX_LIMIT_GAME_X;
    }

    public int getBoardMax_Y()
    {
        return MAX_LIMIT_GAME_Y;
    }

    public int getBoardMin_X()
    {
        return MIN_LIMIT_GAME_X;
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

    public int getVoid_x()
    {
        return void_x;
    }

    public int getVoid_y()
    {
        return void_y;
    }

    // ------------------------------------------------ //

    private boolean positionOfBush(int elemPosX, int elemPosY)
    { //cette fonction permet de ne pas placer un element sur un buisson (sinon il devient impossible de récupérer ou d'interagir avec l'élement)
        for(FixedGameElement elemBush : fixedGameElementList){
            if(elemBush.getType() == "bush")
            {
                if(elemBush.getPosX() == elemPosX && elemBush.getPosY() == elemPosY)
                    return true;
            }
        }
        return false;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        initLevel(g);
        
        doDrawing(g);
    }

    private void initLevel(Graphics g) 
    {//Initialisation du niveau en cours (routes, rivieres et herbe...)
        //placement des routes (dépend du levelNumber)

        int numberOfColumns = roadForLevel[0].length;

        for(int i = 0; i < numberOfColumns; i++)
        {
            if(i == 0) //tout en haut du board
            { //je place une barre noire pour y mettre les vies restantes, le score et le plus gros score
                g.setColor(Color.black);
                g.fillRect(roadPos_x, roadPos_y, roadWidth, roadHeight);
            }
            else if(roadForLevel[levelNumber][i] == road) //route
            {
                //insertion des voies
                g.setColor(ROAD_COLOR);
                g.fillRect(roadPos_x, roadPos_y + (i*DOT_SIZE), roadWidth, roadHeight); 
                
                //bordure des voies
                g.setColor(BORDER_ROAD_COLOR);
                g.drawRect(roadPos_x - 1, roadPos_y + (i*DOT_SIZE), roadWidth + 1, roadHeight); 
                //roadPos_x - 1 et roadWidth + 1 pour ne pas voir la bordure sur l'axe x, rendu : route infini
            }
            else if(roadForLevel[levelNumber][i] == water) //rivière
            {
                //insertion de la rivière
                g.setColor(WATER_COLOR);
                g.fillRect(roadPos_x, roadPos_y + (i*DOT_SIZE), roadWidth, roadHeight); 
            }
        }   
    }

    private void doDrawing(Graphics g) {

        if(inGame) {

            if(nextLevel)
            {
                if(levelNumber == lastLevel) //si j'arrive au dernier niveau fin du jeu
                {
                    endGame(g, "Good job ! You finished the game.", SUCCESS_ENDGAME_COLOR);
                    //restartGame();
                }
                else //sinon on va au niveau suivant
                {
                    clearRoad(g);
                    goToNextLevel();
                }
            }
            else
            {
                drawScoreBar(g);

                for (Trunk elem : trunkList) {
                    g.drawImage(fixedGameElementImageMap.get(elem.getType()).getImage(), elem.getPosX(), elem.getPosY(), this);
                }

                for (FixedGameElement elem : fixedGameElementList) {
                    g.drawImage(fixedGameElementImageMap.get(elem.getType()).getImage(), elem.getPosX(), elem.getPosY(), this);
                }
    
                for(Car elem : carList){
                    elem.moveCar(this);
                    g.drawImage(carImageMap.get(elem.getCarType()).getImage(), elem.getPosX(), elem.getPosY(), this);
                }
                
                frog.setPosX(pos_x);
                frog.setPosY(pos_y);
                g.drawImage(frogImageMap.get(frog.getType()).getImage(), frog.getPosX(), frog.getPosY(), this);
                
                Toolkit.getDefaultToolkit().sync();
            }
        }
        else {
            clearRoad(g);
            restartGame();
        }        
    }

    private void drawScoreBar(Graphics g)
    {
        //---PLACEMENT DES VIES EN HAUT A GAUCHE---//

        posHeart_x = 0;
        posHeart_y = 0;
        
        for(int i = 0; i < remainingLife; i++)
        {
            g.drawImage(fixedGameElementImageMap.get("heart").getImage(), posHeart_x, posHeart_y, this);
            posHeart_x += DOT_SIZE;
        }

        Font small = new Font("Helvetica", Font.BOLD, 12);
        g.setFont(small);
        g.setColor(Color.GREEN);
        g.drawString("LEVEL : " + (levelNumber+1), (B_WIDTH / 2) - DOT_SIZE*6, DOT_SIZE - 5);
        g.setColor(Color.YELLOW);
        g.drawString("YOUR SCORE : " + currentScore, (B_WIDTH / 2) - DOT_SIZE*2 , DOT_SIZE - 5);
        g.setColor(Color.RED);
        g.drawString("HIGH SCORE : " + highScore, (B_WIDTH / 2) + DOT_SIZE*4, DOT_SIZE - 5);
    }

    public void setGameOverOrNot()
    { //fonction permettant de retirer une vie ou de recommencer tout si le frog meurt
        if(remainingLife > 1) //s'il reste encore des vies
        {
            frogRespawn(); //le frog respawn
        }
        else //sinon fin du jeu --> on relance directement le jeu 
        {
            inGame = false;
        }   
    }

    private void restartGame()
    { //fonction qui permet de relancer le jeu à 0 en gardant le plus gros score 
        timer.stop();
        setHighScore();
        inGame = true;
        remainingLife = 3;
        blueCarCounter = 0;
        roadCounter = 0;
        levelNumber = 0;
        nextLevel = false;
        currentScore = 0;
        initGame();
    }

    private void frogRespawn()
    { //fonction permettant de respawn si il y a encore des vies restantes
        remainingLife--;
        invincibleMode = false; //fin du mode invincible
        setFrogColorToNormal();
        //respawn en bas du board (au début du jeu)
        canWalkOverBush = false;
        pos_x = GAME_BEGINNING_X;
        pos_y = GAME_BEGINNING_Y;
    }

    private void goToNextLevel()
    { //fonction permettant de charger le niveau suivant
        nextLevel = false;
        levelNumber += 1;
        roadCounter = 0;
        blueCarCounter = 0;
        invincibleMode = false;
        canWalkOverBush = false;
        //il est possible de passer au niveau suivant en étant tjr en mode invincible
        //il faut donc remettre la couleur de base du frogger
        setFrogColorToNormal();
        initGame();
    }

    private void setHighScore()
    {//fonction permettant d'enregistrer le plus gros score jamais atteint
        if(currentScore > highScore)
            highScore = currentScore;

        String stringHighScore = String.valueOf(highScore);

        try 
		{
			FileWriter myFileWriter = new FileWriter("highscore.txt"); //j'ouvre le fichier.txt en écriture
			
            myFileWriter.write(stringHighScore); //écriture dans le fichier
			
			myFileWriter.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
    }

    private void getHighScore()
    { //fonction permettant de récupérer le plus gros score enregistré sur le fichier text highscore.txt
        try{
            File fileHighScore = new File("highscore.txt");

            BufferedReader buffReader = new BufferedReader(new FileReader(fileHighScore));

            String stringHighScore;

            while ((stringHighScore = buffReader.readLine()) != null)
                highScore = Integer.parseInt(stringHighScore); //je convertis la chaine de caractère en entier
        }
        catch(IOException e)
        {
			e.printStackTrace();
        }
        
    }

    private void clearRoad(Graphics g)
    { //fonction permettant de supprimer les routes du board 
        g.clearRect(0, 0, B_WIDTH, B_HEIGHT);
    }
    
    private void endGame(Graphics g, String messageToShow, Color color) 
    {//fonction permettant d'afficher un message en cas de succes du jeu
        Font small = new Font("Helvetica", Font.BOLD, 22);
        FontMetrics metr = getFontMetrics(small);
        int espaceEntreMessages = 50;

        String messageFinalScore = "Your final score : " + currentScore;

        g.setColor(Color.black);
        g.fillRect(0, 0, B_WIDTH, B_HEIGHT); //0 et 0 = début du rectangle
        g.setColor(color);
        g.setFont(small);
        g.drawString(messageToShow, (B_WIDTH - metr.stringWidth(messageToShow)) / 2, B_HEIGHT / 2);
        g.drawString(messageFinalScore, (B_WIDTH - metr.stringWidth(messageFinalScore)) / 2, B_HEIGHT / 2 + espaceEntreMessages);
    }

    public void incScore(int valueToIncrease)
    { //fonction permettant d'incrémenter le score lorsque l'on collecte une pièce ou un insecte
        currentScore += valueToIncrease;
    }

    public void decreaseCoinAmount()
    { //fonction permettant de décrémenter le nombre de pièces restantes
        coinCounter -= 1;
    }

    private void move(int frogSpeed) {

        int nextPosition; //variable qui va contenir la position suivante (en fonction du clique de l'utilisateur)

        nextPosition = pos_x - frogSpeed; //pour leftDirection

        //on verifie la direction, si la prochaine position en fonction de cette direction ne depasse pas la limite du jeu
        //et on verifie egalement que l'on ne se deplace pas vers un buisson
        if (leftDirection && nextPosition >= MIN_LIMIT_GAME_X && isNotABush(nextPosition, "X")) {
                pos_x -= frogSpeed;
        }

        nextPosition = pos_x + frogSpeed; //pour rightDirection

        if (rightDirection && nextPosition < MAX_LIMIT_GAME_X && isNotABush(nextPosition, "X")) {
            pos_x += frogSpeed;
        }

        nextPosition = pos_y - frogSpeed; //pour upDirection

        if (upDirection) {
            if(nextPosition < LIMIT_TO_NEXT_LEVEL)//arrivé en haut donc vérif si tous les coins ont été récup avant de passer au niveau suivant
            {
                if(coinCounter == 0)
                {
                    nextLevel = true; //on passe au niveau suivant car plus aucune pièce
                }
            }
            else //on est pas encore tout en haut donc move normal
            {
                if(isNotABush(nextPosition, "Y"))
                    pos_y -= frogSpeed;
            }
        }

        nextPosition = pos_y + frogSpeed; //pour downDirection

        if (downDirection && nextPosition < MAX_LIMIT_GAME_Y && isNotABush(nextPosition, "Y")) {
            pos_y += frogSpeed;
        }
    }

    public void setWalkOverBushMode()
    {//fonction permettant de marcher sur les buissons
        canWalkOverBush = true;
    }

    private boolean isNotABush(int futurPositionOfFrog, String axe)
    {//fonction permettant verifier si le frog se déplace vers un buisson 
        if(canWalkOverBush) //inute de verifier si c'est un buisson car on peut marcher dessus
            return true;
        else
        {
            for(FixedGameElement elem : fixedGameElementList)
            {
                if(elem.getType() == "bush") //seulement les buissons nous intéressent
                {
                    if(axe == "Y") //si on se deplace verticalement on verifie l'axe Y
                    {
                        if(pos_x == elem.getPosX() && futurPositionOfFrog == elem.getPosY())
                            return false;
                    }
                    else if(axe == "X") //si on se deplace verticalement on verifie l'axe X
                    {
                        if(pos_y == elem.getPosY() && futurPositionOfFrog == elem.getPosX())
                            return false;
                    }
                }
            }
        }  
        return true;
    }

    //--------- checkCollision ------//

    private void checkCollision() {
        //fonction permettant de verifier la collision entre le frog et les élements du jeu
        
        frogRec.setBounds(frog.getPosX(), frog.getPosY(), frog.getImgWidth(), frog.getImgHeight());
        
        //parcours la liste de tronc
        for(Trunk elem : trunkList)
        {
            if(pos_y == elem.getPosY()) //si Frogger se trouve au niveau de la riviere et des troncs
            {
                if(!itIsATrunk()) //riviere donc pas un tronc
                {
                    setGameOverOrNot();
                }
            }
        }

         //--- rectangle collision ---//

        for(FixedGameElement elem: fixedGameElementList)
        {
            elemRec.setBounds(elem.getPosX(), elem.getPosY(), elem.getImgWidth(), elem.getImgHeight());

            if (frogRec.intersects(elemRec))
            {
                elem.triggerAction(this); 
            }
        }

        if(!invincibleMode) // si je suis pas en mode invincible alors on verifie les collisions
        {
            for(Car elem : carList){

                elemRec.setBounds(elem.getPosX(), elem.getPosY(), elem.getImgWidth(), elem.getImgHeight());

                if(frogRec.intersects(elemRec)) //collision entre la grenouille et un elem de type car
                {
                    elem.triggerAction(this);
                }
            }
        }
        else //sinon on est en mode invincible et donc on verifie quand le mode sera fini
        {
            checkCurrentTime(); //fonction que l'on va appelé à chaque fois pour voir si le mode invincible peut etre désactivé   
        }

        
        //--- end rectangle collision ---//

        if (!inGame || nextLevel) //si fin du jeu ou niveau suivant
        {
            timer.stop();
        }
    }

    private void checkCurrentTime()
    {//fonction permettant de voir si la durée du mode invincible a été atteinte
        current_time = System.currentTimeMillis(); //recupère le temps actuel

        int elapsedTimeInMillis = (int)(current_time - startInvincibleMode); //temps écoulé (on convert les long en int)
        int elapsedTimeInSeconds = elapsedTimeInMillis / 1000; //on convertit ce temps écoulé en secondes

        if(elapsedTimeInSeconds == invincibleModeDuration) //si 10 secondes se sont écoulés
        {
            invincibleMode = false; //fin du mode invincible 
            //mais on doit également arranger la position du frogger pour qu'elle soit adaptée à sa vitesse normale
            fixFrogPosition();
            setFrogColorToNormal();
        }
    }

    private void fixFrogPosition()
    { //fonction permettant de corriger la position du frog après le mode invincible
        if(pos_x % DOT_SIZE != 0) //mal placé sur l'axe x (provoque des bugs pour le jeu)
        {
            int nextPositionLeft, nextPositionRight;

            nextPositionLeft = pos_x - INVINCIBLE_MODE_SPEED; //pour leftDirection
            nextPositionRight = pos_x + INVINCIBLE_MODE_SPEED; //pour rightDirection


            if (nextPositionLeft >= 0 && isNotABush(nextPositionLeft, "X")) {
                pos_x -= INVINCIBLE_MODE_SPEED;
            }
            else if (nextPositionRight < MAX_LIMIT_GAME_X && isNotABush(nextPositionRight, "X")) {
                pos_x += INVINCIBLE_MODE_SPEED;
            }

        }   

        if(pos_y % DOT_SIZE != 0) //mal placé sur l'axe y (provoque des bugs pour le jeu)
        {
            int nextPositionUp, nextPositionDown;

            nextPositionUp = pos_y - INVINCIBLE_MODE_SPEED; //pour upDirection
            nextPositionDown = pos_y + INVINCIBLE_MODE_SPEED; //pour downDirection

            if (downDirection && nextPositionDown < MAX_LIMIT_GAME_Y && isNotABush(nextPositionDown, "Y")) {
                pos_y += INVINCIBLE_MODE_SPEED;
            }

            else if(nextPositionUp < LIMIT_TO_NEXT_LEVEL)//arrivé en haut donc vérif si tous les coins ont été récup avant de passer au niveau suivant
            {
                if(coinCounter == 0)
                {
                    nextLevel = true; //on passe au niveau suivant car plus aucune pièce
                }
            }
            else //on est pas encore tout en haut donc move normal
            {
                if(isNotABush(nextPositionUp, "Y"))
                    pos_y -= INVINCIBLE_MODE_SPEED;
            }
        }
        
    }

    //--------- fin checkCollision ------//

    private boolean itIsATrunk()
    { //fonction pertmettant de verifier si le frog marche bien sur un tronc et non sur la riviere
        for(int i = 0; i < trunkPositionTabX.length; i++)
        {
            if(pos_x == trunkPositionTabX[i])
            {
                return true;
            }
        }
        return false;
    }

    // -------- getRandomCoordinate / Position ------- //
    
    private int getRandomCoordinate() 
    { //position random pour les élements fixes
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

            if(invincibleMode) //vitesse lente
                move(INVINCIBLE_MODE_SPEED);
            else //vitesse normale
                move(DOT_SIZE);
        }
           
    }
}
