import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String [] args) {
        System.out.println("Hello world!!!");
        String file = "C:\\Users\\karot\\OneDrive\\Desktop\\TKOM\\midi_files\\MIDI_sample.mid";
        try (FileInputStream in = new FileInputStream(file);
             BufferedInputStream reader =
                     new BufferedInputStream(in)) {
            MidiParser a = new MidiParser(reader);
            a.parse();
        } catch (IOException x) {
            System.err.println(x);
        }
        System.out.println("Reading ends");
    }
}
