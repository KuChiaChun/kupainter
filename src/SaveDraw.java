import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.io.Serializable;

//¸êºÞ3A ¥j¨Î®m 104403019
public class SaveDraw implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -838349629824257319L;
	private Shape shape;
	private Color color;
	private Color color2;
	private Color Backgroundcolor;
	private BasicStroke basicstroke;
	boolean fillflag;
	boolean gradientflag;
	private int gradientx[];
	private int gradienty[];
	private boolean eraser ;
	private int thick;
	private boolean dottedlineflag;
	private boolean Backgroundcolorflag;
	public SaveDraw(Shape shape,Color color,Color color2,int thick,boolean fillflag,boolean gradientflag,int gradientx[],int gradienty[]){
		this.shape = shape;
		this.color = color;
		this.color2 = color2; 
		this.thick = thick;
		this.fillflag = fillflag;
		this.gradientflag = gradientflag;
		this.gradientx = gradientx;
		this.gradienty = gradienty;
	}
	public SaveDraw(Shape shape,Color color,Color color2,int thick,boolean fillflag){
		this.shape = shape;
		this.color = color;
		this.color2 = color2; 
		this.thick = thick;
		this.fillflag = fillflag;
	}
	public SaveDraw(Shape shape,Color color,Color color2,int thick,boolean fillflag,boolean dottedlineflag){
		this.shape = shape;
		this.color = color;
		this.color2 = color2; 
		this.thick = thick;
		this.fillflag = fillflag;
		this.dottedlineflag = true;
	}
	public SaveDraw(Shape shape,Color color,boolean fillflag,boolean eraser){
		this.shape = shape;
		this.color = color;
		this.fillflag = fillflag;
		this.eraser = eraser;
	}
	public SaveDraw(Color color,boolean Backgroundcolorflag)
	{
		this.Backgroundcolor = color;  
		this.Backgroundcolorflag = Backgroundcolorflag;
	}
	public Shape getShape() {
		return shape;
	}
	public Color getColor() {
		return color;
	}
	public Color getColor2() {
		return color2;
	}	
	public int getthick() {
		return thick;
	}
	public int getGradientx(int i) {
		return gradientx[i];
	}
	
	public int getGradienty(int i) {
		return gradienty[i];
	}
	public boolean getFillflag() {
		return fillflag;
	}
	public boolean getGradientflag() {
		return gradientflag;
	}
	public boolean getEraser() {
		return eraser;
	}
	public boolean getDottedlineflag() {
		return dottedlineflag;
	}
	public boolean getBackgroundcolorflag() {
		return Backgroundcolorflag;
	}
	public Color getBackgroundcolor() {
		return Backgroundcolor;
	}
	
}
