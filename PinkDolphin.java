import java.util.List;

public class PinkDolphin extends GenderedAnimal
{
    // The age at which a pink dolphin can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a pink dolphin can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a pink dolphin breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;

    private static final int RABBIT_FOOD_VALUE = 9;


    // Individual characteristics (instance fields).

    /**
     * Create a pink dolphin. A pink dolphin can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the pink dolphin will have random age and hunger level.
     * @param location The location within the field.
     */
    public PinkDolphin(Boolean randomAge, Location location)
    {
        super(randomAge, MAX_AGE, location);

        foodSources.add(Clownfish.class);
        breedingAge = BREEDING_AGE;
        breedingProbability = BREEDING_PROBABILITY;
        maxLitterSize = MAX_LITTER_SIZE;
        foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
    }
    
    /**
     * This is what the pink dolphin does most of the time: it hunts for
     * clown fish. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, WorldState worldState)
    {
        super.act(currentField, nextFieldState, worldState);
        if(isAlive()) {
            List<Location> freeLocations =
                    nextFieldState.getFreeAdjacentLocations(getLocation());
            if(!freeLocations.isEmpty()) {
                checkForMate(currentField, nextFieldState);
            }
            // Move towards a source of food if found.
            Location nextLocation = findFood(currentField);
            if(nextLocation == null && ! freeLocations.isEmpty()) {
                // No food found - try to move to a free location.
                nextLocation = freeLocations.remove(0);
            }
            // See if it was possible to move.
            if(nextLocation != null) {
                setLocation(nextLocation);
                nextFieldState.placeEntity(this, nextLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }



    @Override
    public String toString() {
        return "PinkDolphin{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + foodLevel +
                '}';
    }
}
