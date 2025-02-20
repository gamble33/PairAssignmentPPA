import java.util.List;

public abstract class GenderedAnimal extends Animal {
    private final boolean isMale;
    private static int totalMated = 0;


    /**
     * Constructor for objects of class Animal.
     *
     * @param location The animal's location.
     */
    public GenderedAnimal(Boolean randomAge, int maxAge, Location location) {
        super(randomAge, maxAge, location);
        this.isMale = rand.nextBoolean();
    }

    protected void findMateWithinRadius(Field field, float radius) {
        findWithinRadius(
                field,
                radius,
                () -> findClosestMate(field, radius),
                (location) -> doesLocationContainMate(field, location),
                (location) -> mate(field, location)
        );
    }

    protected void checkForMate(Field currentField, Field nextFieldState) {
        // Only females give birth.
        if (isMale) return;

        // Don't attempt to mate if there is no space to give birth to children.
        List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
        if (freeLocations.isEmpty()) return;

        List<Location> locations = currentField.getLocationsWithinRadius(getLocation(), 5);
        for (Location location : locations) {
            if (mate(nextFieldState, location)) break;
        }
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

    private boolean doesLocationContainMate(Field field, Location location) {
        LivingEntity livingEntity = field.getLivingEntity(location);
        if (livingEntity == null) return false;
        if (!this.getClass().equals(livingEntity.getClass())) return false;
        GenderedAnimal mate = (GenderedAnimal) livingEntity;
        return livingEntity.isAlive()
                && mate.isMale != this.isMale
                && mate.age >= breedingAge;
    }

    /**
     * Mates with another animal of the same species located at mateLocation
     * @param nextField The next field state.
     * @param mateLocation The location containing the mating animal.
     * @return True if a baby was born, false otherwise.
     */
    private boolean mate(Field nextField, Location mateLocation) {
        // Only females give birth.
        if (isMale) return false;

        Class<?> runtimeClass = this.getClass();
        if (runtimeClass.isInstance(nextField.getLivingEntity(mateLocation))) {
            GenderedAnimal genderedAnimal = (GenderedAnimal) nextField.getLivingEntity(mateLocation);
            // If animals are opposite gender, reproduction occurs.
            if (genderedAnimal.isMale != this.isMale) {
                // Give birth if there is space.
                giveBirth(nextField, nextField.getFreeAdjacentLocations(getLocation()));
                totalMated++;

                // Infect mate with all infections.
                if (isInfected()) {
                    getInfections().forEach(genderedAnimal::infect);
                }

                if (genderedAnimal.isInfected()) {
                    genderedAnimal.getInfections().forEach(this::infect);
                }

                return true;
            }
        }
        return false;
    }
}
