import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

public class MidiParser {
    private BufferedInputStream reader;
    private MdiHeaderChunk m;
    private Vector<MidiTrackEvent> trackChunks = new Vector<>();
    private String error;


    MidiParser(BufferedInputStream reader) {
        this.reader = reader;
    }

    public void getHeader() throws IOException {
        m = new MdiHeaderChunk(reader);
    }

    public int parse() throws IOException {
        m = new MdiHeaderChunk(reader);
        if(m.parse(reader) != 0 ) {
            this.error = m.getError();
            return 1;
        }
        //System.out.println("Z parsowania headera mamy error: " + m.getError());
        for(int i = 0; i < m.getN(); i++) {
            MidiTrackChunk s = new MidiTrackChunk(reader);
            if( s.parse() != 0 ) {
                this.error = s.getError();
                return 1;
            }
        }

        return 0;
    }

    public MdiHeaderChunk getM() {
        return m;
    }

    public Vector<MidiTrackEvent> getTrackChunks() {
        return trackChunks;
    }

    public String getError() {
        return error;
    }
}
