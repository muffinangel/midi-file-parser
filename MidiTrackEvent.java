import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Vector;

public class MidiTrackEvent {
    private int deltaTime;
    public enum type{
        MIDI, META, SYSEX;
    }
    private type eventType;
    private int maxDataByteslenght = 1000; // for this MIDI parser maxDataBytesLenght can't be longer

    // meta events / sysex events / bytes of information
    private Vector<Byte> dataBytes =  new Vector<>();
    // only meta-event
    private int metaType;
    // midi events
    private int byteNr;
    private int firstByte;
    private int secondByte;
    private int thirdByte;

    private BufferedInputStream reader;
    private String error;
    private int lenght;

    MidiTrackEvent(BufferedInputStream reader) throws IOException {
        this.reader = reader;
    }

    public int parse() throws IOException {
        //System.out.println("TRACK CHUNK----");

        // ---- delta time ----//
        byte[] buff = new byte[1];
        reader.read(buff, 0, 1);
        this.deltaTime = (int) ByteCalculation.byteToInt(buff, 1);
        //System.out.println("-delta_time = " + this.deltaTime);

        // --- time to recognize the type of the event --- //
        reader.read(buff, 0, 1);
        int eventStart =  (int) ByteCalculation.byteToInt(buff, 1);
        this.lenght = 2;
        //System.out.println("-event_start_symbol = " + eventStart);

        switch (eventStart) {
            case 0xFF:
                this.eventType = type.META;
          //      System.out.println("--META----");

                // --- meta type -----
                reader.read(buff, 0, 1);
                this.metaType = (int) ByteCalculation.byteToInt(buff, 1);
                this.lenght += 1;
           //     System.out.println("--meta_type= " + this.metaType);

                // ---- lenght of data bytes ----
                reader.read(buff, 0, 1);
                int len = (int) ByteCalculation.byteToInt(buff, 1);
                this.lenght += len + 1;
             //   System.out.println("--data_bytes_lenght= " + len);

                // ---- data bytes ----
                int dataBytesCounter = 0;
                while(len > 0 && ++dataBytesCounter <= maxDataByteslenght) {
                    reader.read(buff, 0, 1);
                    this.dataBytes.add(buff[0]);
                    System.out.println("---" + buff[0]);
                    len -= 1;
                }
                break;
            case 0xF0:
            case 0xF7:
                this.eventType = type.SYSEX;
               // System.out.println("--SYSEX----");
                // --- data bytes ------
                // proceed as sysex event
                for(reader.read(buff, 0, 1), this.lenght += 1, dataBytesCounter = 0 ;
                    buff[0] != (byte)0xF7 && dataBytesCounter < maxDataByteslenght;
                    reader.read(buff, 0, 1), this.lenght += 1, dataBytesCounter++) {
                    this.dataBytes.add(buff[0]);
                    System.out.println("---" + buff[0]);
                }
                if (dataBytesCounter == maxDataByteslenght) {
                    this.error = "Data bytes too long";
                    return 1;
                }
                break;
            default:
                this.eventType = type.MIDI;
                System.out.println("--MIDI----");
                // proceed as midi event
                // first byte is a first byte of midi
                this.firstByte = eventStart;
                this.dataBytes.add((byte) eventStart);
                // -- chceck for System Exclusive --
                if(eventStart == 0xF0) {
                    byteNr = 0;
                    dataBytesCounter = 0;
                    while(buff[0] != (byte)0x7F && dataBytesCounter++ < maxDataByteslenght) {
                        reader.read(buff, 0, 1);
                        dataBytes.add(buff[0]);
                        this.lenght += 1;
                        System.out.println("---" + buff[0]);
                    }
                    this.lenght += 1;
                    if (dataBytesCounter == maxDataByteslenght) {
                        this.error = "Data bytes too long";
                        return 1;
                    }
                    break;
                }
                // -- normal ---
                if(eventStart == 0xF1 ||  eventStart >= 0xF4 ) {
                    byteNr = 1;
                    break;
                }
                // second byte
                reader.read(buff, 0, 1);
                this.dataBytes.add(buff[0]);
                //this.secondByte = (int) ByteCalculation.byteToInt(buff, 1);
                this.lenght += 1;
                if (eventStart <= 0xBF || (eventStart >= 0xE0 && eventStart <= 0xEF) || eventStart == 0xF2) {
                    // third byte
                    reader.read(buff, 0, 1);
                    this.dataBytes.add(buff[0]);
                    //this.thirdByte = (int) ByteCalculation.byteToInt(buff, 1);
                    this.lenght += 1;
                    byteNr = 3;
                }
                else {
                    byteNr = 2;
                }
                break;
        }
        return 0;
    }

    // getters and setters
    public int getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(int deltaTime) {
        this.deltaTime = deltaTime;
    }

    public type getEventType() {
        return eventType;
    }

    public void setEventType(type eventType) {
        this.eventType = eventType;
    }

    public Vector<Byte> getDataBytes() {
        return dataBytes;
    }

    public void setDataBytes(Vector<Byte> dataBytes) {
        this.dataBytes = dataBytes;
    }

    public int getMetaType() {
        return metaType;
    }

    public void setMetaType(int metaType) {
        this.metaType = metaType;
    }

    public BufferedInputStream getReader() {
        return reader;
    }

    public void setReader(BufferedInputStream reader) {
        this.reader = reader;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }
}