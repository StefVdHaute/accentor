package accentor.browser.subBrowsers.albums;

import accentor.browser.subBrowsers.TableCompanion;
import accentor.domain.Album;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlbumsCompanion extends TableCompanion<AlbumsModel, Album> {
    private TableColumn<Album, String> cover                   = new TableColumn<>("Cover");
    private TableColumn<Album, String> title                   = new TableColumn<>("Title");
    private TableColumn<Album, List<Album.AlbumArtist>> artist = new TableColumn<>("Artist");
    private TableColumn<Album, LocalDate> release              = new TableColumn<>("Release date");

    public AlbumsCompanion(AlbumsModel model) {
        super(model);
    }

    @FXML
    public void initialize() {
        table.getColumns().add(cover);
        table.getColumns().add(title);
        table.getColumns().add(artist);
        table.getColumns().add(release);

        cover.setCellValueFactory( new PropertyValueFactory<>("smallImageURL"));
        title.setCellValueFactory( new PropertyValueFactory<>("title"));
        artist.setCellValueFactory( new PropertyValueFactory<>("albumArtists"));
        release.setCellValueFactory( new PropertyValueFactory<>("release"));

        cover.setCellFactory(column -> new CoverCell());
        artist.setCellFactory(column -> new ArtistCell());
        release.setCellFactory(column -> new ReleaseCell());

        table.getItems().addAll(model.getData());

        //updateButtons(1, model.getPages());
        updateLabel();
    }


    private class CoverCell extends TableCell<Album, String> {// Is slow
        @Override
        public void updateItem(String url, boolean empty){
            super.updateItem(url, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(new ImageView(new Image(url)));
            }
        }
    }

    private class ArtistCell extends TableCell<Album, List<Album.AlbumArtist>> {
        @Override
        public void updateItem(List<Album.AlbumArtist> ids, boolean empty){
            super.updateItem(ids, empty);

            if (empty) {
                setText(null);
            } else {
                setText(model.getArtistsName(ids));
            }
    }
}

    private class ReleaseCell extends TableCell<Album, LocalDate> {
        @Override
        public void updateItem(LocalDate date, boolean empty){
            super.updateItem(date, empty);

            if (empty) {
                setText(null);
            } else {
                setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        }
    }
}
