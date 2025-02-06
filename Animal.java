import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Common elements of all animals.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Animal extends LivingEntity
{
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();

    protected int breedingAge;
    protected double breedingProbability;
    protected int maxLitterSize;
    protected List<Class<?>> foodSources;

    /**
     * Constructor for objects of class Animal.
     * @param location The animal's location.
     */
    public Animal(Boolean randomAge, int maxAge, Location location)
    {
        super(randomAge, maxAge, location);
        this.foodSources = new ArrayList<>();
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);
    }

    /**
     * Look for food sources adjacent to the current location.
     * Only the first live food source is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            LivingEntity livingEntity = field.getLivingEntity(loc);
            for (Class<?> type : foodSources) {
                if(type.isInstance(livingEntity)) {
                    if(livingEntity.isAlive()) {
                        livingEntity.setDead();
                        foodLevel = livingEntity.getFoodValue();
                        foodLocation = loc;
                    }
                }
            }

        }
        return foodLocation;
    }

    /**
     * Check whether this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = generateNumberOfBirths();
        Class<?> runtimeClass = this.getClass();
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.removeFirst();

                try {
                    Animal baby = (Animal) runtimeClass.getConstructor(Boolean.class, Location.class).newInstance(false, loc);
                    nextFieldState.placeEntity(baby, loc);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                         InvocationTargetException e) {
                        // This shouldn't happen.
                        System.err.println("Error giving birth with finding the right constructor.");
                        System.exit(1);
                }
            }
        }
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= breedingAge;
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int generateNumberOfBirths()
    {
        int births;
        if(canBreed() && rand.nextDouble() <= breedingProbability) {
            births = rand.nextInt(maxLitterSize) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

}
