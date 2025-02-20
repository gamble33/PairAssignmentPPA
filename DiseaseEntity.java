import java.util.List;
import java.util.function.Supplier;

/// DiseaseEntity is a class which represents a water-borne disease particle which has not found
/// a host, that is, it has not infected an animal yet. DiseaseEntity is a moving dot within the
/// simulation. The Disease class represents the pathogen which has a host and is not autonomous
/// within the simulation.
public class DiseaseEntity extends LivingEntity {
    private final static int WATER_BORNE_DISEASE_LIFE_SPAN = 15;

    /// The disease with which the victims will be infected.
    private final Supplier<Disease> diseaseSupplier;

    public DiseaseEntity(Location location, Supplier<Disease> diseaseSupplier) {
        super(false, WATER_BORNE_DISEASE_LIFE_SPAN, location);
        this.diseaseSupplier = diseaseSupplier;
        maxAge = 10;
        foodLevel = Integer.MAX_VALUE; // Diseases should not die from lack of food.
        age = 0;
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);
        List<Location> adjacent = currentField.getAdjacentLocations(getLocation());
        for (Location location : adjacent) {
            if (currentField.getLivingEntity(location) instanceof Animal) {
                Animal animal = (Animal) currentField.getLivingEntity(location);
                animal.infect(diseaseSupplier.get());
                duplicate(currentField, nextFieldState);
            }
        }
    }

    public void duplicate(Field currentField, Field nextFieldState){
        nextFieldState.placeEntity(this, getLocation());
        List<Location> adjacent = currentField.getFreeAdjacentLocations(getLocation());
        if (adjacent.isEmpty()) return;

        Location spawnLocation = adjacent.removeFirst();
        nextFieldState.placeEntity(new DiseaseEntity(spawnLocation, diseaseSupplier), spawnLocation);
    }

}
