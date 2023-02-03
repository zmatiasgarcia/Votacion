package VotacionUNGS;

public class Tupla <T1, T2>{
	private T1 x;
	private T2 y;
	
	public Tupla(T1 x, T2 y) {
		this.x = x;
		this.y = y;
	}
	
	public T1 getX() {
		return this.x;
	}
	
	public T2 getY() {
		return this.y;
	}

	void setX (T1 x) {
		this.x = x;
	}
	
	void setY (T2 y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Tupla [x=" + x + ", y=" + y + "]";
	}
}
