import java.util.Random;

public class FishPox extends Disease {
    private static final float PROBABILITY_TO_DECREASE_FOOD_LEVEL = 0.3f;
    private static int MAX_AGE = 15;
    private final Random random = Randomizer.getRandom();
    private int age = 0;

    @Override
    public void applySymptoms() {
         super.applySymptoms();
         age++;
         if (random.nextFloat() < PROBABILITY_TO_DECREASE_FOOD_LEVEL)
             host.decreaseFoodLevel(1);
         if (checkCanBeCured()) {
             onCure();
         }
    }

    @Override
    protected boolean checkCanBeCured() {
        return age >= MAX_AGE;
    }
}
