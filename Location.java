/**
 * Represent a location in a rectangular grid.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 7.0
 */
public record Location(int row, int col) {

    /**
     * Calculates the next location from the current location toward a specified goal.
     * The direction to the goal is normalized, its components are rounded up,
     * and the resulting direction is used to determine the next location.
     *
     * @param goal the target location to which the direction is calculated
     * @return a new Location that represents the next position toward the goal
     */
    public Location getLocationToGoal(Location goal) {
        Direction directionToGoal = getDirection(goal).normalised().ceil();
        return addDirection(directionToGoal);
    }

    /**
     * Determines whether moving to a considered location is helpful in progressing toward a goal location.
     * Specifically, it evaluates the angle between the direction to the considered location and
     * the direction to the goal location, returning true if the angle represents a meaningful progression.
     *
     * @param consideredLocation the location being evaluated as the next possible move
     * @param goal the target location that is the ultimate destination
     * @return true if the considered location direction is within 90 degrees of the goal direction, false otherwise
     */
    public boolean isNextLocationHelpful(Location consideredLocation, Location goal) {
        Direction dirToConsidered = getDirection(consideredLocation);
        Direction dirToGoal = getDirection(goal);
        double angleBetween = dirToConsidered.angleBetween(dirToGoal);
        return angleBetween < Math.PI / 2
                || 360 - angleBetween < Math.PI / 2;
    }

    public Direction getDirection(Location to) {
        return new Direction(
                to.row - row,
                to.col - col
        );
    }

    private Location addDirection(Direction direction) {
        return new Location(
                row + (int) direction.x(),
                col + (int) direction.y()
        );
    }

}