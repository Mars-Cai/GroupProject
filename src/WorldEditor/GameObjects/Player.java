package WorldEditor.GameObjects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.GameObjects.InanimateObjects.Chest;
import WorldEditor.GameObjects.InanimateObjects.Door;
import WorldEditor.GameObjects.InanimateObjects.Vendor;
import WorldEditor.GameObjects.WeaponObjects.Gun;
import WorldEditor.GameObjects.WeaponObjects.Weapon;
import gameWorld.Hit;
import gameWorld.Inventory;
import gameWorld.PlayerMove;
import gameWorld.Raycast2D;
import gameWorld.Statemanager;
import renderer.Bitmap;
import renderer.Input;

public class Player extends GameObject {
	public Inventory invertory;
	public Statemanager statemanager = new Statemanager();
	private double money=0;
	private PlayerMove playerMove;
	private boolean isInRangeOfVendor;
	private boolean isInRangeOfChest;
	public Weapon weapon = null;
	public FuelGage fuelGage;

	public Player() {
		money = 0;
		this.invertory = new Inventory();
		statemanager.setMaxHealth(100);
		statemanager.setDefualtAttackDamage(5);
		statemanager.setRunSpeed(0.05);
		statemanager.setDefualtAttackRange(1);
		this.exactLocation = new Location(1, 1);
		this.playerMove = new PlayerMove();
		playerMove.setPlayer(this);
		fuelGage = new FuelGage(this);
	}

	private Set<Enemy> enemiesToAttack = new HashSet<>();
	private Set<Chest> chestsHit = new HashSet<>();
	private Set<Item> itemsHit = new HashSet<>();

	private void clearLists() {
		enemiesToAttack.clear();
		chestsHit.clear();
		itemsHit.clear();
	}

	public void playerTick(World world, Input input) {
		PlayerMove rotOnly = PlayerMove.newRotationOnly(input.keys, this);
		fuelGage.drain();
		setHitBoxActive(false);
		Set<Hit> hits = Raycast2D.hitCastAll(exactLocation, rotOnly.rotation, statemanager.getAttackRange(), 0.1,
				world.world, Layer.ALL);
		setHitBoxActive(true);

		clearLists();
		double vendorHit = 0;
		Door doorHit = null;

		if (!hits.isEmpty()) {
			for (Hit h : hits) {
				if (h.hitObject instanceof Item) {
					itemsHit.add((Item) h.hitObject);
				}
				if (h.hitObject instanceof Enemy) {
					enemiesToAttack.add((Enemy) h.hitObject);
					Enemy e = (Enemy) h.hitObject;
					System.out.println(e.getSquare());
				}
				if (h.hitObject instanceof Vendor) {
					vendorHit++;
				}
				if (h.hitObject instanceof Door) {
					doorHit = (Door) h.hitObject;
				}
				if (h.hitObject instanceof Chest) {
					chestsHit.add((Chest) h.hitObject);
				}
			}
		}
		if (vendorHit > 0) {
			isInRangeOfVendor = true;
		} else {
			isInRangeOfVendor = true;
		}
		if (input.mouseButtons[Input.LEFTCLICK]) {
			//interact
			if (!itemsHit.isEmpty()) {
				for (Item i : itemsHit) {
					this.pick(i);
				}
			}
			if (!chestsHit.isEmpty()) {
				for (Chest c : chestsHit) {
					GUI.Frame.appendText("chest");
					for (Key k : invertory.getKeys()) {
						GUI.Frame.appendText("chest and key");
						if (c.open(k)) {
							GUI.Frame.appendText("OPened chest");
							break;
						}
					}

				}
			}
			if (doorHit != null) {
				doorHit.setOpen(!doorHit.isOpen());
			}

		}

		if (input.mouseButtons[Input.RIGHTCLICK]) {
			// attack
			if (!enemiesToAttack.isEmpty()) {
				for (Enemy h : enemiesToAttack) {
					this.attackEnemy(h);

				}
			}
		}
		// move
		if (input.moveKeysPressed()) {
			PlayerMove move = PlayerMove.newPlayerMove(input.keys, this);
			Hit moveHit = playerLineCast(Layer.MOVEMENTBLOCKING, move, world.world);
			if (moveHit == null) {
				this.movePlayer(move, world);
			} else {

				move = PlayerMove.newRotationOnly(input.keys, this);
				this.getPlayerMove().execute(move);
			}

		}
	}

	public Player getPlayer() {
		// TODO Auto-generated method stub
		return this;
	}

	public void plusMoney(double d) {
		// TODO Auto-generated method stub
		this.money += d;
	}

	public void minusMoney(double d) {
		// TODO Auto-generated method stub
		this.money -= d;
		if (money < 0) {
			this.money = 0;
		}

	}

	public double getMoney() {
		return money;
	}

