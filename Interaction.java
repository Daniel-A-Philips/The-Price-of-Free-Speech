import java.io.IOException;

public class Interaction {
    private String Ticker;
    private int IntervalIndex;
    private String Interval;
    private int MonthIndex;
    private String Slice;
    private String Handle;

    Interaction(String Ticker, int IntervalIndex, int MonthIndex, String Handle) throws IOException {
        this.Ticker = Ticker;
        this.IntervalIndex = IntervalIndex;
        this.MonthIndex = MonthIndex;
        this.Handle = Handle;
    
    }

    public void run(){
        try{
            parseTicker();
            parseInterval();
            parseSlice();
            System.out.println("created stock");
            Stock stock = new Stock(Ticker,Interval,Slice);
            System.out.println("finalized stock");
            print("Day SD of " + this.Ticker + ": " + stock.DayDeviation() + "\nDaily Average of " + this.Ticker + ": " + stock.avgDayPrice());
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
        System.out.println("printed");
    }
}
