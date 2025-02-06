public class Phytoplankton extends Plant {
    private static final int MAX_AGE = 600;

    /**
     * Constructor for objects of class Animal.
     *
     * @param location The animal's location.
     */
    public Phytoplankton(Boolean randomAge, Location location) {
        super(randomAge, MAX_AGE, location);
        foodValue = 9;
        foodLevel = rand.nextInt(100);
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);

    }
}
