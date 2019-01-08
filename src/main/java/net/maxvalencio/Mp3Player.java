package net.maxvalencio;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javazoom.jlgui.basicplayer.*;
import net.maxvalencio.interfaces.Player;
import java.io.File;
import java.util.Map;
import static net.maxvalencio.PlayerStatus.*;

public class Mp3Player implements Player {

    PlayerStatus playerStatus = STARTED;
    private int currentPosition = 0;
    private double currentVolumeValue = 0;
    private BasicPlayer basicPlayer = new BasicPlayer();
    private CollectionPlaylist playlist = new CollectionPlaylist();
    private Mp3Track currentTrack = null;

    //<editor-fold defaultstate="collapsed" desc="переменные для прокрутки песни">
    private double posValue = 0.0;
    private boolean moveFromJump = false;
    private boolean moveAutomatic = false;
    private long positionSeconds;
    private long positionByte;
    private long newPosition;
    private int minutes;
    private int seconds;
    //</editor-fold>
    // сначало загружаем плейлист по умолчанию(последний плейлист),
    // если плейлиста нет, то при нажатии на "Воспроизведение" открываеться окно выбора
    // "открыть плейлист, открыть трек или создать плейлист

    @Override
    public void play(int i) {
        // !!!! - ПРИ СТАРТЕ - !!!
        // если кнопка нажата и плейлист не загружен, открываем FileChooser...
        // если плейлист загружен и не выбран ни один трек - воспроизводим первый трек
        // если плейлист загружен и выбран один трек - воспроизведение начинаем с него

        currentTrack = playlist.getPlaylist().get(i);

        try {

            basicPlayer.open(new File(currentTrack.getPath()));
            if(playerStatus == STARTED){ currentVolumeValue = 1.0; }
            playerStatus = PLAYING;// this needed for pause implementation
            basicPlayer.play();
            basicPlayer.setGain(currentVolumeValue);
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        try {
            basicPlayer.pause();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
        playerStatus = PAUSED;
    }

    @Override
    public void resume() {
        try {
            basicPlayer.resume();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
        playerStatus = PLAYING;
    }

    @Override
    public void stop() {
        try {
            basicPlayer.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
        playerStatus = STOPPED;
    }

    @Override
    public void nextPlay() {
        stop();

        if (playlist.getPlaylist().size() <= currentPosition + 1) {
            System.out.println(currentPosition);
            System.out.println(playlist.getPlaylist().size());
            currentPosition = 0;
            play(currentPosition);

        } else {
            play(++currentPosition);
        }
    }

    @Override
    public void previousPlay() {
        stop();
        if (currentPosition == 0) {
            currentPosition = playlist.getPlaylist().size();
        }
        play(--currentPosition);
    }

    @Override
    public void addTrack() { }

    @Override
    public void setVolume(double currentValue, double maximumValue) {
        this.currentVolumeValue = currentValue;
        if(currentValue == 0) {
            try {
                basicPlayer.setGain(0);
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }else {
            try {
                currentVolumeValue = currentValue/maximumValue;
                basicPlayer.setGain(currentVolumeValue);
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public double getCurrentVolumeValue() {
        return currentVolumeValue;
    }

    @Override
    public void volumeOFF(Slider volumeSlider) {
        volumeSlider.setValue(0);
    }

    @Override
    public void volumeON(Slider volumeSlider) {
        setVolume(volumeSlider.getValue(), volumeSlider.getMax());
    }

    @Override
    public void loadPlaylist() { }

    @Override
    public void savePlaylist() { }

    @Override
    public void seek() {

        jump(newPosition);
    }

    public void jump(long bytes) {
        try {
            basicPlayer.seek(bytes);
            basicPlayer.setGain(currentVolumeValue);
            moveFromJump = false;
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentData(Label labelName, Label labelTime){
        labelName.setText(currentTrack.getName());
        labelTime.setText(currentTrack.getTime());
    }

    PlayerStatus getStatus(){ return playerStatus; }

    int getPosition(){ return currentPosition; }
// во время проигрования мп3 слушатель прогресс передвигает слайдер прогресса и считает время от начала проигрования песни
//если переместить ползунок слайдера то песня перематываеться вперед.
//
    void initListeners(Label labelName, Label labelTime, Label timePosition, Slider volumeSlider, Slider progressSlider){

        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
             public void invalidated(Observable observable) {
                  setVolume(volumeSlider.getValue(), volumeSlider.getMax());
             }
        });

        basicPlayer.addBasicPlayerListener(new BasicPlayerListener() {
            @Override
            public void opened(Object o, Map map) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                                setCurrentData(labelName, labelTime);
                            }
                });
            }

            //пересчет времени и движение ползунка прогресса
            @Override
            public void progress(int bytesRead, long l, byte[] bytes, Map map) {
                positionByte = (((Long) map.get("mp3.position.byte")).longValue());
                positionSeconds = (((Long) map.get("mp3.position.microseconds")).longValue()) / 1000000;
                minutes = (int) positionSeconds / 60;
                seconds = (int) positionSeconds % 60;
                String progressTime = (minutes < 10 ? "0" + minutes : minutes) + ":"
                        + (seconds < 10 ? "0" + seconds : seconds);
                if(moveFromJump == false){
                    progressSlider.setValue(positionByte * 1000 / currentTrack.getByteLength());
                }
                if(moveFromJump == true) {
                    newPosition = (currentTrack.getByteLength()/1000)*(long)progressSlider.getValue();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        timePosition.setText(progressTime);
                    }
                });
            }
            @Override
            public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
                int state = basicPlayerEvent.getCode();
                if (state == BasicPlayerEvent.PLAYING) {

                }
//                if(state == BasicPlayerEvent.SEEKING) {
//
//                }
                if (state == BasicPlayerEvent.EOM) {
                    nextPlay();
                }
            }
            @Override
            public void setController(BasicController basicController) {
                System.out.println("setController do");
            }
        });
    }

    public void setMoveAutomatic(boolean b) { moveAutomatic = b; }

    public void setMoveFromJump(boolean b) { moveFromJump = b; }

    public boolean getMoveAutomatic(){ return moveAutomatic; }

    public boolean getMoveFromJump() { return moveFromJump; }
}
