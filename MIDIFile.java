

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class MIDIFile {
    private String pathToMidi;
    private String pathToResultFile;

    private MidiParser parser;

    // TODO timeout
    private String parseFile() { // null - no error
        try (FileInputStream in = new FileInputStream(this.pathToMidi);
             BufferedInputStream reader =
                     new BufferedInputStream(in)) {
            MidiParser a = new MidiParser(reader);
            if( a.parse() != 0) {
                return a.getError();
            }
        } catch (IOException x) {
            System.err.println(x);
        }
        return null;
    }

    // TODO


}
