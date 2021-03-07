package GUI;

import java.util.*;
import java.io.*;

/**
 * A class to read the file "Images//Images.json", and parse the data into a usable 2D array containing labels, positions, and photo names
 */

public class imageReader {

    private static String rawData;
    private static String[][] parsedData;
    public static ArrayList<Hashtable<String,String>> finalData = new ArrayList<Hashtable<String,String>>();
    public static void main(String[] args) {
        //Used for testing
        Run();
    }

    public static void Run() {
        getData();
        parseData();
    }

    private static void getData(){
        rawData = "";
        File file;
        try{
            file = new File("Images//Images.json");
            Scanner scanner = new Scanner(file);
            String line;
            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                rawData += line;
                
            }
            rawData = rawData.replaceAll("\\s+","");
            System.out.println(rawData);
        }catch(Exception e){System.out.println(e);}
    }

    private static void parseData(){
        String[] split = rawData.substring(rawData.indexOf("[")+1,rawData.indexOf("]")).replaceAll("\\{","").split("},"); //Remove all unnecessary brackets
        parsedData = new String[split.length][3];
        for(int i = 0; i < split.length; i++){
            parsedData[i] = split[i].replaceAll("}","").replaceAll("\"","").split(",");
        }
        for(String[] sa : parsedData){
            Hashtable <String,String> tempDict = new Hashtable<>();
            for(String s : sa){
                String[] dict = s.split(":");
                tempDict.put(dict[0],dict[1]);
            }
            finalData.add(tempDict);
        }
    }
}
