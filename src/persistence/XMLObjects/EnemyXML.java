package persistence.XMLObjects;

import WorldEditor.GameObjects.Boss;
import WorldEditor.GameObjects.Enemy;

public class EnemyXML {
	public LocationXML location;
	public boolean isBoss;
	
	public static EnemyXML toXML(Enemy enemy) {
		EnemyXML enemyXML = new EnemyXML();
		enemyXML.location = LocationXML.toXML(enemy.getExactLocation());
		if(enemy instanceof Boss) {
			enemyXML.isBoss = true;
		}
		return enemyXML;
	}
}
