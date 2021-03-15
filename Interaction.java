import java.io.*;
import java.util.*;

public class Interaction {
    private String Ticker;
    private int IntervalIndex;
    private String Interval;
    private int MonthIndex;
    private String Slice;
    private String Handle;
    private boolean forSMVI;
    private Stock stock;

    Interaction(String Ticker, int IntervalIndex, int MonthIndex, String Handle, boolean forSMVI) throws IOException {
        this.Ticker = Ticker;
        this.IntervalIndex = IntervalIndex;
        this.MonthIndex = MonthIndex;
        this.Handle = Handle;
        this.forSMVI = forSMVI;
    
    }

    public void run(){
        try{
            parseTicker();
            parseInterval();
            parseSlice();
            writeHandles();
            System.out.println("created stock");
            stock = new Stock(Ticker,Interval,Slice, forSMVI);
            if(!forSMVI) print("Day SD of " + this.Ticker + ": " + stock.DayDeviation() + "\nDaily Average of " + this.Ticker + ": " + stock.avgDayPrice());
        } catch(Exception e){System.err.println(e + "\nThis error occured in the \"run\" method in \"Interaction\"");}
    }

    private void parseTicker(){
        Ticker = Ticker.replaceAll("\\s","");
        Ticker = Ticker.toUpperCase();
        System.out.println("parsed Ticker");
    }

    private void parseInterval() {
        String[] stringOptions = new String[]{"1","5","15","30","60"};
        Interval = stringOptions[IntervalIndex];
        System.out.println("parsed Interval");
    }

    private void parseSlice(){
        String year = "year";
        if(MonthIndex < 12) year+="1";
        else if(MonthIndex < 24) year += "2";
        String month = "month";
        if(MonthIndex < 12) month += MonthIndex+1;
        else if(MonthIndex < 24) month += MonthIndex-11;
        Slice = year+month;
        System.out.println("parsed Slice");
    }

    private void print(String data){
        System.out.println("Started print");
        gui.WriteText(data);
    }

    private void writeHandles(){
        if(forSMVI) return;
        try{
            File file = new File("Data\\Handles.csv");
            FileWriter writer = new FileWriter(file);
            Handle = Handle.replaceAll("\\s+","");
            String[] Handles = Handle.split(",");
            writer.write("Handle,ID\n");
            for(String s : Handles){
                writer.write(s+"\n");
            }
            writer.close();
        }catch(Exception e){System.out.println(e);}
    }

    public int getNumberOfDataPoints(){
        return stock.RawData.size()-1;
    }

    public ArrayList<String[]> getRawData(){
        return stock.RawData;
    }

    public double getVariation(ArrayList<String[]> a){
        return stock.getVariation(a);
    }
}
