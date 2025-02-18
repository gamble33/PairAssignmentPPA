import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Common elements of all animals.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public abstract class Animal extends LivingEntity {
    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();

    protected int breedingAge;
    protected double breedingProbability;
    protected int maxLitterSize;
    protected List<Class<?>> foodSources;
    private final HashMap<Class<?>, Disease> infections = new HashMap<>();

    /**
     * Constructor for objects of class Animal.
     *
     * @param location The animal's location.
     */
    public Animal(Boolean randomAge, int maxAge, Location location) {
        super(randomAge, maxAge, location);
        this.foodSources = new ArrayList<>();
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);
    }

    /**
     * Infects the animal with the given disease. The disease's effects
     * will be applied to the host during each simulation step until it is cured.
     * If the animal is already infected by the type of disease, then the animal is not reinfected and continues
     * as is.
     *
     * @param disease The disease that will infect this animal.
     */
    public void infect(Disease disease) {
        infections.putIfAbsent(disease.getClass(), disease);
    }

    /**
     * Removes the infection from the animal. Stops all symptoms from this disease.
     *
     * @param disease A disease which the animal is currently infected by.
     */
    public void cure(Disease disease) {
        infections.remove(disease.getClass());
    }

    /**
     * Look for food sources adjacent to the current location.
     * Only the first live food source is eaten.
     *
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(Field field) {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while (foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            LivingEntity livingEntity = field.getLivingEntity(loc);
            for (Class<?> type : foodSources) {
                if (type.isInstance(livingEntity)) {
                    if (livingEntity.isAlive()) {
                        livingEntity.setDead();
                        foodLevel = livingEntity.getFoodValue();
                        foodLocation = loc;
                    }
                }
            }

        }
        return foodLocation;
    }

    protected void findFoodWithinRadius(Field field, float radius) {
        findWithinRadius(
                field,
               radius,
                () -> findClosestFoodSource(field, radius),
                (location) -> doesLocationContainFood(field, location),
                (location) -> eatFood(field, location)
        );
    }

    /**
     * Check whether this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     *
     * @param freeLocations The locations that are free in the current field.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations) {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = generateNumberOfBirths();
        Class<?> runtimeClass = this.getClass();
        if (births > 0) {
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

    protected Location getRandomAdjacentLocation(Field field) {
        List<Location> adjacent = field.getFreeAdjacentLocations(getLocation());
        if (adjacent.isEmpty()) return null;
        return adjacent.getFirst();
    }

    protected void moveRandomly(Field field) {
        List<Location> adjacent = field.getFreeAdjacentLocations(getLocation());
        Location nextLocation;
        if (adjacent.isEmpty()) nextLocation = getLocation();
        else nextLocation = adjacent.removeFirst();
        advanceTo(field, nextLocation);
    }

    private void findWithinRadius(Field field, float radius, Supplier<Location> closestLocationSupplier, Function<Location, Boolean> checkContains, Consumer<Location> onFound) {
        Location goal = closestLocationSupplier.get();
        Location nextLocationTowardsGoal;

        // If there is no food nearby, then the animal will move randomly.
        if (goal == null) nextLocationTowardsGoal = getRandomAdjacentLocation(field);
        else {
            nextLocationTowardsGoal = getLocation().getLocationToGoal(goal);

            // If field is not empty, then it will search for suboptimal locations which advance
            // the animal towards food.
            if (field.getLivingEntity(nextLocationTowardsGoal) == null) {

                // Check all nearby locations
                List<Location> adjacent = field.getFreeAdjacentLocations(getLocation());
                for (Location location : adjacent) {
                    if (getLocation().isNextLocationHelpful(location, goal)) {
                        nextLocationTowardsGoal = location;
                        break;
                    }
                }
            }
        }

        // If no advancing location was found, the best option is to stay in the current location.
        if (nextLocationTowardsGoal == null) nextLocationTowardsGoal = getLocation();

        // Check if this location contains the desired entity.
        if (checkContains.apply(nextLocationTowardsGoal)) {
            // Execute the code relating to finding the goal.
            onFound.accept(nextLocationTowardsGoal);
        } else {
            advanceTo(field, nextLocationTowardsGoal);
        }
    }

    private void eatFood(Field field, Location location) {
        LivingEntity food = field.getLivingEntity(location);
        foodLevel = food.getFoodValue();
        food.setDead();
        advanceTo(field, location);
    }

    /**
     * Moves the current entity to the specified location. Only use this if you are sure
     * the location is empty.
     *
     * @param field    The next field state which will contain this entity at the specified location.
     * @param location The *EMPTY* location which the entity will move to in the next simulation state.
     */
    private void advanceTo(Field field, Location location) {
        setLocation(location);
        field.placeEntity(this, location);
    }

    /**
     * Finds the closest food source within a specified radius in the given field.
     *
     * @param field  The field in which to search for food sources.
     * @param radius The radius within which to locate the closest food source.
     * @return The location of the closest food source, or null if no food source is found.
     */
    private Location findClosestFoodSource(Field field, float radius) {
        return findClosest(
                field,
                radius,
                (location) -> doesLocationContainFood(field, location)
        );
    }

    /**
     * Finds the closest mate within a specified radius in the given field.
     *
     * @param field  The field in which to search for food sources.
     * @param radius The radius within which to locate the closest food source.
     * @return The location of the closest mate, or null if no mate is found.
     */
    private Location findClosestMate(Field field, float radius) {
        return findClosest(
                field,
                radius,
                (location) -> doesLocationContainMate(field, location)
        );
    }

    /**
     * Finds the closest location within a specified radius from the current location
     * that satisfies a given condition.
     *
     * @param field               The field in which to search for locations.
     * @param radius              The radius within which to locate the closest location.
     * @param conditionPredicate  A function that determines whether a location satisfies the condition.
     * @return The closest location that satisfies the condition, or null if no such location is found.
     */
    private Location findClosest(Field field, float radius, Function<Location, Boolean> conditionPredicate) {
        List<Location> nearBy = field.getLocationsWithinRadius(getLocation(), radius);
        double minDistance = Float.MAX_VALUE;
        Location closest = null;
        for (Location location : nearBy) {
            if (!conditionPredicate.apply(location)) continue;
            double distance = getLocation().getDirection(location).magnitude();
            if (distance < minDistance) {
                minDistance = distance;
                closest = location;
            }
        }
        return closest;
    }

    private boolean doesLocationContainFood(Field field, Location location) {
        LivingEntity livingEntity = field.getLivingEntity(location);
        if (livingEntity == null) return false;

        return livingEntity.isAlive() && foodSources.contains(livingEntity.getClass());
    }

    private boolean doesLocationContainMate(Field field, Location location) {
        LivingEntity livingEntity = field.getLivingEntity(location);
        if (livingEntity == null) return false;
        return livingEntity.isAlive() && this.getClass().equals(livingEntity.getClass());
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= breedingAge;
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     *
     * @return The number of births (may be zero).
     */
    private int generateNumberOfBirths() {
        int births;
        if (canBreed() && rand.nextDouble() <= breedingProbability) {
            births = rand.nextInt(maxLitterSize) + 1;
        } else {
            births = 0;
        }
        return births;
    }

    private boolean checkAlreadyInfected(Disease disease) {
        // todo.
        return true;
    }

}
