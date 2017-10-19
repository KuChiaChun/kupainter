import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.MouseEvent;
import java.awt.event.ItemEvent;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension; 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import java.nio.file.Path;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.NoSuchElementException;

 
//���3A �j�ήm 104403019
public class PaintFrame extends JFrame{
	

	private static final long serialVersionUID = -4067285394523727922L;
	private final BorderLayout layout;
	private final JLabel label1; //ø�Ϥu��label
	private final JLabel label2;
	private final JLabel label3;
	private final JLabel label4; //����j�plabel
	private final JLabel label5; //�u���label	
	private final JComboBox<String> painttoolJComboBox; 	
	private final JPanel toolPanel;
	private static final String[] painttools = 
		{"����","�����","���u","����","�x��","�ꨤ�x��"};	
	private final JRadioButton smallJRadioButton;
	private final JRadioButton mediumJRadioButton;
	private final JRadioButton bigJRadioButton;
	private final ButtonGroup radioGroup;	
	private final JCheckBox fillupCheckBox;	
	private final JCheckBox GradientCheckBox;	
	private final JButton ForegroundButton;
	private final JButton BackgroundButton;
	private final JButton ClearButton;	
	private paintPanel paintPanel; //��ܷƹ����A��panel
	private JLabel statusBar;  //��ܷƹ���m��label	
	static Color color = Color.BLACK;;/////////////
	private Color Backcolor = Color.WHITE;//////////
	static Color Gradientcolor = Color.CYAN;
	public static String paintMode="����";
	private int thickness = 4;
	private BasicStroke stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_MITER, 10);//�]�w�u��
	static boolean FillFlag = true;//�񺡺X��
	static boolean GradientFlag = false;//���h�X��
	int SolidLine=0;//��u=0  ��u=1
	int DrawFlag =1;//ø�ϺX��
	int x1,x2,y1,y2;//�ƹ��y��
	private ArrayList<SaveDraw> points = new ArrayList<SaveDraw>();
	private ArrayList<SaveDraw> Undopoints = new ArrayList<SaveDraw>();	
	private String OpenName ="";
	private static Path OpenPath;	
	private String SaveName ="";
	private static Path SavePath;
	private  ObjectOutputStream output;
	private  ObjectInputStream input;
	private final JButton SaveButton;
	private final JButton OpenButton;
	private final JButton UndoButton; 
	private final JButton RedoButton;	
	int Erasersize = 30;
	private SaveFile savefile = new SaveFile();
	private OpenFile openfile = new OpenFile();
	int SavePoint = 0;
	public PaintFrame(){
		super("�p�e�a");
		layout = new BorderLayout();		
		setLayout(layout) ;
		
		//�إ�JLabels
		label1 = new JLabel("[ø�Ϥu��]");
		label1.setFont(new Font("����",Font.BOLD,18));
		label2 = new JLabel("�ШϥΫe����ӿ�ܺ��h��",JLabel.CENTER);		
		label2.setFont(new Font("����",Font.BOLD,26));
		label3 = new JLabel("���A�C");	
		label3.setFont(new Font("����",Font.BOLD,16));
		label4 = new JLabel("[����j�p]");
		label4.setFont(new Font("����",Font.BOLD,18));
		label5 = new JLabel("[�u���]");
		label5.setFont(new Font("����",Font.BOLD,18));
		add(label2,BorderLayout.NORTH);
		label2.setVisible(false);
		add(label3,BorderLayout.SOUTH);	
		//�إ�JComboBox �H�Ϋإ�Listener
		painttoolJComboBox = new JComboBox<String>(painttools);//�]�wø�Ϥu�㪺comboBox
		painttoolJComboBox.setMaximumRowCount(4);	//�@���̦h��ܤT�ؤu��
		painttoolJComboBox.setSize(100, 30);
		painttoolJComboBox.setFont(new Font("����",Font.BOLD,18));
		painttoolJComboBox.addItemListener(
				new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent event) 
					{
						if(event.getStateChange()==ItemEvent.SELECTED) 
						//	JOptionPane.showMessageDialog(PaintFrame.this, String.format("�A�I��F: %s",painttoolJComboBox.getSelectedItem()));	
						paintMode = (String)painttoolJComboBox.getSelectedItem();
						if (painttoolJComboBox.getSelectedItem().equals("����")||painttoolJComboBox.getSelectedItem().equals("�����")) {
							
							fillupCheckBox.setEnabled(false);
							GradientCheckBox.setEnabled(false);
						}
						else if(painttoolJComboBox.getSelectedItem().equals("���u")) {
							fillupCheckBox.setEnabled(true);
							GradientCheckBox.setEnabled(false);
						}
				
						else 
							{fillupCheckBox.setEnabled(true);
						    GradientCheckBox.setEnabled(true);}
					}
				}
		);
		
		//�إ�radioButtons			
		smallJRadioButton = new JRadioButton("�p",true);              
		smallJRadioButton.setFont(new Font("����",Font.BOLD,18));   
		smallJRadioButton.setActionCommand("�p");
		mediumJRadioButton =  new JRadioButton("��",false);
		mediumJRadioButton.setFont(new Font("����",Font.BOLD,18));
		mediumJRadioButton.setActionCommand("��");
		bigJRadioButton = new JRadioButton("�j",false);
		bigJRadioButton.setFont(new Font("����",Font.BOLD,18));
		bigJRadioButton.setActionCommand("�j");
		
		//create new RadioButtonHandler for radiobutton event handling
		RadioButtonHandler RadioHandler = new RadioButtonHandler();
		smallJRadioButton.addActionListener(RadioHandler);
		mediumJRadioButton.addActionListener(RadioHandler);
		bigJRadioButton.addActionListener(RadioHandler);
		
		//create logical relationship between JRadioButtons
		radioGroup = new ButtonGroup();     
		radioGroup.add(smallJRadioButton);
		radioGroup.add(mediumJRadioButton);  
		radioGroup.add(bigJRadioButton);		
		
		//�إ߶�checkBox
		fillupCheckBox = new JCheckBox("��");
		fillupCheckBox.setFont(new Font("����",Font.BOLD,18));
		fillupCheckBox.setSelected(true);
		CheckBoxHandler Boxhandler = new CheckBoxHandler();
		fillupCheckBox.addItemListener(Boxhandler);		
		fillupCheckBox.setEnabled(false); //�T�� checkbox
		//�إߺ��hcheckBox
		GradientCheckBox = new JCheckBox("���h");
		GradientCheckBox.setFont(new Font("����",Font.BOLD,18));
		GradientCheckBox.setSelected(false);
		GradientBoxHandler Gradienthandler = new GradientBoxHandler();
		GradientCheckBox.addItemListener(Gradienthandler);	
		GradientCheckBox.setEnabled(false);
		//�إ߫e���B�I���B�M��button
		ForegroundButton = new JButton("�e����");
		BackgroundButton = new JButton("�I����");
		ClearButton = new JButton("�M��");
		UndoButton = new JButton("�W�@�B");
		RedoButton = new JButton("�U�@�B");
		OpenButton = new JButton("�}���ɮ�");
		SaveButton = new JButton("�x�s�ɮ�");
		
		//create new ButtonHandler for button event handling
		ButtonHandler handler = new ButtonHandler();
		ForegroundButton.addActionListener(handler);
		BackgroundButton.addActionListener(handler);
		ClearButton.addActionListener(handler);
		OpenButton.addActionListener(handler);
		SaveButton.addActionListener(handler);
		UndoButton.addActionListener(handler);	
		RedoButton.addActionListener(handler);			
		//�إߥΨө�m����ø�Ϥu�㪺panel
		toolPanel = new JPanel();
		toolPanel.setLayout(new GridLayout(16,1));//�]�w Panel���s�Ƥ覡
		toolPanel.setPreferredSize(new Dimension(100,700));
		toolPanel.setMaximumSize(new Dimension(100, 700));
		
		//�N�u��ﶵ�v�@�[�JJPanel��
		toolPanel.add(label1);
		toolPanel.add(painttoolJComboBox);
		toolPanel.add(label4);
		toolPanel.add(smallJRadioButton);
		toolPanel.add(mediumJRadioButton);
		toolPanel.add(bigJRadioButton);
		toolPanel.add(fillupCheckBox);
		toolPanel.add(GradientCheckBox);
		toolPanel.add(label5);
		toolPanel.add(ForegroundButton);
		toolPanel.add(BackgroundButton);
		toolPanel.add(UndoButton);
		toolPanel.add(RedoButton);
		toolPanel.add(OpenButton);
		toolPanel.add(SaveButton);
		toolPanel.add(ClearButton);
		add(toolPanel,BorderLayout.WEST);
		//�Ψ�ø�ϥH����ܷƹ����A��Panel
		paintPanel = new paintPanel();
		paintPanel.setOpaque(true);
		add(paintPanel,BorderLayout.CENTER); //add Panel to JFrame
		paintPanel.setBackground(Color.WHITE);//set background color --> white
		statusBar = new JLabel("��Ц�m");
		add(statusBar,BorderLayout.SOUTH); //add label to JFrame

		//create and register listener for mouse event 		
		paintPanel.addMouseMotionListener(new MouseMotionAdapter()
		{			
			@Override 
			public void mouseDragged(MouseEvent e)
			{
		
		    	statusBar.setText(String.format("��Ц�m : (%d,%d)", e.getX(),e.getY()));
			
					if(paintMode.equals("����"))
					{
						x2=e.getX();
						y2=e.getY();
						Shape pen =new Line2D.Double(x1,y1,x2,y2);
						stroke = new BasicStroke( thickness, BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
						SaveDraw pen2D = new SaveDraw(pen,color,Gradientcolor,thickness,FillFlag);
						points.add(pen2D);
						x1=x2;
						y1=y2;		
						repaint();
					}
					if(paintMode.equals("�����"))
						{
						x2=e.getX();
						y2=e.getY();
						Shape eraser = new Rectangle2D.Double(x1-Erasersize/2,y1-Erasersize/2,Erasersize,Erasersize);
						SaveDraw eraser2d = new SaveDraw(eraser,Backcolor,true,true);
						points.add(eraser2d);
						x1=x2;
						y1=y2;
						repaint();
					}
					//   �|�ݭץ����\��  ��e���䲣�͹w��
//					if(paintMode.equals("����")) {
//						x2=e.getX();
//						y2=e.getY();
//						stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
//						Shape ellipse = new Ellipse2D.Double(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x1-x2),Math.abs(y1-y2));
//						int x[] = {Math.min(x1, x2),Math.max(x1, x2)};//���h�ݭn�ϥΨ쪺�Ѽ�
//						int y[] = {Math.min(y1, y2),Math.max(y1, y2)};//
//						SaveDraw ellipse2d = new SaveDraw(ellipse,color,Gradientcolor,thickness,FillFlag,GradientFlag,x,y);
//						if(points.size()>=1)
//						points.add(ellipse2d);
//						if(points.size()>=3)		
//						points.remove(points.size()-2);
//						if(points.size()>1)
//						repaint();
//					}
//					if(paintMode.equals("�x��")) {
//						x2=e.getX();
//						y2=e.getY();
//						stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
//						Shape rect = new Rectangle2D.Double(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x1-x2),Math.abs(y1-y2));
//						int x[] = {Math.min(x1, x2),Math.max(x1, x2)};
//						int y[] = {Math.min(y1, y2),Math.max(y1, y2)};
//						SaveDraw rect2d = new SaveDraw(rect,color,Gradientcolor,thickness,FillFlag,GradientFlag,x,y);
//						points.add(rect2d);
//						if(points.size()>=3)		
//						points.remove(points.size()-3);
//						if(points.size()>2)
//						repaint();
//					}
//					if(paintMode.equals("�ꨤ�x��")) {
//						x2=e.getX();
//						y2=e.getY();
//						stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
//						Shape roundrect = new RoundRectangle2D.Double(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x1-x2),Math.abs(y1-y2),25,25);
//						int x[] = {Math.min(x1, x2),Math.max(x1, x2)};
//						int y[] = {Math.min(y1, y2),Math.max(y1, y2)};
//						SaveDraw roundrect2d = new SaveDraw(roundrect,color,Gradientcolor,thickness,FillFlag,GradientFlag,x,y);
//						points.add(roundrect2d);
//						if(points.size()>=3)		
//						points.remove(points.size()-3);
//						if(points.size()>2)
//						repaint();					
//					}				
			}
			
			@Override
			public void mouseMoved(MouseEvent e)
			{

				statusBar.setText(String.format("��Ц�m : (%d,%d)", e.getX(),e.getY()));				
			}
			}
		);
		
		paintPanel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)//��ƹ�������
			{			  
				x1=e.getX();//���o�ƹ����U�ɪ�X�y��
				y1=e.getY();//���o�ƹ����U�ɪ�Y�y��
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				x2=e.getX();
				y2=e.getY();
				//�P�_��e���e���Ҧ�  �æs�J�۹������Ѽ�
				if(paintMode.equals("���u")) {
					if(FillFlag)
					{
						
						Shape line = new Line2D.Double(x1, y1, x2, y2);					
						SaveDraw line2d = new SaveDraw(line,color,Gradientcolor,thickness,FillFlag);
						points.add(line2d);
						repaint();
					}
					else{
						stroke = new BasicStroke( thickness, BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND,1,new float[]{25, 25},0f);
						Shape line = new Line2D.Double(x1, y1, x2, y2);					
						SaveDraw line2d = new SaveDraw(line,color,Gradientcolor,thickness,FillFlag,true);
						points.add(line2d);
						repaint();	
					}
				}
				else if(paintMode.equals("����")) {
					stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
					Shape ellipse = new Ellipse2D.Double(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x1-x2),Math.abs(y1-y2));
					int x[] = {Math.min(x1, x2),Math.max(x1, x2)};//���h�ݭn�ϥΨ쪺�Ѽ�
					int y[] = {Math.min(y1, y2),Math.max(y1, y2)};//
					SaveDraw ellipse2d = new SaveDraw(ellipse,color,Gradientcolor,thickness,FillFlag,GradientFlag,x,y);					
					points.add(ellipse2d);
					repaint();
				}
				else if(paintMode.equals("�x��")) {
					stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
					Shape rect = new Rectangle2D.Double(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x1-x2),Math.abs(y1-y2));
					int x[] = {Math.min(x1, x2),Math.max(x1, x2)};
					int y[] = {Math.min(y1, y2),Math.max(y1, y2)};
					SaveDraw rect2d = new SaveDraw(rect,color,Gradientcolor,thickness,FillFlag,GradientFlag,x,y);
					points.add(rect2d);
					repaint();
				}
				else if(paintMode.equals("�ꨤ�x��")) {
					stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
					Shape roundrect = new RoundRectangle2D.Double(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x1-x2),Math.abs(y1-y2),25,25);
					int x[] = {Math.min(x1, x2),Math.max(x1, x2)};
					int y[] = {Math.min(y1, y2),Math.max(y1, y2)};
					SaveDraw roundrect2d = new SaveDraw(roundrect,color,Gradientcolor,thickness,FillFlag,GradientFlag,x,y);
					points.add(roundrect2d);
					repaint();					
				}								
			}	
			
			public void mouseEntered(MouseEvent e){}
			
		@Override
			public void mouseExited(MouseEvent e){
			statusBar.setText("��Ц�m:�e���ϥ~");
			}
		}
				);		
	}
	
	//private inner class for ItemListener event handling
	private class CheckBoxHandler implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent event)
		{
			
			if(fillupCheckBox.isSelected())
			{
				System.out.println("��");
				FillFlag = true;
				if(!painttoolJComboBox.getSelectedItem().equals("���u"))
				GradientCheckBox.setEnabled(true);
			}
			else 
			{
				System.out.println("����");
				FillFlag = false;
				//GradientCheckBox.setEnabled(false);
			}	
			
		}
	}
	private class GradientBoxHandler implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent event) {
			if(GradientCheckBox.isSelected())
			{
				System.out.println("���h");
				GradientFlag = true;
				//JOptionPane.showMessageDialog(PaintFrame.this,"���I��   '�e����' �ӿ�ܺ��h�C��");	
				label2.setVisible(true);
			}
			else 
			{
				System.out.println("�������h");
				GradientFlag = false;
				label2.setVisible(false);
			}				
		}
	}

	//private inner class for Button event handling
	private class ButtonHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			//JOptionPane.showMessageDialog(PaintFrame.this, String.format("�A�I��F: %s",event.getActionCommand()));			
			if(event.getActionCommand()=="�e����")
			{
				color = JColorChooser.showDialog(PaintFrame.this, "���ø���C��", color);///////////////
				if(GradientCheckBox.isSelected()) {
					if(!paintMode.equals("����")&&!paintMode.equals("���u")) {
					Gradientcolor = JColorChooser.showDialog(PaintFrame.this, "��ܺ��h���C��", Gradientcolor);
					if(Gradientcolor == null)
						Gradientcolor = Color.WHITE;
					}
				}
				if(color == null)
					color = Color.BLACK;
				ForegroundButton.setBackground(color);
			}
			else if(event.getActionCommand()=="�I����") {
				Backcolor = JColorChooser.showDialog(PaintFrame.this, "Choose a color", Backcolor);///////////////
				
				if(Backcolor == null)//�P�_�ϥΪ̦��S�����C�� �S���h�]�w�I�����զ�
				{	
					paintPanel.setBackground(Color.WHITE);
					Backcolor=Color.WHITE;	
				}
				else paintPanel.setBackground(Backcolor);
				BackgroundButton.setBackground(Backcolor);												
			}
			else if(event.getActionCommand()=="�M��") {
				paintPanel.setBackground(Color.WHITE);	
				points.clear();
				Undopoints.clear();
				paintPanel.repaint();	
				BackgroundButton.setBackground(null);				
			}	
			else if(event.getActionCommand()=="�x�s�ɮ�") {				
				savefile.open();
				savefile.addRecord();
				savefile.closeFile();				
			}
			else if(event.getActionCommand()=="�}���ɮ�") {
				openfile.open();
				points.clear();
				openfile.readRecords();
				openfile.closeFile();
				paintPanel.setBackground(points.get(points.size()-1).getBackgroundcolor());
				points.remove(points.size()-1);
				repaint();
			}
			else if(event.getActionCommand()=="�W�@�B") {     
				try
				{
					//���q����{ "Undopoints�٦s����ƪ����p�U"�s�W�F�s��points �]�N�O�S�e�F�s���F��  �o�ɭ� ���ӭn��쥻�x�s�bUndopoints�̪���ƲM�� �M��s�J�s�e��points
					if(Undopoints.size()==0) {SavePoint=points.size();}//�p�Gundopoints�S���s�F�� �N�Npoints���ƶq�O���_��					
					//�p�G���U�W�@���� points���ƶq�[�Wundopoints���ƶq �j��Savepoints���� �N�N��ϥΪ̦bundopoints�٦s����ƪ����p�U�e�s�� ���ɭn�M��unpoints
					if(points.size()+Undopoints.size()>SavePoint) {						
						Undopoints.clear();
						Undopoints.add(points.get(points.size()-1));
						points.remove(points.size()-1);
						repaint();
						}
					else 
					{
					Undopoints.add(points.get(points.size()-1)); // add the last points into undopoints 
					points.remove(points.size()-1); //remove the last point from points
					repaint();
					}																									
				}								
				catch(IndexOutOfBoundsException indexOutOfBoundsException)
				{
					JOptionPane.showMessageDialog(PaintFrame.this, "�S���W�@�B��!!!");
				}
			}
			else if(event.getActionCommand()=="�U�@�B") {
				try
				{																
						points.add(Undopoints.get(Undopoints.size()-1)); //add the last undopoint into points
						Undopoints.remove(Undopoints.size()-1);//remvoe the last undopoints from undopoints
						repaint();					
				}
				//�w�g�S���U�@�B���ҥ~�B�z
				catch(IndexOutOfBoundsException indexOutOfBoundsException)
				{
					JOptionPane.showMessageDialog(PaintFrame.this, "�S���U�@�B��!!!");
				}
			}
		}
	}
	
	//private inner class for RadioButton event handling
	private class RadioButtonHandler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			//JOptionPane.showMessageDialog(PaintFrame.this, String.format("�A�I��F: %s",event.getActionCommand()));
			if(event.getActionCommand()=="�j") {thickness=12;
			Erasersize=150;}
			else if(event.getActionCommand()=="��") {thickness=8;
			Erasersize=90;}
			else if(event.getActionCommand()=="�p") {thickness=3;
			Erasersize=30;}
		}
	}
	private class paintPanel extends JPanel 
	{
		//override paintcomponent   ��s�bpoints �̭��� savepoint ����@�Ӥ@��Ū�� �M��Ū���ݩ� �̫�̾��ݩʱN�ϧεe�X��
		@Override
		 public void paintComponent(Graphics g) {
			 super.paintComponent(g);
			 Graphics2D g2 = (Graphics2D) g.create();
			 for(SaveDraw point:points)//�e�X�C�@�Ӥw�x�s���ϧ�
			 {
				if(point.getGradientflag()) //�ˬd�O�_�n�]�w���h 
				{
					g2.setPaint(new GradientPaint(point.getGradientx(0),point.getGradienty(0),point.getColor()!=null?point.getColor():Color.BLACK,point.getGradientx(1),point.getGradienty(1),point.getColor2()));
					g2.setStroke(new BasicStroke(point.getthick(), BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND));;
					g2.draw(point.getShape());
					if(point.fillflag) {g2.fill(point.getShape());}
				}
				else if(point.getBackgroundcolorflag()){}
				else if(point.getEraser())//�����
				{
					g2.setPaint(Backcolor);//��������C��n�ʺA�ᤩ�I����
					g2.draw(point.getShape());
					if(point.fillflag) {g2.fill(point.getShape());}//�ˬd�O�_�n��	
				}
				else if(point.getDottedlineflag()) 
				{
					g2.setPaint(point.getColor()); //Ū�C��	
					g2.setStroke(new BasicStroke( point.getthick(), BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND,1,new float[]{25, 25},0f));//Ū���u��
					g2.draw(point.getShape());//Ū���ϧ�
				}
				else if(point.getGradientflag()==false)
				{
				g2.setPaint(point.getColor()); //Ū�C��	
				g2.setStroke(new BasicStroke(point.getthick(), BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND));//Ū���u��
				g2.draw(point.getShape());//Ū���ϧ�
					if(point.fillflag) {g2.fill(point.getShape());}//�ˬd�O�_�n��															
				}
				
			 }

		 }
	}
	
	private class OpenFile{
		private void open() {
			JFileChooser chooser = new JFileChooser();
			 int result = chooser.showOpenDialog(null);
		      if (result == JFileChooser.APPROVE_OPTION) {
		    	  OpenName =chooser.getSelectedFile().getName();
		    	  OpenPath=chooser.getSelectedFile().toPath();
		      }
		      if (result == JFileChooser.CANCEL_OPTION) {
		        System.out.println("You pressed cancel");
		      
		      }
		      try {
		    	  input = new ObjectInputStream(Files.newInputStream(OpenPath));
		    	  
		      }			     
		      catch(IOException ioException)
			     {
			   	  System.err.println("Error opening file. Terminating");
			   	System.err.println(ioException.getMessage());
			   	  System.exit(1);
		      }					      
		}
		private  void readRecords()
		{
			
			try {
					
				while(true) {
				points.add(((SaveDraw)input.readObject())) ;
				}
			}
			catch(EOFException endOfFileException) {
				JOptionPane.showMessageDialog(null, "�}���ɮ�:"+OpenName);
				System.out.printf("�}���ɮ�:%s\n",OpenName);
				
			}
			catch(ClassNotFoundException classNotFoundException) {
				System.out.println("�ɮ׮榡���~");
			}
			catch(IOException ioException) {
				System.err.println(ioException.getMessage());
			}
		
		}
		private void closeFile() {
			try
			{
				if(input!=null)
				{
					
					input.close();
				}
			}
			catch(IOException ioException)
			{
				System.err.println("error closing file");
				System.exit(1);
			}
		}
			
	}
	
	private class SaveFile{
		public  void open()
		{
		  JFileChooser chooser = new JFileChooser();
		  int result = chooser.showSaveDialog(null);
//		  chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG | JFileChooser.DIRECTORIES_ONLY);
		  if (result == JFileChooser.APPROVE_OPTION) {
	    	  SaveName =chooser.getSelectedFile().getName();
	    	  
		   	  SavePath=chooser.getSelectedFile().toPath();
		     }
		     if (result == JFileChooser.CANCEL_OPTION) {
		       System.out.println("You pressed cancel");
//		       System.exit(1);
		     }
		     try // open file
	         {
		        output = new ObjectOutputStream(          
	            Files.newOutputStream(SavePath));
		     } 
		     catch (IOException ioException)
		     {
		        System.err.println("Error opening file.");
		        System.exit(1);
		     } 
	    }
		public void addRecord()
		{
			if(points!=null) {		
				SaveDraw backgroundcolor = new SaveDraw(Backcolor,true);
				points.add(backgroundcolor);
				for(Object point:points) {
					try {
					output.writeObject(point);
					}
				
					catch (NoSuchElementException elementException)
					{
						System.err.println("Invalid input Please try again");
					}
					catch (IOException ioException)
					{
						System.err.println("Error writing");
					
					}
					
				}
			}
		}
		
		public void closeFile()
		{
			try
			{
				if(output!=null)
				{
				JOptionPane.showMessageDialog(null, "�x�s�ɦW:"+SaveName);
				System.out.printf("�x�s�ɦW:%s\n", SaveName);
					output.close();
				}
			}
			catch(IOException ioException)
			{
				System.err.println("Error Closing file");
			}
		}
		
	}
	


	
}
//end class PaintFrame
