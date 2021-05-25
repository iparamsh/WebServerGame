import java.io.PrintStream;

public class Rectangle extends Shape{
	private int _width;	//width of the Rectangle
	private int _height; //height of the Rectangle
	
	
	public Rectangle (int x, int y, String color, int width, int height) {
		super(x, y, color);
		this._width = width;
		this._height = height;
	}
	
	//makes velocity random 
	public static int legalVelocity() {
		int randomVal = (int) (Math.random()*8) + 4;
		int operator = (int) (Math.random()*2) + 1;
		if (operator == 1) {
			randomVal = randomVal - (randomVal * 2);
		}
		return randomVal;
	}
	
	
	//finding bottom of the rectangle 
	public int bottom() {
		return _y + _height;
	}
	
	//finding right of the rectangle 
	public int right() {
		return _x + _width;
	}
	
	//finding top of the rectangle
	public int top() {
		return _y;
	}
	
	//finding left of the rectangle 
	public int left() {
		return _x;
	}
	
	
	//rendering Rectangle in HTML
	@Override
	public void renderAsHTML(PrintStream out) {
		out.println("  <rect x='"+ _x +"' y='"+ _y +"' width='"+ _width +"' height='"+ _height +"' style='fill:"+ _color +";stroke-width:2;stroke:black'/>");
	}
}