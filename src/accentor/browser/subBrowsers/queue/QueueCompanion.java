package accentor.browser.subBrowsers.queue;

import accentor.Listener;
import accentor.browser.BrowseCompanion;
import accentor.browser.subBrowsers.cells.AlbumCell;
import accentor.browser.subBrowsers.cells.DurationCell;
import accentor.browser.subBrowsers.cells.NameListCell;
import accentor.domain.Track;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.util.List;

public class QueueCompanion implements Listener {
    @FXML public RadioButton repeatBtn;
    @FXML public TableView<Track> table;

    @FXML public TableColumn<Track, Integer> nr;
    @FXML public TableColumn<Track, String> title;
    @FXML public TableColumn<Track, List<Track.TrackArtist>> artist;
    @FXML public TableColumn<Track, String> album;
    @FXML public TableColumn<Track, Integer> length;

    private BrowseCompanion superCompanion;
    private QueueModel model;

    public QueueCompanion(BrowseCompanion superCompanion, QueueModel model) {
        this.superCompanion = superCompanion;
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

        table.setRowFactory(column -> {
            TableRow<Track> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.getItem() != null) {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        showContextMenu(row);
                    } else if (event.getButton() == MouseButton.PRIMARY){
                            play(row.getIndex());
                    }
                }
            });

            return row;
        });

        table.getItems().addAll(model.getData());
    }

    private void showContextMenu(TableRow<Track> row) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem removeBtn = new MenuItem("Remove from playlist");
        MenuItem artistBtn = new MenuItem("Show artist");
        MenuItem albumBtn  = new MenuItem("Show album");

        artistBtn.setOnAction(x -> {
            for (Track.TrackArtist trackArtist : row.getItem().getTrackArtists()) {
                openTab(trackArtist.getArtistId(), true);
            }
        });
        albumBtn.setOnAction(x -> openTab(row.getItem().getAlbumId(), false));

        contextMenu.getItems().addAll(removeBtn, artistBtn, albumBtn);
        row.setContextMenu(contextMenu);
    }

    private void play(int index) {
        superCompanion.playSong(index);
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

    private void openTab(String id, boolean isArtist) {
        if (id != null && !id.isEmpty()) {
            superCompanion.openTab(id, isArtist);
        }
    }
}
