import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class MdiHeaderChunk {
    final private String literalString = "MThd";
    private long lenght;
    private int format;
    private int n;
    private int division;
    private BufferedInputStream reader;
    private String error;

    MdiHeaderChunk(BufferedInputStream reader) throws IOException {
        this.reader = reader;
    }

    public int parse(BufferedInputStream reader) throws IOException { // 0 - ok; 1 - not ok
        // Reads up to byte.length bytes of data from this input stream into an array of bytes.
        // This method blocks until some input is available.
        byte[] buff = new byte[4];
        reader.read(buff, 0, 4);

        // read the literal string - MIDI file must start with MThd
        if(new String(buff,"UTF-8").equals(literalString)) {
            // OK. reader.read(buff, 0, 0);
            // - ------ lenght ----------
            reader.read(buff, 0, 4);
            long x = ByteCalculation.byteToInt(buff, 4);
            System.out.println(x);
            this.lenght = x;
            // - ------ format ----------
            reader.read(buff, 0, 2);
            x = ByteCalculation.byteToInt(buff, 2);
            for (byte b : new String(buff).getBytes()) {
                System.out.format("0x%x ", b); // 00 c0
            }
            System.out.println(x);
            this.format = (int)x;
            if(this.format != 0 && this.format != 1 && this.format != 2) {
                error = "INVALID FORMAT - FROMAT SHOULD BE 0, 1 OR 2";
                return 1;
            }
            else {
                // - ------ n ----------
                reader.read(buff, 0, 2);
                x = ByteCalculation.byteToInt(buff, 2);
                for (byte b : new String(buff).getBytes()) {
                    System.out.format("0x%x ", b);
                }
                System.out.println(x);
                this.n = (int)x;
                // - ------ division ----------
                buff[0] = '\0'; buff[1] = '\0'; buff[2] = '\0'; buff[3] = '\0';
                reader.read(buff, 0, 2);
                x = ByteCalculation.byteToInt(buff, 2);
                for (byte b : new String(buff).getBytes()) {
                    System.out.format("0x%x ", b); // 00 c0 ?!
                }
                System.out.println(x);
                this.division = (int)x;
                return 0;
            }
        }
        else
            this.error = "INVALID HEADER BEGINNIG - FILE SHOULD START WITH \"MThd\" ";
        return 1;
    }

    //getters and setters
    public long getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }

    public String getLiteralString() {
        return literalString;
    }

    public BufferedInputStream getReader() {
        return reader;
    }

    public void setReader(BufferedInputStream reader) {
        this.reader = reader;
    }

    public void setLenght(long lenght) {
        this.lenght = lenght;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
