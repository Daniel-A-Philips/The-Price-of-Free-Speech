package GUI;

public class Const {

    public final int a;
    public final String b;

    public Const(int a){
        this.a = a;
        this.b = "";
    }

    public Const(String b){
        this.b = b;
        this.a = 0;
    }

    public String getString(){
        return b;
    }

    public int getInt(){
        return a;
    }
}
