package accentor.browser.subBrowsers.tracks;

import accentor.api.TrackFinder;
import accentor.browser.BrowseCompanion;
import accentor.browser.subBrowsers.TableCompanion;
import accentor.browser.subBrowsers.cells.AlbumCell;
import accentor.browser.subBrowsers.cells.DurationCell;
import accentor.browser.subBrowsers.cells.NameListCell;
import accentor.domain.Track;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.util.List;

public class TracksCompanion extends TableCompanion<TracksModel, Track, TrackFinder.SortOption> {
    private TableColumn<Track, Integer> nr                     = new TableColumn<>("#");
    private TableColumn<Track, String> title                   = new TableColumn<>("Title");
    private TableColumn<Track, List<Track.TrackArtist>> artist = new TableColumn<>("Artist");
    private TableColumn<Track, String> album                   = new TableColumn<>("Album");
    private TableColumn<Track, Integer> length                 = new TableColumn<>("Time");
    
    public TracksCompanion(BrowseCompanion superCompanion, TracksModel model) {
        super(superCompanion, model);

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

        // Nothing i've tried works for proper time-placement
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

        table.setRowFactory(column -> {
            TableRow<Track> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.getItem() != null) {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        showContextMenu(row);
                    } else if (event.getButton() == MouseButton.PRIMARY) {
                        play(row.getItem());
                    }
                }
            });

            return row;
        });

        table.getItems().addAll(model.getData());

        updateButtons(1, model.getPages());
        updateLabel();
    }

    private void showContextMenu(TableRow<Track> row) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem nowBtn = new MenuItem("Play song now");
        nowBtn.setOnAction(x -> play(row.getItem()));

        MenuItem nextBtn = new MenuItem("Play song next");
        nextBtn.setOnAction(x -> playNext(row.getItem()));

        MenuItem addBtn  = new MenuItem("Add song to playlist");
        addBtn.setOnAction(x -> add(row.getItem()));

        MenuItem artistBtn = new MenuItem("Show artist");
        artistBtn.setOnAction(x -> {
            for (Track.TrackArtist trackArtist : row.getItem().getTrackArtists()) {
                openTab(trackArtist.getArtistId(), true);
            }
        });

        MenuItem albumBtn  = new MenuItem("Show album");
        albumBtn.setOnAction(x -> openTab(row.getItem().getAlbumId(), false));


        contextMenu.getItems().addAll(nowBtn, nextBtn, addBtn, artistBtn, albumBtn);
        row.setContextMenu(contextMenu);
    }

    public void runAlbumDetailMode(boolean yes){//TODO: kies betere namen
        album.setVisible(!yes);
    }

    private void openTab(String id, boolean isArtist) {
        if (id != null && !id.isEmpty()) {
            superCompanion.openTab(id, isArtist);
        }
    }

    private void play(Track track){
        superCompanion.playSong(track);
    }

    private void playNext(Track track) {
        superCompanion.nextSong(track);
    }

    private void add(Track track) {
        superCompanion.addToQueueEnd(track);
    }
}
