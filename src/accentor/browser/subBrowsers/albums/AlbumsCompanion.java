package accentor.browser.subBrowsers.albums;

import accentor.browser.BrowseCompanion;
import accentor.browser.subBrowsers.TableCompanion;
import accentor.browser.subBrowsers.cells.DateCell;
import accentor.browser.subBrowsers.cells.NameListCell;
import accentor.browser.subBrowsers.cells.PictureCell;
import accentor.domain.Album;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class AlbumsCompanion extends TableCompanion<AlbumsModel, Album> {
    private TableColumn<Album, String> cover                   = new TableColumn<>("Cover");
    private TableColumn<Album, String> title                   = new TableColumn<>("Title");
    private TableColumn<Album, List<Album.AlbumArtist>> artist = new TableColumn<>("Artist");
    private TableColumn<Album, LocalDate> release              = new TableColumn<>("Release date");

    private BrowseCompanion superCompanion;

    public AlbumsCompanion(BrowseCompanion superCompanion, AlbumsModel model) {
        super(model);
        this.superCompanion = superCompanion;
    }

    @FXML
    public void initialize() {
        super.initialize();

        table.getColumns().add(cover);
        table.getColumns().add(title);
        table.getColumns().add(artist);
        table.getColumns().add(release);

        cover.setMinWidth(110);
        cover.setMaxWidth(120);
        cover.setResizable(false);

        release.setMinWidth(120);
        release.setMaxWidth(130);
        release.setResizable(false);

        cover.setCellValueFactory( new PropertyValueFactory<>("smallImageURL"));
        title.setCellValueFactory( new PropertyValueFactory<>("title"));
        artist.setCellValueFactory( new PropertyValueFactory<>("albumArtists"));
        release.setCellValueFactory( new PropertyValueFactory<>("release"));

        cover.setCellFactory(column -> new PictureCell<>());
        //title.setCellFactory(column -> new TitleCell());
        title.setCellFactory(column -> {
            TableCell<Album, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String name, boolean empty) {
                    super.updateItem(name, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(name);
                    }
                }
            };

            cell.setAlignment(Pos.CENTER_LEFT);
            return cell;
        });
        artist.setCellFactory(column -> new NameListCell<>(model));
        release.setCellFactory(column -> new DateCell<>());

        table.setRowFactory(column -> {
            TableRow<Album> row = new TableRow<>();
            row.setOnMouseClicked(event -> openTab(row.getItem()));
            return row;
        });

        table.getItems().addAll(model.getData());

        updateButtons(1, model.getPages());
        updateLabel();
    }

    private void openTab(Album album) {
        if (album != null) {
            superCompanion.openTab(album);
        }
    }
}
