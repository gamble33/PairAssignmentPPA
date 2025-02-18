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

    public double dot(Direction other) {
        return x * other.x + y * other.y;
    }

    public double angleBetween(Direction other) {
        return Math.acos(dot(other) / magnitude() * other.magnitude());
    }
}
