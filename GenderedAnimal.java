import java.util.List;

public abstract class GenderedAnimal extends Animal {
    private boolean isMale;
    private Class<?> mateType;


    /**
     * Constructor for objects of class Animal.
     *
     * @param location The animal's location.
     */
    public GenderedAnimal(Location location, Class<?> mateType) {
        super(location);
        this.isMale = rand.nextBoolean();
        this.mateType = mateType;
    }

    protected void checkForMate(Field currentField, Field nextFieldState) {
        // Only females give birth.
        if (isMale) return;

        List<Location> locations = nextFieldState.getAdjacentLocations(getLocation());
        for (Location location : locations) {
            if (mateType.isInstance(nextFieldState.getLivingEntity(location))) {
                GenderedAnimal genderedAnimal = (GenderedAnimal) nextFieldState.getLivingEntity(location);
                // If animals are opposite gender, reproduction occurs.
                if (genderedAnimal.isMale != this.isMale) {
                    // Give birth.
                    System.out.println("MATED");
                    giveBirth(nextFieldState, locations, mateType);
                    break;
                }
            }
        }
    }
}
