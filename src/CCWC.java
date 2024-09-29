import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CCWC {
  public static void main(String[] args) {
    try {
      if (args.length == 1 && args[0].startsWith("-")) {
        // Read from stdin (piped input)
        processStdin(args[0]);
      } else {
        // Process file
        processFile(args);
      }
    } catch (IOException e) {
      System.err.println("Error reading input: " + e.getMessage());
    }
  }

  private static void processStdin(String input) throws IOException {
    BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(System.in));
    Stream<String> lines = reader.lines();

    if (input.equals("-l")) {
      long lineCount = lines.count();
      System.out.printf("%8d%n", lineCount);
    }
  }

  private static void processFile(String[] args) throws IOException {
    String fileName = args[args.length - 1];
    Path filePath = Paths.get(fileName);

    long byteCount = Files.size(filePath);
    long lineCount = Files.lines(filePath).count();
    long wordCount = Files.lines(filePath)
        .flatMap(line -> Stream.of(line.split("\\s+")))
        .filter(word -> !word.isEmpty())
        .count();

    if (args.length == 1) {
      // No options provided, show all by default
      System.out.printf("%8d %8d %8d %s%n", lineCount, wordCount, byteCount, fileName);
    } else {
      String option = args[0];
      switch (option) {
        case "-c" -> System.out.printf("%8d %s%n", byteCount, fileName);
        case "-l" -> System.out.printf("%8d %s%n", lineCount, fileName);
        case "-w" -> System.out.printf("%8d %s%n", wordCount, fileName);
        case "-m" -> {
          long charCount = Files.readString(filePath).length();
          System.out.printf("%8d %s%n", charCount, fileName);
        }
        default -> System.out.println("Unsupported option");
      }
    }

  }
}