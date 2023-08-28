package Project6;

public class HuffmanNode implements Comparable<HuffmanNode> {
    private char character;
    private int frequency;
    public HuffmanNode left;
    public HuffmanNode right;

    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        left = null;
        right = null;
    }

    public HuffmanNode(char character) {
        this.character = character;
    }


    public HuffmanNode() {

    }

    public char getCharacter() {
        return this.character;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public boolean noChildren() {
        return this.left == null && this.right == null;
    }

    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency;
    }

    public String toString() {
        if (this.character == ' ') {
            return "char: (space) " + "   freq: " + this.frequency;
        } else if (this.character == '\n') {
            return "char: (enter) " + "   freq: " + this.frequency;
        }
        return "char: " + this.character + "   freq: " + this.frequency;
    }
}
