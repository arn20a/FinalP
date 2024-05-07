
/*************************************************************************
 *  Aimen Nesru
 *  SE2
 *  Brent Reeves
 *  SchubsLTest.java
 * 
 *  
 *  Tests different cases using LZW compression
 *  
 *  
 *  To run with maven run : mvn test
 *************************************************************************/
import static org.junit.Assert.*;

import org.junit.Test;
import java.io.*;

public class SchubsLTest {

    @Test(expected = FileNotFoundException.class)
    public void testCompressMissingFile() throws IOException {
        String filename = "missingFile.txt";
        SchubsL.compress(filename, filename + ".lzw");
    }

    @Test
    public void testCompressManyThings() throws IOException {
        String filename = "manyThings.txt";
        createFileWithContents(filename, "This file contains many things!");

        SchubsL.compress(filename, filename + ".lzw");
        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.exists() && compressedFile.length() > 0);

        compressedFile.delete();
        new File(filename).delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfArguments() throws IOException {
        String[] args = {};
        SchubsL.main(args);
    }

    @Test
    public void testCompressLongWord() throws IOException {
        String filename = "longWord.txt";
        createFileWithContents(filename, "supercalifragilisticexpialidocious");
        SchubsL.compress(filename, filename + ".lzw");
        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.exists() && compressedFile.length() > 0);
        compressedFile.delete();
        new File(filename).delete();
    }

    @Test
    public void testCompressEmptyFile() throws IOException {
        String filename = "emptyFile.txt";
        File file = new File(filename);
        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.length() == 0);

        compressedFile.delete();
        new File(filename).delete();
    }

    @Test
    public void testCompressLowercase() throws IOException {
        String filename = "lowercase.txt";
        createFileWithContents(filename, "abcdefghijklmnopqrstuvwxyz");

        SchubsL.compress(filename, filename + ".lzw");

        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.exists() && compressedFile.length() > 0);

        compressedFile.delete();
        new File(filename).delete();
    }

    @Test
    public void testCompressUppercase() throws IOException {
        String filename = "uppercase.txt";
        createFileWithContents(filename, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        SchubsL.compress(filename, filename + ".lzw");

        File compressedFile = new File(filename + ".lzw");
        assertTrue(compressedFile.exists() && compressedFile.length() > 0);

        compressedFile.delete();
        new File(filename).delete();
    }

    private void createFileWithContents(String filename, String contents) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(contents);
        writer.close();
    }
}
