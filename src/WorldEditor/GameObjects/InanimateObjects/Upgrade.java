package WorldEditor.GameObjects.InanimateObjects;

import WorldEditor.Layer;
import WorldEditor.GameObjects.Item;
import WorldEditor.GameObjects.Player;

public class Upgrade extends Item {

  private Player player;

  public Upgrade(Player player) {
    // TODO Auto-generated constructor stub
    this.player = player;
  }
  public void upgradeDamage(double amount) {
    this.player.statemanager.setAttackDamage(this.player.statemanager.getAttackDamage() + amount);
  }
  @Override
  protected void setHitBoxAndRadius() {
    // TODO Auto-generated method stub
    hitBoxRadius = 0;
  }

  @Override
  protected void setLayer() {
    // TODO Auto-generated method stub
    layer = Layer.FLOOR;
  }

}