	/**
	 * hit cast from player, towards movement direction
	 * @param layer
	 * @param move
	 * @param world
	 * @return
	 */
	private Hit playerLineCast(Layer layer, PlayerMove move, Square[][] world) {
		double x = 0;
		double y = 0;
		double rot = 0;
		if (move.backward)
			y--;
		if (move.right)
			x++;
		if (move.left)
			x--;
		if (move.forward)
			y++;
		rot = globalRad(new Location(x, y));

		double lineRot = rot + move.rotation;

		return Raycast2D.hitCast(this.getExactLocation(), lineRot, 1, 0.1, world, layer);
	}

	/**
	 * calcualtes the angle relative to 360
	 * @param loc
	 * @return
	 */
	private double globalRad(Location loc) {
		double x = loc.x;
		double y = loc.y;
		double rot = 0;
		if (x != 0 && y != 0) {
			rot = Math.toRadians(90) - Math.atan2(y, x);
		}

		if (x == 0) {
			if (y > 0) {
				rot = 0;
			} else {
				rot += Math.toRadians(180 * y);
			}
		}
		if (y == 0) {
			rot += Math.toRadians(90 * -x);
		}

		if (x < 0 && y < 0)
			rot += 180;
		if (x < 0 && y > 0)
			rot += 270;
		if (x > 0 && y < 0)
			rot += 90;

		if (y == 0 && x == 0) {
			rot = 0;
		}
		return rot;
	}

	public double getFacingDirectionRadian() {
		return (playerMove.rotation - (Math.toRadians(360) * ((int) (playerMove.rotation / Math.toRadians(360)))));
	}

	public double getFacingDirectionDegree() {
		return Math.toDegrees(
				playerMove.rotation - (Math.toRadians(360) * ((int) (playerMove.rotation / Math.toRadians(360)))));
	}

	public void attackEnemy(Enemy e) {
		e.takeDamage(statemanager.getAttackDamage());
	}

	
	public void useWeapon(Weapon w) {
		this.statemanager.setAttackRange(w.getRange());
		this.statemanager.setAttackDamage(w.getDamage());
		weapon = w;
	}

	/**
	 * pick up item, 
	 * @param i
	 */
	public void pick(Item i) {
		if (i instanceof Coin) {
			Coin c = (Coin) i;

			this.plusMoney(c.getValue());
			GUI.Frame.appendText("+$" + c.getValue() + "\n");
			c.destroy();
		} else if (i instanceof HealthPotion) {
			HealthPotion h = (HealthPotion) i;
			if (this.statemanager.getCurrentHealth() < this.statemanager.getMaxHealth()) {
				this.statemanager.addHealth(h.getHealthRestored());
			}
			GUI.Frame.appendText("Health restored +" + h.getHealthRestored());
			h.destroy();
		} else if (i instanceof Fuel) {
			useFuel((Fuel) i);
			i.destroy();
		} else if (this.invertory.hasEmptySpace()) {
			if (i.getSquare() == null)
				return;
			this.invertory.addItem(i);
			i.getSquare().removeGameObject(i);
			i.destroy();
			GUI.Frame.appendText("This ItemObject is in your bag!");
		} else {
			GUI.Frame.appendText("Your bag is full!");
		}

	}

	
	public String drop(Item i) {
		if (this.invertory.containsItem(i)) {
			this.invertory.removeItem(i);
			return "This ItemObject remove from your bag!";
		}
		return "You dont have this ItemObject!";
	}

	public String sellToVendor(Item i, Vendor v) {

		this.invertory.removeItem(i);
		this.statemanager.addMoney(i.getSellValue());
		return "You sold " + i.toString() + "!";
	}

	public Statemanager getStatemanager() {
		return statemanager;
	}

	public void setStatemanager(Statemanager statemanager) {
		this.statemanager = statemanager;
	}

	public void movePlayer(PlayerMove move, World world) {
		// TODO Auto-generated method stub
		Location newLoc = playerMove.execute(move);
		moveTo(newLoc, world.world[(int) newLoc.x][(int) newLoc.y]);
	}

	public PlayerMove getPlayerMove() {
		return playerMove;
	}

	@Override
	protected void setLayer() {
		// TODO Auto-generated method stub
		layer = Layer.PLAYER;

	}

	@Override
	protected void setHitBoxAndRadius() {
		// TODO Auto-generated method stub
		hitBoxRadius = 0.2;
	}

	public double getRotation() {
		// TODO Auto-generated method stub
		return playerMove.rotation;
	}

	public boolean isInRangeOfChest() {
		return isInRangeOfChest;
	}

	public boolean isInRangeOfVendor() {
		return isInRangeOfVendor;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void useFuel(Fuel fuel) {
		fuelGage.useFuel(fuel);

	}

}
