package Project6;

import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * Name: Daniel Le
 * <p>
 * Class: CS143
 * <p>
 * This class represents a Huffman tree used for encoding and decoding text.
 * The tree is built based on the given character frequencies and is used to generate
 * Huffman codes for each character. The root field represents the root node of the
 * Huffman tree. The constructor takes an array of character frequencies and builds
 * the tree. The write method writes the tree structure and Huffman codes to a file.
 * The encode method encodes text using the generated Huffman codes. The decode
 * method decodes binary back into text using the Huffman tree.
 * Precondition: The array of character frequencies provided in the constructor
 * must be valid and non-empty.
 */
public class HuffmanTree {
    private HuffmanNode root;

    /**
     * Constructs a HuffmanTree using the given character frequencies.
     *
     * @param counts The array of character frequencies.
     */
    public HuffmanTree(int[] counts) {
        root = buildTree(counts);
    }

    /**
     * Builds a Huffman tree using the given character frequencies.
     *
     * @param counts The array of character frequencies.
     * @return The root node of the Huffman tree.
     */
    private HuffmanNode buildTree(int[] counts) {
        Queue<HuffmanNode> queue = new PriorityQueue<>();
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0) {
                HuffmanNode node = new HuffmanNode((char) i, counts[i]);
                queue.add(node);
            }
        }
        HuffmanNode eofNode = new HuffmanNode((char) counts.length, 1);
        queue.add(eofNode);

        while (queue.size() > 1) {
            HuffmanNode leftChild = queue.remove();
            HuffmanNode rightChild = queue.remove();
            HuffmanNode node = new HuffmanNode('\0',
                    leftChild.getFrequency() + rightChild.getFrequency());
            node.left = leftChild;
            node.right = rightChild;
            queue.add(node);
        }
        return queue.remove();
    }

    /**
     * Constructs a HuffmanTree by reading the tree structure and codes from the given scanner.
     *
     * @param input The scanner containing the tree structure and codes.
     */
    public HuffmanTree(Scanner input) {
        root = buildTree(input);
    }

    /**
     * Builds a Huffman tree by reading the tree structure and codes from the given scanner.
     *
     * @param input The scanner containing the tree structure and codes.
     * @return The root node of the Huffman tree.
     */
    private HuffmanNode buildTree(Scanner input) {
        HuffmanNode root = new HuffmanNode();
        HuffmanNode current = root;

        while (input.hasNextLine()) {
            int ascii = Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            HuffmanNode node = new HuffmanNode((char) ascii);

            for (char c : code.toCharArray()) {
                if (c == '0') {
                    if (current.left == null) {
                        current.left = new HuffmanNode();
                    }
                    current = current.left;
                } else if (c == '1') {
                    if (current.right == null) {
                        current.right = new HuffmanNode();
                    }
                    current = current.right;
                }
            }
            current.setCharacter(node.getCharacter());
            current = root;
        }
        return root;
    }

    /**
     * Writes the Huffman tree structure and codes to the output stream.
     *
     * @param output The output stream to write the tree and codes to.
     * @pre The Huffman tree must be built.
     * @post The tree structure and codes are written to the output stream.
     */
    public void write(PrintStream output) {
        StringBuilder bits = new StringBuilder();
        writeHelper(root, bits, output);
    }

    /**
     * Recursive helper method to write the Huffman tree structure and
     * codes to the output stream.
     *
     * @param node   The current node being traversed.
     * @param bits   The StringBuilder representing the current path of bits.
     * @param output The output stream to write the tree and codes to.
     */
    private void writeHelper(HuffmanNode node, StringBuilder bits, PrintStream output) {
        if (node.noChildren()) {
            output.println((int) node.getCharacter());
            output.println(bits.toString());
        } else {
            writeHelper(node.left, bits.append('0'), output);
            bits.deleteCharAt(bits.length() - 1);
            writeHelper(node.right, bits.append('1'), output);
            bits.deleteCharAt(bits.length() - 1);
        }
    }

    /**
     * Decodes the input stream using the Huffman tree.Writes the decoded characters
     * to the output stream until the end of file (EOF) character is reached.
     *
     * @param input  The input stream to decode.
     * @param output The output stream to write the decoded characters to.
     * @param eof    The end of file (EOF) character.
     * @throws RuntimeException if an unexpected end of input stream is encountered.
     * @pre The Huffman tree must be built.
     * @post The input stream is decoded and the characters are written to the output stream.
     */
    public void decode(BitInputStream input, PrintStream output, int eof) {
        HuffmanNode current = root;
        int bit;
        boolean decoded = false;

        while (!decoded) {
            bit = input.readBit();
            if (bit != -1) {
                if (bit == 0) {
                    current = current.left;
                } else if (bit == 1) {
                    current = current.right;
                }

                if (current.noChildren()) {
                    char character = current.getCharacter();
                    if (character == eof) {
                        decoded = true;
                    } else {
                        output.write(character);
                        current = root;
                    }
                }
            } else {
                throw new RuntimeException("Unexpected end of input stream");
            }
        }
    }
}
