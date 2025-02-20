import java.util.Random;

public class FishPox extends Disease {
    private final Random random = Randomizer.getRandom();

    @Override
    public void applySymptoms() {
         super.applySymptoms();
         if (host == null) return;
         host.maxAge /= 2;
    }

    @Override
    protected boolean checkCanBeCured() {
        return true;
    }
}
