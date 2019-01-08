package net.maxvalencio.interfaces;

import net.maxvalencio.Mp3Track;

public interface Playlist {
 void add(Mp3Track mp3Track);
 void getTrack(Mp3Track mp3Track);
 void delete(Mp3Track mp3Track);
 void repeatON();
 void repeatOFF();
 void moveUpTrack(Mp3Track mp3Track);
 void moveDownTrack(Mp3Track mp3Track);
}
