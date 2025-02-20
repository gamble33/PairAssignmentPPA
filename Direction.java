/**
 * Represents a direction from one location to another as a 2-dimensional vector.
 */
public record Direction(double x, double y) {
    public Direction ceil() {
        return new Direction(
            Math.ceil(x),
            Math.ceil(y)
        );
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Direction normalised() {
        return this.divide(magnitude());
    }

    public Direction divide(double scalar) {
        return new Direction(x / scalar, y / scalar);
    }

    /**
     * Calculates the dot product of this direction with another direction.
     * The dot product represents the scalar product of the two vectors.
     *
     * @param other the other direction to calculate the dot product with
     * @return the dot product of this direction and the specified direction
     */
    public double dot(Direction other) {
        return x * other.x + y * other.y;
    }

    /**
     * Calculates the angle in radians between this direction and another direction.
     * The angle is determined using the dot product and magnitudes of the vectors
     * and represents the smallest angle between them, ranging from 0 to π.
     *
     * @param other the other direction to calculate the angle with
     * @return the angle in radians between this direction and the specified direction
     */
    public double angleBetween(Direction other) {
        return Math.acos(dot(other) / magnitude() * other.magnitude());
    }
}
