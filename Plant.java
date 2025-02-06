import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Common elements of foxes and rabbits.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Plant extends LivingEntity {
    /// The food level required for a plant to grow.
    protected int minFoodForGrowth = 6;

    public Plant(Boolean randomAge, int maxAge, Location location) {
        super(randomAge, maxAge, location);
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);
        photosynthesize(worldState);

        List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
        if (!freeLocations.isEmpty()) {
            // if (foodLevel >= minFoodForGrowth) grow(nextFieldState, freeLocations);
        }

    }

    private void photosynthesize(WorldState worldState) {
        // If there is sun, the plant will gain food via photosynthesis
        if (worldState.getTimeOfDay() == TimeOfDay.Day) {
            foodLevel += 3;
        }
    }

    private void grow(Field nextFieldState, List<Location> freeLocations) {
        int births = 1;

        // Giving birth takes 75% of the food of the plant.
        // foodLevel -= (int) (minFoodForGrowth * 0.75);

        Class<?> runtimeClass = this.getClass();
        for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
            Location loc = freeLocations.removeFirst();

            try {
                Plant seedling = (Plant) runtimeClass.getConstructor(Boolean.class, Location.class).newInstance(false, loc);
                nextFieldState.placeEntity(seedling, loc);
                System.out.println("Plant grown!");
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                // This shouldn't happen.
                System.err.println("Error giving birth with finding the right constructor.");
                System.exit(1);
            }
        }
    }
}

