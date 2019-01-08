package net.maxvalencio.interfaces;

import javafx.scene.control.Slider;

public interface Player {
    void play(int i);
    void pause();
    void resume();
    void stop();
    void nextPlay();
    void previousPlay();
    void seek();
    void addTrack();
    void setVolume(double currentValue, double maximumValue);
    double getCurrentVolumeValue();
    void volumeOFF(Slider volumeSlider);
    void volumeON(Slider volumeSlider);
    void loadPlaylist();
    void savePlaylist();
}
