public abstract class Disease extends LivingEntity {


    public Disease(Boolean randomAge, int maxAge, Location location) {
        super(randomAge, maxAge, location);
    }

    @Override
    public void act(Field currentField, Field nextFieldState, WorldState worldState) {
        super.act(currentField, nextFieldState, worldState);
    }

    public void duplicate(Field currentField, Field nextFieldState){

    }

}
