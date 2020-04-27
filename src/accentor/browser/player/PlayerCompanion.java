package accentor.browser.player;

import accentor.Helper;
import accentor.Listener;
import accentor.browser.subBrowsers.queue.QueueModel;
import accentor.domain.Track;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

public class PlayerCompanion implements Listener {
    @FXML public HBox player;
    @FXML public Label durationLabel;
    @FXML public Label time;
    @FXML public Label songName;
    @FXML public Label artistName;
    @FXML public Button playPauseBtn;
    @FXML public Button nextBtn;
    @FXML public Slider timeSlider;
    @FXML public Slider volumeSlider;
    @FXML public ProgressBar bufferBar;

    private MediaPlayer mp;
    private Track currentTrack;
    private MediaPlayer nextMp; //Used for preloading music
    private Track nextTrack;

    private final QueueModel model;

    private long volumeEventPassed = 0;
    private long timeEventPassed = 0;

    public PlayerCompanion(QueueModel model) {
        this.model = model;
        model.registerListener(this);
    }

    @FXML
    public void initialize(){
        timeSlider.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.isDragDetect() || event.getButton() == MouseButton.PRIMARY) {
                timeEventPassed = System.currentTimeMillis();
            }
        });
        timeSlider.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                timeEventPassed = System.currentTimeMillis();
            }
        });

        volumeSlider.setMax(1.00000);
        volumeSlider.addEventFilter(MouseEvent.ANY, event -> {
            if (event.isDragDetect() || event.getButton() == MouseButton.PRIMARY) {
                volumeEventPassed = System.currentTimeMillis();
            }
        });
        volumeSlider.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                volumeEventPassed = System.currentTimeMillis();
            }
        });
    }

    public void setNext() {
        nextTrack = model.getNext();

        if (nextTrack != null) {
            nextMp = new MediaPlayer(new Media(nextTrack.getAudioUrl()));
            nextMp.setAutoPlay(false);

            mp.setOnEndOfMedia(this::nextSong);
            nextBtn.setDisable(false);
        } else {
            nextBtn.setDisable(true);
            mp.setOnEndOfMedia(() -> {
                mp.dispose();
                player.setDisable(true);

                timeSlider.setValue(0);
                bufferBar.setProgress(INDETERMINATE_PROGRESS);
                time.setText("");
                durationLabel.setText("");
                songName.setText("");
                artistName.setText("");
            });
        }
    }

    public void playNow(Track track) {
        if (mp != null){
            mp.dispose();
        }

        mp = new MediaPlayer(new Media(track.getAudioUrl()));
        mp.setAutoPlay(true);
        playPauseBtn.setText("||");

        bindPlayer(mp, track);
        setNext();
    }

    public void playNow(int index) {
        playNow(model.get(index));
    }

    ///////////////////////////////////////////////////ButtonFunctions//////////////////////////////////////////////////
    public void handlePlayBtn() {
        MediaPlayer.Status status = mp.getStatus();

        if ( status == MediaPlayer.Status.PAUSED
                || status == MediaPlayer.Status.READY) {
            mp.play();
            playPauseBtn.setText("||");
        } else if (status == MediaPlayer.Status.PLAYING){
            mp.pause();
            playPauseBtn.setText(">");
        }else {
            //TODO: handle buffering and other possibilities
        }
    }

    public void goBack(){//Er is geen verplichting om naar het vorige nummer te kunnen gaan
        mp.seek(new Duration(0));
    }

    public void nextSong(){
        mp.dispose();
        mp = nextMp;

        bindPlayer(mp, nextTrack);
        model.incrementPlaying(1);
        mp.play();

        setNext();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Separated in order to remove time-interruption on volume-change
    private boolean volumeEventWithinLast(long t) {
        return (System.currentTimeMillis() - volumeEventPassed) <= t;
    }

    private boolean timeEventWithinLast(long t) {
        return (System.currentTimeMillis() - timeEventPassed) <= t;
    }

    private void bindPlayer(MediaPlayer mp, Track track) {
        mp.setVolume(volumeSlider.getValue());
        player.setDisable(true);
        timeSlider.setValue(0);
        bufferBar.setProgress(INDETERMINATE_PROGRESS);
        time.setText("");
        durationLabel.setText("");

        songName.setText(track.getTitle());
        artistName.setText(Helper.getArtistsTrack(track.getTrackArtists()));
        currentTrack = track;

        if (mp.getStatus() != MediaPlayer.Status.READY) {
            mp.setOnReady(() -> initPlayer(mp));
        } else {
            initPlayer(mp);
            bufferBar.setProgress(mp.bufferProgressTimeProperty().getValue().toSeconds()
                                    / mp.getTotalDuration().toSeconds());
        }

        timeSlider.valueProperty().addListener(iv -> {
            if (timeEventWithinLast(100)) {
                mp.seek(Duration.seconds(timeSlider.getValue()));
            }
        });

        volumeSlider.valueProperty().addListener(iv -> {
            if (volumeEventWithinLast(100)) {
                mp.setVolume(volumeSlider.getValue());
            }
        });

        mp.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
            double seconds = newValue.toSeconds();
            timeSlider.setValue(seconds);
            time.setText(String.format("%d:%02d", (int)(seconds / 60), (int)(seconds % 60)));
        });

        mp.bufferProgressTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
            double seconds = newValue.toSeconds();
            bufferBar.setProgress(seconds / mp.getTotalDuration().toSeconds());
        });
    }

    private void initPlayer(MediaPlayer mp) {
        player.setDisable(false);

        double duration = mp.getTotalDuration().toSeconds();

        time.setText("0:00");
        durationLabel.setText(String.format("%d:%02d", (int) (duration / 60), (int) (duration % 60)));
        timeSlider.setMax(duration);
        bufferBar.setProgress(0);
    }


    @Override
    public void modelHasChanged() {
        if (currentTrack != model.getCurrent()) {
            playNow(model.getCurrent());
        } else {
            setNext();
        }
    }
}
