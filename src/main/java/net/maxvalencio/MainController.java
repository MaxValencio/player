package net.maxvalencio;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javazoom.jlgui.basicplayer.*;
import java.io.*;
import java.util.Map;

public class MainController  {
    private Mp3Track currentTrack = null;
    private BasicPlayer player = new BasicPlayer();
    private static int playerStatus = 0;
    private static int PLAYING = 1;
    private static int PAUSED = 2;
    private double currentVolume;
    private static int currentPosition = 0;
    String nameSong = "";
    private FileInputStream fis;
    private BufferedInputStream bis;
    private CollectionPlaylist collectionPlaylist = new CollectionPlaylist();
    @FXML
    public TableView tablePlaylist;

    @FXML
    public Button btnPrevious;

    @FXML
    public Button btnNext;

    @FXML
    public Button btnStop;

    @FXML
    public Button btnPlaylist;

    @FXML
    public TableColumn columnTrack;

    @FXML
    public Button btnPausePlay;

    @FXML
    public Label durationTrack;

    @FXML
    public Label labelCount;

    @FXML
    public Label nameTrack;

    @FXML
    public Slider volumeSlider;

    public void initialize() {
        nameTrack.setText("");
        columnTrack.setCellValueFactory(new PropertyValueFactory<Mp3Track, String>("name"));
        updateCountLabel();
        collectionPlaylist.getMp3TracksList().addListener(new ListChangeListener<Mp3Track>() {
            public void onChanged(Change<? extends Mp3Track> c) {
                updateCountLabel();
            }
        });
        tablePlaylist.setItems(collectionPlaylist.getMp3TracksList());
        tablePlaylist.getSelectionModel().selectFirst();
        player.addBasicPlayerListener(new BasicPlayerListener() {
            @Override
            public void opened(Object o, Map map) {
                nameSong = (String)map.get("title");
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        nameTrack.setText(String.valueOf(nameSong));
                    }
                });
                volumeSlider.setValue(volumeSlider.getMax());
                volumeSlider.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        setVolume(volumeSlider.getValue(), volumeSlider.getMax());
                    }
                });

            }




            @Override
            public void progress(int i, long l, byte[] bytes, Map map) { }

            @Override
            public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
                int state = basicPlayerEvent.getCode();
                if (state == BasicPlayerEvent.EOM) {
                    playNextOfPlaylist();
                }
            }

            @Override
            public void setController(BasicController basicController) {
                System.out.println("setController do");
            }
        });
    }

    public void actionButtonPressed(ActionEvent actionEvent) throws BasicPlayerException {

        Object source = actionEvent.getSource();

        if (!(source instanceof Button)) {
            if(!(source instanceof Slider)) {
                return;
            } else {
                Slider clikedSlider= (Slider) source;

                switch (clikedSlider.getId()){
                    case "volumeSlider":
                        setVolume(volumeSlider.getValue(), volumeSlider.getMax());
                        break;
                }

            }
        }

        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {
            case "btnPausePlay":

                if (playerStatus == PLAYING) {
                    player.pause();
                    playerStatus = PAUSED;
                    return;
                }

                if (playerStatus == PAUSED) {
                    player.resume();
                    playerStatus = PLAYING;
                    return;
                }

                playPlaylist(currentPosition);
                break;


            case "btnStop":
                player.stop();
                playerStatus = 0;
                break;

            case "btnNext":
                tablePlaylist.getSelectionModel().selectNext();
                tablePlaylist.getFocusModel();
                playNextOfPlaylist();

                break;

            case "btnPrevious":
                player.stop();
                tablePlaylist.getSelectionModel().selectNext();
                if (currentPosition == 0) {
                    System.out.println(currentPosition);
                    System.out.println(tablePlaylist.getItems().size());
                    currentPosition = tablePlaylist.getItems().size();
                }
                playPlaylist(--currentPosition);

                break;


        }
    }

    public void updateCountLabel() {
        labelCount.setText("Total tracks: " + collectionPlaylist.getMp3TracksList().size());
    }

    public  void playPlaylist(int i) {

        currentTrack = (Mp3Track) tablePlaylist.getItems().get(i);

        try {
            fis = new FileInputStream(currentTrack.getPath());
            bis = new BufferedInputStream(fis);
            player.open(bis);
            playerStatus = PLAYING;// this needed for pause implementation
            player.play();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void playNextOfPlaylist() {
        try {
            player.stop();
        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }

        if (tablePlaylist.getItems().size() <= currentPosition + 1) {
            System.out.println(currentPosition);
            System.out.println(tablePlaylist.getItems().size());
            currentPosition = 0;
            playPlaylist(currentPosition);

        } else {
            playPlaylist(++currentPosition);
        }
    }
    public void setVolume(double currentValue, double maximumValue) {
        this.currentVolume = currentValue;
        if(currentValue == 0) {
            try {
                player.setGain(0);
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }else {
            try {
                player.setGain(calcVolume(currentValue, maximumValue));
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
    }

    private double calcVolume(double currentValue, double maximumValue){
        currentVolume = (double)currentValue/(double) maximumValue;
        return currentVolume;
    }
}



