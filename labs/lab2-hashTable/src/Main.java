import model.HashTable;

public class Main {
    public static void main(String[] args) {
        HashTable<String> hashTable = new HashTable<>(107);
        System.out.println("ADDING");
        hashTable.add("a");
        assert hashTable.add("a");
        System.out.println(hashTable.add("a"));
        assert hashTable.add("a");
        System.out.println("FINISHED ADDING\n");

        System.out.println("CONTAINS");
        System.out.println(hashTable.contains("Hello"));
        System.out.println(hashTable.contains("World"));
        System.out.println(hashTable.contains("Hello World"));
        System.out.println("FINISHED CONTAINS\n");

        System.out.println("REMOVE");
        System.out.println(hashTable.remove("Hello"));
        System.out.println(hashTable.remove("World"));
        System.out.println(hashTable.remove("Hello World"));
        System.out.println(hashTable.remove("Hello"));
        System.out.println("FINISHED REMOVE\n");

        hashTable.add("Hello");
        hashTable.add("World");

        System.out.println("POSITION");
        System.out.printf("Hello: %s%n", hashTable.getPosition("Hello"));
        System.out.printf("World: %s%n", hashTable.getPosition("World"));
        System.out.printf("Hello World: %s%n", hashTable.getPosition("Hello World"));
        System.out.println("FINISHED POSITION\n");

        System.out.println("PRINTING");
        System.out.println(hashTable);
        System.out.println("FINISHED PRINTING");
    }
}