import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
	HAMZA SALEEM 12J
	SNAKE GAME - ICS4U
	A RECREATION OF THE CLASSIC SNAKE GAME IN JAVA!

**/

public class Snake_hamzaSaleem
{

    public static void main (String[] args)
    {
	javax.swing.SwingUtilities.invokeLater (new Runnable () //Makes the threading in SWING more effecient
	{
	    public void run ()
	    {
		Frame f = new Frame (); //Create instance of the game Frame
		f.launch (); //Launch the game
	    }
	}
	);
    }
} // Snake_hamzaSaleem class


class Frame extends JFrame
{
    private static final long serialVersionUID = 4492613655770108018L;

    ComponentsStorage cs = new ComponentsStorage ();

    //This gets the dimensions for the screen in order to find a proper size based on the users sceren
    static Dimension dimension = Toolkit.getDefaultToolkit ().getScreenSize ();
    static int posX = (int) ((dimension.getWidth () - 1200));
    static int posY = (int) ((dimension.getHeight () - 1000));

    public void launch ()  //Constructor the JFrame of the game
    {
	setSize (800, 800);
	setTitle ("Snake by Hamza Saleem");
	setVisible (true);
	setLocation (posX, posY);
	setLocationRelativeTo (null);
	setResizable (false);
	getContentPane ().setLayout (null);
	setDefaultCloseOperation (WindowConstants.EXIT_ON_CLOSE);

	//////////////////////////////////////panel 1////////////////////////////////////////////////////////////////////////
	cs.menu1.setSize (800, 800);
	cs.menu1.setBackground (Color.BLACK);
	cs.menu1.setLayout (new BoxLayout (cs.menu1, BoxLayout.PAGE_AXIS));

	//title
	cs.title.setText ("SNAKE");
	cs.title.setFont (new Font ("Arial", Font.PLAIN, 100));
	cs.title.setForeground (Color.GREEN);
	cs.title.setAlignmentX (Component.CENTER_ALIGNMENT);

	//play button
	cs.play.setText ("PLAY");
	cs.play.setAlignmentX (Component.CENTER_ALIGNMENT);
	cs.play.add (Box.createRigidArea (new Dimension (400, 20)));
	cs.play.addActionListener (new ActionListener ()
	{
	    public void actionPerformed (ActionEvent e)
	    {
		cs.menu1.setVisible (false);

		ComponentsStorage.g = new GameScreen (); //Initialize a new game panel
		getContentPane ().add (ComponentsStorage.g); //add it to the parent frame

		;
	    }
	}
	);

	//how to play button
	cs.howtoPlay.setText ("HOW TO PLAY");
	cs.howtoPlay.setAlignmentX (Component.CENTER_ALIGNMENT);
	cs.howtoPlay.add (Box.createRigidArea (new Dimension (400, 20)));
	cs.howtoPlay.addActionListener (new ActionListener ()
	{
	    public void actionPerformed (ActionEvent e)
	    {
		cs.menu1.setVisible (false);
		cs.menu2.setVisible (true);
		repaint ();
	    }
	}
	);

	cs.menu1.add (Box.createRigidArea (new Dimension (0, 60))); //create rigid area to create space between two DOCKED elements
	cs.menu1.add (cs.title);
	cs.menu1.add (Box.createRigidArea (new Dimension (0, 170))); //create rigid area to create space between two DOCKED elements
	cs.menu1.add (cs.play);
	cs.menu1.add (Box.createRigidArea (new Dimension (0, 20))); //create rigid area to create space between two DOCKED elements
	cs.menu1.add (cs.howtoPlay);


	cs.menu2.setSize (800, 800);
	cs.menu2.setBackground (Color.BLACK);
	cs.menu2.setLayout (new BoxLayout (cs.menu2, BoxLayout.PAGE_AXIS));



	cs.home.setText ("<- GO BACK"); //This JButton is to take the user back to main menu from how to play menu
	cs.home.setAlignmentX (Component.CENTER_ALIGNMENT);
	cs.home.add (Box.createRigidArea (new Dimension (400, 20)));
	cs.home.addActionListener (new ActionListener ()
	{
	    public void actionPerformed (ActionEvent e)
	    {
		cs.menu2.setVisible (false);
		cs.menu1.setVisible (true);
	    }
	}
	);

	String text = "1. Use the ARROW keys to control the snake " + "<br>" + "<br>" +  //How to play string using HTML for spacing
	    "2. Collect the YELLOW SQUARES to gain score and increase length" + "<br>" + "<br>" +
	    "3. Game will be over if you run into yourself or the boundaries of the level";

	JLabel instructions = new JLabel (); //Instructions JLabel
	instructions.setForeground (Color.WHITE);
	instructions.setLocation (0, 0);
	instructions.setText ("<html><div style='text-align: center;'>" + text + "</div></html>");
	instructions.setAlignmentX (Component.CENTER_ALIGNMENT);
	instructions.setFont (new Font ("Arial", Font.PLAIN, 20));
	instructions.setHorizontalAlignment (SwingConstants.CENTER);
	instructions.setVerticalAlignment (SwingConstants.CENTER);

	cs.menu2.add (Box.createRigidArea (new Dimension (50, 200)));
	cs.menu2.add (instructions);
	cs.menu2.add (Box.createRigidArea (new Dimension (0, 200))); //create rigid area to create space between two DOCKED elements
	cs.menu2.add (cs.home);

	getContentPane ().add (cs.menu1); //add the main menu panel to the frame
	getContentPane ().add (cs.menu2); //add the how to play menu to the parent
	cs.menu2.setVisible (false); //only menu that should be visible upon launch is cs.menu1


	setVisible (true); //set visible AFTER adding all components

    }
}

