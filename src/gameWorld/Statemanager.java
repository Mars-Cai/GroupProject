package gameWorld;

public class Statemanager {

	private int maxHealth;
	private int currentHealth;

	private double attackDamage;
	private double attackRange;
	private double defaultAttackRange;
	private double runSpeed;
	private double defaultAttackDamage;

	private double money;

	public Statemanager() {
		// TODO Auto-generated constructor stub
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		if(currentHealth >maxHealth) {
			currentHealth = maxHealth;
		}
		if(currentHealth == 0) {
			currentHealth = maxHealth;
		}
	}

	public double getCurrentHealth() {
		return currentHealth;
	}

	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
		if(currentHealth >maxHealth) {
			currentHealth = maxHealth;
		}
	}

	public double getAttackDamage() {
		if(attackDamage == 0) {
			return defaultAttackDamage;
		}
		return attackDamage;
	}

	public void setAttackDamage(double attackDamage) {
		this.attackDamage = attackDamage;
	}

	public double getAttackRange() {
		if(attackRange == 0) {
			return defaultAttackRange;
		}
		return attackRange;
	}

	public void setAttackRange(double attackRange) {

		this.attackRange = attackRange;
	}

	public double getDefualtAttackRange() {
		return defaultAttackRange;
	}

	public void setDefualtAttackRange(double defualtAttackRange) {
		this.defaultAttackRange = defualtAttackRange;

	}

	public double getRunSpeed() {
		return runSpeed;
	}

	public void setRunSpeed(double runSpeed) {
		this.runSpeed = runSpeed;
	}

	public double getDefualtAttackDamage() {
		return defaultAttackDamage;
	}

	public void setDefualtAttackDamage(double defualtAttackDamage) {
		this.defaultAttackDamage = defualtAttackDamage;
	}

	public boolean takeDamage(double attackDamage) {
		// TODO Auto-generated method stub

		this.currentHealth -= attackDamage;
		if(currentHealth<0) {
			currentHealth = 0;
		}
		return isAlive();
	}

	public double getMoney() {
		return money;
	}

	public boolean isAlive() {
		return currentHealth > 0;

	}

	public void addMoney(double newMoney) {
		// TODO Auto-generated method stub
		this.money += newMoney;
		if (money < 0)
			money = 0;
	}

	public void addHealth(double healthRestored) {
		// TODO Auto-generated method stub
		currentHealth+=healthRestored;
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
	}

}
