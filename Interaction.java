import java.io.IOException;

public class Interaction {
    private String Ticker;
    private String Interval;
    private int MonthIndex;
    private String Slice;

    Interaction(String Ticker, String Interval, int MonthIndex) throws IOException {
        this.Ticker = Ticker;
        this.Interval = Interval;
        this.MonthIndex = MonthIndex;
    
    }

    public void run(){
        try{
            parseTicker();
            parseInterval();
            parseSlice();
            Stock stock = new Stock(Ticker,Interval,Slice);
            print("Day SD of " + this.Ticker + ": " + stock.DayDeviation() + "\nDaily Average of " + this.Ticker + ": " + stock.avgDayPrice());
        } catch(Exception e){System.err.println(e);}
    }

    private void parseTicker(){
        Ticker = Ticker.replaceAll("\\s","");
        Ticker = Ticker.toUpperCase();
    }

    private void parseInterval() {
        int[] intOptions = new int[]{1, 5, 15, 30, 60};
        String[] stringOptions = new String[]{"daily", "weekly", "monthly"};
        try {
            int interval = Integer.parseInt(Interval);
            for (int o : intOptions) {
                if (o == interval) return;
            }
        }catch(Exception E){
            Interval = Interval.toLowerCase();
            for(String o : stringOptions){
                if(Interval.equals(o)) return;
            }
        }
        throw new Error("Interval Parsing Error");
    }

    private void parseSlice(){
        String year = "year";
        if(MonthIndex < 12) year+="1";
        else if(MonthIndex < 24) year += "2";
        String month = "month";
        if(MonthIndex < 12) month += MonthIndex+1;
        else if(MonthIndex < 24) month += MonthIndex-11;
        Slice = year+month;
    }

    private void print(String data){
        gui.WriteText(data);
    }
}
