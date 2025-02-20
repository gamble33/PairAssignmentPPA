import java.util.List;

/**
 * A simple model of a clownfish.
 * Clownfish age, move, breed, and die.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Clownfish extends Animal {
    // Characteristics shared by all clownfish (class variables).
    // The age at which a clownfish can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a clownfish can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a clownfish breeding.
    private static final double BREEDING_PROBABILITY = 0.22;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;

    /**
     * Create a new clownfish. A clownfish may be created with age
     * zero (a newborn) or with a random age.
     *
     * @param randomAge If true, the clownfish will have a random age.
     * @param location  The location within the field.
     */
    public Clownfish(Boolean randomAge, Location location) {
        super(randomAge, MAX_AGE, location);
        foodSources.add(Phytoplankton.class);
        foodValue = 30;
        breedingAge = BREEDING_AGE;
        breedingProbability = BREEDING_PROBABILITY;
        maxLitterSize = MAX_LITTER_SIZE;

        foodLevel = rand.nextInt(10);
    }

    /**
     * This is what the clownfish does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     *
     * @param currentField   The field occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);
        if (isAlive()) {
            List<Location> freeLocations =
                    nextFieldState.getFreeAdjacentLocations(getLocation());
            if (!freeLocations.isEmpty()) {
                giveBirth(nextFieldState, freeLocations);
            } else {
                // Over-crowding.
                setDead();
                return;
            }
            if (foodLevel < 15) {
                if (checkForFoodAdjacent(nextFieldState)) return;
            }
            moveRandomly(nextFieldState);
        }
    }

    @Override
    public String toString() {
        return "Clownfish{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }

}
