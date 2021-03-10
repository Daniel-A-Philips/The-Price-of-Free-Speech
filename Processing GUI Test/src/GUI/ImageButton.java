package GUI;

import processing.core.*;


public class ImageButton{
    protected String label;
    protected int[] topLeft; //[X,Y]
    protected int[] Size; //[X,Y]
    protected String imageString;
    protected PImage standardImage;
    protected String standardImagePath;
    protected PImage clickedImage;
    protected String clickedImagePath;
    public PImage currentSelected;
    protected String currentSelectedPath;
    protected int VERSION;


    public ImageButton(){
        int a = 0;
    }
    public ImageButton(String label ,int[] topLeft, int[] Size, PImage standardImage, PImage clickedImage){
        this.label = label;
        this.topLeft = topLeft;
        this.Size = Size;
        this.standardImage = standardImage;
        this.clickedImage = clickedImage;
        currentSelected = standardImage;
        VERSION = 0;
    }

    ImageButton(String label ,int[] topLeft, int[] Size, String standardImagePath, String clickedImagePath){
        this.label = label;
        this.topLeft = topLeft;
        this.Size = Size;
        this.standardImagePath = standardImagePath;
        this.clickedImagePath = clickedImagePath;
        currentSelectedPath = standardImagePath;
        VERSION = 1;
    }

    public String getLabel(){
        return label;
    }

    public boolean Clicked(int[] pos){
        for(int i = 0; i < 2; i++){ //Runs through X check then Y check
            if(pos[i] < topLeft[i] || pos[i] > topLeft[i]+Size[i]){
                return false;
            }
        }
        currentSelected = clickedImage;
        return true;
    }

    public void setImageStandard(){
        currentSelected = standardImage;
    }

    public void setImagePathStandard(){
        currentSelectedPath = standardImagePath;
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

    public String getImagePath(){
        return currentSelectedPath;
    }

    public void setCoordinates(int[] topLeft) { this.topLeft = topLeft; }

}
