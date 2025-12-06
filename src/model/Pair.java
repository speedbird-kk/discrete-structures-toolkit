package model;

public record Pair<A, B>(A a, B b) {
    @Override
    public final String toString() {
        return "(" + a + ", " + b + ")";
    }
}
