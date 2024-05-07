import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.beans.Transient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeschubsTest {
    @Test
    public void decompressHuffman() throws IOException {
        System.out.println("Testing Decompress huffman");
        String archiveName = "src/DeschubsTests/DeschubsH.txt.hh";
        File file = new File("src/DeschubsTests/DeschubsH.txt");
        Path filePath = Paths.get("src/DeschubsTests/DeschubsH.txt");

        Deschubs.main(new String[] { archiveName });
        assertTrue(file.exists());

        String originalFile = "src/DeschubsTests/DeschubsHCopy.txt";
        Path originalPath = Paths.get(originalFile);
        assertEquals(Files.readString(originalPath), Files.readString(filePath));

        if (Files.readString(originalPath).equals(Files.readString(filePath))) {
            System.out.println("    Untars and Decompress");
        }
    }

    @Test
    public void decompressLZW() throws IOException {

        try {
            String testFileName = "src/TestingDeschubs/testFile.txt";
            String testFileContent = "Testing decompress LZw";
            createTestFile(testFileName, testFileContent);
            String compressedFileName = "src/TestingDeschubs/testFile.ll";
            SchubsL.compress(testFileName, compressedFileName);
            Deschubs.expand(compressedFileName);
            String decompressedContent = readTextFile(testFileName.substring(0, testFileName.length() - 3));
            assertEquals(testFileContent, decompressedContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTestFile(String fileName, String content) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write(content);
        writer.close();
    }

    private String readTextFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    @Test
    public void decompressTars() throws IOException {
        System.out.println("Testing Tars decompression");
        String archiveName = "src/DeschubsTests/DeschubsTests.zh";
        File file = new File("src/DeschubsTests/DeschubsTars.txt");
        File file2 = new File("src/DeschubsTests/DeschubsTars1.txt");
        Path filePath = Paths.get("src/DeschubsTests/DeschubsTars.txt");
        Path filePath2 = Paths.get("src/DeschubsTests/DeschubsTars1.txt");

        Deschubs.main(new String[] { archiveName });
        assertTrue(file2.exists());
        assertTrue(file.exists());

        String originalFile = "src/DeschubsTests/DeschubsTars1Copy.txt";
        Path originalPath = Paths.get(originalFile);
        String originalFile2 = "src/DeschubsTests/DeschubsTarsCopy.txt";
        Path originalPath2 = Paths.get(originalFile2);
        assertEquals(Files.readString(originalPath), Files.readString(filePath2));
        assertEquals(Files.readString(originalPath2), Files.readString(filePath));

        if (Files.readString(originalPath).equals(Files.readString(filePath2))) {
            if (Files.readString(originalPath2).equals(Files.readString(filePath))) {
                System.out.println("    Untars and Decompress successful");
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfArguments() throws IOException {
        System.out.println("Testing wrong number of Args");
        String[] args = {};
        Deschubs.main(args);
    }
}