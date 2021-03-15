import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class RunPython {

    private static String[] toRun = new String[]{"getTwitterID.py","tweetCounter.py","SMVI.py"};
    private static String[] Output = new String[toRun.length];

    public static void main(String[] args) {
        System.out.println("This method has no contents");
    }

    public static void Run(int index) {
        String fileName = toRun[index];
        System.out.println("Running \"" + fileName + "\"");
        InputStream output = null;
        String text = "";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", fileName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            output = (process.getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        try (Scanner scanner = new Scanner(output, StandardCharsets.UTF_8.name())) {
            text = scanner.useDelimiter("\\A").next();
            Output[index] = text;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String getOutput(int index){
        return Output[index];
    }
}
