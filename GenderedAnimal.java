import java.util.List;

public abstract class GenderedAnimal extends Animal {
    private final boolean isMale;
    private static int totalMated = 0;


    /**
     * Constructor for objects of class Animal.
     *
     * @param location The animal's location.
     */
    public GenderedAnimal(Location location) {
        super(location);
        this.isMale = rand.nextBoolean();
    }

    protected void checkForMate(Field currentField, Field nextFieldState) {
        // Only females give birth.
        if (isMale) return;

        List<Location> locations = nextFieldState.getAdjacentLocations(getLocation());
        Class<?> runtimeClass = this.getClass();
        for (Location location : locations) {
            if (runtimeClass.isInstance(nextFieldState.getLivingEntity(location))) {
                GenderedAnimal genderedAnimal = (GenderedAnimal) nextFieldState.getLivingEntity(location);
                // If animals are opposite gender, reproduction occurs.
                if (genderedAnimal.isMale != this.isMale) {
                    // Give birth.
                    System.out.println("MATED: " + ++totalMated);
                    giveBirth(nextFieldState, locations);
                    break;
                }
            }
        }
    }
}
