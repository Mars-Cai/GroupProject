package WorldEditor;

import java.util.Random;

public class Rand {
	public final Long seed;
	private final Random generator;

	public Rand(Long seed) {
		System.currentTimeMillis();
		generator = new Random(seed);
		this.seed = seed;
	}

	public int ranIntSeed(int max, int min) {
		if (max == min) {
			return max;
		}
		if (max < 1) {
			return (int) ranDoubleSeed(0.1, min);
		}
		return (int) (generator.nextInt(max - min)) + min;

	}

	public int ranIntSeed(float max, float min) {
		return (int) ((int) (generator.nextInt((int) (max - min))) + min);

	}

	public int ranIntSeed(double max, double min) {
		return (int) ((int) (generator.nextInt((int) (max - min))) + min);

	}

	public float ranFloatSeed(float max, float min) {
		return (generator.nextFloat() * (max - min)) + min;

	}

	public double ranDoubleSeed(double max, double min) {
		return (generator.nextDouble() * (max - min)) + min;

	}

	// static versions
	public static int ranInt(int max, int min) {
		return (int) (Math.random() * (max - min)) + min;

	}

	public static int ranInt(float max, float min) {
		return (int) ((int) (Math.random() * (max - min)) + min);

	}

	public static int ranInt(double max, double min) {
		return (int) ((int) (Math.random() * (max - min)) + min);

	}

	public static float ranFloat(float max, float min) {
		return (float) ((Math.random() * (max - min)) + min);

	}

	public static double ranDouble(double max, double min) {
		return (Math.random() * (max - min)) + min;

	}

}
