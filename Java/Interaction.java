package Java;
import java.io.*;
import java.time.Month;
import java.util.*;


public class Interaction {
    private String Ticker;
    private int IntervalIndex;
    private String Interval;
    private int MonthIndex = -1;
    private int StartMonthIndex;
    private int EndMonthIndex;
    private String Slice;
    private String StartSlice;
    private String EndSlice;
    private String Handle;
    private boolean forSMVI;
    private Stock stock;
    protected static ArrayList<String> allSlices = new ArrayList<String>();

    public Interaction(String Ticker, int IntervalIndex, int MonthIndex, String Handle, boolean forSMVI) throws IOException {
        this.Ticker = Ticker;
        this.IntervalIndex = IntervalIndex;
        this.MonthIndex = MonthIndex;
        System.out.println("Month Index= "+MonthIndex);
        this.Handle = Handle;
        this.forSMVI = forSMVI;
    }

    public Interaction(String Ticker, int IntervalIndex, int StartMonthIndex, int EndMonthIndex, String Handle, boolean forSMVI) throws IOException {
        this.Ticker = Ticker;
        this.IntervalIndex = IntervalIndex;
        this.StartMonthIndex = StartMonthIndex;
        this.EndMonthIndex = EndMonthIndex;
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
            if(MonthIndex != -1){
                stock = new Stock(Ticker,Interval,Slice, forSMVI);
            }else{
                stock = new Stock(Ticker, Interval, StartSlice, EndSlice, forSMVI);
            }
            if(!forSMVI) print("Day SD of " + this.Ticker + ": " + stock.DayDeviation() + "\nDaily Average of " + this.Ticker + ": " + stock.avgDayPrice());
        } catch(Exception e){System.err.println(e + e.getLocalizedMessage());}
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
        for(int i = 0; i < 24; i++){
            String year = "year";
            if(i < 12) year+="1";
            else if(i < 24) year += "2";
            String month = "month";
            if(i < 12) month += i+1;
            else if(i < 24) month += i-11;
            allSlices.add(year+month);
        }
        if(MonthIndex != 1){ //Check if we only have one month given
            Slice = allSlices.get(MonthIndex); 
        }else{ //Runs if we have two dates for the months
            StartSlice = allSlices.get(StartMonthIndex);
            EndSlice = allSlices.get(EndMonthIndex);
        }
        System.out.println("parsed Slice");
    }

    private void print(String data){
        System.out.println("Started print");
        gui.WriteText(data);
    }

    private void writeHandles(){
        if(forSMVI) return;
        try{
            File file = new File(gui.absPath + "Handles.csv");
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
