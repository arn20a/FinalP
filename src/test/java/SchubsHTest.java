
/*************************************************************************
 *  Aimen Nesru
 *  SE2
 *  Brent Reeves
 *  SchubsHTest.java
 * 
 *  
 *  Tests different cases for compressing a file using huffman compression
 *  
 *  
 *  To run with maven run : mvn test
 *************************************************************************/
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SchubsHTest {

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testEmptyFile() {
        System.out.println("Testing Empty file");
        String filename = "empty.txt";
        File file = new File(filename);
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("    File created successfully");
                } else {
                    throw new RuntimeException("    Failed to create file");
                }
            } else {
                System.out.println("    File already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("    Error creating file: " + e.getMessage());
        }

        if (file.length() == 0) {
            System.out.println("    File is empty");
        } else {
            System.out.println("    File is not empty");
            SchubsH.compress(filename, filename + ".hh");
        }

        File compressedFile = new File(filename + ".hh");
        assertTrue(compressedFile.length() == 0);

        file.delete();
        compressedFile.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingFile() {
        System.out.println("Testing nonexistent file");
        String filename = "nonexistent.txt";
        String[] args = { filename };
        SchubsH.main(args);
    }

    @Test
    public void testContainsManyThings() {
        System.out.println("Testing file with many things");

        String filename = "src" + File.separator + "SchubsHTests" + File.separator + "test1.txt";

        SchubsH.compress(filename, filename + ".hh");

        File compressedFile = new File(filename + ".hh");
        assertTrue(compressedFile.length() > 0);
        if (compressedFile.length() > 0) {
            System.out.println("    File is not empty");
        } else {
            System.out.println("    File is empty");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfArguments() {
        System.out.println("Testing Wrong number of args");
        String[] args = {};
        SchubsH.main(args);

    }

    @Test
    public void testOneReallyLongWordWithNoSpaces() throws IOException {
        System.out.println("Testing file with long word");
        String filename = "src" + File.separator + "SchubsHTests" + File.separator + "test2.txt";
        File file = new File(filename);
        String filename2 = filename + ".hh";
        File file2 = new File(filename2);
        SchubsH.compress(filename, filename2);

        assertTrue(file2.exists());
    }

    @Test
    public void testOnlyLowercaseCharacters() throws IOException {
        System.out.println("Testing file with only lower case");
        String filename = "src" + File.separator + "SchubsHTests" + File.separator + "test3.txt";
        File file = new File(filename);
        String filename2 = filename + ".hh";
        File file2 = new File(filename2);
        SchubsH.compress(filename, filename2);

        assertTrue(file2.exists());
    }

    @Test
    public void testOnlyUppercaseCharacters() throws IOException {
        System.out.println("Testing file with uppercase");
        String filename = "src" + File.separator + "SchubsHTests" + File.separator + "test4.txt";
        File file = new File(filename);
        String filename2 = filename + ".hh";
        File file2 = new File(filename2);
        SchubsH.compress(filename, filename2);

        assertTrue(file2.exists());
    }

}
