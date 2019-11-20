package GUI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import WorldEditor.*;
import WorldEditor.GameObjects.GameObject;
import WorldEditor.GameObjects.Player;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import persistence.Loader;
import persistence.Saver;
import renderer.Core;
import renderer.Game;
import renderer.Input;

/**
 * Frame which models the frame in which the game is inside.
 *
 * @author Mars Cai
 *
 */
public class Frame extends JFrame {

  /**
   *
   */
  private static final long serialVersionUID = 1066290271417769606L;

  public static final Color COLOR_THEME = new Color(185, 211, 238);
  public static final Color LIGHT_THEME = new Color(198, 226, 255);

  private int CANVAS_WIDTH; // starting canvas width

  private int CANVAS_HEIGHT; // starting canvas height

  private int BORDER_HEIGHT_ERROR; // have to take this away from the bottom component so it fits
                                   // properenderer.Game;ly on the frame

  final int BORDER_WIDTH_ERROR = 16;

  final int BUTTON_PANEL_HEIGHT = 35;

 // private final BufferedImage cursor;

  // private GameCanvas gameCanvas;enderer.Game;
  private static JTextArea statusTextArea;
  private JScrollPane scrollPane;
  private Core game;
  private PlayerStatusPanel panel;
  private Player player;

  private World world;
  public static boolean isDead = false;
  public static boolean passed = false;
  private static int cheated = 0;
  /**
   * Constructs the Frame
   */
  public Frame(int width, int height, Core game) {
    super("This is a Game");
    this.CANVAS_WIDTH = width;
    this.CANVAS_HEIGHT = height + 200;
    this.game = game;
    setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    setMinimumSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    setMaximumSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    setupTextArea();
    setupMenu();
    setupListeners();
    setSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    setResizable(false);
    setLocationRelativeTo(null);
    setAlwaysOnTop(true);
    // Removes cursor
    /*cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    Cursor noCursor =
        Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "noCursor");
    getContentPane().setCursor(noCursor);*/
    setupButtonPanel();
    add(game);
    pack();
    setVisible(true);
    // gameStart();world
    game.start();
  }

  public void setupButtonPanel() {
    panel = new PlayerStatusPanel();
    getContentPane().add(panel, BorderLayout.SOUTH);

  }

  /**
   * Sets up the text area for the monopoly game
   */
  public void setupTextArea() {
    statusTextArea = new JTextArea();
    statusTextArea.setLineWrap(true);
    statusTextArea.setWrapStyleWord(true); // pretty line wrap.
    statusTextArea.setEditable(false);
    scrollPane = new JScrollPane(statusTextArea);
    scrollPane.setPreferredSize(new Dimension(640, 70)); // 140 is perfect
    // these two lines make the JScrollPane always scroll to the bottom when
    // text is appended to the JTextArea.
    DefaultCaret caret = (DefaultCaret) statusTextArea.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    Font font = new Font("Segoe Script", Font.BOLD, 11);
    statusTextArea.setFont(font);
    statusTextArea.setForeground(Color.WHITE);
    statusTextArea.setText("This is the message bar.\n");
    // // Makes a black border
    statusTextArea.setBackground(Color.DARK_GRAY);
    scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    getContentPane().add(scrollPane, BorderLayout.SOUTH);
    setVisible(true);
  }

  /**
   * Method to control the text input to the text area
   *
   * @param text - Text input
   */
  public static void appendText(String text) {
    statusTextArea.append(text + "\n");
    // makes sure its always at the end of the text so you see the latest updates of the game
    statusTextArea.setCaretPosition(statusTextArea.getDocument().getLength());
  }

