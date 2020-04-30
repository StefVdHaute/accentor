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
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ArtistDetailCompanion extends DetailCompanion<ArtistDetailModel, Artist> {
    private final BrowseCompanion superCompanion;

    public ArtistDetailCompanion(BrowseCompanion superCompanion, ArtistDetailModel model) {
        super(model);
        this.superCompanion = superCompanion;
    }

    @FXML
    public void initialize(){
        ButtonBar playlistBtns = new ButtonBar();

        Button playSongsBtn = new Button("Play all songs now");
        playSongsBtn.setOnAction(x -> playSongs());

        Button nextSongsBtn = new Button("Play all songs next");
        nextSongsBtn.setOnAction(x -> playSongsNext());

        Button addSongsBtn = new Button("Add all songs to your playlist");
        addSongsBtn.setOnAction(x -> addSongs());

        playlistBtns.getButtons().addAll(playSongsBtn, nextSongsBtn, addSongsBtn);
        details.getChildren().add(playlistBtns);

        Artist artist = getModel().getSubject();

        tab.setText(artist.getName());
        title.setText(artist.getName());
        image.setImage(new Image(artist.getLargeImageURL(), true));

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

    public void playSongs() {
        superCompanion.playSong(getModel().getTracksModel().getAllData());
    }

    public void playSongsNext() {
        superCompanion.nextSong(getModel().getTracksModel().getAllData());
    }

    public void addSongs() {
        superCompanion.addToQueueEnd(getModel().getTracksModel().getAllData());
    }
}
