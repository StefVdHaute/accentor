package accentor.browser.subBrowsers.tracks;

import accentor.domain.Track;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class TrackCompanion{//TODO fix sorting
    @FXML
    public BorderPane pane;

    private TableView<Track> tracks = new TableView<>();
    private TableColumn<Track, Integer> nr                     = new TableColumn<>("#");
    private TableColumn<Track, String> title                   = new TableColumn<>("Title");
    private TableColumn<Track, Integer> length                 = new TableColumn<>("Length");
    private TableColumn<Track, String> album                   = new TableColumn<>("Album");
    private TableColumn<Track, List<Track.TrackArtist>> artist = new TableColumn<>("Artist");

    private TracksModel model;

    public TrackCompanion(TracksModel model) {
        this.model = model;
    }

    @FXML
    public void initialize(){
        //Adding tableview
        pane.setCenter(tracks);

        //Adding columns to tableview
        tracks.getColumns().add(nr);
        tracks.getColumns().add(title);
        tracks.getColumns().add(length);
        tracks.getColumns().add(album);
        tracks.getColumns().add(artist);

        //Setting cellValueFactory's
        nr.setCellValueFactory( new PropertyValueFactory<>("number"));
        title.setCellValueFactory( new PropertyValueFactory<>("title"));
        length.setCellValueFactory( new PropertyValueFactory<>("length"));
        album.setCellValueFactory( new PropertyValueFactory<>("albumId"));
        artist.setCellValueFactory( new PropertyValueFactory<>("trackArtists"));

        length.setCellFactory(column -> new LengthCell());
        album.setCellFactory(column -> new AlbumCell());
        artist.setCellFactory(column -> new ArtistCell());

        tracks.getItems().addAll(model.getTracks());
    }

    private class LengthCell extends TableCell<Track, Integer> {
        @Override
        public void updateItem(Integer duration, boolean empty){
            super.updateItem(duration, empty);
            if (duration != null) {
                int min = duration / 60;
                int sec = duration % 60;
                if (sec >= 10) {
                    setText(min + ":" + sec);
                } else {
                    setText(min + ":0" + sec);
                }
            }
        }
    }

    private class AlbumCell extends TableCell<Track, String> {
        @Override
        public void updateItem(String id, boolean empty){
            String name = model.getAlbumName(id);
            super.updateItem(name, empty);
            setText(name);
        }

    }
    private class ArtistCell extends TableCell<Track, List<Track.TrackArtist>> {
        @Override
        public void updateItem(List<Track.TrackArtist> ids, boolean empty){
            String name = model.getArtistsName(ids);
            super.updateItem(ids, empty);
            setText(name);
        }

    }
}