  /**
   * Sets up all the listener objects for the Monopoly frame
   */
  public void setupListeners() {
    // add component listener. For resizing
    addComponentListener(new ComponentListener() {

      public void componentResized(ComponentEvent e) {

        int newCanvasWidth = Frame.this.getWidth() - CANVAS_WIDTH;
        int newCanvasHeight = Frame.this.getHeight() - CANVAS_HEIGHT;
        // change size of canvas when resized
        // gameCanvas.setSize(Math.min(newCanvasWidth,newCanvasHeight),Math.min(newCanvasWidth,newCanvasHeight));
        // change the size of the scroll pane when resized
        scrollPane.setBounds(0, newCanvasHeight, newCanvasWidth,
            ((Frame.this.getHeight() - (newCanvasHeight) - BORDER_HEIGHT_ERROR))
                - BUTTON_PANEL_HEIGHT);
        // change the size of the buttonPanel when resized

      }

      public void componentHidden(ComponentEvent e) {
        /* not implemented */}

      public void componentMoved(ComponentEvent e) {
        /* not implemented */}

      public void componentShown(ComponentEvent e) {
        /* not implemented */}
    });
    // Set window closing event
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        exit();
      }
    });
  }

  /**
   * Sets up the menu bar for the frame
   */
  public void setupMenu() {
    JMenuBar menubar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    menu.getPopupMenu().setLightWeightPopupEnabled(false);
    // set the short cut key to M
    menu.setMnemonic('M');
    // New Game option
    JMenuItem newGame = new JMenuItem("New Game");
    // attach action listener to the JMenuItem
    newGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int option = JOptionPane.showConfirmDialog(Frame.this, "Are you ready?", "GLHF",
            JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION)
          game.start();
      }
    });

    // set the short cut key to n
    newGame.setMnemonic('N');
    // add the menu bar to the file menu
    menu.add(newGame);
    // Exit Game option
    JMenuItem exitGame = new JMenuItem("Exit Game");
    exitGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        exit();
      }
    });
    // set the short cut key to E
    exitGame.setMnemonic('E');
    menu.add(exitGame);

    // Save Game
    JMenuItem saveGame = new JMenuItem("Save Game");
    saveGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        save();
      }
    });
    // set the short cut key to S
    saveGame.setMnemonic('S');
    menu.add(saveGame);
    // Load Game
    JMenuItem loadGame = new JMenuItem("Load Game");
    loadGame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        load();
      }
    });
    // set the short cut key to L
    loadGame.setMnemonic('L');
    menu.add(loadGame);
    // add the menu bar to the menu
    menubar.add(menu);

    // Help
    JMenu help = new JMenu("Help");
    help.getPopupMenu().setLightWeightPopupEnabled(false);
    // set the short cut key to H
    help.setMnemonic('H');
    // Show the rules
    JMenuItem rule = new JMenuItem("Rule");
    rule.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        rule();
      }
    });
    // set the short cut key to R
    rule.setMnemonic('R');
    help.add(rule);
    // show the tips
    JMenuItem tips = new JMenuItem("Tips");
    tips.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        tips();
      }
    });
    // set the short cut key to T
    tips.setMnemonic('T');
    help.add(tips);
    menubar.add(help);
    // Other
    JMenu other = new JMenu("Other");
    other.getPopupMenu().setLightWeightPopupEnabled(false);
    // set the short cut key to O
    other.setMnemonic('O');
    // cheating
    JMenuItem cheat = new JMenuItem("Cheat");
    cheat.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cheat();
      }
    });
    // set the short cut key to S
    cheat.setMnemonic('C');
    other.add(cheat);
    menubar.add(other);
    setJMenuBar(menubar);
  }

  public void gameStart() {
    BufferedImage gameStart = loadImage("gamename.jpg");

    JPanel pane = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(gameStart, 0, 0, null);
      }
    };
    add(pane);
    setVisible(true);
    Button start = new Button("Let's Begin");
    add(start, BorderLayout.CENTER);
    start.setBackground(Color.BLACK);
    // start.setSize(100, 50);
    start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
       // String name = JOptionPane.showInputDialog(Frame.this, "Tell me your name, fighter.");
        game.start();
      }
    });


  }

  public void gameOver() {
    BufferedImage gameOver = loadImage("gameover.jpg");

    JPanel pane = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(gameOver, 0, 0, null);
      }
    };
    add(pane);
    setVisible(true);
  }

  /**
   * cheat method for cheating
   */
  public void cheat() {
   if(cheated == 0) {
     player.plusMoney(1000);
     appendText("Shhhhhhhh.\n");
     cheated++;
   }else if(0<cheated&& cheated<5){
     appendText("Seriously? No more cheating!\n");
     cheated++;
   }else if(cheated==5){
     player.plusMoney(10000);
     appendText("Fine,spotted. That's all I have!.\n");
   }else {
     appendText("That's all I have! People watching you!.\n");
   }
  }

  /**
   * rule method for showing the rules
   */
  public void rule() {
    appendText("\nHello, there is only one rule, get out!" + "You may use anything you can find"
        + "Remember, kill monsters can help get new tools and hurt you as well"
        + "W/A/S/D to control yourself, don't lose control!"
        + "Right click for attacking and left click to talk, don't try to talk to monsters, they don't speak English."
        + "Left click for collect stuff."
        + "GLHF!");
  }

  /**
   * tips method for showing the tips
   */
  public void tips() {
    appendText("You can buy all you need from vendor if you have enough money.\n");
    appendText("You don't need to kill small enemies but it is a good way to get items.\n");
  }

  /*
   * when player killed the boss
   */

  public void pass() {
    int option = JOptionPane.showConfirmDialog(Frame.this, "You won! How good are you!",
        "Congratulations!", JOptionPane.YES_OPTION);
    if (option == JOptionPane.YES_OPTION)
      System.exit(0);
  }
  /*
   * When player killed by enemy
   */

  public void dead() {
    int option = JOptionPane.showConfirmDialog(Frame.this, "You are dead, loser!", "Game Over",
        JOptionPane.YES_OPTION);
    if (option == JOptionPane.YES_OPTION)
      System.exit(0);
  }

  /**
   * Exit method for exiting the frame
   */
  public void exit() {
    int option = JOptionPane.showConfirmDialog(Frame.this, "Wanna give up now?", "Exit Game?",
        JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.YES_OPTION)
      System.exit(0);
  }

  /*
   * Save method for saving the game
   */
  public void save() {
    String filename = getSaveName();
    int option = JOptionPane.showConfirmDialog(Frame.this, "Game saved, you're welcome!",
        "Save Game", JOptionPane.OK_OPTION);
    if (option == JOptionPane.OK_OPTION)
      Saver.save(filename, game.world);
  }

  /**
   * get the name of the game to be saved
   */
  public String getSaveName() {
    String option = JOptionPane.showInputDialog("Name Your Game");
    if (option.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Please give a name!");
      return getSaveName();
    }
    return option;
  }

  /*
   * Load method for loading the game
   */
  public void load() {
    int option = JOptionPane.showConfirmDialog(Frame.this, "Wanna load saved game?", "Load Game",
        JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.YES_OPTION) {
      String name = JOptionPane.showInputDialog("Which game you want to load?");
      World newWorld = (World) Loader.load(name+".xml");
      new Core(newWorld);
      this.dispose();
    }
  }
  /**
   * Load an image from the file system, using a given filename.
   *
   * @param filename
   * @return
   */
  public static BufferedImage loadImage(String filename) {
    // using the URL means the image loads when stored
    // in a jar or expanded into individual files.

    try {
      BufferedImage image = ImageIO.read(Core.class.getResourceAsStream(filename));
      return image;
    } catch (IOException e) {
      // we've encountered an error loading the image. There's not much we
      // can actually do at this point, except to abort the game.
      throw new RuntimeException("Unable to load image: " + filename);
    }
  }

  public void tick(World world, Input input) {
    // TODO Auto-generated method stub
    this.player = world.player;
    panel.tick(world, input);
    if (isDead)
      dead();
    if (passed)
      pass();


  }

}
