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

 
//資管3A 古佳峻 104403019
public class PaintFrame extends JFrame{
	

	private static final long serialVersionUID = -4067285394523727922L;
	private final BorderLayout layout;
	private final JLabel label1; //繪圖工具label
	private final JLabel label2;
	private final JLabel label3;
	private final JLabel label4; //筆刷大小label
	private final JLabel label5; //工具區label	
	private final JComboBox<String> painttoolJComboBox; 	
	private final JPanel toolPanel;
	private static final String[] painttools = 
		{"筆刷","橡皮擦","直線","橢圓形","矩形","圓角矩形"};	
	private final JRadioButton smallJRadioButton;
	private final JRadioButton mediumJRadioButton;
	private final JRadioButton bigJRadioButton;
	private final ButtonGroup radioGroup;	
	private final JCheckBox fillupCheckBox;	
	private final JCheckBox GradientCheckBox;	
	private final JButton ForegroundButton;
	private final JButton BackgroundButton;
	private final JButton ClearButton;	
	private paintPanel paintPanel; //顯示滑鼠狀態的panel
	private JLabel statusBar;  //顯示滑鼠位置的label	
	static Color color = Color.BLACK;;/////////////
	private Color Backcolor = Color.WHITE;//////////
	static Color Gradientcolor = Color.CYAN;
	public static String paintMode="筆刷";
	private int thickness = 4;
	private BasicStroke stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_MITER, 10);//設定線條
	static boolean FillFlag = true;//填滿旗標
	static boolean GradientFlag = false;//漸層旗標
	int SolidLine=0;//實線=0  虛線=1
	int DrawFlag =1;//繪圖旗標
	int x1,x2,y1,y2;//滑鼠座標
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
		super("小畫家");
		layout = new BorderLayout();		
		setLayout(layout) ;
		
		//建立JLabels
		label1 = new JLabel("[繪圖工具]");
		label1.setFont(new Font("黑體",Font.BOLD,18));
		label2 = new JLabel("請使用前景色來選擇漸層色",JLabel.CENTER);		
		label2.setFont(new Font("黑體",Font.BOLD,26));
		label3 = new JLabel("狀態列");	
		label3.setFont(new Font("黑體",Font.BOLD,16));
		label4 = new JLabel("[筆刷大小]");
		label4.setFont(new Font("黑體",Font.BOLD,18));
		label5 = new JLabel("[工具區]");
		label5.setFont(new Font("黑體",Font.BOLD,18));
		add(label2,BorderLayout.NORTH);
		label2.setVisible(false);
		add(label3,BorderLayout.SOUTH);	
		//建立JComboBox 以及建立Listener
		painttoolJComboBox = new JComboBox<String>(painttools);//設定繪圖工具的comboBox
		painttoolJComboBox.setMaximumRowCount(4);	//一次最多顯示三種工具
		painttoolJComboBox.setSize(100, 30);
		painttoolJComboBox.setFont(new Font("黑體",Font.BOLD,18));
		painttoolJComboBox.addItemListener(
				new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent event) 
					{
						if(event.getStateChange()==ItemEvent.SELECTED) 
						//	JOptionPane.showMessageDialog(PaintFrame.this, String.format("你點選了: %s",painttoolJComboBox.getSelectedItem()));	
						paintMode = (String)painttoolJComboBox.getSelectedItem();
						if (painttoolJComboBox.getSelectedItem().equals("筆刷")||painttoolJComboBox.getSelectedItem().equals("橡皮擦")) {
							
							fillupCheckBox.setEnabled(false);
							GradientCheckBox.setEnabled(false);
						}
						else if(painttoolJComboBox.getSelectedItem().equals("直線")) {
							fillupCheckBox.setEnabled(true);
							GradientCheckBox.setEnabled(false);
						}
				
						else 
							{fillupCheckBox.setEnabled(true);
						    GradientCheckBox.setEnabled(true);}
					}
				}
		);
		
		//建立radioButtons			
		smallJRadioButton = new JRadioButton("小",true);              
		smallJRadioButton.setFont(new Font("黑體",Font.BOLD,18));   
		smallJRadioButton.setActionCommand("小");
		mediumJRadioButton =  new JRadioButton("中",false);
		mediumJRadioButton.setFont(new Font("黑體",Font.BOLD,18));
		mediumJRadioButton.setActionCommand("中");
		bigJRadioButton = new JRadioButton("大",false);
		bigJRadioButton.setFont(new Font("黑體",Font.BOLD,18));
		bigJRadioButton.setActionCommand("大");
		
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
		
		//建立填滿checkBox
		fillupCheckBox = new JCheckBox("填滿");
		fillupCheckBox.setFont(new Font("黑體",Font.BOLD,18));
		fillupCheckBox.setSelected(true);
		CheckBoxHandler Boxhandler = new CheckBoxHandler();
		fillupCheckBox.addItemListener(Boxhandler);		
		fillupCheckBox.setEnabled(false); //禁用 checkbox
		//建立漸層checkBox
		GradientCheckBox = new JCheckBox("漸層");
		GradientCheckBox.setFont(new Font("黑體",Font.BOLD,18));
		GradientCheckBox.setSelected(false);
		GradientBoxHandler Gradienthandler = new GradientBoxHandler();
		GradientCheckBox.addItemListener(Gradienthandler);	
		GradientCheckBox.setEnabled(false);
		//建立前景、背景、清除button
		ForegroundButton = new JButton("前景色");
		BackgroundButton = new JButton("背景色");
		ClearButton = new JButton("清除");
		UndoButton = new JButton("上一步");
		RedoButton = new JButton("下一步");
		OpenButton = new JButton("開啟檔案");
		SaveButton = new JButton("儲存檔案");
		
		//create new ButtonHandler for button event handling
		ButtonHandler handler = new ButtonHandler();
		ForegroundButton.addActionListener(handler);
		BackgroundButton.addActionListener(handler);
		ClearButton.addActionListener(handler);
		OpenButton.addActionListener(handler);
		SaveButton.addActionListener(handler);
		UndoButton.addActionListener(handler);	
		RedoButton.addActionListener(handler);			
		//建立用來放置左側繪圖工具的panel
		toolPanel = new JPanel();
		toolPanel.setLayout(new GridLayout(16,1));//設定 Panel的編排方式
		toolPanel.setPreferredSize(new Dimension(100,700));
		toolPanel.setMaximumSize(new Dimension(100, 700));
		
		//將工具選項逐一加入JPanel內
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
		//用來繪圖以及顯示滑鼠狀態的Panel
		paintPanel = new paintPanel();
		paintPanel.setOpaque(true);
		add(paintPanel,BorderLayout.CENTER); //add Panel to JFrame
		paintPanel.setBackground(Color.WHITE);//set background color --> white
		statusBar = new JLabel("游標位置");
		add(statusBar,BorderLayout.SOUTH); //add label to JFrame

		//create and register listener for mouse event 		
		paintPanel.addMouseMotionListener(new MouseMotionAdapter()
		{			
			@Override 
			public void mouseDragged(MouseEvent e)
			{
		
		    	statusBar.setText(String.format("游標位置 : (%d,%d)", e.getX(),e.getY()));
			
					if(paintMode.equals("筆刷"))
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
					if(paintMode.equals("橡皮擦"))
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
					//   尚待修正的功能  邊畫圖邊產生預覽
//					if(paintMode.equals("橢圓形")) {
//						x2=e.getX();
//						y2=e.getY();
//						stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
//						Shape ellipse = new Ellipse2D.Double(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x1-x2),Math.abs(y1-y2));
//						int x[] = {Math.min(x1, x2),Math.max(x1, x2)};//漸層需要使用到的參數
//						int y[] = {Math.min(y1, y2),Math.max(y1, y2)};//
//						SaveDraw ellipse2d = new SaveDraw(ellipse,color,Gradientcolor,thickness,FillFlag,GradientFlag,x,y);
//						if(points.size()>=1)
//						points.add(ellipse2d);
//						if(points.size()>=3)		
//						points.remove(points.size()-2);
//						if(points.size()>1)
//						repaint();
//					}
//					if(paintMode.equals("矩形")) {
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
//					if(paintMode.equals("圓角矩形")) {
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

				statusBar.setText(String.format("游標位置 : (%d,%d)", e.getX(),e.getY()));				
			}
			}
		);
		
		paintPanel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)//當滑鼠按壓時
			{			  
				x1=e.getX();//取得滑鼠按下時的X座標
				y1=e.getY();//取得滑鼠按下時的Y座標
			}
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				x2=e.getX();
				y2=e.getY();
				//判斷當前的畫筆模式  並存入相對應的參數
				if(paintMode.equals("直線")) {
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
				else if(paintMode.equals("橢圓形")) {
					stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
					Shape ellipse = new Ellipse2D.Double(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x1-x2),Math.abs(y1-y2));
					int x[] = {Math.min(x1, x2),Math.max(x1, x2)};//漸層需要使用到的參數
					int y[] = {Math.min(y1, y2),Math.max(y1, y2)};//
					SaveDraw ellipse2d = new SaveDraw(ellipse,color,Gradientcolor,thickness,FillFlag,GradientFlag,x,y);					
					points.add(ellipse2d);
					repaint();
				}
				else if(paintMode.equals("矩形")) {
					stroke = new BasicStroke( thickness,  BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND);
					Shape rect = new Rectangle2D.Double(Math.min(x1, x2),Math.min(y1, y2),Math.abs(x1-x2),Math.abs(y1-y2));
					int x[] = {Math.min(x1, x2),Math.max(x1, x2)};
					int y[] = {Math.min(y1, y2),Math.max(y1, y2)};
					SaveDraw rect2d = new SaveDraw(rect,color,Gradientcolor,thickness,FillFlag,GradientFlag,x,y);
					points.add(rect2d);
					repaint();
				}
				else if(paintMode.equals("圓角矩形")) {
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
			statusBar.setText("游標位置:畫布區外");
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
				System.out.println("填滿");
				FillFlag = true;
				if(!painttoolJComboBox.getSelectedItem().equals("直線"))
				GradientCheckBox.setEnabled(true);
			}
			else 
			{
				System.out.println("不填滿");
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
				System.out.println("漸層");
				GradientFlag = true;
				//JOptionPane.showMessageDialog(PaintFrame.this,"請點選   '前景色' 來選擇漸層顏色");	
				label2.setVisible(true);
			}
			else 
			{
				System.out.println("取消漸層");
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
			//JOptionPane.showMessageDialog(PaintFrame.this, String.format("你點選了: %s",event.getActionCommand()));			
			if(event.getActionCommand()=="前景色")
			{
				color = JColorChooser.showDialog(PaintFrame.this, "選擇繪圖顏色", color);///////////////
				if(GradientCheckBox.isSelected()) {
					if(!paintMode.equals("筆刷")&&!paintMode.equals("直線")) {
					Gradientcolor = JColorChooser.showDialog(PaintFrame.this, "選擇漸層的顏色", Gradientcolor);
					if(Gradientcolor == null)
						Gradientcolor = Color.WHITE;
					}
				}
				if(color == null)
					color = Color.BLACK;
				ForegroundButton.setBackground(color);
			}
			else if(event.getActionCommand()=="背景色") {
				Backcolor = JColorChooser.showDialog(PaintFrame.this, "Choose a color", Backcolor);///////////////
				
				if(Backcolor == null)//判斷使用者有沒有選顏色 沒有則設定背景為白色
				{	
					paintPanel.setBackground(Color.WHITE);
					Backcolor=Color.WHITE;	
				}
				else paintPanel.setBackground(Backcolor);
				BackgroundButton.setBackground(Backcolor);												
			}
			else if(event.getActionCommand()=="清除") {
				paintPanel.setBackground(Color.WHITE);	
				points.clear();
				Undopoints.clear();
				paintPanel.repaint();	
				BackgroundButton.setBackground(null);				
			}	
			else if(event.getActionCommand()=="儲存檔案") {				
				savefile.open();
				savefile.addRecord();
				savefile.closeFile();				
			}
			else if(event.getActionCommand()=="開啟檔案") {
				openfile.open();
				points.clear();
				openfile.readRecords();
				openfile.closeFile();
				paintPanel.setBackground(points.get(points.size()-1).getBackgroundcolor());
				points.remove(points.size()-1);
				repaint();
			}
			else if(event.getActionCommand()=="上一步") {     
				try
				{
					//此段為實現 "Undopoints還存有資料的情況下"新增了新的points 也就是又畫了新的東西  這時候 應該要把原本儲存在Undopoints裡的資料清空 然後存入新畫的points
					if(Undopoints.size()==0) {SavePoint=points.size();}//如果undopoints沒有存東西 就將points的數量記錄起來					
					//如果按下上一部時 points的數量加上undopoints的數量 大於Savepoints的話 就代表使用者在undopoints還存有資料的情況下畫新的 此時要清空unpoints
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
					JOptionPane.showMessageDialog(PaintFrame.this, "沒有上一步啦!!!");
				}
			}
			else if(event.getActionCommand()=="下一步") {
				try
				{																
						points.add(Undopoints.get(Undopoints.size()-1)); //add the last undopoint into points
						Undopoints.remove(Undopoints.size()-1);//remvoe the last undopoints from undopoints
						repaint();					
				}
				//已經沒有下一步的例外處理
				catch(IndexOutOfBoundsException indexOutOfBoundsException)
				{
					JOptionPane.showMessageDialog(PaintFrame.this, "沒有下一步啦!!!");
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
			//JOptionPane.showMessageDialog(PaintFrame.this, String.format("你點選了: %s",event.getActionCommand()));
			if(event.getActionCommand()=="大") {thickness=12;
			Erasersize=150;}
			else if(event.getActionCommand()=="中") {thickness=8;
			Erasersize=90;}
			else if(event.getActionCommand()=="小") {thickness=3;
			Erasersize=30;}
		}
	}
	private class paintPanel extends JPanel 
	{
		//override paintcomponent   把存在points 裡面的 savepoint 物件一個一個讀取 然後讀取屬性 最後依據屬性將圖形畫出來
		@Override
		 public void paintComponent(Graphics g) {
			 super.paintComponent(g);
			 Graphics2D g2 = (Graphics2D) g.create();
			 for(SaveDraw point:points)//畫出每一個已儲存的圖形
			 {
				if(point.getGradientflag()) //檢查是否要設定漸層 
				{
					g2.setPaint(new GradientPaint(point.getGradientx(0),point.getGradienty(0),point.getColor()!=null?point.getColor():Color.BLACK,point.getGradientx(1),point.getGradienty(1),point.getColor2()));
					g2.setStroke(new BasicStroke(point.getthick(), BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND));;
					g2.draw(point.getShape());
					if(point.fillflag) {g2.fill(point.getShape());}
				}
				else if(point.getBackgroundcolorflag()){}
				else if(point.getEraser())//橡皮擦
				{
					g2.setPaint(Backcolor);//橡皮擦的顏色要動態賦予背景色
					g2.draw(point.getShape());
					if(point.fillflag) {g2.fill(point.getShape());}//檢查是否要填滿	
				}
				else if(point.getDottedlineflag()) 
				{
					g2.setPaint(point.getColor()); //讀顏色	
					g2.setStroke(new BasicStroke( point.getthick(), BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND,1,new float[]{25, 25},0f));//讀取線條
					g2.draw(point.getShape());//讀取圖形
				}
				else if(point.getGradientflag()==false)
				{
				g2.setPaint(point.getColor()); //讀顏色	
				g2.setStroke(new BasicStroke(point.getthick(), BasicStroke.CAP_ROUND , BasicStroke.JOIN_ROUND));//讀取線條
				g2.draw(point.getShape());//讀取圖形
					if(point.fillflag) {g2.fill(point.getShape());}//檢查是否要填滿															
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
				JOptionPane.showMessageDialog(null, "開啟檔案:"+OpenName);
				System.out.printf("開啟檔案:%s\n",OpenName);
				
			}
			catch(ClassNotFoundException classNotFoundException) {
				System.out.println("檔案格式錯誤");
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
				JOptionPane.showMessageDialog(null, "儲存檔名:"+SaveName);
				System.out.printf("儲存檔名:%s\n", SaveName);
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
