package WorldEditor.GameObjects;

public class FuelGage {

	private double max = 100000;
	private double min = 10000;
	private double current = max;
	private Player player;
	private long lastTick = 0;
	private long cooldown = 100;
	private double tickAmount = 500;

	public FuelGage(Player player) {
		// TODO Auto-generated constructor stub
		this.player = player;
	}

	public void drain() {
		long time = System.currentTimeMillis();
		if (time > lastTick + cooldown) {
			current -= tickAmount;
			if (current < min) {
				current = min;
			}
			if (current > max) {
				current = max;
			}
			lastTick = time;
		}

	}

	public void useFuel(Fuel fuel) {
		current += fuel.getFuelAmount();
		if (current < min) {
			current = min;
		}
		if (current > max) {
			current = max;
		}

	}

	public double getCurrent() {
		return current;
	}
}
