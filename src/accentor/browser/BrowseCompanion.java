package accentor.browser;

import accentor.browser.subBrowsers.albums.AlbumsCompanion;
import accentor.browser.subBrowsers.artists.ArtistsCompanion;
import accentor.browser.subBrowsers.tracks.TracksCompanion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

public class BrowseCompanion {
    @FXML public Tab artists;
    @FXML public Tab albums;
    @FXML public Tab tracks;

    private BrowseModel model;

    public BrowseCompanion(BrowseModel model){
        this.model = model;
    }

    public void initialize(){
        FXMLLoader fxmlLoader;
        ArtistsCompanion artistsCompanion = new ArtistsCompanion(model.getArtistsModel());
        AlbumsCompanion albumsCompanion = new AlbumsCompanion(model.getAlbumsModel());
        TracksCompanion tracksCompanion = new TracksCompanion(model.getTracksModel());

        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("subBrowsers/table.fxml"));
            fxmlLoader.setController(artistsCompanion);
            artists.setContent(fxmlLoader.load());

            fxmlLoader = new FXMLLoader(getClass().getResource("subBrowsers/table.fxml"));
            fxmlLoader.setController(albumsCompanion);
            albums.setContent(fxmlLoader.load());

            fxmlLoader = new FXMLLoader(getClass().getResource("subBrowsers/table.fxml"));
            fxmlLoader.setController(tracksCompanion);
            tracks.setContent(fxmlLoader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
