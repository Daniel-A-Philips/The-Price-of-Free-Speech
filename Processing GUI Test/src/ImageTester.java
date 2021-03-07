import GUI.ImageButton;
import GUI.ImageDropdown;
import GUI.ImageTextBox;
import GUI.imageReader;
import GUI.Text;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class ImageTester extends PApplet {

    /*
        To create buttons, use https://www.clickminded.com/button-generator/ where a clicked button has a border of size 5, width of 200, height of 50, open sans, size 26 bold
    */

    public ArrayList<ImageButton> buttons = new ArrayList<ImageButton>();
    public ArrayList<ImageTextBox> textBoxes = new ArrayList<ImageTextBox>();
    public ArrayList<ImageDropdown> dropdowns = new ArrayList<ImageDropdown>();
    public ArrayList<Text> texts = new ArrayList<>();
    private ImageDropdown currentDropdown;
    private PFont font;

    public static void main(String[] args){
        PApplet.main("ImageTester");
    }



    @Override
    public void setup(){
        font = createFont("lib\\OpenSans-Bold.ttf",26);
        textFont(font);
        fill(0);
        createObjects();
	}

    private void createObjects(){
        String label;
        int[] coordinates = new int[2];
        int[] Size = new int[2];
        PImage standardImage;
        PImage clickedImage;
        imageReader.Run();
        for( Hashtable dict : GUI.imageReader.finalData){
            String type = (String) dict.get("type");
            label = (String)dict.get("label");
            try{
                coordinates =  toIntArray((String)dict.get("coordinates"));
            } catch(Exception e){ System.out.println("No position data was given for \"" + label + "\""); }
            switch(type){
                case "Button":
                    standardImage = loadImage("Images//"+type+"//"+dict.get("standard_Image"));
                    clickedImage = loadImage("Images//"+type+"//"+dict.get("clicked_Image"));
                    buttons.add(new GUI.ImageButton(label,coordinates,Size,standardImage,clickedImage));
                    break;

                case "Textbox":
                    standardImage = loadImage("Images//"+type+"//"+dict.get("standard_Image"));
                    clickedImage = loadImage("Images//"+type+"//"+dict.get("clicked_Image"));
                    textBoxes.add(new GUI.ImageTextBox(label,coordinates,Size,font.getSize(),standardImage,clickedImage));
                    break;

                case "Dropdown":
                    String[] labels = ((String)dict.get("labels")).split("~");
                    dropdowns.add(new GUI.ImageDropdown(label, coordinates, Size, labels, labels[0]));
                    break;

                case "Text":
                    texts.add(new Text(label,coordinates));
                    break;

                default:
                    System.out.println("Error\n\""+type+"\" is not a valid type of object");
                    System.exit(0);
            }
        }
    }



    public int[] toIntArray(String sa) throws Exception{
        return Arrays.asList(sa.split("x")).stream().mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public void settings() {
		size(900,900);
	}


    @Override
    public void draw(){
        background(255);
        boolean showAll = true;
        for(ImageDropdown dropdown : dropdowns) if(dropdown.isOpen) showAll = false;
        if(showAll){
            showButtons();
            showTextBoxes();
            showText();
        }
        showDropdownSelected();
    }

    public void showText(){
        for(Text text : texts){
            text(text.getText(),text.getCoordinates()[0],text.getCoordinates()[1]);
        }
    }

    public void showTextBoxes(){
        for(ImageTextBox textbox : textBoxes){
            image(textbox.getImage(),textbox.getCoordinates()[0],textbox.getCoordinates()[1]);
            int[] pos = textbox.textBoxPosition();
            text(textbox.getText(),pos[0],pos[1]);
        }
    }

    public void showButtons(){
        for(ImageButton test : buttons){
            image(test.getImage(),test.getCoordinates()[0],test.getCoordinates()[1]);
        }
    }

    public void showDropdownSelected(){
        for(int i = 0; i < dropdowns.size(); i++){
            ImageDropdown dropdown = dropdowns.get(i);
            ArrayList<ImageButton> btnlst = dropdown.getToShow();
            for (ImageButton btn : btnlst){
                PImage image = loadImage(btn.getImagePath());
                image(image, btn.getCoordinates()[0], btn.getCoordinates()[1]);
            }
            dropdowns.set(i,dropdown);
        }
    }

    public void mousePressed(){
        int[] mousePos = new int[]{mouseX,mouseY};
        for(ImageButton test : buttons){
            boolean isClicked = test.Clicked(mousePos);
            if(isClicked){
                switch(test.getLabel()){
                    case "Run":
                        break;

                    case "Clear":
                        break;
                }
            }
        }
        for(ImageTextBox textbox : textBoxes){
            boolean isClicked = textbox.Clicked(mousePos);
            if(!isClicked && textbox.getText().equals("")){
                textbox.resetText();
            }
        }
        for(int i = 0; i < dropdowns.size(); i++) {
            ImageDropdown dropdown = dropdowns.get(i);
            if (dropdown.isOpen) {
                ArrayList<ImageButton> buttonList = dropdown.Buttons;
                for (ImageButton button : buttonList) {
                    if (button.Clicked(mousePos)) {
                        dropdown.updatePositions(button);
                        dropdown.isClicked = false;
                        dropdown.isOpen = false;
                        break;
                    }
                }
            }else{
                if(!dropdown.isOpen && dropdown.getCurrentButton().Clicked(mousePos)){
                    dropdown.isClicked = true;
                    dropdown.isOpen = true;
                }
            }
            dropdowns.set(i,dropdown);
        }

    }

    @Override
    public void keyPressed(){
        for(ImageTextBox textbox : textBoxes){
            if(textbox.isClicked){
                textbox.editText(key,keyCode);
            }
        }
    }

    @Override
    public void mouseReleased(){
        for(ImageButton test : buttons){
            test.setImageStandard();
        }
    }


}
