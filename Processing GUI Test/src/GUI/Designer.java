package GUI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Designer {

    private static FileWriter file;
    private static JSONObject obj = new JSONObject();

    public static void main(String[] args) throws IOException {
        file = new FileWriter("Images//JSONTest.json");
    }
    
    public static void write(){

    }
}
