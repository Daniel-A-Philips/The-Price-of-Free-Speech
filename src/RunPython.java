import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RunPython {

    public static void main(String[] args) {
        System.out.println("This method has no contents");
    }

    public static void run(String fileName) {
        InputStream output = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src//" + fileName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            output = (process.getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
        String text;
        try (Scanner scanner = new Scanner(output, StandardCharsets.UTF_8.name())) {
            text = scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(text);
    }
}
