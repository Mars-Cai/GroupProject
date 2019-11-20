package GUI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import WorldEditor.World;
import WorldEditor.GameObjects.Fuel;
import WorldEditor.GameObjects.HealthPotion;
import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Key;
import WorldEditor.GameObjects.Player;
import WorldEditor.GameObjects.InanimateObjects.Upgrade;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import renderer.Core;

public class VendorFrame extends JFrame {

  private JLabel HealthPotion,fuel,Key,Damageup;
  Vendor vendor;
  Map<Item, Integer> stock;
  private Player player;


  public VendorFrame(Vendor vendor, Player player) {
    super("Welcome to spend");
    this.player = player;
    this.setLayout(null);
    this.stock = vendor.getVendorStock();
    this.vendor = vendor;
  //  this.panel = new ButtonPanel();
    setSize(550,550);
    this.setPreferredSize(new Dimension(600,600));
  //  panel.setBounds(0, this.getHeight() - 80, this.getWidth(), 50);
    setResizable(false);
    setFocusable(false);
    setFocusableWindowState(false);

    for(Item i:stock.keySet()) {
      if(i instanceof HealthPotion) {
        addImage("HealthPotion.jpg", i);
      }else
      if(i instanceof Fuel) {
        addImage("fuel.jpeg",i);
      }else
      if(i instanceof Key) {
        addImage("Key.jpg",i);
      }else {
        addImage("Damageup.png",i);
      }

    }
    this.setVisible(true);

  }
  /**
   * Adds the image to the Frame
   */
  public void addImage(String filename, Item i){
    if(i instanceof HealthPotion) {
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
          int option = JOptionPane.showConfirmDialog(HealthPotion, "Wanna buy some health?", "Money taker",JOptionPane.YES_NO_OPTION);
          if (option == JOptionPane.YES_OPTION) {
            vendor.sellToPlayer(i);
            //new BagFrame(player);
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
          int option = JOptionPane.showConfirmDialog(fuel, "Wanna buy a bottle of fuel?", "Money taker",JOptionPane.YES_NO_OPTION);
          if (option == JOptionPane.YES_OPTION) {
            vendor.sellToPlayer(i);
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
          int option = JOptionPane.showConfirmDialog(Key, "Wanna buy a Key?", "Money taker",JOptionPane.YES_NO_OPTION);
          if (option == JOptionPane.YES_OPTION) {
            vendor.sellToPlayer(i);
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
      add(Key);
    }else if(i instanceof Upgrade) {
      Upgrade u = (Upgrade)i;
      Image image = loadImage(filename);
      Damageup = new JLabel(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
      Damageup.setBounds(50,260,200,200);
      Damageup.addMouseListener(new MouseListener(){
        @Override
        public void mousePressed(MouseEvent e) {
          // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {
          // TODO Auto-generated method stub
          int option = JOptionPane.showConfirmDialog(Damageup, "Wanna make more damage?", "Money taker",JOptionPane.YES_NO_OPTION);
          if (option == JOptionPane.YES_OPTION) {
            vendor.sellToPlayer(u);
            u.upgradeDamage(4);
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
      add(Damageup);
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
}
