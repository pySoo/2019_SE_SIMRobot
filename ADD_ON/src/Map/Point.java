package Map;
/**
 * 좌표 클래스 정의
 */

public class Point {
	public int row;
	public int col;

	public Point(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public Point() {
		row = -1;
		col = -1;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", row, col);
	}
}
