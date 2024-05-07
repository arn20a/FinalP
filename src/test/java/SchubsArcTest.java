
/*************************************************************************
 *  Aimen Nesru
 *  SE2
 *  Brent Reeves
 *  SchubsArcTest.java
 * 
 *  
 *  Tests the schubsArc file
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

public class SchubsArcTest {

    @Test
    public void testEmptyFilesToTar() throws IOException {
        System.out.println("Testing Tar with empty file");
        String filename = "src/SchubsArcTest/empty.txt";
        String archiveName = "src/SchubsArcTest";
        File file = new File(filename);

        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("    File created");
                } else {
                    throw new RuntimeException("    Failed to create file");
                }
            } else {
                System.out.println("    File exists");
            }
        } catch (IOException e) {
            throw new RuntimeException("    Error creating file: " + e.getMessage());
        }

        if (file.length() == 0) {
            System.out.println("    File is empty");
            SchubsArc.main(new String[] { archiveName, filename });
        } else {
            System.out.println("    File is not empty");
            SchubsArc.main(new String[] { archiveName, filename });
        }

        String archiveResults = "src/SchubsArcTest/SchubsArcTest.zh";
        File compressedFile = new File(archiveResults);
        assertTrue(compressedFile.exists());
        if (compressedFile.exists()) {
            System.out.println("    Archive Test should pass");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonExistentFilesToTar() throws IOException {
        System.out.println("Testing Tar with File that doesnt exist");
        String filename = "src/nonExistent.txt";
        String archiveName = "src/SchubsArcTest";
        SchubsArc.main(new String[] { archiveName, filename });
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("    File confirmed does not exist - Exception should be thrown");
        }
    }

    @Test
    public void testDestinationArchiveExists() throws IOException {
        System.out.println("Testing if the destination archive exists");
        String filename = "src/SchubsArcTest/blee.txt";
        String archiveName = "src/SchubsArcTest";
        File file = new File(archiveName + File.separator + "SchubsArcTest.zh");
        SchubsArc.main(new String[] { archiveName, filename });
        assertTrue(file.exists());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDirectoryAsFileToTar() throws IOException {
        System.out.println("Testing directory as File");
        String filename = "src/SchubsArcTest";
        File folder = new File(filename);
        String archiveName = "src";
        if (folder.isDirectory()) {
            System.out.println("    File is a directory - Exception thrown");
        } else {
            System.out.println("    File is not a directory");
        }
        SchubsArc.main(new String[] { archiveName, filename });
    }

    @Test
    public void testFilesWithManyCharacters() throws IOException {
        System.out.println("Testing Multicharacter files");
        String filename = "src/SchubsArcTest/blue.txt";
        String archiveName = "src/SchubsArcTest";

        SchubsArc.main(new String[] { archiveName, filename });

        String archiveResults = "src/SchubsArcTest/SchubsArcTest.zh";
        File compressedFile = new File(archiveResults);
        assertTrue(compressedFile.exists());
        if (compressedFile.exists()) {
            System.out.println("    Archive Test SHOULD pass");
        }
    }

    @Test
    public void testFilesWithSpacesAndLineEndings() throws IOException {
        System.out.println("Testing files with spacings and Line endings");
        String filename = "src/SchubsArcTest/blue.txt";
        String archiveName = "src/SchubsArcTest";

        SchubsArc.main(new String[] { archiveName, filename });

        String archiveResults = "src/SchubsArcTest/SchubsArcTest.zh";
        File compressedFile = new File(archiveResults);
        assertTrue(compressedFile.exists());
        if (compressedFile.exists()) {
            System.out.println("    Archive Test SHOULD pass");
        }
    }

    @Test
    public void testCombinationOfFiles() throws IOException {
        System.out.println("Testing commbinations of files");
        String filename1 = "src/SchubsArcTest/blah.txt";
        String filename2 = "src/SchubsArcTest/blue.txt";
        String filename3 = "src/SchubsArcTest/blee.txt";
        String archiveName = "src/SchubsArcTest";

        SchubsArc.main(new String[] { archiveName, filename1, filename2, filename3 });

        String archiveResults = "src/SchubsArcTest/SchubsArcTest.zh";
        File compressedFile = new File(archiveResults);
        assertTrue(compressedFile.exists());
        if (compressedFile.exists()) {
            System.out.println("    Archive Test SHOULD pass");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfArguments() throws IOException {
        System.out.println("Testing Wrong number of arguments");
        String[] args = { "src/SchubsArcTest" };
        SchubsArc.main(args);

    }
}
