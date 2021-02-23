import java.util.*;
import processing.core.*;
import controlP5.*;

public class Test extends PApplet {

	public ControlP5 cp5;
	public ControlFont font1;
	private String[] Labels = new String[]{"Ticker","Interval","Month"};
	private int[] startPosition;
	private int[] incrementPosition = new int[]{150,400};
	protected int XPixels = 800;
	protected int YPixels = 600;

	private void createInputs(){
		for(String label : Labels){
			cp5.addTextfield(label,startPosition[0],startPosition[1],incrementPosition[0]-20,60);
			startPosition[0] += incrementPosition[0];
		}
	}

	@Override
	public void setup(){
		startPosition = new int[]{30,height-100};
		font1 = new ControlFont(createFont("Arial",20));
		cp5 = new ControlP5(this);
		cp5.setFont(font1);
		createInputs();
	}

	@Override
	public void settings() {
		size(XPixels,YPixels);

	}

	@Override
	public void draw(){
		background(0);
		//cp5.setFont(new ControlFont(createFont("Verdana",20)));

	}
  
	public static void main(String[] args) {
		PApplet.main("Test");
    }
}