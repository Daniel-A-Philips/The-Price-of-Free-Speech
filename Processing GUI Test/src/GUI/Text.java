package GUI;

public class Text {
    private String TEXT;
    private int[] COORDINATES;

    public Text(String TEXT, int[] COORDINATES){
        this.TEXT = TEXT;
        this.COORDINATES = COORDINATES;
    }

    public String getText(){
        return TEXT;
    }

    public int[] getCoordinates(){
        return COORDINATES;
    }

}
