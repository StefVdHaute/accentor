package accentor.browser.subBrowsers.queue;

import accentor.Listener;
import accentor.browser.subBrowsers.cells.AlbumCell;
import accentor.browser.subBrowsers.cells.DurationCell;
import accentor.browser.subBrowsers.cells.NameListCell;
import accentor.domain.Track;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class QueueCompanion implements Listener {
    @FXML public RadioButton repeatBtn;
    @FXML public TableView<Track> table;

    @FXML public TableColumn<Track, Integer> nr;
    @FXML public TableColumn<Track, String> title;
    @FXML public TableColumn<Track, List<Track.TrackArtist>> artist;
    @FXML public TableColumn<Track, String> album;
    @FXML public TableColumn<Track, Integer> length;

    private QueueModel model;

    public QueueCompanion(QueueModel model) {
        this.model = model;
        model.registerListener(this);
    }

    @FXML
    public void initialize(){
        //Setting cellValueFactory's
        nr.setCellValueFactory( new PropertyValueFactory<>("number"));
        title.setCellValueFactory( new PropertyValueFactory<>("title"));
        artist.setCellValueFactory( new PropertyValueFactory<>("trackArtists"));
        album.setCellValueFactory( new PropertyValueFactory<>("albumId"));
        length.setCellValueFactory( new PropertyValueFactory<>("length"));

        artist.setCellFactory(column -> new NameListCell<>(model));
        album.setCellFactory(column -> new AlbumCell<>(model));
        length.setCellFactory(column -> new DurationCell<>());

        table.getItems().addAll(model.getData());
    }

    public void setRepeat() {
        model.setRepeat(repeatBtn.isSelected());
    }

    public void shuffle() {
        model.shuffle();
    }

    @Override
    public void modelHasChanged() {
        List<Track> data = model.getData();

        table.getItems().clear();
        table.getItems().addAll(data);
    }
}
