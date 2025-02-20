import java.util.List;

public class Turtle extends Animal
{
    // Characteristics shared by all turtles (class variables).
    // The age at which a turtle can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a turtle can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a turtle breeding.
    private static final double BREEDING_PROBABILITY = 0.12;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;

    /**
     * Create a new turtle. A turtle may be created with age
     * zero (a newborn) or with a random age.
     *
     * @param randomAge If true, the turtle will have a random age.
     * @param location The location within the field.
     */
    public Turtle(Boolean randomAge, Location location)
    {
        super(randomAge, MAX_AGE, location);
        foodSources.add(Seaweed.class);
        foodSources.add(Phytoplankton.class);
        foodValue = 90;
        breedingAge = BREEDING_AGE;
        breedingProbability = BREEDING_PROBABILITY;
        maxLitterSize = MAX_LITTER_SIZE;

        foodLevel = rand.nextInt(15);
    }
    
    /**
     * This is what the turtle does most of the time - it runs
     * around. Sometimes it will breed or die of old age.
     * @param currentField The field occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, WorldState worldState)
    {
        super.act(currentField, nextFieldState, worldState);
        if(isAlive()) {
            List<Location> freeLocations =
                nextFieldState.getFreeAdjacentLocations(getLocation());
            if(!freeLocations.isEmpty()) {
                giveBirth(nextFieldState, freeLocations);
            }

            Location nextLocation = findFood(currentField);
            // Try to move into a free location.
            if (nextLocation == null) {
                if (!freeLocations.isEmpty()) {
                    nextLocation = freeLocations.removeFirst();
                    setLocation(nextLocation);
                    nextFieldState.placeEntity(this, nextLocation);
                } else {
                    // Overcrowding.
                    setDead();
                }
            } else setLocation(nextLocation);

        }
    }

    @Override
    public String toString() {
        return "Turtle{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }

}
