import java.util.Random;

public abstract class LivingEntity {
    /// A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();
    /// Whether the living entity is alive or not.
    private boolean alive;
    /// The living entity's position.
    private Location location;

    protected int age = 0;
    protected int maxAge;

    /// This is the number why which food level increases when this animal is eaten by another.
    /// The food value of a single rabbit. In effect, this is the
    /// number of steps a fox can go before it has to eat again.
    protected int foodValue;
    protected int foodLevel = 5;

    public LivingEntity(Boolean randomAge, int maxAge, Location location) {
        this.alive = true;
        this.location = location;
        this.maxAge = maxAge;

        if (randomAge) age = rand.nextInt(maxAge);
    }

    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        incrementAge();
        incrementHunger();
    }

    public void increaseFoodLevel(int amount) {
        foodLevel += amount;
        if (foodLevel < 0) {
            foodLevel = 0;
            setDead();
        }
    }

    public void decreaseFoodLevel(int amount) {
        foodLevel -= amount;
        if (foodLevel < 0) {
            foodLevel = 0;
            setDead();
        }
    }

    public int getFoodValue() {
        return foodValue;
    }

    /**
     * Check whether the living entity is alive or not.
     * @return true if the living entity is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the living entity is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }

    /**
     * Return the living entity's location.
     * @return The living entity's location.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Set the living entity's location.
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }

    private void incrementAge() {
        if (age++ > maxAge) {
            setDead();
        }
    }

    private void incrementHunger() {
        foodLevel--;
        if (foodLevel < 0) {
            setDead();
        }
    }
}
