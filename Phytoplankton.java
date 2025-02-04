public class Phytoplankton extends Plant {

    /**
     * Constructor for objects of class Animal.
     *
     * @param location The animal's location.
     */
    public Phytoplankton(Location location) {
        super(location);
        foodValue = 9;
        maxAge = 300;
    }

    @Override
    public void act(Field currentField, Field nextFieldState) {
        super.act(currentField, nextFieldState);

    }
}
