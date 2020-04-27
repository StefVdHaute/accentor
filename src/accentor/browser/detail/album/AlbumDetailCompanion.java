package accentor.browser.detail.album;

import accentor.Helper;
import accentor.browser.BrowseCompanion;
import accentor.browser.BrowseModel;
import accentor.browser.detail.DetailCompanion;
import accentor.browser.subBrowsers.tracks.TracksCompanion;
import accentor.domain.Album;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AlbumDetailCompanion extends DetailCompanion<AlbumDetailModel, Album> {
    private final BrowseCompanion superCompanion;

    public AlbumDetailCompanion(BrowseCompanion superCompanion, AlbumDetailModel model){
        super(model);
        this.superCompanion = superCompanion;
    }

    @FXML
    public void initialize(){
        Button addSongsBtn = new Button("Add all songs to playlist");
        addSongsBtn.setOnAction(x -> addSongs());

        details.getChildren().add(addSongsBtn);

        Album album = getModel().getSubject();

        tab.setText(album.getTitle());
        title.setText(album.getTitle());
        image.setImage(new Image(album.getLargeImageURL(), true));
        info.getChildren().add(new Label(album.getRelease().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        info.getChildren().add(new Label(Helper.getArtistsAlbum(album.getAlbumArtists())));

        TracksCompanion tracksCompanion = new TracksCompanion(superCompanion, getModel().getTracksModel());

        try {
            FXMLLoader fxmlLoader;
            // The path is apparently not "../subBrowsers/table.fxml" from here and i do not know what it should be
            fxmlLoader = new FXMLLoader(BrowseModel.class.getResource("subBrowsers/table.fxml"));
            fxmlLoader.setController(tracksCompanion);
            BorderPane pane = fxmlLoader.load();
            tracksCompanion.runAlbumDetailMode(true);

            VBox.setVgrow(pane, Priority.ALWAYS);// So that the table uses all available space
            details.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSongs() {
        superCompanion.addSongsToPlaylist(getModel().getTracksModel().getAllData());
    }
}
