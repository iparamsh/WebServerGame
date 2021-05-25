import java.io.PrintStream;

public class Ball extends Shape{
	
	private int _radius;//radius of the ball
	
	public Ball (int x, int y, String color, int radius) {
		super(x, y, color);
		this._radius = radius;
	}
	
	public void update() 
	{
		super.update();
	}
	
	//finding bottom of the ball
	public int bottom() {
		return _y + _radius;
	}
	
	//finding right of the ball
	public int right() {
		return _x + _radius;
	}
	
	//finding top of the ball
	public int top() {
		return _y - _radius;
	}
	
	//finding left of the ball
	public int left() {
		return _x - _radius;
	}
	
	
	//rendering circle in HTML
	@Override
	public void renderAsHTML(PrintStream out) {
		out.println("<circle cx=" + _x + " cy=" + _y + " r=" + _radius + " stroke=\"black\"stroke-width=\"1\" fill=" + _color + " />");
	}
}