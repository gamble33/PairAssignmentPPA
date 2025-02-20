public class Seaweed extends Plant {
    private static final int MAX_AGE = 350;

    public Seaweed(Boolean randomAge, Location location) {
        super(randomAge, MAX_AGE, location);
        foodValue = 38;
        foodLevel = rand.nextInt(9);
        minFoodForGrowth = 200;
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);

    }
}
