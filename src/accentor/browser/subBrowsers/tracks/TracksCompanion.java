package accentor.browser.subBrowsers.tracks;

import accentor.api.TrackFinder;
import accentor.browser.subBrowsers.TableCompanion;
import accentor.browser.subBrowsers.cells.AlbumCell;
import accentor.browser.subBrowsers.cells.DurationCell;
import accentor.browser.subBrowsers.cells.NameListCell;
import accentor.domain.Track;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TracksCompanion extends TableCompanion<TracksModel, Track, TrackFinder.SortOption> {
    private TableColumn<Track, Integer> nr                     = new TableColumn<>("#");
    private TableColumn<Track, String> title                   = new TableColumn<>("Title");
    private TableColumn<Track, List<Track.TrackArtist>> artist = new TableColumn<>("Artist");
    private TableColumn<Track, String> album                   = new TableColumn<>("Album");
    private TableColumn<Track, Integer> length                 = new TableColumn<>("Time");

    public TracksCompanion(TracksModel model) {
        super(model);

        sortMap.put(album.getText(), TrackFinder.SortOption.BY_ALBUM_TITLE);
    }

    @FXML
    public void initialize(){
        super.initialize();

        //Adding columns to tableview
        table.getColumns().add(nr);
        table.getColumns().add(title);
        table.getColumns().add(artist);
        table.getColumns().add(album);
        table.getColumns().add(length);

        //Changing column widths
        nr.setMinWidth(20);
        nr.setMaxWidth(30);
        nr.setResizable(false);
        nr.setSortable(false);

        title.setSortable(false);
        artist.setSortable(false);

        length.setMinWidth(55);
        length.setMaxWidth(55);
        length.setResizable(false);
        length.setSortable(false);

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

        updateButtons(1, model.getPages());
        updateLabel();
    }

    public void setAlbumsVisible(boolean visible){
        album.setVisible(visible);
    }
}
