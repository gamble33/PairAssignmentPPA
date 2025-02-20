import java.util.List;
import java.util.Random;

/**
 * A simple model of a whale.
 * Foxes age, move, eat clownfish and turtles, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Whale extends GenderedAnimal
{
    // Characteristics shared by all whalees (class variables).
    // The age at which a whale can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a whale can live.
    private static final int MAX_AGE = 150;
    // The likelihood of a whale breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    /**
     * Create a whale. A whale can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the whale will have random age and hunger level.
     * @param location The location within the field.
     */
    public Whale(Boolean randomAge, Location location)
    {
        super(randomAge, MAX_AGE, location);

        foodSources.add(Clownfish.class);
        foodSources.add(Turtle.class);
        breedingAge = BREEDING_AGE;
        breedingProbability = BREEDING_PROBABILITY;
        maxLitterSize = MAX_LITTER_SIZE;
        foodLevel = rand.nextInt(10);
    }
    
    /**
     * This is what the whale does most of the time: it hunts for
     * clownfish and turtles. In the process, it might breed, die of hunger,
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
            if(! freeLocations.isEmpty()) {
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
        return "Whale{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + foodLevel +
                '}';
    }
}
