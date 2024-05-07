
/*************************************************************************
 *  Aimen Nesru
 *  SE2
 *  Brent Reeves
 *  SchubsArc.java
 * 
 *  Compilation: javac SchubsArc.java 
 *  Execution: java SchubsArc test-file
 *  
 *  produces one archive.zl or .zh containing compressed versions of all the txt files.
 *  
 *  
 *  To run with maven run : java -cp target/classes SchubsArc test-file
 * 
 *  Algorithm Theory: Tars is a file archiving utility that
 *  combines multiple files into a single archive file while optionally compressing
 *  them using algorithms like gzip, bzip2, or xz. The archive file maintains the
 *  directory structure and metadata of the original files, allowing for easy
 *  extraction and restoration. 
 *  
 *  Trade-offs: Tars provides a convenient way to package and compress multiple
 *  files into a single archive, reducing storage space and simplifying file
 *  management. However, compressing files with Tars may incur additional computational
 *  overhead, especially for large archives or when using compression algorithms with
 *  higher compression ratios.
 *************************************************************************/
import java.io.IOException;
import java.io.File;

public class SchubsArc {
    public static void main(String[] args) throws IOException {
        File in1 = null;
        BinaryIn bin1 = null;
        BinaryOut out = null;

        char separator = (char) 255;

        if (args.length < 2) {
            System.out.println("Usage: java SchubsArc <archive-name> <file1> <file2> ...");
            throw new IllegalArgumentException("Incorrect number of arguments.");
        }

        File input = null;
        for (int i = 1; i < args.length; i++) {
            input = new File(args[i]);
            if (!input.exists()) {
                System.out.println("    File " + args[i] + " does not exist.");
                System.out.println("    Please provide a valid file name.");
                throw new IllegalArgumentException("File " + args[i] + " does not exist.");
            }

            if (input.isDirectory()) {
                System.out.println("    File " + args[i] + " is a directory.");
                System.out.println("    SchubsArc is for files only.");
                throw new IllegalArgumentException("File " + args[i] + " is a directory.");
            }
        }

        String[] filePath = args[0].split("[/\\\\]");
        String archiveName = filePath[filePath.length - 1];

        String archivePath = args[0] + File.separator;
        archivePath += archiveName + ".zh";
        out = new BinaryOut(archivePath);

        try {
            for (int i = 1; i < args.length; i++) {
                String fileName = args[i] + ".hh";
                in1 = new File(fileName);
                File argFile = new File(args[i]);
                if (argFile.length() == 0) {
                    System.out.println("    File " + args[i] + " is empty.");
                } else {
                    SchubsH.compress(args[i], args[i] + ".hh");
                    System.out.println("File " + args[i] + " compressed successfully.");
                }
                if (!in1.exists() || !in1.isFile()) {
                    System.out.println("    File " + fileName + " does not exist or is not a regular file.");
                    continue;
                }

                long fileSize = in1.length();
                int fileNameSize = fileName.length();

                out.write(fileNameSize);
                out.write(separator);

                out.write(fileName);
                out.write(separator);

                out.write(fileSize);
                out.write(separator);

                bin1 = new BinaryIn(fileName);
                while (!bin1.isEmpty()) {
                    out.write(bin1.readChar());
                }
                System.out.println(fileName + " archived successfully.");
            }
        } finally {
            File archiveFinal = new File(archivePath);
            if (archiveFinal.exists() || archiveFinal.isFile())
                System.out.println("Archive " + archivePath + " created successfully.");
            if (out != null)
                out.close();
        }
    }
}