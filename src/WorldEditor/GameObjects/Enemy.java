package WorldEditor.GameObjects;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import WorldEditor.Layer;
import WorldEditor.Location;
import WorldEditor.Square;
import WorldEditor.World;
import WorldEditor.Spawners.ItemSpawner;
import gameWorld.Hit;
import gameWorld.Hits;
import gameWorld.Raycast2D;
import gameWorld.Statemanager;
//import gameWorld.StatsManager;
import renderer.Bitmap;
//sad
public abstract class Enemy extends InteractableObject {

	public Statemanager statemanager;
	protected final double aggroRadius = 10;
	protected ItemSpawner itemSpawner;
	protected Player targetObject;
	private long lastAttack = 0;
	private long coolDownTime= 1000;
	private boolean isHit;

	public Enemy() {
		this.statemanager = new Statemanager();
		this.itemSpawner = new ItemSpawner(this);
	}

	public void attack(Player p) {
		p.statemanager.takeDamage(this.statemanager.getAttackDamage());
	}

	public void takeDamage(double attackDamage) {
		isHit = true;
		if (statemanager.takeDamage(attackDamage)) {
			//alive

		} else {
			if (!isDestroyed()) {
				itemSpawner.spawn();
				System.out.println("destoryed");
				destroy();
			}
		}
	}

	public boolean tick(World world) {
		targetObject = world.player;
		double distToTarget = exactLocation.distanceTo(targetObject.getExactLocation());

		if (withinAggroRange(distToTarget) && square.isDiscovered()) {

			this.setHitBoxActive(false);
			Set<Hit> hitObjects = Raycast2D.hitCastAll(exactLocation, targetObject.exactLocation,
					statemanager.getAttackRange(), statemanager.getRunSpeed(), world.world, Layer.MOVEMENTBLOCKING);
			this.setHitBoxActive(true);

			if (withinAttackRange(distToTarget)) {
				long time = System.currentTimeMillis();
			    if (time > lastAttack + coolDownTime) {
			    	attack(targetObject);
			        lastAttack = time;
			    }

			    System.out.println(targetObject.getPlayer().getStatemanager().getCurrentHealth());
			} else {
				// enemy move if only if out of attack range
		    	enemyMoveTurn(world.world, hitObjects);
			}

		}
		// alive
		return true;

	}

	private List<Location> astarPath = new ArrayList<>();
	private Location mustReachLoc;

	protected void enemyMoveTurn(Square[][] world, Set<Hit> hitObjects) {
		Location nextStep = exactLocation;

		// do Astar if cant move towards target
		if (!hitObjects.isEmpty()) {
			if (!astarPath.isEmpty()) {
				nextStep = continueOnPath();
			} else {
				nextStep = doAstar(world);

			}
		} else {// normal move towards target
			List<Location> steps = Raycast2D.interpolateX2D(exactLocation, targetObject.exactLocation, 0.1,
					statemanager.getRunSpeed());
			if (steps.isEmpty()) {
				nextStep = exactLocation;
			} else {
				nextStep = steps.get(0);
			}
		}
		// moveTo(nextStep, world[(int) nextStep.x][(int) nextStep.y]);

		if (nextStep.distanceTo(exactLocation) < 0.001) {
			nextStep = doAstar(world);
		}
		moveTowards(nextStep, world);
	}

	private Location continueOnPath() {
		Location nextStep;
		if (exactLocation.distanceTo(mustReachLoc) < statemanager.getRunSpeed()) {
			nextStep = astarPath.remove(0);
			mustReachLoc = nextStep;
		} else {

			nextStep = mustReachLoc;
		}
		return nextStep;
	}

	private Location doAstar(Square[][] world) {
		Location nextStep = exactLocation;
		astarPath = generateAstarPathToLocation(targetObject.exactLocation, world);
		if (!astarPath.isEmpty()) {
			nextStep = astarPath.remove(0);
			mustReachLoc = nextStep;
		}
		return nextStep;
	}

	/**
	 * Moves this object towards a location based on movespeed, does not do
	 * collision checks
	 */
	public void moveTowards(Location targetLoc, Square[][] world) {
		Location diff = Location.minus(targetLoc, exactLocation);
		Location nextStep = Location.plus(exactLocation, Location.scale(diff.unitVector(), statemanager.getRunSpeed()));
		moveTo(nextStep, world[(int) nextStep.x][(int) nextStep.y]);
	}

	protected boolean withinAggroRange(double distToTarget) {
		return distToTarget < aggroRadius;
	}

	protected boolean withinAttackRange(double distToTarget) {
		return distToTarget < statemanager.getAttackRange();
	}

	public void giveKey(Key key) {
		// TODO Auto-generated method stub
		itemSpawner.forceSpawn(key);
	}

	public boolean isHit() {
		return isHit;
	}

	public void setHit(boolean isHit) {
		this.isHit = isHit;
	}


}
