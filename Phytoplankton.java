public class Phytoplankton extends Plant {
    private static final int MAX_AGE = 350;

    /**
     * Constructs a new Phytoplankton entity with the specified properties.
     *
     * @param randomAge if true, assigns a random age to the Phytoplankton; otherwise, it starts at age 0
     * @param location the initial location of the Phytoplankton in the simulation
     */
    public Phytoplankton(Boolean randomAge, Location location) {
        super(randomAge, MAX_AGE, location);
        foodValue = 30;
        foodLevel = rand.nextInt(36);
        minFoodForGrowth = 100;
        photoSynthesisEffectiveness = 10;
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);

    }
}
