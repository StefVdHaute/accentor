package accentor.browser.subBrowsers.albums;

import accentor.browser.subBrowsers.TableCompanion;
import accentor.browser.subBrowsers.cells.DateCell;
import accentor.browser.subBrowsers.cells.NameListCell;
import accentor.browser.subBrowsers.cells.PictureCell;
import accentor.domain.Album;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
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
        super.initialize();

        table.getColumns().add(cover);
        table.getColumns().add(title);
        table.getColumns().add(artist);
        table.getColumns().add(release);

        cover.setCellValueFactory( new PropertyValueFactory<>("smallImageURL"));
        title.setCellValueFactory( new PropertyValueFactory<>("title"));
        artist.setCellValueFactory( new PropertyValueFactory<>("albumArtists"));
        release.setCellValueFactory( new PropertyValueFactory<>("release"));

        cover.setCellFactory(column -> new PictureCell<>());
        artist.setCellFactory(column -> new NameListCell<>(model));
        release.setCellFactory(column -> new DateCell<>());

        table.getItems().addAll(model.getData());

        updateButtons(1, model.getPages());
        updateLabel();
    }
}
