import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.WindowConstants;
import java.io.FileWriter;
import java.io.PrintWriter;

class gui {
    private static ArrayList<JLabel> Labels = new ArrayList<>();
    private static ArrayList<Object> Text = new ArrayList<>();
    private static JComboBox<String> SliceButton;
    private static JComboBox<String> IntervalDropdown;
    private static JTextArea ta = new JTextArea();
    private static JPanel panel = new JPanel();
    private static JMenuBar mb = new JMenuBar();
    private static JFrame frame = new JFrame("The Price of Free Speech");
    private static String toPrint = "\n";
    private static boolean Resizable = true;

    public static void main(String[] args) {

        //Creating the Frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 400);
        frame.setResizable(Resizable);

        //Creating the MenuBar and adding components
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Save as");
        m1.add(m11);
        m1.add(m22);
        JButton select = new JButton("Begin");
        createLabels();
        createText();
        createDropdown();
        select.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    Interaction interaction = new Interaction(((JTextField)Text.get(0)).getText(),IntervalDropdown.getSelectedIndex(),SliceButton.getSelectedIndex(),((JTextField)Text.get(2)).getText());
                    writeDate();
                    interaction.run();
                    ta.append("\n"+toPrint);
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
                System.out.println("Slice");
                panel.add(SliceButton);
            }
            else panel.add((JTextField)(Text.get(i)));
        }
        // Dropdown Menu selection
        panel.add(select);
        ta = new JTextArea(toPrint);

        // Text Area at the Center
        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }

    private static void writeDate(){
        String input = SliceButton.getSelectedItem().toString();
        try{
            FileWriter fileWriter = new FileWriter("Data\\Time.txt", false); 
            PrintWriter printWriter = new PrintWriter(fileWriter, false);
            printWriter.flush();
            fileWriter.write(input);
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
        Object[] temp = new Object[]{TickerText,IntervalDropdown,HandleText,SliceButton};
        Text = new ArrayList<>(Arrays.asList(temp));
    }

    /**
     * A method that calculates which months and years should be showning the dropdown menu as well as there order
     */
    private static void createDropdown() {
        ArrayList<String> Months = new ArrayList<>();
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
        LocalDate currentdate = LocalDate.now();
        int currentyear = currentdate.getYear();
        String currentmonth = currentdate.getMonth().toString();
        int MonthNumber = 0;
        int monthsLeft = 24;
        for (int i = 0; i < 12; i++) {
            if (months[i].equalsIgnoreCase(currentmonth)) MonthNumber = i;
        }
        //Add months from the previous year
        for(int i = MonthNumber; i > -1; i--){
            Months.add(months[i]+" "+currentyear);
            monthsLeft--;
        }
        currentyear -= 1;
        int index = 11;
        while(monthsLeft > 0){
            if(index == -1){
                index = 11;
                currentyear -= 1;
            }
            Months.add(months[index]+" "+ currentyear);
            monthsLeft--;
            index--;
        }
        SliceButton = new JComboBox(Months.toArray());
        String[] Intervals = new String[]{"1","5","15","30","60"};
        IntervalDropdown = new JComboBox(Intervals);
    }

    public static void WriteText(String Text){
        toPrint = Text;
    }

}