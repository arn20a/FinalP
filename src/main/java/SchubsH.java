
/*************************************************************************
 *  Aimen Nesru
 *  SE2
 *  Brent Reeves
 *  SchubsH.java
 * 
 *  Compresses files using Huffman compression algorithm
 * 
 *  Compilation: javac SchubsH.java
 *  Execution: java SchubsH test-file
 *  
 *  This will compress the given file <test-file> and produce <testfile>.hh 
 *  Example: test1.txt becomes test1.txt.hh
 *  
 *  For Globs: java SchubsH <Glob>
 *  Example: java SchubsH <Glob> *.txt will change all txt files to *.txt.hh
 * 
 *  To run with maven run : java -cp target/classes SchubsH test-file
 * 
 *  Algorithm Theory: Huffman compression is a prefix coding technique
 *  that assigns variable-length codes to input symbols based on their
 *  frequencies. It constructs a binary tree where the most frequent
 *  symbols have shorter codes and the least frequent symbols have
 *  longer codes. Huffman coding guarantees that no code is a prefix
 *  of another, ensuring uniquely decodable codes.
 *  Trade-offs: Huffman compression is optimal for lossless data
 *  compression when the frequency distribution of symbols is known.
 *  It produces efficient compression for data with skewed frequency
 *  distributions. However, constructing the Huffman tree requires a
 *  pass over the data to determine symbol frequencies, which can be
 *  computationally expensive for large datasets.

 *************************************************************************/
import java.io.*;
import java.util.*;

public class SchubsH {

    private static final int R = 256;

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

    public static void compress(String filename, String filename2) {
        try {
            FileInputStream fis = new FileInputStream(filename);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            fis.close();

            byte[] input = baos.toByteArray();

            int[] freq = new int[R];
            for (int i = 0; i < input.length; i++)
                freq[input[i] & 0xFF]++;

            Node root = buildTrie(freq);

            String[] st = new String[R];
            buildCode(st, root, "");

            FileOutputStream fos = new FileOutputStream(filename2);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            writeTrie(root, oos);

            oos.writeInt(input.length);

            for (int i = 0; i < input.length; i++) {
                String code = st[input[i] & 0xFF];
                for (int j = 0; j < code.length(); j++) {
                    if (code.charAt(j) == '0') {
                        oos.writeBoolean(false);
                    } else if (code.charAt(j) == '1') {
                        oos.writeBoolean(true);
                    } else
                        throw new RuntimeException("Illegal state");
                }
            }

            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Node buildTrie(int[] freq) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.add(new Node(i, freq[i], null, null));

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        return pq.poll();
    }

    private static void writeTrie(Node x, ObjectOutputStream oos) throws IOException {
        if (x.isLeaf()) {
            oos.writeBoolean(true);
            oos.writeChar(x.ch);
            return;
        }
        oos.writeBoolean(false);
        writeTrie(x.left, oos);
        writeTrie(x.right, oos);
    }

    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("    Usage: java SchubsH file1 file2 file3 ... OR java SchubsH <GLOB>");
            System.out.println("    file1: file to be compressed");
            System.out.println("    file2: compressed file");
            System.out.println("    file3: file to be compressed ...");
            System.out.println("    Incorrect number of arguments.");
            throw new IllegalArgumentException("Incorrect number of arguments.");
        }
        for (int i = 0; i < args.length; i++) {
            File in = new File(args[i]);
            if (!in.exists()) {
                System.out.println("    File " + args[i] + " does not exist.");
                throw new IllegalArgumentException("File " + args[i] + " does not exist.");
            } else {
                String filename = args[i];
                String filename2 = filename + ".hh";
                compress(filename, filename2);
            }
        }
    }
}