class GameScreen extends JPanel implements ActionListener //This is the main JPanel in which the game operates
{

    private static final long serialVersionUID = -3446201308161184280L;
    static Dimension dimension = Toolkit.getDefaultToolkit ().getScreenSize ();
    static int posX = (int) ((dimension.getWidth () - 1200));
    static int posY = (int) ((dimension.getHeight () - 1000));

    public static boolean right = false; //These booleans control the direction of the snake
    public static boolean left = false;
    public static boolean up = false;
    public static boolean down = false;

    boolean gameOver = false;
    private Timer timer;
    public static int length;
    public static final int x[] = new int [1024]; //Max x size of the snake
    public static final int y[] = new int [1024]; //Max y size of the snake
    public static final int SIZE = 25;  //The size of one square of snake
    int fooX = randomwithRange (0, 30); //When launching the game, initially generate a yellow square food obj
    int fooY = randomwithRange (0, 30);

    public GameScreen ()  //Constructor for the JPanel
    {
	right = false;
	left = false;
	up = false;
	down = false;

	length = 1; //Set the initial length of the snake to 1

	for (int i = 0 ; i < length ; i++) //Set the initial position for the snake
	{
	    x [i] = 200 - i * 100;
	    y [i] = 100;
	}
	timer = new Timer (60, new ActionListener ()  //Start a timer, check for collisions, snake movement every 60 miliseconds
	{
	    public void actionPerformed (ActionEvent e)
	    {
		if (!gameOver)
		{
		    move (); //Update snakes movement
		    checkCollision (); //Check for collision with boundary/itself
		    repaint (); //paint the updated position
		}

	    }
	}
	);
	timer.start (); //Start the timer
	setFocusable (true);
	setFocusTraversalKeysEnabled (false);
	setSize (800, 800);
	setLocation (0, 0);
	setBackground (Color.BLACK);
	setOpaque (true);

	InputMap im = this.getInputMap (JPanel.WHEN_IN_FOCUSED_WINDOW); //Use inputmap to read keybindings
	ActionMap am = this.getActionMap ();

	im.put (KeyStroke.getKeyStroke (KeyEvent.VK_LEFT, 0), "left"); //left key
	im.put (KeyStroke.getKeyStroke (KeyEvent.VK_RIGHT, 0), "right"); //right key
	im.put (KeyStroke.getKeyStroke (KeyEvent.VK_UP, 0), "up"); //up key
	im.put (KeyStroke.getKeyStroke (KeyEvent.VK_DOWN, 0), "down"); //down key

	im.put (KeyStroke.getKeyStroke (KeyEvent.VK_SPACE, 0), "gameover"); //space bar key

	am.put ("gameover", new AbstractAction ()
	{
	    public void actionPerformed (ActionEvent e)  //This will execute if the user presses space bar after game is over
	    {
		if (gameOver)
		{
		    goBack ();
		}
	    }
	}
	);

	am.put ("left", new AbstractAction ()
	{
	    public void actionPerformed (ActionEvent e)
	    {
		if (!GameScreen.right) //Sets the direction of the snake to left, as long as snake is alreay not moving right
		{
		    GameScreen.left = true;
		    GameScreen.up = false;
		    GameScreen.down = false;

		}
	    }
	}
	);
	am.put ("right", new AbstractAction ()
	{

	    public void actionPerformed (ActionEvent e)
	    {
		if (!GameScreen.left) //Sets the direction of the snake to right, as long as snake is alreay not moving left
		{
		    GameScreen.right = true;
		    GameScreen.up = false;
		    GameScreen.down = false;

		}
	    }
	}
	);
	am.put ("up", new AbstractAction ()
	{

	    public void actionPerformed (ActionEvent e)
	    {
		if (!GameScreen.down) //Sets the direction of the snake to up, as long as snake is alreay not moving down
		{
		    GameScreen.up = true;
		    GameScreen.right = false;
		    GameScreen.left = false;

		}

	    }

	}
	);
	am.put ("down", new AbstractAction ()
	{

	    public void actionPerformed (ActionEvent e)
	    {
		if (!GameScreen.up) //Sets the direction of the snake to down, as long as snake is alreay not moving up
		{
		    GameScreen.down = true;
		    GameScreen.right = false;
		    GameScreen.left = false;

		}
	    }
	}
	);
    }


