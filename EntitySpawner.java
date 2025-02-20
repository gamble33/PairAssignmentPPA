import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class EntitySpawner {
    private final Random rand = Randomizer.getRandom();
    private final List<EntityWeight> entityWeights = new ArrayList<>();

    private static class EntityWeight {
        double relativeFrequency;
        Function<Location, LivingEntity> creator;

        public EntityWeight(double weight, Function<Location, LivingEntity> creator) {
            this.relativeFrequency = weight;
            this.creator = creator;
        }
    }


    public EntitySpawner() {
        // Plants
        addSpawnRule(1f, (loc) -> new Phytoplankton(true, loc));
        addSpawnRule(0.25f, (loc) -> new Seaweed(true, loc));

        // Predators
        addSpawnRule(3.9f, (loc) -> new Shark(true, loc));
        addSpawnRule(0.5f, (loc) -> new Whale(true, loc));
        addSpawnRule(0.2f, (loc) -> new PinkDolphin(true, loc));

        // Prey
        addSpawnRule(1, (loc) -> new Clownfish(true, loc));
        addSpawnRule(0.5f, (loc) -> new Turtle(true, loc));

        // Diseases
        addSpawnRule(0.01f, (loc) -> new DiseaseEntity(loc, Chlamydia::new));

        // Case for nothing being created, leaving the cell unoccupied.
        addSpawnRule(50, (loc) -> null);
    }

    public LivingEntity spawn(Location location) {
        double frequencySum = entityWeights.stream().mapToDouble((e) -> e.relativeFrequency).sum();
        double probability = rand.nextDouble() * frequencySum;
        double cumulative = 0.0d;

        for (EntityWeight ew : entityWeights) {
            cumulative += ew.relativeFrequency;
            if (probability < cumulative)
                return ew.creator.apply(location);
        }
        return null;
    }

    private void addSpawnRule(float relativeFrequency, Function<Location, LivingEntity> creator) {
        entityWeights.add(new EntityWeight(relativeFrequency, creator));
    }
}
