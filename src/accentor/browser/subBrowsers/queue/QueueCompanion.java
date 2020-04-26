package accentor.browser.subBrowsers.queue;

import accentor.Listener;
import accentor.browser.subBrowsers.cells.AlbumCell;
import accentor.browser.subBrowsers.cells.DurationCell;
import accentor.browser.subBrowsers.cells.NameListCell;
import accentor.domain.Track;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.List;

public class QueueCompanion implements Listener {
    @FXML
    public TableView<Track> table;

    @FXML public TableColumn<Track, Integer> nr                     = new TableColumn<>("#");
    @FXML public TableColumn<Track, String> title                   = new TableColumn<>("Title");
    @FXML public TableColumn<Track, List<Track.TrackArtist>> artist = new TableColumn<>("Artist");
    @FXML public TableColumn<Track, String> album                   = new TableColumn<>("Album");
    @FXML public TableColumn<Track, Integer> length                 = new TableColumn<>("Time");

    private QueueModel model;

    public QueueCompanion(QueueModel model) {
        this.model = model;
        model.setListener(this);
    }

    @FXML
    public void initialize(){
        //Setting cellValueFactory's
        title.setCellValueFactory( new PropertyValueFactory<>("title"));
        artist.setCellValueFactory( new PropertyValueFactory<>("trackArtists"));
        album.setCellValueFactory( new PropertyValueFactory<>("albumId"));
        length.setCellValueFactory( new PropertyValueFactory<>("length"));

        artist.setCellFactory(column -> new NameListCell<>(model));
        album.setCellFactory(column -> new AlbumCell<>(model));
        length.setCellFactory(column -> new DurationCell<>());

        table.getItems().addAll(model.getData());
    }

    @Override
    public void modelHasChanged() {
        List<Track> data = model.getData();

        table.getItems().clear();
        table.getItems().addAll(data);
    }
}
