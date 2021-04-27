package Java;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.time.*;
import java.awt.event.*;
import java.nio.file.*;

class gui {
    private static ArrayList<JLabel> Labels = new ArrayList<>();
    private static ArrayList<Object> Text = new ArrayList<>();
    private static JComboBox<String> SliceDropdown;
    private static JComboBox<String> IntervalDropdown;
    private static JTextArea ta = new JTextArea();
    private static JPanel panel = new JPanel();
    private static JMenuBar mb = new JMenuBar();
    private static JFrame frame = new JFrame("The Price of Free Speech");
    private static boolean Resizable = true;
    private static Interaction STOCK;
    private static Interaction DIA;
    private static int timeRange;
    public static ArrayList<String> Months = new ArrayList<>();
    public static String absPath = Paths.get("PathTest.java").toAbsolutePath().toString().substring(0,Paths.get("PathTest.java").toAbsolutePath().toString().lastIndexOf("/"))+"/Data/";

    public static void main(String[] args) {
        //Creating the Frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(900, 400);
        frame.setResizable(Resizable);
        //Creating the MenuBar and adding components
        JMenu file = new JMenu("FILE");
        JMenu help = new JMenu("Help");
        mb.add(file);
        mb.add(help);
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save_as = new JMenuItem("Save as");
        file.add(open);
        file.add(save_as);
        JButton select = new JButton("Begin");
        createLabels();
        createText();
        createDropdown();
        select.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    String ticker = ((JTextField)Text.get(0)).getText();
                    int IntervalInt = IntervalDropdown.getSelectedIndex();
                    int SliceInt = SliceDropdown.getSelectedIndex();
                    String handles = ((JTextField)Text.get(2)).getText();
                    STOCK = new Interaction(ticker,IntervalInt,SliceInt,handles,false);
                    DIA = new Interaction("DIA",IntervalInt,SliceInt,handles,true);
                    writeDate();
                    STOCK.run();
                    DIA.run();
                    writeSMVI();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        for(int i = 0; i < Labels.size(); i++){
            panel.add(Labels.get(i));
            if(i == 1){
                panel.add(IntervalDropdown);
            }
            else if(i == 3){
                System.out.println("Creating Slice Dropdown");
                panel.add(SliceDropdown);
            }
            else panel.add((JTextField)(Text.get(i)));
        }
        // Dropdown Menu selection
        panel.add(select);
        ta = new JTextArea("");

        // Text Area at the Center
        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }

    private static void writeDate(){
        String[] tempMonths = new String[]{"" ,"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayList<String> monthList = new ArrayList<String>(Arrays.asList(tempMonths));
        String EndInput = SliceDropdown.getSelectedItem().toString();
        String StartInput = EndInput; //TODO: Change once the range functionn has been implemented
        if(EndInput.equals(StartInput)){
            System.out.println("Only one month is being used");
        }
        Date currentDate = new Date();
        int[] year = {Integer.parseInt(StartInput.split(" ")[1]),Integer.parseInt(EndInput.split(" ")[1])};
        int[] months = {monthList.indexOf(StartInput.split(" ")[0]),monthList.indexOf(EndInput.split(" ")[0])};
        System.out.println(StartInput + " : " + Months.get(0));
        Date startDate = new Date(year[0], months[0], 1);
        YearMonth temp = YearMonth.of(year[1],months[1]);
        int endDateDays = temp.lengthOfMonth();
        Date endDate = new Date(year[1],months[1],endDateDays);
        if(months[1] == currentDate.getMonth() && year[1] == currentDate.getYear()){
            endDate = currentDate;
        }
        startDate = endDate;
        getDaysInTimeRange(startDate, endDate);
        try{
            String dataPath = absPath + "Time.txt";
            FileWriter fileWriter = new FileWriter(dataPath, false); 
            PrintWriter printWriter = new PrintWriter(fileWriter, false);
            printWriter.flush();
            fileWriter.write(EndInput);
            fileWriter.close();
            printWriter.close();
        }catch(Exception e){System.err.println(e);}
    }

    private static void createLabels(){
        JLabel TickerLabel = new JLabel("Ticker");
        JLabel IntervalLabel = new JLabel("\tInterval (Minutes)");
        JLabel HandleLabel = new JLabel("\tTwitter Handle");
        JLabel SliceLabel = new JLabel("\tMonth");
        Labels = new ArrayList<>(Arrays.asList(TickerLabel,IntervalLabel,HandleLabel,SliceLabel));
    }

    private static void createText() {
        JTextField TickerText = new JTextField(6); // accepts up to 6 characters
        JTextField HandleText = new JTextField(15);
        Object[] temp = new Object[]{TickerText,IntervalDropdown,HandleText,SliceDropdown};
        Text = new ArrayList<>(Arrays.asList(temp));
    }

    /**
     * A method that calculates which months and years should be showning the dropdown menu as well as there order
     */
    private static void createDropdown() {
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
        LocalDate currentdate = LocalDate.now();
        int currentyear = currentdate.getYear();
        String currentmonth = currentdate.getMonth().toString();
        int MonthNumber = 0;
        int monthsLeft = 24;
        for (int i = 0; i < 12; i++) {
            if (months[i].equalsIgnoreCase(currentmonth)) MonthNumber = i;
        }
        //Add months from the current year
        for(int i = MonthNumber; i > -1; i--){
            Months.add(months[i]+" "+currentyear);
            monthsLeft--;
        }
        currentyear -= 1;
        int index = 11;
        //Add months from the previous years
        while(monthsLeft > 0){
            if(index == -1){
                index = 11;
                currentyear -= 1;
            }
            Months.add(months[index]+" "+ currentyear);
            monthsLeft--;
            index--;
        }
        SliceDropdown = new JComboBox(Months.toArray());
        String[] Intervals = new String[]{"1","5","15","30","60"};
        IntervalDropdown = new JComboBox(Intervals);
    }

    public static void WriteText(String Text){ ta.append("\n" + Text);}

    
    private static void writeSMVI(){
        try{
            RunPython.Run(0);
            RunPython.Run(1);
            //RunPython.Run(2);
            File file = new File(absPath + "SMVI_Data.txt");
            FileWriter writer = new FileWriter(file);
            String[] varNames = new String[]{"va","na","vb","nb","T","t"};
            double va = STOCK.getVariation(STOCK.getRawData());
            double na= STOCK.getNumberOfDataPoints();
            double vb = DIA.getVariation(DIA.getRawData());
            double nb = DIA.getNumberOfDataPoints();
            double t = timeRange; //TODO: Get the time from the range
            if(t < 31){
                t = 31;
            }
            double T = Double.parseDouble(RunPython.getOutput(1));
            double[] var = new double[]{va,na,vb,nb,t,T};
            System.out.println("Writing");
            for(int i = 0; i < var.length; i++){
                writer.write(varNames[i]+":"+var[i]+"\n");
            }
            writer.close();
            RunPython.Run(2);
            WriteText("Social Media Volatility Index: " + RunPython.getOutput(2));
        }catch(Exception e){System.out.println("Error in \"writeSMVI\"\n"+ e + "\n" + "Error on line number " + e.getStackTrace()[0].getLineNumber());}
    }

    private static void getDaysInTimeRange(Date Start, Date End){
        long startDateTime = Start.getTime();
        long endDateTime = End.getTime();
        long milPerDay = 1000*60*60*24; 
        timeRange = (int) ((endDateTime - startDateTime) / milPerDay);
    }

}