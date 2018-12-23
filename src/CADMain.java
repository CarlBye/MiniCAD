import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class CADMain extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Color[] color = { Color.black, Color.blue, Color.white, Color.gray,
            Color.red, Color.cyan, Color.green, Color.darkGray, Color.pink };
    public static String[] colorName = { "black", "blue", "white", "gray", "red", "cyan",
    		"green", "darkGray", "pink" };
    
	public CADMain() {
		setTitle("My MiniCAD");
		setSize(800, 800);	
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setLayout(new BorderLayout(15, 15));
//菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("文件");
//        JMenu menu2 = new JMenu("使用说明");
        JMenuItem menuItem1 = new JMenuItem("新建");
        JMenuItem menuItem2 = new JMenuItem("打开");
        JMenuItem menuItem3 = new JMenuItem("保存");
        
        menuItem1.addActionListener(Controller.pl);
        menuItem2.addActionListener(Controller.pl);
        menuItem3.addActionListener(Controller.pl);
//        menu2.addActionListener(Controller.pl);
        menu1.add(menuItem1);
        menu1.add(menuItem2);
        menu1.add(menuItem3);
        menuBar.add(menu1);
//        menuBar.add(menu2);	
// 画板
// 图形面板
        String[] shape = { "线段", "矩形", "椭圆", "填充矩形", "填充椭圆", 
        		"多点折线", "多边形", "文字块"};
        JPanel shapePanel = new JPanel();
        shapePanel.setBackground(Color.black);
        shapePanel.setLayout(new GridLayout(1,shape.length));        
        for (int i = 0; i < shape.length; i++) {
            JButton button = new JButton(shape[i]);
            button.setBackground(Color.GRAY);
            button.setPreferredSize(new Dimension(50, 80));
            button.setBorder(BorderFactory.createRaisedBevelBorder());//让它凸起来
            button.addActionListener(Controller.pl);
            shapePanel.add(button);
        }
//工具面板
        String[] tool = { "选定", "放大", "缩小", "变粗", "变细","修改文字","删除" };
        JPanel toolPanel = new JPanel();
        toolPanel.setBackground(Color.black);
        toolPanel.setLayout(new GridLayout(tool.length,1));
        for (int i = 0; i < tool.length; i++) {
            JButton button = new JButton(tool[i]);
            button.setBackground(Color.GRAY);
            button.setPreferredSize(new Dimension(80, 80));
            button.setBorder(BorderFactory.createRaisedBevelBorder());//让它凸起来
            button.addActionListener(Controller.pl);
            toolPanel.add(button);
        }
// 颜色面板
        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(Color.black);
        colorPanel.setLayout(new GridLayout(color.length,1));      
        for (int i = 0; i < color.length; i++) {
            JButton button = new JButton(colorName[i]);
            button.addActionListener(Controller.pl);
//            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(80, 80));
            button.setBackground(color[i]);
            colorPanel.add(button);
        }
// 底部状态栏
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));       
        JLabel statusLabel = new JLabel("Powerby 3160105684 bds");
        statusPanel.add(statusLabel);

        setJMenuBar(menuBar);
        add(shapePanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);
        add(colorPanel, BorderLayout.WEST);
        add(toolPanel, BorderLayout.EAST);
	}
	
	public static void main(String[] args){
		CADMain window = new CADMain();
		View view = new View();
		window.add(view, BorderLayout.CENTER);
		window.setVisible(true);
		ArrayList<Shape> shapes = new ArrayList<>();
		Model model = new Model(shapes);
		@SuppressWarnings("unused")
		Controller ctrl = new Controller(model, view);
	}		
}