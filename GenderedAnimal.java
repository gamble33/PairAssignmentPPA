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

    protected void checkForMate(Field currentField, Field nextFieldState) {
        // Only females give birth.
        if (isMale) return;

        // Don't attempt to mate if there is no space to give birth to children.
        List<Location> freeLocations = nextFieldState.getFreeAdjacentLocations(getLocation());
        if (freeLocations.isEmpty()) return;

        List<Location> locations = currentField.getLocationsWithinRadius(getLocation(), 5);
        // System.out.println("Locations: " + locations.size());
        Class<?> runtimeClass = this.getClass();
        for (Location location : locations) {
            if (runtimeClass.isInstance(nextFieldState.getLivingEntity(location))) {
                GenderedAnimal genderedAnimal = (GenderedAnimal) nextFieldState.getLivingEntity(location);
                // If animals are opposite gender, reproduction occurs.
                if (genderedAnimal.isMale != this.isMale) {
                    // Give birth if there is space.
                        giveBirth(nextFieldState, freeLocations);
                        System.out.println("Birthed: " + totalMated);
                        totalMated++;
                    break;
                }
            }
        }
    }
}
