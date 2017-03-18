
public class Nodes implements Comparable<Nodes>{
	private int[][] puzzle;
	private int valueH;
	private int valueG;
	public int[][] getPuzzle() {
		return puzzle;
	}
	public void setPuzzle(int[][] puzzle) {
		this.puzzle = puzzle;
	}
	public int compareTo(Nodes node) {
		int valueH = node.getValueH();
		int valueG = node.getValueG();
	    int[][] puzzle = node.getPuzzle();
	    int value = valueH+valueG;
	    if ((this.getValueH()+this.getValueG()) <= value) {
	      return -1;
	    }

	    if ((this.getValueH()+this.getValueG()) > value) {
	      return 1;
	    }
	     
	    return 0;
	}
	public int getValueG() {
		return valueG;
	}
	public void setValueG(int valueG) {
		this.valueG = valueG;
	}
	public int getValueH() {
		return valueH;
	}
	public void setValueH(int valueH) {
		this.valueH = valueH;
	}
}
