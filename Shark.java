import java.util.List;

/**
 * A simple model of a shark.
 * Foxes age, move, eat clownfishs, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Shark extends GenderedAnimal
{
    // Characteristics shared by all sharks (class variables).
    // The age at which a shark can start to breed.
    private static final int BREEDING_AGE = 2;
    // The age to which a shark can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a shark breeding.
    private static final double BREEDING_PROBABILITY = 0.08;
    // The likelihood of a shark doing nothing during the day.
    private static final double IDLE_PROBABILITY = 0.1d;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;

    
    // Individual characteristics (instance fields).

    /**
     * Create a shark. A shark can be created as a newborn (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the shark will have random age and hunger level.
     * @param location The location within the field.
     */
    public Shark(Boolean randomAge, Location location)
    {
        super(randomAge, MAX_AGE, location);

        foodSources.add(Clownfish.class);
        breedingAge = BREEDING_AGE;
        breedingProbability = BREEDING_PROBABILITY;
        maxLitterSize = MAX_LITTER_SIZE;
        foodLevel = rand.nextInt(30);
    }
    
    /**
     * This is what the shark does most of the time: it hunts for
     * clownfishes. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState, WorldState worldState)
    {
        super.act(currentField, nextFieldState, worldState);
        if(isAlive()) {

            // If it is day, there is a good chance the shark will sleep and do nothing for a
            // given iteration.
            if (
                    worldState.getTimeOfDay() == TimeOfDay.Day
                    && rand.nextDouble() < IDLE_PROBABILITY
            ) {
                remainInSameLocation(nextFieldState);
                return;
            }


            List<Location> freeLocations =
                    nextFieldState.getFreeAdjacentLocations(getLocation());

            if (freeLocations.isEmpty()) {
                // Overcrowding.
                setDead();
                return;
            }

            // If not too hungry, shark will search for mate. otherwise, it will search for food.
            checkForMate(currentField, nextFieldState);
            if (age >= breedingAge && foodLevel > 15) findMateWithinRadius(nextFieldState, 15f);
            else {
                findFoodWithinRadius(nextFieldState, 10f);
            }

        }
    }

    @Override
    public String toString() {
        return "Fox{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                ", foodLevel=" + foodLevel +
                '}';
    }
}
