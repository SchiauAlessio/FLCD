package tests;

import exceptions.ScannerException;
import model.MyScanner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MyScannerTests {

    @Test
    public void testP1(){
        try {
            Scanner programReader = new java.util.Scanner(new File("src\\tests\\test_resources\\p1.alx"));
            StringBuilder program = new StringBuilder();
            while (programReader.hasNextLine()) {
                program.append(programReader.nextLine()).append('\n');
            }
            programReader.close();
            Scanner tokensReader = new java.util.Scanner(new File("src\\tests\\test_resources\\token.in"));
            List<String> tokens = new ArrayList<>();
            while (tokensReader.hasNextLine()) {
                tokens.add(tokensReader.nextLine());
            }
            MyScanner scanner = new MyScanner(program.toString(), tokens);
            try {
                scanner.scan();
            } catch (ScannerException e) {
                System.out.println(e.getMessage());
                fail();
            }
            assertTrue(true);
        } catch (FileNotFoundException e) {
            System.out.println("Source file not found");
        }
    }

    @Test
    public void testP2(){
        try {
            Scanner programReader = new java.util.Scanner(new File("src\\tests\\test_resources\\p2.alx"));
            StringBuilder program = new StringBuilder();
            while (programReader.hasNextLine()) {
                program.append(programReader.nextLine()).append('\n');
            }
            programReader.close();
            Scanner tokensReader = new java.util.Scanner(new File("src\\tests\\test_resources\\token.in"));
            List<String> tokens = new ArrayList<>();
            while (tokensReader.hasNextLine()) {
                tokens.add(tokensReader.nextLine());
            }
            MyScanner scanner = new MyScanner(program.toString(), tokens);
            try {
                scanner.scan();
            } catch (ScannerException e) {
                System.out.println(e.getMessage());
                fail();
            }
            assertTrue(true);
        } catch (FileNotFoundException e) {
            System.out.println("Source file not found");
        }
    }

    @Test
    public void testP3(){
        try {
            Scanner programReader = new java.util.Scanner(new File("src\\tests\\test_resources\\p3.alx"));
            StringBuilder program = new StringBuilder();
            while (programReader.hasNextLine()) {
                program.append(programReader.nextLine()).append('\n');
            }
            programReader.close();
            Scanner tokensReader = new java.util.Scanner(new File("src\\tests\\test_resources\\token.in"));
            List<String> tokens = new ArrayList<>();
            while (tokensReader.hasNextLine()) {
                tokens.add(tokensReader.nextLine());
            }
            MyScanner scanner = new MyScanner(program.toString(), tokens);
            try {
                scanner.scan();
            } catch (ScannerException e) {
                System.out.println(e.getMessage());
                fail();
            }
            assertTrue(true);
        } catch (FileNotFoundException e) {
            System.out.println("Source file not found");
        }
    }

    @Test
    public void testP1errToken(){
        try {
            Scanner programReader = new java.util.Scanner(new File("src\\tests\\test_resources\\p1err.alx"));
            StringBuilder program = new StringBuilder();
            while (programReader.hasNextLine()) {
                program.append(programReader.nextLine()).append('\n');
            }
            programReader.close();
            Scanner tokensReader = new java.util.Scanner(new File("src\\tests\\test_resources\\token.in"));
            List<String> tokens = new ArrayList<>();
            while (tokensReader.hasNextLine()) {
                tokens.add(tokensReader.nextLine());
            }
            MyScanner scanner = new MyScanner(program.toString(), tokens);
            try {
                scanner.scan();
            } catch (ScannerException e) {
                System.out.println(e.getMessage());
                assertTrue(e.getMessage().contains("Cannot classify token 1nr"));
                return;
            }
            fail();
        } catch (FileNotFoundException e) {
            System.out.println("Source file not found");
        }
    }

    @Test
    public void testP1errString(){
        try {
            Scanner programReader = new java.util.Scanner(new File("src\\tests\\test_resources\\p1errString.alx"));
            StringBuilder program = new StringBuilder();
            while (programReader.hasNextLine()) {
                program.append(programReader.nextLine()).append('\n');
            }
            programReader.close();
            Scanner tokensReader = new java.util.Scanner(new File("src\\tests\\test_resources\\token.in"));
            List<String> tokens = new ArrayList<>();
            while (tokensReader.hasNextLine()) {
                tokens.add(tokensReader.nextLine());
            }
            MyScanner scanner = new MyScanner(program.toString(), tokens);
            try {
                scanner.scan();
            } catch (ScannerException e) {
                System.out.println(e.getMessage());
                assertTrue(e.getMessage().contains("String not closed on line"));
                return;
            }
            fail();
        } catch (FileNotFoundException e) {
            System.out.println("Source file not found");
        }
    }
}
