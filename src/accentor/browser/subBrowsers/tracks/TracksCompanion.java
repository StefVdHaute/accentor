package accentor.browser.subBrowsers.tracks;

import accentor.api.TrackFinder;
import accentor.browser.BrowseCompanion;
import accentor.browser.subBrowsers.TableCompanion;
import accentor.specialistFxElements.cells.AlbumCell;
import accentor.specialistFxElements.cells.DurationCell;
import accentor.specialistFxElements.cells.NameListCell;
import accentor.domain.Track;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;

import java.util.List;

public class TracksCompanion extends TableCompanion<TracksModel, Track, TrackFinder.SortOption> {
    private final TableColumn<Track, Integer> nr                     = new TableColumn<>("#");
    private final TableColumn<Track, String> title                   = new TableColumn<>("Title");
    private final TableColumn<Track, List<Track.TrackArtist>> artist = new TableColumn<>("Artist");
    private final TableColumn<Track, String> album                   = new TableColumn<>("Album");
    private final TableColumn<Track, Integer> length                 = new TableColumn<>("Time");
    
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
                        event.consume();
                    } else if (event.getButton() == MouseButton.PRIMARY) {
                        play(row.getItem());
                        event.consume();
                    }
                }
            });
            row.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    play(row.getItem());
                    keyEvent.consume();
                }
            });

            return row;
        });

        renewData();
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
        artistBtn.setOnAction(actionEvent -> {
            for (Track.TrackArtist trackArtist : row.getItem().getTrackArtists()) {
                Platform.runLater(() -> openTab(trackArtist.getArtistId(), true));
            }
            actionEvent.consume();
        });

        MenuItem albumBtn  = new MenuItem("Show album");
        albumBtn.setOnAction(actionEvent -> {
            Platform.runLater(() -> openTab(row.getItem().getAlbumId(), false));
            actionEvent.consume();
        });


        contextMenu.getItems().addAll(nowBtn, nextBtn, addBtn, artistBtn, albumBtn);
        row.setContextMenu(contextMenu);
    }

    public void runAlbumDetailMode(boolean yes) {
        album.setVisible(!yes);
    }

    private void openTab(String id, boolean isArtist) {
        if (id != null && !id.isEmpty()) {
            superCompanion.openTab(id, isArtist);
        }
    }

    private void play(Track track) {
        superCompanion.playSong(track);
    }

    private void playNext(Track track) {
        superCompanion.nextSong(track);
    }

    private void add(Track track) {
        superCompanion.addToQueueEnd(track);
    }
}
