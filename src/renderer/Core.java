package renderer;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import javax.imageio.ImageIO;
import GUI.Frame;
import WorldEditor.TabMap;
import WorldEditor.World;

public class Core extends Canvas implements Runnable {

  /**
   *
   */
  private static final long serialVersionUID = 1085343357999827060L;

  public static final Bitmap textures = loadTexture("textures.png");
  public static final Bitmap wall = loadTexture("TestWallSWEN.jpg");

  public static final int WIDTH = 640, HEIGHT = WIDTH * 3 / 4;
  public static final int SCALE = 1; // implement with graphics settings

  private final BufferedImage image;
  private final int[] pixels;
  private boolean isRunning = false;

  public Bitmap3D view;
  public Bitmap screen;
  private Game game;
  private Thread thread;
  private Input input;
  private int oldMouseX, newMouseX;
  private int fps;
  public World world;
  public Frame guiFrame;

  public Core() {
    input = new Input();
    addMouseListener(input);
    addKeyListener(input);
    addFocusListener(input);
    addMouseMotionListener(input);
    addMouseWheelListener(input);
    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    guiFrame = new Frame(WIDTH * SCALE, HEIGHT * SCALE, this);
  }

  public Core(World newWorld) {
    // TODO Auto-generated constructor stub
    this.world = newWorld;
    input = new Input();
    addMouseListener(input);
    addKeyListener(input);
    addFocusListener(input);
    addMouseMotionListener(input);
    addMouseWheelListener(input);
    image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    guiFrame = new Frame(WIDTH * SCALE, HEIGHT * SCALE, this);
  }

  public void initialize() {
      if(world == null) {
        this.world = new World();
      }
        game = new Game(world);
        screen = new Bitmap(WIDTH, HEIGHT);
        view = new Bitmap3D(WIDTH, HEIGHT);
        // screen = new Screen(WIDTH, HEIGHT);

    }

  public synchronized void start() {
    initialize();
    thread = new Thread(this);
    thread.start();
    isRunning = true;
  }

  public synchronized void stop() {
    try {
      thread.join();
      isRunning = false;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;
    long timer = System.currentTimeMillis();
    int frames = 0;

    while (isRunning) {
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;
      while (delta >= 1) {
        tick();
        delta--;
      }
      if (isRunning) {
        render();
      }
      frames++;

      if (System.currentTimeMillis() - timer > 1000) {
        timer += 1000;
        fps = frames;
        frames = 0;
      }

      /*
       * newMouseX = input.mouseX; if(newMouseX > oldMouseX) Player.turningRight = true; else if
       * (newMouseX < oldMouseX) Player.turningLeft = true; else { Player.turningLeft = false;
       * Player.turningRight = false; } oldMouseX = newMouseX;
       */
    }
    stop();
  }

  private void tick() {
    world.worldTick(input);
    game.player = world.player;
    game.tick(input.keys);
    guiFrame.tick(world, input);
  }

  private void render() {
    BufferStrategy bs = this.getBufferStrategy();

    if (bs == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = bs.getDrawGraphics();

    for (int i = 0; i < pixels.length; i++) {
      pixels[i] = 0;
    }

    screen.clear();
    view.render(game);
    view.renderFog();
    screen.render(view, 0, 0);

    for (int i = 0; i < pixels.length; i++) {
      pixels[i] = screen.pixels[i];
    }

    g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
    g.setFont(new Font("Serif Plain", 2, 30));
    g.setColor(Color.GREEN);
    g.drawString(fps + " FPS", WIDTH - 110, 25);
    g.setColor(Color.BLACK);
    g.fillRect(5, 5, 180, 45);
    g.setColor(Color.RED);
    double percentage =
        world.player.statemanager.getCurrentHealth() / world.player.statemanager.getMaxHealth();
    g.fillRect(5, 5, (int) (180 * percentage), 45);

    g.dispose();
    bs.show();
  }

  /**
   * Returns a bitmap object that can be rendered.
   *
   * @param path the path of the texture to be loaded
   * @return Bitmap a bitmap from the specified image
   */
  public static Bitmap loadTexture(String path) {

    try {
      BufferedImage image = ImageIO.read(Core.class.getResourceAsStream(path));
      Bitmap res = new Bitmap(image.getWidth(), image.getHeight());
      image.getRGB(0, 0, res.width, res.height, res.pixels, 0, res.width);

      for (int i = 0; i < res.pixels.length; i++) {
        res.pixels[i] = res.pixels[i] & 0x00ffffff; // 0x'ff'ffffff
      }

      return res;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Bitmap loadFromBufferedImage(BufferedImage img) {

    Bitmap res = new Bitmap(img.getWidth(), img.getHeight());
    img.getRGB(0, 0, res.width, res.height, res.pixels, 0, res.width);

    for (int i = 0; i < res.pixels.length; i++) {
      res.pixels[i] = res.pixels[i] & 0xffffff; // 0x'ff'ffffff
    }

    return res;

  }

  public static void main(String[] args) {
    new Core();
  }
}
