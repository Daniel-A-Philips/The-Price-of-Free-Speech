package GUI;

import processing.core.*;
import java.util.*;

public class ImageDropdown extends PApplet {
    protected String label;
    protected int[] topLeft; //[X,Y]
    protected int[] Size; //[X,Y]
    protected String imageString;
    public boolean isClicked;
    public boolean isOpen = false;
    protected String[] Labels;
    public ArrayList<ImageButton> Buttons = new ArrayList<ImageButton>();
    protected final ArrayList<ImageButton> ButtonsUnchanged;
    protected ArrayList<ImageButton> toShow = new ArrayList<ImageButton>();
    protected String defaultLabel;
    protected ImageButton previouslySelected;
    protected ImageButton currentSelected;
    protected int ymargin = 15;
    public ArrayList<int[]> allTopLefts = new ArrayList<int[]>();

    public ImageDropdown(){
        int a = 0;
        ButtonsUnchanged = new ArrayList<>();
    }

    public ImageDropdown(String label ,int[] topLeft, int[] Size, String[] Labels, String defaultLabel){
        this.label = label;
        this.topLeft = topLeft;
        this.Size = Size;
        this.Labels = Labels;
        this.defaultLabel = defaultLabel;
        createButtons();
        ButtonsUnchanged = Buttons;
    }

    private void createButtons(){
        topLeft = new int[]{topLeft[0],topLeft[1]+Size[1]+ymargin};
        System.out.println(topLeft[1]);
        for(String lbl : Labels){
            allTopLefts.add(topLeft);
            topLeft = new int[]{topLeft[0],topLeft[1]-ymargin-Size[1]};
            String standardImage = "Images//Dropdown//"+label+"//"+lbl+"1.png";
            String clickedImage = "Images//Dropdown//"+label+"//"+lbl+"2.png";
            ImageButton button = new ImageButton(lbl,topLeft,Size,standardImage,clickedImage);
            Buttons.add(button);
        }
        currentSelected = Buttons.get(0);
        toShow.add(currentSelected);
        previouslySelected = currentSelected;
    }

    public void updatePositions(ImageButton clicked){
        int IndexOfClickedUnchanged = ButtonsUnchanged.indexOf(clicked);
        ArrayList<ImageButton> temp = new ArrayList<>();
        temp.add(ButtonsUnchanged.get(IndexOfClickedUnchanged));
        ArrayList<ImageButton> unchanged = new ArrayList<>();
        unchanged.addAll(ButtonsUnchanged);
        ArrayList<int[]> coordinates = new ArrayList<>();
        for(ImageButton btn : Buttons){
            coordinates.add(btn.getCoordinates());
        }
        unchanged.remove(IndexOfClickedUnchanged);
        temp.addAll(unchanged);
        for(int i = 0; i < temp.size(); i++) {
            ImageButton button = temp.get(i);
            button.setCoordinates(coordinates.get(i));
            temp.set(i,button);
        }
        Buttons = temp;
        currentSelected = Buttons.get(0);
        toShow = new ArrayList<>();
        toShow.add(Buttons.get(0));

    }


    public ImageButton getCurrentButton() {
        return currentSelected;
    }

    public ArrayList<ImageButton> getToShow(){
        if(isClicked){
            toShow = Buttons;
        }else{
            toShow.clear();
            toShow.add(Buttons.get(0));
        }
        return toShow;
    }

}
