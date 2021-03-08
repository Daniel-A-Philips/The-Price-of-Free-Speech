package GUI;

import processing.core.*;

public class ImageTextBox extends PApplet{
    protected String label;
    protected int[] topLeft; //[X,Y]
    protected int[] Size; //[X,Y]
    protected String imageString;
    protected PImage standardImage;
    protected PImage clickedImage;
    protected PImage currentSelected;
    public boolean isClicked;
    protected int[] textTopLeft;
    protected int[] textTopRight;
    protected int margin;
    protected int ymargin;
    protected String text;
    protected int fontSize;
    protected boolean isOrignalLabel;

    public ImageTextBox(){
        int a = 0;
    }

    public ImageTextBox(String label, int[] topLeft, int[] Size, int fontSize, PImage standardImage, PImage clickedImage){
        this.label = label;
        this.topLeft = topLeft;
        this.Size = Size;
        this.fontSize = fontSize;
        this.standardImage = standardImage;
        this.clickedImage = clickedImage;
        currentSelected = standardImage;
        isClicked = false;
        text = label;
        isOrignalLabel = true;
        createTextBox();
    }

    public void createTextBox(){
        int[] center = new int[2];
        for(int i = 0; i < 2; i++){
            center[i] = topLeft[i]+(Size[i]/2);
        }
        textTopLeft = new int[2];
        textTopRight = new int[2];
        margin = 15;
        ymargin = 10;
        textTopLeft[0] = topLeft[0] + margin;
        textTopLeft[1] = topLeft[1] + fontSize + ymargin;
        textTopRight[0] = Size[0] - 2*margin;
        textTopRight[1] = Size[1] - 2*ymargin;
    }

    public void editText(char a, int keyCode){
        // keyCode = int
        // a = char
        if(isOrignalLabel){
            isOrignalLabel = false;
            text = "";
        }
        if(keyCode == SHIFT || a == ENTER){
            return;
        }
        if(a != BACKSPACE){
            if(text.length() < 12) {
                text += a;
            }
        }else{
            try{
                text = text.substring(0,text.length()-1);
            }catch(Exception e){}
        }
    }

    public void resetText(){
        text = label;
        isOrignalLabel = true;
    }

    public String getText(){
        return text;
    }

    public int[] textBoxPosition(){
        return new int[]{textTopLeft[0], textTopLeft[1]};
    }

    public boolean Clicked(int[] pos){
        for(int i = 0; i < 2; i++){ //Runs through X check then Y check
            if(pos[i] < topLeft[i] || pos[i] > topLeft[i]+Size[i]){
                currentSelected = standardImage;
                isClicked = false;
                return false;
            }
        }
        currentSelected = clickedImage;
        isClicked = true;
        return true;
    }

    public void setImageStandard(){
        currentSelected = standardImage;
    }

    public int[] getCoordinates(){
        return topLeft;
    }

    public int[] getSize(){
        return Size;
    }

    public PImage getImage(){
        return currentSelected;
    }

    public String getLabel(){
        return label;
    }
    
}
