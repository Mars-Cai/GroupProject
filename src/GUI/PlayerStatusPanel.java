package GUI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import WorldEditor.World;
import WorldEditor.GameObjects.Player;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import WorldEditor.GameObjects.WeaponObjects.Gun;
import WorldEditor.GameObjects.WeaponObjects.Sword;
import renderer.Input;

/**
 * Class that represents a component for the monopoly game
 *
 * @author Mars Cai
 *
 */
public class PlayerStatusPanel extends JPanel {

  private static Player player;
  private static JLabel cash, weapon;
  private MoneyUpdater updater;
  private Image image;
  private Vendor vendor;
  private Button Vendor;
  private boolean vendorAround = false;
  private boolean vendorShowing = false;
  private boolean bagShowing = false;
  VendorFrame vF;
  BagFrame bf;
  public void tick(World world, Input input) {
    this.player = world.player;
    this.vendor = world.vendorObjects.get(0);
    resetCashLabel();
    resetWeaponLabel();
    if (player.isInRangeOfVendor())
      vendorAround = true;
    setVenderShowing(input.vendorPressed);
    setBagShowing(input.bagPressed);
  }

  private void setVenderShowing(boolean vendorPressed) {
    if (vendorPressed) {
      if (vendorShowing) {
      } else {
        vF = new VendorFrame(vendor, player);
        vendorShowing = true;
      }
    } else {
      if (vF != null) {
        vendorShowing = false;
        vF.dispose();
      }
    }
  }

  private void setBagShowing(boolean bagPressed) {
    if (bagPressed) {
      if (bagShowing) {
      } else {
        bf = new BagFrame(player);
        bagShowing = true;
      }
    } else {
      if (bf != null) {
        bagShowing = false;
        bf.dispose();
      }
    }
  }

  /**
   * Constructs a Player status panel
   *
   * @param money Image for the money
   */
  public PlayerStatusPanel() {
    setBackground(Color.DARK_GRAY);
    setBounds(0, 640, 640, 35);
    setPreferredSize(new Dimension(640, 140));
    setLayout(null);
    setupBagButton();
    setupVendorButton();
    setupCashLabel();
    setupWeaponLabel();
    updater = new MoneyUpdater();
    updater.start();
  }

  /**
   * Method set the bag button
   */
  public void setupBagButton() {
    Button Vendor = new Button("My Bag");
    add(Vendor, BorderLayout.WEST);
    Vendor.setSize(new Dimension(100, 50));
    Vendor.setBounds(500, 10, 100, 50);
    Vendor.setBackground(Color.LIGHT_GRAY);
    Vendor.setFont(new Font("Segoe Script", Font.BOLD, 16));
    Vendor.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        new BagFrame(player);
      }
    });
  }

  /**
   * Method set the Vendor button
   */
  public void setupVendorButton() {
    Vendor = new Button("Vendor");
    add(Vendor, BorderLayout.EAST);
    Vendor.setSize(new Dimension(100, 50));
    Vendor.setBounds(40, 10, 100, 50);
    Vendor.setBackground(Color.LIGHT_GRAY);
    Vendor.setFont(new Font("Segoe Script", Font.BOLD, 16));
    Vendor.setVisible(true);
    Vendor.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (vendorAround) {
          new VendorFrame(vendor, player);
        } else {
          Frame.appendText("There is no vendor near you!");
        }
      }
    });
  }

  /**
   * Method to update the cash label
   */
  public static void resetCashLabel() {
    if (player == null)
      return;
    cash.setText("$" + player.getMoney());
  }

  /**
   * Sets up the cash label on the component
   */
  public void setupCashLabel() {
    JLabel title = new JLabel();
    title.setForeground(Color.YELLOW);
    title.setFont(new Font("sansserif", Font.BOLD, 11));
    title.setText("And you have");
    title.setBounds(310, 10, 150, 20);
    add(title);
    cash = new JLabel("", JLabel.CENTER);
    cash.setFont(new Font("sansserif", Font.BOLD, 25));

    cash.setForeground(Color.YELLOW);
    cash.setBounds(300, 30, 200, 20);
    add(cash);
  }

  /**
   * Sets up the cash label on the component
   */
  public void setupWeaponLabel() {
    ImageIcon icon = new ImageIcon("Images/money.jpg");
    JLabel title = new JLabel();
    title.setForeground(Color.RED);
    title.setFont(new Font("sansserif", Font.BOLD, 11));
    title.setText("You're holding a");
    title.setBounds(160, 10, 150, 20);
    add(title);
    weapon = new JLabel();
    weapon.setForeground(Color.RED);
    weapon.setFont(new Font("sansserif", Font.BOLD, 20));

    weapon.setIcon(icon);
    weapon.setBounds(170, 30, 150, 20);
    add(weapon);
  }

  public static void resetWeaponLabel() {
    if (player.getWeapon() == null)
      weapon.setText("Fist");
    else if (player.getWeapon() instanceof Gun)
      weapon.setText("Lazer gun");
    else if (player.getWeapon() instanceof Sword)
      weapon.setText("Huge Sword");
  }
}


class MoneyUpdater extends Thread {

  public void run() {
    /*
     * Constantly updates the reset cash label through out the game
     */
    while (true) {
      PlayerStatusPanel.resetCashLabel();
    }
  }

}
