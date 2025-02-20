import java.util.*;
import java.util.spi.LocaleServiceProvider;

/**
 * A simple predator-prey simulator, based on a rectangular field containing 
 * rabbits and foxes.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.1
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 240;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 160;
    private static final double DISEASE_SPAWN_PROBABILITY = 0.1f;
    private static final Random rand = Randomizer.getRandom();

    private final WorldState worldState;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private final SimulatorView view;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        field = new Field(depth, width);
        view = new SimulatorView(depth, width);

        // Initialise world state.
        worldState = new WorldState();
        worldState.setTimeOfDay(TimeOfDay.Day);
        worldState.setWeatherState(WeatherState.Sunny);

        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long 
     * period (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(700);
    }
    
    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        reportStats();
//        for(int n = 1; n <= numSteps && field.isViable(); n++) {
        for(int n = 1; n <= numSteps; n++) {
            simulateOneStep();
            delay(50);         // adjust this to change execution speed
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        // Use a separate Field to store the starting state of
        // the next step.
        Field nextFieldState = new Field(field.getDepth(), field.getWidth());
        adjustTimeOfDay();
        Animal.resetInfectedCount();

        // Call every entity's act method.
        List<LivingEntity> entities = field.getEntities();
        for (LivingEntity anEntity : entities) {
            anEntity.act(field, nextFieldState, worldState);
        }

        attemptSpawnDisease(nextFieldState);
        
        // Replace the old state with the new one.
        field = nextFieldState;

        // reportStats();
        view.showStatus(step, field, worldState);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        populate();
        view.showStatus(step, field, worldState);
    }

    private void attemptSpawnDisease(Field nextFieldState) {
        if (rand.nextDouble() > DISEASE_SPAWN_PROBABILITY) return;

        int randomRow = rand.nextInt(field.getDepth());
        int randomCol = rand.nextInt(field.getWidth());
        Location location = new Location(randomRow, randomCol);
        if (nextFieldState.getLivingEntity(location) == null) {
            nextFieldState.placeEntity(new DiseaseEntity(location, Chlamydia::new), location);
        }
    }

    private void adjustTimeOfDay() {
        if (worldState.getTimeOfDay() == TimeOfDay.Night) {
            if (worldState.getTime() >= WorldState.NIGHT_LENGTH){
                System.out.println("It turned day.");
                worldState.setTimeOfDay(TimeOfDay.Day);
                worldState.setTime(0);

                // On the dawn of every day, the weather may change.
                // Most days will be sunny days.
                worldState.setRandomWeather();
            }
        } else if (worldState.getTimeOfDay() == TimeOfDay.Day) {
            if (worldState.getTime() >= WorldState.DAY_LENGTH) {
                System.out.println("It turned night.");
                worldState.setTimeOfDay(TimeOfDay.Night);
                worldState.setTime(0);
            }
        }
        worldState.incrementTime();
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate()
    {
        EntitySpawner entitySpawner = new EntitySpawner();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                LivingEntity entity = entitySpawner.spawn(location);
                if (entity == null) continue;
                field.placeEntity(entity, location);
            }
        }
    }

    /**
     * Report on the number of each type of animal in the field.
     */
    public void reportStats()
    {
        //System.out.print("Step: " + step + " ");
        field.fieldStats();
    }
    
    /**
     * Pause for a given time.
     * @param milliseconds The time to pause for, in milliseconds
     */
    private void delay(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch(InterruptedException e) {
            // ignore
        }
    }
}
