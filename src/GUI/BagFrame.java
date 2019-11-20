package GUI;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import WorldEditor.GameObjects.Fuel;
import WorldEditor.GameObjects.HealthPotion;
import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Key;
import WorldEditor.GameObjects.Player;
import WorldEditor.GameObjects.InanimateObjects.Upgrade;
import WorldEditor.GameObjects.WeaponObjects.Gun;
import WorldEditor.GameObjects.WeaponObjects.Sword;
import WorldEditor.GameObjects.WeaponObjects.Weapon;
import renderer.Core;

public class BagFrame extends JFrame {

  private JLabel label;
  private Player player;
  private JLabel HealthPotion,fuel,Key,Gun,Sword;


  public BagFrame(Player player){
        super("This is all you have");
        this.player = player;
        this.setLayout(null);
        setSize(775,550);
        this.setPreferredSize(new Dimension(775,550));
        setResizable(false);
        setFocusable(false);
        setFocusableWindowState(false);
        for(int i = 0; i< player.invertory.getAllItems().length;i++) {
          Item current = player.invertory.getAllItems()[i];
              if(current instanceof Gun) {
                addImage("gun.png", current);
              }else if(current instanceof Sword) {
                addImage("sword.png",current);
              }else if(current instanceof HealthPotion) {
                addImage("HealthPotion.jpg", current);
              }else if(current instanceof Fuel) {
                addImage("fuel.jpeg",current);
              }else if(current instanceof Key) {
                addImage("Key.jpg",current);
              }else {
                addImage("Damageup.png",current);
              }

        }
        this.setLocation(1500, 0);
        this.setVisible(true);
    }
  /**
   * Adds the image to the Frame
   */
  public void addImage(String filename, Item i){
    if(i instanceof HealthPotion) {
      HealthPotion h = (HealthPotion)i;
      Image image = loadImage(filename);
      HealthPotion = new JLabel(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
      HealthPotion.setBounds(50,30,200,200);
      HealthPotion.addMouseListener(new MouseListener(){
        @Override
        public void mousePressed(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
          // TODO Auto-generated method stub
          int option = JOptionPane.showConfirmDialog(HealthPotion, "Get heal now?", "Health Potion",JOptionPane.YES_NO_OPTION);
          if (option == JOptionPane.YES_OPTION) {
            player.statemanager.addHealth(h.getHealthRestored());
            player.invertory.removeItem(h);
            newBagFrame();
          }
        }



        @Override
        public void mouseEntered(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseClicked(MouseEvent e) {
          // TODO Auto-generated method stub

        }
      });
      add(HealthPotion);
    }else if(i instanceof Fuel) {
      Fuel f = (Fuel) i;
      Image image = loadImage(filename);
      fuel = new JLabel(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
      fuel.setBounds(300,30,200,200);
      fuel.addMouseListener(new MouseListener(){
        @Override
        public void mousePressed(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
          // TODO Auto-generated method stub
          int option = JOptionPane.showConfirmDialog(fuel, "Fuel up your torch?", "Torch Fuel",JOptionPane.YES_NO_OPTION);
          if (option == JOptionPane.YES_OPTION) {
              player.useFuel(f);
              player.invertory.removeItem(f);
              newBagFrame();
          }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseClicked(MouseEvent e) {
          // TODO Auto-generated method stub

        }
      });
      add(fuel);
    }else if(i instanceof Key) {
      Image image = loadImage(filename);
      Key = new JLabel(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
      Key.setBounds(300,260,200,200);
      Key.addMouseListener(new MouseListener(){
        @Override
        public void mousePressed(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
          // TODO Auto-generated method stub
          int option = JOptionPane.showConfirmDialog(Key, "It will be used when need", "An important key",JOptionPane.YES_NO_OPTION);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseClicked(MouseEvent e) {
          // TODO Auto-generated method stub

        }
      });
      add(Key);
    }else if(i instanceof Gun) {
      Image image = loadImage(filename);
      Gun = new JLabel(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
      Gun.setBounds(50,260,200,200);
      Gun.addMouseListener(new MouseListener(){
        @Override
        public void mousePressed(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
          // TODO Auto-generated method stub
          int option = JOptionPane.showConfirmDialog(Gun, "Wanna use the Lazer Gun now?", "  ",JOptionPane.YES_NO_OPTION);
          if (option == JOptionPane.YES_OPTION) {
            player.useWeapon(((Weapon) i));
            newBagFrame();
          }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseClicked(MouseEvent e) {
          // TODO Auto-generated method stub

        }
      });
      add(Gun);
    }else if(i instanceof Sword) {
      Image image = loadImage(filename);
      Sword = new JLabel(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
      Sword.setBounds(550,30,200,200);
      Sword.addMouseListener(new MouseListener(){
        @Override
        public void mousePressed(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
          // TODO Auto-generated method stub
          int option = JOptionPane.showConfirmDialog(Sword, "Wanna use the Huge Sword now?", "  ",JOptionPane.YES_NO_OPTION);
          if (option == JOptionPane.YES_OPTION) {
            player.useWeapon(((Weapon) i));
            newBagFrame();
          }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseExited(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseClicked(MouseEvent e) {
          // TODO Auto-generated method stub

        }
      });
      add(Sword);
    }
  }

  void newBagFrame() {
    new BagFrame(player);
    this.dispose();
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
}