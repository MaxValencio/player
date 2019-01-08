package net.maxvalencio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.maxvalencio.interfaces.Playlist;
import java.io.Serializable;
import java.util.Iterator;

public class CollectionPlaylist implements Playlist, Serializable {

    private FileFinder fileFinder = new FileFinder();

    private ObservableList<Mp3Track> playlist = FXCollections.observableArrayList(fileFinder.getTracks());

    public void add(Mp3Track mp3Track) {
        playlist.add(mp3Track);
    }

    @Override
    public void getTrack(Mp3Track mp3Track) {

    }

    public void delete(Mp3Track mp3Track) {
        playlist.remove(mp3Track);
    }

    @Override
    public void repeatON() {

    }

    @Override
    public void repeatOFF() {

    }

    @Override
    public void moveUpTrack(Mp3Track mp3Track) {

    }

    @Override
    public void moveDownTrack(Mp3Track mp3Track) {

    }

    public String getTotalTime(){
        int totalInSeconds = 0;
        Iterator<Mp3Track> iter = playlist.iterator();
        while(iter.hasNext()){
            totalInSeconds += iter.next().getLengthInSeconds();
        }
        int minutes = totalInSeconds / 60;
        int seconds = totalInSeconds % 60;
        String totalTime = minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
        return totalTime;
    }

    ObservableList<Mp3Track> getPlaylist() { return playlist;}
}
