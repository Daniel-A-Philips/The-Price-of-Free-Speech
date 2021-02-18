import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
    APIs Used:
        Alpha Vantage: https://www.alphavantage.co/documentation/
 */

public class Stock {

    protected ArrayList<String[]> RawData = new ArrayList<>();
    protected ArrayList<String[]> DayData = new ArrayList<>();
    protected ArrayList<ArrayList<String[]>> WeekData = new ArrayList<>(); //Day,Data Select
    protected String IntervalTime;
    protected String Ticker;
    protected String Slice;
    protected String Time_Series = "INTRADAY_EXTENDED";
    protected String APIKey = "4HRPSUDJ4S8WR99F";
    protected double LatestOpeningPrice;
    protected double SevenDayOpeningPrice;

    public Stock(String Ticker, String IntervalTime, String Slice) throws IOException {
        this.IntervalTime = IntervalTime;
        this.Ticker = Ticker;
        this.Slice = Slice;
        if(IntervalTime.equals("")) this.IntervalTime = "5";
        if(Ticker.equals("")) this.Ticker = "IBM";
        if(Slice.equals("")) this.Slice = "year1month1";
        run();
    }

    public Stock() {
        IntervalTime = "5";
        Ticker = "IBM";
        Slice = "year1month1";
        try{
            run();
        }catch(Exception e){System.out.println(e);}
    }

    public void run() throws IOException {
        getHistory("TIME_SERIES_"+Time_Series,Ticker,IntervalTime+"min",Slice);
    }

    private void getHistory(String Time_Series, String Ticker, String Interval, String Slice) throws IOException {
        URL url = new URL("https://www.alphavantage.co/query?function="+Time_Series+"&symbol="+Ticker+"&interval="+Interval+"&slice="+Slice+"&apikey="+APIKey);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try
        {
            InputStream in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            File file = new File("Data.csv");
            FileWriter writer = new FileWriter(file);
            String line;
            while((line = reader.readLine()) != null) {
                writer.write(line+"\n");
            }
            writer.close();
        }
        catch(IOException E){System.out.println(E);}
        finally {urlConnection.disconnect();}
        RawData = parseData();
        DayData = GetCurrentDayData();
        WeekData = GetWeekData();
        LatestOpeningPrice = getLatestOpenPrice();
        SevenDayOpeningPrice = getSevenDayOpeningPrice();
    }

    private ArrayList<String[]> parseData() throws IOException {
        ArrayList<String[]> ParsedData = new ArrayList<String[]>();
        BufferedReader reader = new BufferedReader(new FileReader("Data.csv"));
        String line;
        while((line = reader.readLine()) != null){
            ParsedData.add(line.split(","));
        }
        return ParsedData;
    }

    public ArrayList<String[]> GetCurrentDayData(){
        ArrayList<String[]> DailyData = new ArrayList<>();
        DailyData.add(RawData.get(0));
        String CurrentDay = RawData.get(1)[0].substring(0,RawData.get(1)[0].indexOf(":")-3);
        for(int i = 1; i < RawData.size(); i++){
            if(RawData.get(i)[0].contains(CurrentDay)) DailyData.add(RawData.get(i));
            else break;
        }
        return DailyData;
    }

    public ArrayList<String[]> GetDayData(String Date){
        ArrayList<String[]> DayData = new ArrayList<>();
        int start = -1;
        int end = -1;
        for(int i = 1; i < RawData.size(); i++){
            if(RawData.get(i)[0].substring(0,RawData.get(i)[0].indexOf(":")-3).equals(Date.substring(0,Date.indexOf(":")-3))){
                if(start == -1) start = i;
                end = i;
            }
        }

        for(int f = start; f <= end; f++){
            DayData.add(RawData.get(f));
        }
        return DayData;
    }

    public ArrayList<ArrayList<String[]>> GetWeekData(){
        ArrayList<ArrayList<String[]>> WeekData = new ArrayList<>();
        String[] Week = new String[7];
        Week[0] = RawData.get(1)[0];
        int Index = 1;
        for(int i = 2; i < RawData.size(); i++){
            if(Index > 6) break;
            if(!RawData.get(i)[0].substring(0,RawData.get(i)[0].indexOf(":")-3).equals(Week[Index-1].substring(0,Week[Index-1].indexOf(":")-3))){
                Week[Index] = RawData.get(i)[0];
                Index++;
            }
        }
        for(String date : Week){
            WeekData.add(GetDayData(date));
        }
        return WeekData;
    }

    public double getLatestOpenPrice(){
        return Double.parseDouble(DayData.get(DayData.size()-1)[1]);
    }

    public double getSevenDayOpeningPrice(){ return Double.parseDouble(WeekData.get(6).get(WeekData.get(6).size()-1)[1]);}

    /**
     * A method to find the average price of a stock on a set interval on the most recent day
     * @return the average price of a stock as a double
     */
    public double AvgCurrentDayPrice(){
        double tp = 0.0;
        for(int i = 1; i < DayData.size(); i++){
            tp += (Double.parseDouble(RawData.get(i)[1])+Double.parseDouble(RawData.get(i)[2])+Double.parseDouble(RawData.get(i)[3])+Double.parseDouble(RawData.get(i)[4]))/4;
        }
        double ap = tp/(DayData.size()-1);
        return ap;
    }

    public double AvgDayPrice(ArrayList<String[]> data){
        double tp = 0.0;
        for(int i = 0; i < data.size(); i++){
            tp += (Double.parseDouble(data.get(i)[1])+Double.parseDouble(data.get(i)[2])+Double.parseDouble(data.get(i)[3])+Double.parseDouble(data.get(i)[4]))/4;
        }
        double ap = tp/(data.size()-1);
        return ap;
    }

    public double AvgSevenPrice(){
        double total = 0;
        for(ArrayList<String[]> as : WeekData){
            total += AvgDayPrice(as);
        }
        return total/7;
    }

    /**
     * A method to find the standard deviation of a given stock for the most recent day
     * @return the standard deviation of the stock as a double
     */
    public double CurrentDayDeviation(){
        double top = 0.0;
        for(int i = 1; i < DayData.size(); i++){
            top += Math.pow(((Float.parseFloat(DayData.get(i)[1])+Float.parseFloat(DayData.get(i)[2])+Float.parseFloat(DayData.get(i)[3])+Float.parseFloat(DayData.get(i)[4]))/4.0)-AvgCurrentDayPrice(),2.0);
        }
        double bottom = DayData.size()-2;
        double total = top/bottom;
        return Math.sqrt(total);
    }

    public double SevenDayDeviation(){
        double top = 0.0;
        for(int i = 0; i < 7; i++){
            for(ArrayList<String[]> as : WeekData){
                top += Math.pow((AvgDayPrice(as)-AvgSevenPrice()),2);
            }
        }
        double Bottom = 0.0;
        for(int i = 0; i < 7; i++){
            Bottom += WeekData.get(i).size()-1;
        }
        Bottom--;
        return Math.sqrt(top/Bottom);
    }

    public double CurrentDayDeviationPercentage(){
        return CurrentDayDeviation()/AvgCurrentDayPrice();
    }

    public double SevenDayDeviationPercentage(){
        return SevenDayDeviation()/AvgSevenPrice();
    }

}
