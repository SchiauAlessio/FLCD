package model;

public record Transition(String from, String to, String label) {
    @Override
    public String toString() {
        return from + " -> " + to + " : " + label;
    }
}
