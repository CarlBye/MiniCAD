import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class Shape implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Point> ps = new ArrayList<>(); //存储多边形和多点折线的顶点
	public Color color;
	public float thick;
	Shape(Point pp1, Point pp2) {
		ps.add(new Point(pp1.x, pp1.y));
		ps.add(new Point(pp2.x, pp2.y));		
		color = Color.BLACK;
		thick = 3;
	}
	
	protected abstract void draw(Graphics g);
	
	protected abstract boolean isSelected(Point p);
	
	public Point[] adjustPoint() { //画图时确定从左上到右下的对角线端点坐标
		Point[] tmp = new Point[2];
		Point p1 = ps.get(0);
		Point p2 = ps.get(1);
		if(p1.x > p2.x && p1.y > p2.y) {
			tmp[0] = p2;
			tmp[1] = p1;
        }
        else if(p1.x < p2.x && p1.y < p2.y) {
			tmp[0] = p1;
			tmp[1] = p2;
        }
        else if(p1.x > p2.x && p1.y < p2.y) {
        	tmp[0] = new Point(p2.x, p1.y);
			tmp[1] = new Point(p1.x, p2.y);
        }
        else {
        	tmp[0] = new Point(p1.x, p2.y);
			tmp[1] = new Point(p2.x, p1.y);
        }
		return tmp;
	}
	
	public void changeSize(String s) {
		Point centerP = getCenterP();
		for(int i = 0; i < ps.size(); i++) {
			changeSizePoint(s, i, centerP);			
		}
    }
	
    public void changeSizePoint(String s, int i, Point centerP) {
    	int flag = 1;
    	Point p1 = ps.get(i);
    	Point p2 = centerP;
    	if(s.equals("small")) {
    		flag = -1;
    	}
    	if((p1.x < p2.x && p1.y < p2.y)) {
    		double dx = flag * (p2.x - p1.x) * 0.1;
    		double dy = flag * (p2.y - p1.y) * 0.1;
    		p1.x -= dx;
    		p1.y -= dy;
    	}
    	if((p1.x > p2.x && p1.y > p2.y)) {
    		double dx = flag * (p1.x - p2.x) * 0.1;
    		double dy = flag * (p1.y - p2.y) * 0.1;
    		p1.x += dx;
    		p1.y += dy;
    	}
    	if((p1.x < p2.x && p1.y > p2.y)) {
    		double dx = flag * (p2.x - p1.x) * 0.1;
    		double dy = flag * (p1.y - p2.y) * 0.1;
    		p1.x -= dx;
    		p1.y += dy;
    	}
    	if((p1.x > p2.x && p1.y < p2.y)) {
    		double dx = flag * (p1.x - p2.x) * 0.1;
    		double dy = flag * (p2.y - p1.y) * 0.1;
    		p1.x += dx;
    		p1.y -= dy;
    	}
    }
    
    public Point getCenterP() {
		int sumX = 0;
		int sumY = 0;
		for(int i = 0; i < ps.size(); i++) {
			sumX += ps.get(i).x;
			sumY += ps.get(i).y;
		}
		return new Point(sumX/ps.size(), sumY/ps.size());
    }
    
}

class Line extends Shape implements Serializable {//线段
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Line(Point pp1, Point pp2) {
		super(pp1, pp2);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(thick));
		g2.setColor(color);
		g2.drawLine(ps.get(0).x, ps.get(0).y, ps.get(1).x, ps.get(1).y);	
		
	}

	@Override
	protected boolean isSelected(Point p) {
		Point[] points = adjustPoint();
		if(p.x > points[0].x && p.y > points[0].y && p.x<points[1].x && p.y<points[1].y && pointToLine(p) < 12.0)
			return true;
		else return false;
	}
	
	double pointToLine(Point p) {
		double k = (ps.get(1).y - ps.get(0).y) * 1.0 / (ps.get(1).x - ps.get(0).x);
		double b = ps.get(1).y - k * ps.get(1).x;
		double d = java.lang.Math.abs((k * p.x - p.y + b) / (java.lang.Math.sqrt(k * k + 1)));
		return d;
	}

}

