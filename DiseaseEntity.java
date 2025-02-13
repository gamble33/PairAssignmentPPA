/// DiseaseEntity is a class which represents a water-borne disease particle which has not found
/// a host, that is, it has not infected an animal yet. DiseaseEntity is a moving dot within the
/// simulation. The Disease class represents the pathogen which has a host and is not autonomous
/// within the simulation.
public class DiseaseEntity extends LivingEntity {
    private final static int WATER_BORNE_DISEASE_LIFE_SPAN = 15;

    /// The disease with which the victims will be infected.
    private final Disease disease;

    public DiseaseEntity(Location location, Disease disease) {
        super(false, WATER_BORNE_DISEASE_LIFE_SPAN, location);
        this.disease = disease;
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);
    }

    public void duplicate(Field currentField, Field nextFieldState){

    }

}
