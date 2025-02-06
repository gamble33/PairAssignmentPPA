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
        entityWeights.add(new EntityWeight(1, (loc) -> new Phytoplankton(true, loc)));
        entityWeights.add(new EntityWeight(0, (loc) -> new Shark(true, loc)));
        entityWeights.add(new EntityWeight(0, (loc) -> new Clownfish(true, loc)));

        // Case for nothing being created, leaving the cell unoccupied.
        entityWeights.add(new EntityWeight(50, (_) -> null));
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
}
