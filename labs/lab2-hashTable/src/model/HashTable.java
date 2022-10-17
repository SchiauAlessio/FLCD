package model;

import java.util.ArrayList;
import java.util.List;

public class HashTable<T> {
    private int size;
    private List<List<T>> buckets;

    private double loadFactor = 0.75;

    private int addedCount = 0;

    public HashTable(int size) {
        this.size = size;
        this.buckets = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.buckets.add(new ArrayList<>());
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * computes the hash code of a given key
     * @param key the key to be hashed
     * @return the hash code of the key calculated as the absolute value of the remainder of the division of
     * the key's hash code by the size of the table
     */
    public int hash(T key) {
        return Math.abs(key.hashCode() % size);
    }

    /**
     * Adds a key to the symbol table. If the number of added elements exceeds the load factor, the table is resized
     * @param key the key to be added
     * @return true if the key was added and false if it already exists
     */
    public boolean add(T key) {
        int index = hash(key);
        List<T> bucket = buckets.get(index);
        if (bucket.contains(key)) {
            return false;
        }
        bucket.add(key);
        addedCount++;

        if (addedCount > size * loadFactor) {
            this.resize();
        }
        return true;
    }

    /**
     * checks if a key is present in the symbol table
     * @param key the key that is being searched
     * @return true if the key is present and false if it is not
     */
    public boolean contains(T key) {
        int index = hash(key);
        return buckets.get(index).contains(key);
    }

    /**
     * removes a key from the symbol table
     * @param key the key that is being removed
     * @return true if the key was removed and false if it was not present
     */
    public boolean remove(T key) {
        int index = hash(key);
        List<T> bucket = buckets.get(index);
        return bucket.remove(key);
    }

    /**
     * gets the position of a key in the symbol table
     * @param key the key that is being searched
     * @return the position of the key in the symbol table represented as a pair of integers (positions)
     * the first position is the bucket index and the second position is the position in the bucket
     */
    public Pair<Integer,Integer> getPosition(T key) {
        int index = hash(key);
        List<T> bucket = buckets.get(index);
        if (bucket.contains(key)) {
            return new Pair<>(index, bucket.indexOf(key));
        }
        return new Pair<>(-1, -1);
    }

    /**
     * resizes the symbol table by creating a new table having as size the smallest prime number greater than
     * double the current size and rehashes all the elements
     */
    private void resize() {
        List<List<T>> oldBuckets = buckets;
        this.size = Utils.getNextPrimeStartingFromDouble(size);
        this.buckets = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            this.buckets.add(new ArrayList<>());
        }
        for (List<T> bucket : oldBuckets) {
            for (T key : bucket) {
                this.add(key);
            }
        }
    }

    @Override
    public String toString() {
        return "HashTable{" +
                "size=" + size +
                ", buckets=" + buckets +
                '}';
    }
}
