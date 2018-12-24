package net.maxvalencio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.maxvalencio.interfaces.Playlist;
import java.io.Serializable;

public class CollectionPlaylist implements Playlist, Serializable {

    FileFinder fileFinder = new FileFinder();

    private ObservableList<Mp3Track> mp3TracksList = FXCollections.observableArrayList(fileFinder.getTrecks());

    public void add(Mp3Track mp3Track) {
        mp3TracksList.add(mp3Track);
    }

    public void delete(Mp3Track mp3Track) {
        mp3TracksList.remove(mp3Track);
    }

    public ObservableList<Mp3Track> getMp3TracksList() { return mp3TracksList;}
}
