package shared.weapons;


/**
 * @author hlf764
 * The abstract class for all weapons in the game.
 */
abstract class Weapon {
	
	float  	damage 			= 1.0f;		// per bullet
	float 	weight 			= 20.0f;	// grams
	String 	name			= "None";	// name of the weapon
	
	/**
	 * Constructor of the weapon class
	 * @param _damage	Damage of the weapon
	 * @param _weight	Weight of the weapon
	 * @param _name		Name of the weapon
	 */
	public Weapon(float _damage, float _weight, String _name) {
		setDamage(_damage);
		setWeight(_weight);
		setName(_name);
	}
	
	public float getDamage() {
		return this.damage;
	}
	
	public void setDamage(float newDamage) {
		if (newDamage > 0 && newDamage < 100.0f) {
			this.damage = newDamage;
		}
	}
	
	public float getWeight() {
		return this.weight;
	}
	
	
	public void setWeight(float newWeight) {
		if (newWeight > 0 && newWeight < 1000.0f) {
			this.weight = newWeight;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	
}