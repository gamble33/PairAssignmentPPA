import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

/**
 * Common elements of seaweed and rabbits.
 *
 * @author George Novachuk and Ansh Thukral
 * @version 7.0
 */
public abstract class Plant extends LivingEntity {
    /// The food level required for a plant to grow.
    protected int minFoodForGrowth = 6;
    /// The chance that a plant will successfully grow on a given iteration.
    protected double probabilityForGrowth = 0.10;
    /// The amount of food points gained per step of simulation due to photosynthesis.
    protected int photoSynthesisEffectiveness = 3;
    /// The amount of food produced is multiplied by this scalar.
    private float effectivenessMultiplier = 1f;

    public Plant(Boolean randomAge, int maxAge, Location location) {
        super(randomAge, maxAge, location);
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);
        photosynthesize(worldState);

        // Plants don't move.
        if (isAlive()) nextFieldState.placeEntity(this, getLocation());

        List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
        if (!freeLocations.isEmpty()) {
             if (foodLevel >= minFoodForGrowth) grow(nextFieldState, freeLocations);
        }

    }

    private void photosynthesize(WorldState worldState) {
        // Plant photosynthesis effectiveness varies depending on the weather.
        switch (worldState.getWeatherState()) {
            case Sunny -> effectivenessMultiplier = 1f;
            case Heatwave -> effectivenessMultiplier = 0.3f;
            case Thunderstorm -> effectivenessMultiplier = 1.5f;
        }

        // If there is sun, the plant will gain food via photosynthesis
        if (worldState.getTimeOfDay() == TimeOfDay.Day) {
            foodLevel += (int) (photoSynthesisEffectiveness * effectivenessMultiplier);
        }
    }

    private void grow(Field nextFieldState, List<Location> freeLocations) {
        if (rand.nextDouble() > probabilityForGrowth) return;
        int births = 1;

        // Giving birth takes 50% of the food of the plant.
         foodLevel -= (int) (minFoodForGrowth * 0.50);

        Class<?> runtimeClass = this.getClass();
        for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
            Location loc = freeLocations.removeFirst();

            try {
                Plant seedling = (Plant) runtimeClass.getConstructor(Boolean.class, Location.class).newInstance(false, loc);
                nextFieldState.placeEntity(seedling, loc);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                // This shouldn't happen.
                System.err.println("Error giving birth with finding the right constructor.");
                System.exit(1);
            }
        }
    }
}

