
/*************************************************************************
 *  Aimen Nesru
 *  SE2
 *  Brent Reeves
 *  Deschubs.java
 * 
 *  Uncompresses a file that has been compressed using LZW, Tars, or 
 *  Huffman compression 
 * 
 *  Compilation:  javac Deschubs.java
 *  Execution:    java Deschubs compressed-filename
 
 *  Example: java deschubs blee.txt.hh becomes blee.txt
 *  
 *  To run with maven run : java -cp target/classes Deschubs compressed-filename
 * 
 *   Algorithm Theory: File decompression involves reversing the compression process
 *   to restore the original data. For LZW compression, this  involves 
 *   decoding the compressed data using the dictionary entries. For Huffman compression,
 *   it requires traversing the Huffman tree to map the compressed codes back to their
 *   original symbols. Untars, or extracting files from a Tars archive, involves 
 *   reversing the compression and archiving process to restore the original files.
 *   This typically requires parsing the archive file, decompressing compressed files,
 *   and reconstructing the directory structure and metadata.

 * 
 *   Trade-offs: File decompression typically requires less computational resources than
 *   compression since it involves straightforward decoding rather than complex encoding.
 *   However, the efficiency of decompression may vary depending on the compression 
 *   algorithm used and the structure of the compressed data. Untarring involves less
 *   computational complexity compared to compression since it primarily focuses on
 *   extracting files from the archive and decompressing them. However, the efficiency
 *   of untarring may vary depending on the size and complexity of the archive file and
 *   the compression algorithms used.


 *************************************************************************/
import java.io.*;
import java.util.*;

public class Deschubs {
    private static final int R = 256;
    private static final int L = 4096;
    private static final int W = 500;

    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    public static void decompress(String inputFile, String outputFile) {
        try {
            if (inputFile.endsWith(".hh")) {
                FileInputStream fis = new FileInputStream(inputFile);
                ObjectInputStream ois = new ObjectInputStream(fis);

                Node root = readTrie(ois);

                int length = ois.readInt();

                FileOutputStream fos = new FileOutputStream(outputFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                for (int i = 0; i < length; i++) {
                    Node x = root;
                    while (!x.isLeaf()) {
                        boolean bit = ois.readBoolean();
                        if (bit)
                            x = x.right;
                        else
                            x = x.left;
                    }
                    bos.write(x.ch);
                }
                bos.flush();
                bos.close();
                ois.close();
            } else if (inputFile.endsWith(".ll")) {
                try (DataInputStream inputStream = new DataInputStream(new FileInputStream(inputFile));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

                    String compressedData = inputStream.readUTF();
                    String[] codes = compressedData.split(" ");

                    HashMap<Integer, String> st = new HashMap<>();
                    for (int i = 0; i < R; i++) {
                        st.put(i, "" + (char) i);
                    }
                    int code = R + 1;

                    StringBuilder outputData = new StringBuilder();
                    String current = "" + (char) Integer.parseInt(codes[0]);
                    outputData.append(current);

                    for (int i = 1; i < codes.length; i++) {
                        int nextCode = Integer.parseInt(codes[i]);
                        String entry;
                        if (st.containsKey(nextCode)) {
                            entry = st.get(nextCode);
                        } else if (nextCode == code) {
                            entry = current + current.charAt(0);
                        } else {
                            throw new IllegalStateException("Invalid compressed file");
                        }

                        outputData.append(entry);
                        if (code < L) {
                            st.put(code++, current + entry.charAt(0));
                        }
                        current = entry;
                    }

                    writer.write(outputData.toString());
                }
            } else {
                System.out.println("Unsupported file format: " + inputFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void expand(String filename) throws IOException {
        File file = new File(filename.substring(0, filename.length() - 3));
        System.out.println(filename.substring(0, filename.length() - 3));
        if (file.exists()) {
            System.out.println("File " + filename.substring(0, filename.length() - 3) + " already exists.");
            throw new IOException("File already exists.");
        }
        try {
            System.out.println("New file being created");
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Node root = readTrie(ois);
            int length = ois.readInt();
            FileOutputStream fos = new FileOutputStream(filename.substring(0, filename.length() - 3)); // remove ".hh"
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            for (int i = 0; i < length; i++) {
                Node x = root;
                while (!x.isLeaf()) {
                    boolean bit = ois.readBoolean();
                    if (bit)
                        x = x.right;
                    else
                        x = x.left;
                }
                bos.write(x.ch);
            }
            bos.flush();
            bos.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Node readTrie(ObjectInputStream ois) throws IOException {
        boolean isLeaf = ois.readBoolean();
        if (isLeaf) {
            return new Node(ois.readChar(), -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(ois), readTrie(ois));
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Deschubs <filename>.<extension>");
            throw new IllegalArgumentException("Incorrect number of arguments.");
        }
        String filename = args[0];
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        if (extension.equals("hh")) {
            expand(filename);
        } else if (extension.equals("zh")) {
            BinaryIn in = null;
            BinaryOut out = null;
            char sep = (char) 255;

            try {
                in = new BinaryIn(args[0]);

                while (!in.isEmpty()) {
                    int filenamesize = in.readInt();
                    sep = in.readChar();
                    String filenameUntar = "";
                    for (int i = 0; i < filenamesize; i++)
                        filenameUntar += in.readChar();

                    sep = in.readChar();
                    long filesize = in.readLong();
                    sep = in.readChar();
                    System.out.println("Extracting file: " + filenameUntar + " (" + filesize + ").");
                    out = new BinaryOut(filenameUntar);
                    for (int i = 0; i < filesize; i++)
                        out.write(in.readChar());

                    System.out.println(filenameUntar + " extracted successfully.");
                    if (out != null) {
                        out.close();
                        System.out.println("File " + filenameUntar + " closed successfully.");
                    }
                    expand(filenameUntar);
                }

            } finally {
                if (out != null)
                    out.close();
            }
        } else if (extension.equals("ll")) {
            decompress(filename, filename + ".ll");
        }

        else {
            System.out.println("Invalid arguments. Only .hh and .zh and .ll files are supported.");
        }
    }
}
