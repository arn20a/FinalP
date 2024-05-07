
/*************************************************************************
 *  Aimen Nesru
 *  SE2
 *  Brent Reeves
 *  SchubsL.java
 * 
 *  Compilation: javac SchubsL.java
 *  Execution: java SchubsL uncompressed-file 
 *  
 *  
 *  Compresses files using LZW compression algorithm
 *  
 *  To run with maven run : java -cp target/classes SchubsL uncompressed-file
 * 
 *  Algorithm Theory: LZW (Lempel-Ziv-Welch) is a dictionary-based compression
 *  algorithm. It works by encoding sequences of symbols into a dictionary of
 *  variable-length codes. Initially, the dictionary contains single-character
 *  codes for all possible input symbols. As the algorithm progresses, it
 *  dynamically adds longer sequences of symbols to the dictionary, replacing
 *  them with shorter codes. This process effectively compresses the data by
 *  replacing repetitive sequences with shorter codes.
 *  Trade-offs: LZW compression is efficient for compressing text and other
 *  data with repetitive patterns. However, it requires maintaining a dictionary
 *  during both compression and decompression, which can increase memory usage
 *  and processing time. Additionally, LZW is patented in some jurisdictions,
 *  which may limit its use in certain contexts.

 *************************************************************************/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.HashMap;

public class SchubsL {
    private static final int R = 256;
    private static final int L = 4096;
    private static final int W = 500;

    public static void compress(String inputFile, String outputFile) throws FileNotFoundException {
        File file = new File(inputFile);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + inputFile);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
                DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(outputFile))) {

            int character;
            StringBuilder input = new StringBuilder();
            while ((character = reader.read()) != -1) {
                input.append((char) character);
            }

            if (input.length() == 0) {
                outputStream.writeUTF("");
                return;
            }

            HashMap<String, Integer> st = new HashMap<>();
            for (int i = 0; i < R; i++) {
                st.put("" + (char) i, i);
            }
            int code = R + 1;

            StringBuilder compressedData = new StringBuilder();
            String current = "";
            for (int i = 0; i < input.length(); i++) {
                char nextChar = input.charAt(i);
                String combined = current + nextChar;
                if (st.containsKey(combined)) {
                    current = combined;
                } else {
                    compressedData.append(st.get(current)).append(" ");
                    if (code < L) {
                        st.put(combined, code++);
                    }
                    current = "" + nextChar;
                }
            }
            if (!current.equals("")) {
                compressedData.append(st.get(current));
            }

            outputStream.writeUTF(compressedData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Invalid argument");
        }
        for (String filename : args) {
            String outputFileName = filename + ".ll";
            try {
                compress(filename, outputFileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
