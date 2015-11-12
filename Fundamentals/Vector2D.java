package Fundamentals;


public class Vector2D {
	
	public double x;
	public double y;
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(double num) {
		this.x += num;
		this.y += num;
	}
	
	public void sub(double num) {
		this.x -= num;
		this.y -= num;
	}
	
	public void mult(double num) {
		this.x *= num;
		this.y *= num;
	}
	
	public void div(double num) {
		this.x /= num;
		this.y /= num;
	}
	
	public void add(Vector2D v) {
		this.x += v.x;
		this.y += v.y;
	}
	
	public void sub(Vector2D v) {
		this.x -= v.x;
		this.y -= v.y;
	}
	
	public void mult(Vector2D v) {
		this.x *= v.x;
		this.y *= v.y;
	}
	
	public void div(Vector2D v) {
		this.x -= v.x;
		this.y -= v.y;
	}
}
