
/*************************************************************************
* Aimen Nesru
* SE2
* Brent Reeves
* DeschubsTest.java
*
*
* Tests the uncompressing of tars, Huffman, and LZW files
*
*
* To run with maven run : mvn test
*************************************************************************/
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
import java.nio.file.StandardOpenOption;

public class DeschubsTest {
        @Test
        public void decompressHuffman() throws IOException {
                System.out.println("Testing Decompress huffman");
                String archiveName = "src" + File.separator + "DeschubsTests" +
                                File.separator + "DeschubsH.txt.hh";
                File file = new File("src" + File.separator + "DeschubsTests" +
                                File.separator + "DeschubsH.txt");
                Path filePath = Paths.get("src" + File.separator + "DeschubsTests" +
                                File.separator + "DeschubsH.txt");

                Deschubs.main(new String[] { archiveName });
                assertTrue(file.exists());

                String originalFile = "src" + File.separator + "DeschubsTests" +
                                File.separator + "DeschubsHCopy.txt";
                Path originalPath = Paths.get(originalFile);
                assertEquals(Files.readString(originalPath), Files.readString(filePath));

                if (Files.readString(originalPath).equals(Files.readString(filePath))) {
                        System.out.println(" Untars and Decompress");
                }
                file.delete();
        }

        @Test
        public void decompressLZW() throws IOException {

                try {
                        String testFileName = "LZWtestFile.txt";
                        String testFileContent = "Testing decompress LZw";
                        createFileWithContent(testFileName, testFileContent);
                        String compressedFileName = "LZWtestFile.ll";
                        SchubsL.compress(testFileName);
                        Deschubs.expand(compressedFileName);
                        String decompressedContent = readTextFile(testFileName.substring(0,
                                        testFileName.length() - 3));
                        assertEquals(testFileContent, decompressedContent);
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

        private void createFileWithContent(String fileName, String content) throws IOException {
                Files.write(Paths.get(fileName), content.getBytes(), StandardOpenOption.CREATE);
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
                String archiveName = "src" + File.separator + "DeschubsTests" +
                                File.separator + "DeschubsTests.zh";
                String file = "DeschubsTars.txt";
                String file2 = "DeschubsTars1.txt";

                createFileWithContent(file, "Tars test 1");
                createFileWithContent(file2, "Tars test 2");
                assertTrue(Files.exists(Paths.get(file)));
                assertTrue(Files.exists(Paths.get(file2)));

                String originalFile = "src" + File.separator + "DeschubsTests" +
                                File.separator + "DeschubsTars1Copy.txt";
                Path originalPath = Paths.get(originalFile);
                String originalFile2 = "src" + File.separator + "DeschubsTests" +
                                File.separator + "DeschubsTarsCopy.txt";
                Path originalPath2 = Paths.get(originalFile2);
                Files.deleteIfExists(Paths.get(file));
                Files.deleteIfExists(Paths.get(file2));

        }

        @Test(expected = IllegalArgumentException.class)
        public void testWrongNumberOfArguments() throws IOException {
                System.out.println("Testing wrong number of Args");
                String[] args = {};
                Deschubs.main(args);
        }

}
