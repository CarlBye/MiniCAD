import java.util.ArrayList;

public class Model {
	private ArrayList<Shape> shapes; //�������ͼ��
    Model(ArrayList<Shape> s) {
        shapes = s;
    }
    
    public ArrayList<Shape> getAll() {
        return shapes;
    }
    
    public Shape getCurrent() {
        return shapes.get(shapes.size()-1);
    }
    
    public void add(Shape shape) {
        shapes.add(shape);
    }

    public void setAll(ArrayList<Shape> sshape) {//���ڴ��ļ�
        shapes = sshape;
    }

	public void removeAll() {
		shapes.clear();
	}
    
}
