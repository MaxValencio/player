package net.maxvalencio;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class MainController  {
    private Mp3Player player = new Mp3Player();
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
    public TableColumn columnTime;

    @FXML
    public Button btnPausePlay;

    @FXML
    public Label durationTrack;

    @FXML
    public Label progressTime;

    @FXML
    public Label labelCount;

    @FXML
    public Label nameTrack;

    @FXML
    public Label labelTotalTime;

    @FXML
    public Slider volumeSlider;

    @FXML
    public Slider progressSlider;


    public void initialize() {
        volumeSlider.setValue(volumeSlider.getMax());
        nameTrack.setText("");
        columnTrack.setCellValueFactory(new PropertyValueFactory<Mp3Track, String>("name"));
        columnTime.setCellValueFactory(new PropertyValueFactory<Mp3Track, String>("time"));
        updateCountLabel();
        updateTotalTime();
        collectionPlaylist.getPlaylist().addListener(new ListChangeListener<Mp3Track>() {
            public void onChanged(Change<? extends Mp3Track> c) {
                updateCountLabel();
                updateTotalTime();
            }
        });
        tablePlaylist.setItems(collectionPlaylist.getPlaylist());
        tablePlaylist.getSelectionModel().selectFirst();
        player.initListeners(nameTrack, durationTrack, progressTime, volumeSlider, progressSlider);

    }

    public void actionButtonPressed(ActionEvent actionEvent)  {

        Object source = actionEvent.getSource();

        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {
            case "btnPausePlay":
                switch (player.getStatus()){
                    case PLAYING:
                        player.pause();
                        return;

                    case PAUSED:
                        player.resume();
                        return;

                    case STARTED:
                        player.play(player.getPosition());
                        return;

                    case STOPPED:
                        player.play(player.getPosition());
                        return;
                }

            case "btnStop":
                player.stop();
                break;

            case "btnNext":
                tablePlaylist.getSelectionModel().selectNext();
                tablePlaylist.getFocusModel();
                player.nextPlay();
                break;

            case "btnPrevious":
                tablePlaylist.getSelectionModel().selectPrevious();
                tablePlaylist.getFocusModel();
                player.previousPlay();
                break;

            case "btnDeleteTrack":

                break;
        }
    }



    public void mouseReleased(){

    }

    private void updateCountLabel() { labelCount.setText("Total tracks: " + collectionPlaylist.getPlaylist().size()); }

    private void updateTotalTime() { labelTotalTime.setText("Total time: " + collectionPlaylist.getTotalTime() ); }

    public void MouseClicked(MouseEvent mouseEvent) {


        player.seek();

    }

    public void MouseDragged(MouseEvent mouseEvent) {
    }


    public void mousePresed(MouseEvent mouseEvent) {
        player.setMoveFromJump(true);
    }

}



