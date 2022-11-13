package tests;

import model.FiniteAutomaton;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class FiniteAutomatonTests {

    @Test
    public void test(){
        FiniteAutomaton fa = new FiniteAutomaton("src\\tests\\test_resources\\fa.in");
        assertTrue(fa.checkAccepted("11"));
        assertTrue(fa.checkAccepted("101"));
        assertTrue(fa.checkAccepted("10001"));
        assertFalse(fa.checkAccepted("111"));
        assertFalse(fa.checkAccepted("01"));
        assertFalse(fa.checkAccepted("00"));
    }
}
