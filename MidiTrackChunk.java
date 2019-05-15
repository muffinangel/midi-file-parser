import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Vector;

public class MidiTrackChunk {
    private int startTime;
    private int endTime;
    private long lenght;
    final private String literalString = "MTrk";
    private Vector<MidiTrackEvent> trackEvents = new Vector<>();
    private BufferedInputStream reader;

    public String getError() {
        return error;
    }

    private String error;

    MidiTrackChunk(BufferedInputStream reader) throws IOException {
        this.reader = reader;
    }

    public int parse() throws IOException {
        byte[] buff = new byte[4];
        reader.read(buff, 0, 4);
        if(new String(buff,"UTF-8").equals(literalString)) {
            // - ------ lenght ----------
            reader.read(buff, 0, 4);
            long x = ByteCalculation.byteToInt(buff, 4);
            System.out.println(x);
            this.lenght = x;
            while(x > 0) {
                MidiTrackEvent m = new MidiTrackEvent(reader);
                if( m.parse() != 0) {
                    this.error = m.getError();
                    return 1;
                }
                trackEvents.add(m);
                x -= m.getLenght();
            } // read track events

            return 0;
        }
        else
            this.error = "INVALID TRACK BEGINNIG - FILE SHOULD START WITH \"MTrk\" ";
        return 1;

    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getLiteralString() {
        return literalString;
    }

    public Vector<MidiTrackEvent> getTrackEvents() {
        return trackEvents;
    }

    public void setTrackEvents(Vector<MidiTrackEvent> trackEvents) {
        this.trackEvents = trackEvents;
    }

    public BufferedInputStream getReader() {
        return reader;
    }

    public void setReader(BufferedInputStream reader) {
        this.reader = reader;
    }
}