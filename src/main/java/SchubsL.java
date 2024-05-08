import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;

public class SchubsL {
    private static final int R = 256; // number of input chars
    private static final int L = 4096; // number of codewords = 2^W
    private static final int W = 12; // codeword width

    public static void compress(String inputFile) throws IOException {
        if (!fileExists(inputFile)) {
            throw new FileNotFoundException("File not found: " + inputFile);
        }

        String input = readFile(inputFile);
        String outputFile = inputFile + ".ll";
        TST<Integer> st = new TST<>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R + 1; // R is codeword for EOF

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            BinaryOut binaryOut = new BinaryOut(fos);
            while (input.length() > 0) {
                String s = st.longestPrefixOf(input); // Find max prefix match s.
                binaryOut.write(st.get(s), W); // Print s's encoding.
                int t = s.length();
                if (t < input.length() && code < L) // Add s to symbol table.
                    st.put(input.substring(0, t + 1), code++);
                input = input.substring(t); // Scan past s in input.
            }
            binaryOut.write(R, W);
            binaryOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Usage: java SchubsL <filename.txt>");
        }
        for (int i = 0; i < args.length; i++) {
            String inputFile = args[i];
            try {
                compress(inputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Utility method to read file content into a string
    private static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    // Utility method to check if file exists
    private static boolean fileExists(String filename) {
        return new File(filename).exists();
    }
}
