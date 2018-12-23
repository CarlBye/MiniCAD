import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Controller {
    public static Model model;
    private static View view;
    public static paintListener pl= new paintListener();
    public static String state = "idle";
    Controller(Model m, View v) {
        model = m;
    	view = v;
    }
    public static void updateView() {
        view.paintAll(model.getAll());
    }
}

class paintListener implements ActionListener, MouseListener, MouseMotionListener {
    private Shape selectedShape; 
    private Point p1;
    private Point p2;
    private ArrayList<Point> initP = new ArrayList<>();
    private String text;
    private int polyflag = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        String btnName=e.getActionCommand();
        switch (btnName) {
            case "选定":
                Controller.state = "select";
                break;
            case "线段":
                Controller.state = "line";
                break;
            case "矩形":
                Controller.state = "rect";
                break;
            case "椭圆":
                Controller.state = "oval";
                break;
            case "填充矩形":
            	Controller.state = "filledRect";
                break;
            case "填充椭圆":
            	Controller.state = "filledOval";
                break;
            case "多点折线":
            	Controller.state = "polyline";
            	polyflag = 0;
                break;
            case "多边形":
            	Controller.state = "polygon";
            	polyflag = 0;
                break;
            case "文字块":
            	Controller.state = "text";
            	text = JOptionPane.showInputDialog("请输入文本: ");
                break;
            case "black" : case "blue" : case "white" : case "gray" : case "red" : case "cyan" : case "green" : case "darkGray" : case "pink" :
                if(selectedShape != null) {
                	int i;
                	for(i = 0; i < CADMain.colorName.length; i++) {
                		if(btnName.equals(CADMain.colorName[i])) break;
                	}
                    selectedShape.color = CADMain.color[i];
                }
                Controller.updateView();
                break;
            case "放大":
            	if(selectedShape != null) {
            		selectedShape.changeSize("big");
                }
                Controller.updateView();
                break;
            case "缩小":
            	if(selectedShape != null) {
            		selectedShape.changeSize("small");
                }
                Controller.updateView();
                break;
            case "变粗":
            	if(selectedShape != null) {
                    selectedShape.thick ++;
                }
                Controller.updateView();
                break;
            case "变细":
            	if(selectedShape != null) {
            		if(selectedShape.thick <= 1) 
            			selectedShape.thick %= 1.5;
            		else selectedShape.thick --;
                }
                Controller.updateView();
                break;
            case "修改文字":
            	if(selectedShape instanceof Text) {
            		Text tmp = (Text) selectedShape;
            		tmp.setText(JOptionPane.showInputDialog("请输入修改文本: "));   		
                }
                Controller.updateView();
                break;
            case "删除":
            	if(selectedShape != null) {
                    Controller.model.getAll().remove(selectedShape);
                }
                Controller.updateView();
                break;
            case "新建":
                newFile();
                break;
            case "打开":
                openFile();
                break;
            case "保存":
                saveFile();
                break;
            default:
                Controller.state="idle";
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) { 
        p1 = new Point(e.getX(), e.getY());
        p2 = new Point(e.getX(), e.getY());
        switch (Controller.state) {
            case "select":
                selectedShape = findSelected(p1);
                if(selectedShape != null) {
                	initP.clear();
                	for(int i = 0; i < selectedShape.ps.size(); i++) {
                		initP.add(new Point(selectedShape.ps.get(i).x,selectedShape.ps.get(i).y));
                	}
                }
                break;
            case "line":
                Controller.model.add(new Line(p1, p2));
                selectedShape = null;
                break;
            case "rect":
                Controller.model.add(new Rect(p1, p2));
                selectedShape = null;
                break;
            case "oval":
                Controller.model.add(new Oval(p1, p2));
                selectedShape = null;
                break;
            case "filledRect":
                Controller.model.add(new FilledRect(p1, p2));
                selectedShape = null;
                break;
            case "filledOval":
                Controller.model.add(new FilledOval(p1, p2));
                selectedShape = null;
                break;
            case "polyline":
            	if(polyflag == 0) {
            		Controller.model.add(new MyPolyline(p1, p2));
            		polyflag = 1;
            	}
            	else {
            		Shape s = Controller.model.getCurrent();
                	s.ps.add(p2);
                	Controller.updateView();
            	}
            	selectedShape = null;
                break;
            case "polygon":
            	if(polyflag == 0) {
            		Controller.model.add(new MyPolygon(p1, p2));
            		polyflag = 1;
            	}
            	else {
            		Shape s = Controller.model.getCurrent();
                	s.ps.add(p2);
                	Controller.updateView();
            	}
            	selectedShape = null;
                break;
            case "text":
                Controller.model.add(new Text(p1, p2, text));
                selectedShape = null;
                break;
            default:
                selectedShape = null;
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(Controller.state.equals("idle")) return;
        if(Controller.state.equals("select")) {
          if(selectedShape != null) {
        	  for(int i = 0; i < selectedShape.ps.size(); i++) {
        		  selectedShape.ps.get(i).x = initP.get(i).x + e.getX() - p1.x;
        		  selectedShape.ps.get(i).y = initP.get(i).y + e.getY() - p1.y;
        		  Controller.updateView();
        	  }        	
          }
        }
        else { 
        	Shape s=Controller.model.getCurrent();
        	if(s instanceof MyPolygon || s instanceof MyPolyline) return;
            s.ps.get(1).x=e.getX();
            s.ps.get(1).y=e.getY();
            Controller.updateView();
        }
    }

    private Shape findSelected(Point p) {
        for(Shape shape: Controller.model.getAll()) {
            if(shape.isSelected(p)) {
                return shape;
            }
        }
        return null;
    }

    private void newFile() {
    	int value = JOptionPane.showConfirmDialog(null, "是否保存文件", "提示信息", 0);
		if(value == 0){
			saveFile();
		}
		if(value == 1){
			Controller.model.removeAll();
			Controller.updateView();
		}
    }
    
    private void saveFile() { //????
        JFileChooser chooser=new JFileChooser();
        chooser.showSaveDialog(null);
        chooser.setDialogTitle("保存文件");
        File file = chooser.getSelectedFile();
        
        if(file == null) {
        	JOptionPane.showMessageDialog(null,"未选择文件!");
        }
        else {
            try { 
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(Controller.model.getAll());
                JOptionPane.showMessageDialog(null,"保存成功!");
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,"保存失败!");
            }
        }
       
    }

    @SuppressWarnings("unchecked")
	private void openFile() {
		int value = JOptionPane.showConfirmDialog(null, "是否保存文件", "提示信息", 0);
		if(value == 0) {
			saveFile();
		}			
		try {
			//?????????????????????????
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("打开cad文件");
			chooser.showOpenDialog(null);
			File file = chooser.getSelectedFile();
			
			if(file==null){
				JOptionPane.showMessageDialog(null, "未选择文件!");
			}
			else {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
				Controller.model.setAll((ArrayList<Shape>)in.readObject());
				 Controller.updateView();
				in.close();
			}
			
		} 
		catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null,"打开失败!");
		}
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
