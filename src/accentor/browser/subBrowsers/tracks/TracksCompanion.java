package accentor.browser.subBrowsers.tracks;

import accentor.browser.subBrowsers.TableCompanion;
import accentor.domain.Track;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TracksCompanion extends TableCompanion<TracksModel, Track> {

    private TableColumn<Track, String> title                   = new TableColumn<>("Title");
    private TableColumn<Track, List<Track.TrackArtist>> artist = new TableColumn<>("Artist");
    private TableColumn<Track, Integer> nr                     = new TableColumn<>("#");
    private TableColumn<Track, String> album                   = new TableColumn<>("Album");
    private TableColumn<Track, Integer> length                 = new TableColumn<>("Length");

    public TracksCompanion(TracksModel model) {
        super(model);
    }

    @FXML
    public void initialize(){
        super.initialize();

        //Adding columns to tableview
        table.getColumns().add(title);
        table.getColumns().add(artist);
        table.getColumns().add(nr);
        table.getColumns().add(album);
        table.getColumns().add(length);

        //Setting cellValueFactory's
        title.setCellValueFactory( new PropertyValueFactory<>("title"));
        artist.setCellValueFactory( new PropertyValueFactory<>("trackArtists"));
        nr.setCellValueFactory( new PropertyValueFactory<>("number"));
        album.setCellValueFactory( new PropertyValueFactory<>("albumId"));
        length.setCellValueFactory( new PropertyValueFactory<>("length"));

        artist.setCellFactory(column -> new ArtistCell());
        album.setCellFactory(column -> new AlbumCell());
        length.setCellFactory(column -> new LengthCell());

        table.getItems().addAll(model.getData());

        updateButtons(1, model.getPages());
        updateLabel();
    }

    private class ArtistCell extends TableCell<Track, List<Track.TrackArtist>> {
        @Override
        public void updateItem(List<Track.TrackArtist> ids, boolean empty){
            super.updateItem(ids, empty);

            if (empty) {
                setText(null);
            } else {
                String name = model.getArtistsName(ids);
                setText(name);
            }
        }
    }

    private class AlbumCell extends TableCell<Track, String> {
        @Override
        public void updateItem(String id, boolean empty){
            super.updateItem(id, empty);

            if (empty) {
                setText(null);
            } else {
                setText(model.getAlbumName(id));
            }
        }

    }

    private class LengthCell extends TableCell<Track, Integer> {
        @Override
        public void updateItem(Integer duration, boolean empty){
            super.updateItem(duration, empty);
            if (empty) {
                setText(null);
            } else {
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
}
