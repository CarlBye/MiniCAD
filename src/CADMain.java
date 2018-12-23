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
//�˵���
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("�ļ�");
//        JMenu menu2 = new JMenu("ʹ��˵��");
        JMenuItem menuItem1 = new JMenuItem("�½�");
        JMenuItem menuItem2 = new JMenuItem("��");
        JMenuItem menuItem3 = new JMenuItem("����");
        
        menuItem1.addActionListener(Controller.pl);
        menuItem2.addActionListener(Controller.pl);
        menuItem3.addActionListener(Controller.pl);
//        menu2.addActionListener(Controller.pl);
        menu1.add(menuItem1);
        menu1.add(menuItem2);
        menu1.add(menuItem3);
        menuBar.add(menu1);
//        menuBar.add(menu2);	
// ����
// ͼ�����
        String[] shape = { "�߶�", "����", "��Բ", "������", "�����Բ", 
        		"�������", "�����", "���ֿ�"};
        JPanel shapePanel = new JPanel();
        shapePanel.setBackground(Color.black);
        shapePanel.setLayout(new GridLayout(1,shape.length));        
        for (int i = 0; i < shape.length; i++) {
            JButton button = new JButton(shape[i]);
            button.setBackground(Color.GRAY);
            button.setPreferredSize(new Dimension(50, 80));
            button.setBorder(BorderFactory.createRaisedBevelBorder());//����͹����
            button.addActionListener(Controller.pl);
            shapePanel.add(button);
        }
//�������
        String[] tool = { "ѡ��", "�Ŵ�", "��С", "���", "��ϸ","�޸�����","ɾ��" };
        JPanel toolPanel = new JPanel();
        toolPanel.setBackground(Color.black);
        toolPanel.setLayout(new GridLayout(tool.length,1));
        for (int i = 0; i < tool.length; i++) {
            JButton button = new JButton(tool[i]);
            button.setBackground(Color.GRAY);
            button.setPreferredSize(new Dimension(80, 80));
            button.setBorder(BorderFactory.createRaisedBevelBorder());//����͹����
            button.addActionListener(Controller.pl);
            toolPanel.add(button);
        }
// ��ɫ���
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
// �ײ�״̬��
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