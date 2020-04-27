package accentor.browser.detail.artist;

import accentor.browser.BrowseCompanion;
import accentor.browser.BrowseModel;
import accentor.browser.detail.DetailCompanion;
import accentor.browser.subBrowsers.albums.AlbumsCompanion;
import accentor.browser.subBrowsers.tracks.TracksCompanion;
import accentor.domain.Artist;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ArtistDetailCompanion extends DetailCompanion<ArtistDetailModel, Artist> {
    private BrowseCompanion superCompanion;

    public ArtistDetailCompanion(BrowseCompanion superCompanion, ArtistDetailModel model) {
        super(model);
        this.superCompanion = superCompanion;
    }

    @FXML
    public void initialize(){
        Button addSongsBtn = new Button("Add all songs to playlist");
        addSongsBtn.setOnAction(x -> addSongs());

        details.getChildren().add(addSongsBtn);

        Artist artist = getModel().getSubject();

        tab.setText(artist.getName());
        title.setText(artist.getName());
        image.setImage(new Image(artist.getLargeImageURL()));

        AlbumsCompanion albumsCompanion = new AlbumsCompanion(superCompanion, getModel().getAlbumsModel());
        TracksCompanion tracksCompanion = new TracksCompanion(superCompanion, getModel().getTracksModel());

        try {
            FXMLLoader fxmlLoader;
            BorderPane pane;
            // The path is apparently not "../subBrowsers/table.fxml" from here and i do not know what it should be
            fxmlLoader = new FXMLLoader(BrowseModel.class.getResource("subBrowsers/table.fxml"));

            fxmlLoader.setController(albumsCompanion);
            pane = fxmlLoader.load();

            VBox.setVgrow(pane, Priority.ALWAYS);// So that the table uses all available space
            details.getChildren().add(pane);

            fxmlLoader = new FXMLLoader(BrowseModel.class.getResource("subBrowsers/table.fxml"));
            fxmlLoader.setController(tracksCompanion);
            pane = fxmlLoader.load();

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
