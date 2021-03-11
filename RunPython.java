import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RunPython {

    private static String[] toRun = new String[]{"getTwitterID","tweetCounter","SMVI"};
    private static String[] Output = new String[toRun.length];

    public static void main(String[] args) {
        System.out.println("This method has no contents");
    }

    public static String Run(int index) {
        String fileName = toRun[index];
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
        System.out.println(text);
        return (text);
    }

    public static String getOutput(int index){
        return Output[index];
    }
}