    public void paintComponent (Graphics g)  //This handles all  the painting of the objects
    {

	super.paintComponent (g);

	if (gameOver) //Draw if the game is over
	{
	    g.setColor (Color.red);
	    g.setFont (new Font ("Arial", Font.PLAIN, 100));
	    g.drawString ("GAME OVER", 100, 200); //Draw GAME OVER

	    g.setColor (Color.white);
	    g.setFont (new Font ("Arial", Font.PLAIN, 40));
	    g.drawString ("Score: " + length, 330, 300); //Display the score


	    g.setFont (new Font ("Arial", Font.PLAIN, 30));
	    g.drawString ("PRESS SPACEBAR TO RETURN", 180, 400); //Inform user to press space bar to return
	}


	g.setColor (Color.yellow);
	Rectangle2D food = new Rectangle2D.Double (fooX, fooY, 25, 25); //This will paint the yellow square of food
	((Graphics2D) g).fill (food);

	Ellipse2D snakeHead = null;

	for (int i = 0 ; i < length ; i++) //Loop through the snake tails and paint each tail/head correspondingly
	{
	    if (i == 0)
	    {
		g.setColor (Color.green);
		snakeHead = new Ellipse2D.Double (x [i], y [i], 25, 25); //Paint snake head
		((Graphics2D) g).fill (snakeHead);

	    }
	    else
	    {
		g.setColor (Color.green);
		snakeHead = new Ellipse2D.Double (x [i], y [i], 25, 25); //Paint all the snake tails
		((Graphics2D) g).fill (snakeHead);
	    }
	    eatFood (food, snakeHead); //Check if the snake eats the food
	}

	g.dispose ();

    }


    public void checkCollision ()  //This will check if the snake collides with the level boundaries, or itself
    {

	for (int i = length ; i > 0 ; i--)
	{
	    if ((length > 4) && x [0] == x [i] && y [0] == y [i]) //Check if snake head collides with any snake tail
	    {
		gameOver ();
	    }
	}

	if (x [0] > 775 || x [0] < 0 || y [0] < 0 || y [0] > 755) //Check if snake head goes out of bounds
	{
	    gameOver ();
	}
    }


    public void eatFood (Rectangle2D f, Ellipse2D s)  //This checks if the snake collides with a food object
    {

	if (s.intersects (f)) //If it collides, generate new food and increase length by 1
	{

	    fooX = randomwithRange (0, 30);
	    fooY = randomwithRange (0, 30);

	    length++;
	}
    }


    public int randomwithRange (int min, int max)  //Generates a random food object on a grid of 25px x 25px
    {
	int range = (max - min) + 1;
	return (int) ((Math.random () * range) + min) * 25;

    }


    public void actionPerformed (ActionEvent e)
    {
	// TODO Auto-generated method stub

    }


    private void move ()  //Method to move the snake
    {
	for (int i = GameScreen.length ; i > 0 ; i--) //MOST IMPORTANT ALGORITHIM
	{
	    GameScreen.x [i] = GameScreen.x [(i - 1)]; //This algorithim pushes the position of the snake head down 1 position for each tail in the snake
	    GameScreen.y [i] = GameScreen.y [(i - 1)]; //This is so that the snake tails follow the head every position it goes
	}
	if (GameScreen.left) //move the snake left
	{
	    GameScreen.x [0] -= GameScreen.SIZE;

	}
	if (GameScreen.right) //move the snake right
	{
	    GameScreen.x [0] += GameScreen.SIZE;
	}
	if (GameScreen.up) //move the snake up
	{
	    GameScreen.y [0] -= GameScreen.SIZE;
	}
	if (GameScreen.down) //move the snake down
	{
	    GameScreen.y [0] += GameScreen.SIZE;
	}
    }


    public void gameOver ()  //method to recognize that the game is over
    {
	gameOver = true;

    }


    public void goBack ()  //This will take the user back to the main menu after the game is over
    {
	this.setVisible (false);
	ComponentsStorage.menu1.setVisible (true);
    }
}

class ComponentsStorage //This class is to store all JComponents for easy access throughout classes
{

    JLabel title = new JLabel (); //Title for the main menu
    JButton play = new JButton (); //Play button the main menu
    public static JPanel menu1 = new JPanel (); //Menu 1 on the main menu
    JButton howtoPlay = new JButton (); //how to play button on the main menu

    //JComponenets for menu2
    JPanel menu2 = new JPanel ();
    JButton home = new JButton ();
    JLabel title2 = new JLabel ();

    //JComponents for GameScreen
    public static GameScreen g;


}
