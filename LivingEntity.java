public abstract class LivingEntity {
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

    public LivingEntity(Location location) {
        this.alive = true;
        this.location = location;
    }

    /**
     * Act.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    public void act(Field currentField, Field nextFieldState) {
        incrementAge();
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

}