class Rect extends Shape implements Serializable {//矩形

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Rect(Point pp1, Point pp2) {
		super(pp1, pp2);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(thick));
		g2.setColor(color);
		Point[] points = adjustPoint();
		g2.drawRect(points[0].x, points[0].y, points[1].x - points[0].x, points[1].y - points[0].y);
	}

	@Override
	protected boolean isSelected(Point p) {
		Point[] points = adjustPoint();
		if(p.x > points[0].x && p.y > points[0].y && p.x<points[1].x && p.y<points[1].y)
			return true;
		else return false;
	}

}

class Oval extends Shape implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Oval(Point pp1, Point pp2) {
		super(pp1, pp2);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(thick));
		g2.setColor(color);
		Point points[] = adjustPoint(); 
		g2.drawOval(points[0].x, points[0].y, points[1].x - points[0].x, points[1].y - points[0].y);
	}

	@Override
	protected boolean isSelected(Point p) {
		Point[] points = adjustPoint();
		if(p.x > points[0].x && p.y > points[0].y && p.x<points[1].x && p.y<points[1].y)
			return true;
		else return false;
	}
	
}

class FilledRect extends Shape implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	FilledRect(Point pp1, Point pp2) {
		super(pp1, pp2);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(thick));
		g2.setColor(color);
		Point points[] = adjustPoint(); 
		g2.fillRect(points[0].x, points[0].y, points[1].x - points[0].x, points[1].y - points[0].y);
	}

	@Override
	protected boolean isSelected(Point p) {
		Point[] points = adjustPoint();
		if(p.x > points[0].x && p.y > points[0].y && p.x<points[1].x && p.y<points[1].y)
			return true;
		else return false;
	}
	
}

class FilledOval extends Shape implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	FilledOval(Point pp1, Point pp2) {
		super(pp1, pp2);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(thick));
		g2.setColor(color);
		Point points[] = adjustPoint(); 
		g2.fillOval(points[0].x, points[0].y, points[1].x - points[0].x, points[1].y - points[0].y);
	}

	@Override
	protected boolean isSelected(Point p) {
		Point[] points = adjustPoint();
		if(p.x > points[0].x && p.y > points[0].y && p.x<points[1].x && p.y<points[1].y)
			return true;
		else return false;
	}
	
}

class MyPolyline extends Shape implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MyPolyline(Point pp1, Point pp2) {
		super(pp1, pp2);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(thick));
		g2.setColor(color);
		for(int i = 0; i < ps.size()-1; i++) {
			g2.drawLine(ps.get(i).x, ps.get(i).y, ps.get(i+1).x, ps.get(i+1).y);
		}
	}

	@Override
	protected boolean isSelected(Point p) {
		for(int i = 0; i < ps.size()-1; i++) {
			Line l= new Line(ps.get(i), ps.get(i+1));
			if(l.isSelected(p)) return true;
		}
		return false;
	}

}

class MyPolygon extends Shape implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MyPolygon(Point pp1, Point pp2) {
		super(pp1, pp2);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(thick));
		g2.setColor(color);
		for(int i = 0; i < ps.size()-1; i++) {
			g2.drawLine(ps.get(i).x, ps.get(i).y, ps.get(i+1).x, ps.get(i+1).y);
		}
		g2.drawLine(ps.get(ps.size()-1).x, ps.get(ps.size()-1).y, ps.get(0).x, ps.get(0).y);
	}

	@Override
	protected boolean isSelected(Point p) {
		for(int i = 0; i < ps.size()-1; i++) {
			Line l= new Line(ps.get(i), ps.get(i+1));
			if(l.isSelected(p)) return true;
		}
		Line l= new Line(ps.get(ps.size()-1), ps.get(0));
		if(l.isSelected(p)) return true;
		return false;
	}

}

class Text extends Shape implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String t;
	Text(Point pp1, Point pp2, String tt) {
		super(pp1, pp2);
		t = tt;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		Point points[] = adjustPoint();
		g.setFont(new Font("宋体", Font.PLAIN, (points[1].y-points[0].y)/2));
		g.drawString(t, points[0].x, points[1].y);
	}

	@Override
	protected boolean isSelected(Point p) {
		Point[] points = adjustPoint();
		if(p.x > points[0].x && p.y > points[0].y && p.x<points[1].x && p.y<points[1].y)
			return true;
		else return false;
	}
	
	public void setText(String tt) {
		t = tt;
	}
}