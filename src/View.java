import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class View extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Shape> shapes = new ArrayList<>(); //暂时用来传递参数
	View() {
		setBackground(Color.WHITE);
        addMouseListener(Controller.pl);
        addMouseMotionListener(Controller.pl);
	}
	
	public void paintAll(ArrayList<Shape> s) {
		shapes = s;
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);		
		if(!shapes.isEmpty()) {		
			for(Shape shape: shapes) {
				shape.draw(g);
		    }
		}
	}
}
