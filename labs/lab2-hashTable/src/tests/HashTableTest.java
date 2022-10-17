package tests;
import model.HashTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {

    @Test
    public void testAdd() {
        HashTable<String> table = new HashTable<>(10);
        assertTrue(table.add("Hello"));
        assertTrue(table.add("World"));
        assertFalse(table.add("Hello"));
    }

    @Test
    public void testContains() {
        HashTable<String> table = new HashTable<>(10);
        table.add("Hello");
        table.add("World");
        assertTrue(table.contains("Hello"));
        assertTrue(table.contains("World"));
        assertFalse(table.contains("Hello World"));
    }

    @Test
    public void testRemove() {
        HashTable<String> table = new HashTable<>(10);
        table.add("Hello");
        table.add("World");
        assertTrue(table.remove("Hello"));
        assertTrue(table.remove("World"));
        assertFalse(table.remove("Hello World"));
    }

    @Test
    public void testGetPosition() {
        HashTable<String> table = new HashTable<>(10);
        table.add("Hello");
        table.add("World");
        assertTrue(table.getPosition("Hello").getKey() != -1);
        assertTrue(table.getPosition("Hello").getValue() != -1);

        assertTrue(table.getPosition("World").getKey() != -1);
        assertTrue(table.getPosition("World").getValue() != -1);

        assertEquals(-1, (int) table.getPosition("Hello World").getKey());
        assertEquals(-1, (int) table.getPosition("Hello World").getValue());
    }

    @Test
    public void testResize(){
        HashTable<String> table = new HashTable<>(10);
        table.add("a");
        table.add("b");
        table.add("c");
        table.add("d");
        table.add("e");
        table.add("f");
        table.add("g");
        assertEquals(10, table.getSize());

        table.add("h");
        assertEquals(23, table.getSize());

        assertTrue(table.contains("a"));
        assertTrue(table.contains("b"));
        assertTrue(table.contains("c"));
        assertTrue(table.contains("d"));
        assertTrue(table.contains("e"));
        assertTrue(table.contains("f"));
        assertTrue(table.contains("g"));
        assertTrue(table.contains("h"));

        assertFalse(table.contains("i"));
    }
}
