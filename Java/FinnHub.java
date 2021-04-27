package Java;
import java.time.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class FinnHub {

    public String Interval;
    public String Ticker;
    public String Format = "csv";
    public ArrayList<String> Lines = new ArrayList<String>();
    public ArrayList<LocalDate[]> Dates = new ArrayList<LocalDate[]>();
    private String[] tempMonthArray = new String[] {"" ,"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    String[] start_end = new String[2];

    FinnHub(String start, String end, String Ticker, String Interval){
        start_end[0] = start;
        start_end[1] = end;
        this.Ticker = Ticker;
        this.Interval = Interval;
        Run();
    }


    private void Run(){
        try{
            ArrayList<long[]> times = getDates(); //TODO: Used for beta
            for(long[] time : times){
                System.out.println(time[0] + ", "+ time[1]);
                urlConnect(time[0],time[1]);
            }
            writeToCSV();
        } catch(Exception e){ System.out.println(e + " occured on line " + e.getStackTrace()[0].getLineNumber() + " in betaRun.FinnHub.java");}
    }

    private void urlConnect(long start, long end){
        System.out.println("Connecting to URL");
        try{
            URL url = new URL("https://finnhub.io/api/v1/stock/candle?symbol=" + Ticker + "&resolution=" + Interval + "&from=" + start + "&to=" + end + "&format=" + Format + "&token=c1vv82l37jkoemkedus0");
            InputStream in = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            ArrayList<String> temp = new ArrayList<String>();
            while((line = reader.readLine()) != null) {
                if(Lines.contains(line)) continue;
                temp.add(line);
                Lines.add(line);
            }
            temp.addAll(Lines);
            Lines = temp;
        } catch(Exception e){ System.out.println(e + " occured on line " + e.getStackTrace()[0].getLineNumber() + " in urlConnect.FinnHub.java");}
    }

    private ArrayList<long[]> getDates(){
        ArrayList<long[]> holder = new ArrayList<long[]>();
        try{
            String a = start_end[0];
            String b = start_end[1];
            ArrayList<String> months = new ArrayList<String>(Arrays.asList(tempMonthArray));
            System.out.println("Created Lists");
            if(a.equals(b)){ //If only one month is selected
                int year = Integer.parseInt(a.substring(a.length()-4,a.length()));
                System.out.println("One month is selected");
                System.out.println(year);
                int month = Integer.parseInt(a.substring(0,a.length()-3));
                System.out.println("year : " + year + ", month : " + month);
                YearMonth temp = YearMonth.of(year,month);
                LocalDate date1 = LocalDate.of(year,month,1);
                LocalDate date2 = LocalDate.of(year,month,temp.lengthOfMonth());
                Dates.add(new LocalDate[]{date1,date2});
                holder.add(new long[]{toUnix(date1),toUnix(date2)});
                return holder;
            }

            int year1 = Integer.parseInt(a.substring(a.length()-4,a.length()));
            int year2 = Integer.parseInt(b.substring(b.length()-4,b.length()));

            int month1 = months.indexOf(a.substring(0,a.length()-5));
            int month2 = months.indexOf(b.substring(0,b.length()-5));
            
            int timesToRun;
            if(year2 != year1) timesToRun = month2 + (12-month1);
            else timesToRun = month2-month1;

            boolean isPreviousYear = false;
            boolean hasChangedYear = false;
            for(int i = 0; i <= timesToRun; i++){ //TODO : Needs Work
                if(isPreviousYear & !hasChangedYear){
                    month2 = 12+i;
                    year2 = year1;
                    isPreviousYear = true;
                    hasChangedYear = true;
                }
                if(month2-i == 1){
                    isPreviousYear = true;
                }
                LocalDate date1 = LocalDate.of(year2,month2-i,1);
                YearMonth temp = YearMonth.of(year2,month2-i);
                LocalDate date2 = LocalDate.of(year2,month2-i,temp.lengthOfMonth());
                Dates.add(new LocalDate[]{date1,date2});
                holder.add(new long[]{toUnix(date1),toUnix(date2)});
            }
            for(long[] arr : holder){
                System.out.format("start : %d%n       end : %d%n", arr[0],arr[1]);
            }
        }   catch(Exception e) { System.out.println(e + " occured on line " + e.getStackTrace()[0].getLineNumber() + " in getDatesBeta.FinnHub.java");}
        return holder;
    }

    private long toUnix(LocalDate date){ 
        ZoneId zoneId = ZoneId.systemDefault();
        long epoch = date.atStartOfDay(zoneId).toEpochSecond();
        return epoch;
    }

    private void writeToCSV(){
        ArrayList<String> LinesToWrite = new ArrayList<String>();
        try{
            System.out.println("writeToCSV");
            String line;
            File tempFile = new File("FinnHubTest.csv");
            FileWriter writer = new FileWriter(tempFile);
            String ogHeaders = "t,o,h,l,c,v";
            String headers = "time,open,high,low,close,volume";
            Lines.set(0,headers);
            //Object[] lines = Lines.toArray();
            for(Object a : Lines){
                line = a.toString();
                if(line.equals(ogHeaders)) continue;
                if(!line.equals(headers)){
                    long unix = Long.parseLong(line.substring(0,line.indexOf(",")));
                    Date date = new Date();
                    date.setTime((long)unix*1000);
                    line = date + line.substring(line.indexOf(","),line.length()); //Line used to change UNIX to date and time format on csv file
                }
                LinesToWrite.add(line);
            }
            for(int i = 0; i < LinesToWrite.size()/2; i++){
                writer.write(LinesToWrite.get(i)+"\n");
            }
            writer.close();
        }catch(IOException e){System.out.println(e + " occured on line " + e.getStackTrace()[0].getLineNumber() + " in writeToCSV.FinnHub.java");}
    }
}
