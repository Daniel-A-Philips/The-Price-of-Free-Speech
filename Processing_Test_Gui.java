import processing.core.*;

import java.awt.Color;

import controlP5.*;

public class Processing_Test_Gui extends PApplet {

	public ControlP5 cp5;
	public ControlFont font1;
	private String[] Labels = new String[]{"Ticker","Interval","Month"};
	private int[] labelSizes = new int[] {100,100,100};
	private int[] startPosition;
	protected int currentXPos;
	protected int XPixels = 800;
	protected int YPixels = 600;
	protected int[] spaceBetweenLabels = {50,50,50};

	private void createInputs(){
		for(int i = 0; i < Labels.length; i++){
			cp5.addTextfield(Labels[i],startPosition[0],startPosition[1],labelSizes[i],60);
			startPosition[0] += labelSizes[i] + spaceBetweenLabels[i] ;
		}
		currentXPos = startPosition[0];
	}

	@Override
	public void setup(){
		startPosition = new int[] {30,height-100};
		font1 = new ControlFont(createFont("Arial",12));
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
		background(255);
		cp5.setFont(new ControlFont(createFont("Verdana",20,true)));
		cp5.setColorCaptionLabel(0);

	}
  
	public static void main(String[] args) {
		PApplet.main("Processing_Test_Gui");
    }
}