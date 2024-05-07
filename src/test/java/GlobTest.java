
/*************************************************************************
 *  Aimen Nesru
 *  SE2
 *  Brent Reeves
 *  GlobTest.java
 * 
 *  
 *  Tests the different compressions with Glob files
 *  
 *  
 *  To run with maven run : mvn test
 *************************************************************************/
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.beans.Transient;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GlobTest {
    @Test
    public void checkHuffmanGlob() throws IOException {
        System.out.println("Testing globs with Huffman");
        String[] args = new String[] { "src/GlobTests/globtest3.txt", "src/GlobTests/globtest1.txt",
                "src/GlobTests/globtest2.txt" };
        SchubsH.main(args);
        assertTrue(new File("src/GlobTests/globtest3.txt.hh").exists());
        assertTrue(new File("src/GlobTests/globtest1.txt.hh").exists());
        assertTrue(new File("src/GlobTests/globtest2.txt.hh").exists());
    }

    @Test
    public void checkLZWGlob() throws IOException {
        System.out.println("Testing globs with LZW");
        String[] args = new String[] { "src/GlobTests/globtest3.txt", "src/GlobTests/globtest1.txt",
                "src/GlobTests/globtest2.txt" };
        SchubsL.main(args);
        assertTrue(new File("src/GlobTests/globtest3.txt.ll").exists());
        assertTrue(new File("src/GlobTests/globtest1.txt.ll").exists());
        assertTrue(new File("src/GlobTests/globtest2.txt.ll").exists());
    }

    @Test
    public void checkTarsGlob() throws IOException {
        System.out.println("Testing globs with tars");
        String[] args = new String[] { "src/GlobTests/TarsGlob", "src/GlobTests/globtest3.txt",
                "src/GlobTests/globtest1.txt",
                "src/GlobTests/globtest2.txt" };
        SchubsArc.main(args);
        assertTrue(new File("src/GlobTests/TarsGlob/TarsGlob.zh").exists());
    }
}
