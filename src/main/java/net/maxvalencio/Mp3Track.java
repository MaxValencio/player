package net.maxvalencio;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.beans.property.SimpleStringProperty;
import net.maxvalencio.interfaces.Track;

import java.io.IOException;
import java.io.Serializable;

public class Mp3Track implements Track, Serializable {

    private SimpleStringProperty name;
    private SimpleStringProperty path;
    private SimpleStringProperty time;
    private int lengthInSeconds = 0;
    private long lengthInMileseconds = 0L;
    private int byteLength = 0;

    Mp3Track(String name, String path){
        this.name = new SimpleStringProperty(name);
        this.path =  new SimpleStringProperty(path);
        try {
            Mp3File file = new Mp3File(path);
            lengthInSeconds = (int)file.getLengthInSeconds();
            lengthInMileseconds = file.getLengthInMilliseconds();
            byteLength = (int)file.getLength();
            int minutes = lengthInSeconds / 60;
            int seconds = lengthInSeconds % 60;
            String duration = (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds) ;
            this.time = new SimpleStringProperty(duration);
        } catch (IOException | InvalidDataException | UnsupportedTagException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name.get();
    }

    public String getPath() {
        return  path.get();
    }

    public String getTime() {
        return time.get();
    }

    public int getLengthInSeconds() { return lengthInSeconds; }

    public long getLengthInMileseconds() { return lengthInMileseconds; }

    public int getByteLength() { return byteLength; }

    public String toString(){
        return getName();
    }

}
