package net.maxvalencio;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Mp3Player {

    private static boolean NEXT = false;
    private BasicPlayer basicPlayer  = new BasicPlayer();
    private Mp3Track currentMp3Track = null;
    private String currentMp3Name = "";
    private FileInputStream fis;
    private BufferedInputStream bis;


    public void play(Mp3Track mp3Track){
        try{
            fis = new FileInputStream(mp3Track.getPath());
            bis = new BufferedInputStream(fis);
            basicPlayer.open(bis);
            basicPlayer.play();



        }catch (BasicPlayerException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            basicPlayer.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }


    }
    public void pause(){
        try {
            basicPlayer.pause();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }
    public void resume(){
        try {
            basicPlayer.resume();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }




}
