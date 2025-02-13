/// This class represents a disease which has a host and is not autonomous within the simulation.
/// The DiseaseEntity class is used to represent an autonomous water-borne disease with no host.
public abstract class Disease {
    protected Animal host;

    public Disease(Animal host) {
        this.host = host;
    }

    public void setHost(Animal host) {
        this.host = host;
    }

    /**
     * Triggers and applies the symptoms associated with the disease.
     * This method is intended to simulate the effects of the disease on its host. This method is
     * called every step of the simulation.
     */
    public abstract void applySymptoms();

    /**
     * This method is called immediately prior to the eradication of the disease. Serves as a way
     * to benefit the animal once it is cured.
     */
    public void onCure() {
        host.increaseFoodLevel(5);
    }

    /**
     * This method
     * @return True -- if the disease has met the conditions to leave the host and be eradicated.
     */
    protected abstract boolean checkCanBeCured();
}
