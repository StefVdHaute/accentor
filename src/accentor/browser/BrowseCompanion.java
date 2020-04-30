package accentor.browser;

import accentor.browser.detail.album.AlbumDetailCompanion;
import accentor.browser.detail.album.AlbumDetailModel;
import accentor.browser.detail.artist.ArtistDetailCompanion;
import accentor.browser.detail.artist.ArtistDetailModel;
import accentor.browser.player.PlayerCompanion;
import accentor.browser.subBrowsers.albums.AlbumsCompanion;
import accentor.browser.subBrowsers.artists.ArtistsCompanion;
import accentor.browser.subBrowsers.queue.QueueCompanion;
import accentor.browser.subBrowsers.tracks.TracksCompanion;
import accentor.domain.Album;
import accentor.domain.Artist;
import accentor.domain.Track;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class BrowseCompanion {
    @FXML public BorderPane base;
    @FXML public TabPane tabPane;
    @FXML public Tab queue;
    @FXML public Tab artists;
    @FXML public Tab albums;
    @FXML public Tab tracks;

    private final BrowseModel model;

    //Tooltip not properly working (on linux mint 19.3)
    private final Tooltip closeTip = new Tooltip("alt+F4");

    public BrowseCompanion(BrowseModel model){
        this.model = model;
    }


    @FXML
    public void initialize(){
        FXMLLoader fxmlLoader;
        QueueCompanion queueCompanion = new QueueCompanion(this, model.getQueueModel());
        ArtistsCompanion artistsCompanion = new ArtistsCompanion(this, model.getArtistsModel());
        AlbumsCompanion albumsCompanion = new AlbumsCompanion(this, model.getAlbumsModel());
        TracksCompanion tracksCompanion = new TracksCompanion(this, model.getTracksModel());
        PlayerCompanion playerCompanion = new PlayerCompanion(model.getQueueModel());

        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("subBrowsers/table.fxml"));
            fxmlLoader.setController(albumsCompanion);
            albums.setContent(fxmlLoader.load());

            fxmlLoader = new FXMLLoader(getClass().getResource("subBrowsers/table.fxml"));
            fxmlLoader.setController(artistsCompanion);
            artists.setContent(fxmlLoader.load());

            fxmlLoader = new FXMLLoader(getClass().getResource("subBrowsers/table.fxml"));
            fxmlLoader.setController(tracksCompanion);
            tracks.setContent(fxmlLoader.load());

            fxmlLoader = new FXMLLoader(getClass().getResource("subBrowsers/queue/queue.fxml"));
            fxmlLoader.setController(queueCompanion);
            queue.setContent(fxmlLoader.load());

            fxmlLoader = new FXMLLoader(getClass().getResource("player/player.fxml"));
            fxmlLoader.setController(playerCompanion);
            base.setBottom(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        tabPane.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.F4 && keyEvent.isControlDown()) {
                Tab selected = tabPane.getSelectionModel().getSelectedItem();

                if (selected.isClosable()) {
                    tabPane.getTabs().remove(selected);
                    keyEvent.consume();
                }
            }
        });
    }

    public void openTab(Album album) {
        HashMap<String, Tab> albumTabs = model.getAlbumTabs();
        String albumId = album.getId();
        Tab tab = albumTabs.get(albumId);

        if (tab == null) {
            AlbumDetailCompanion albumDetailCompanion = new AlbumDetailCompanion(this, new AlbumDetailModel(album, model));

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detail/detail.fxml"));
                fxmlLoader.setController(albumDetailCompanion);
                Tab newTab = fxmlLoader.load();


                newTab.setOnClosed(e -> albumTabs.remove(album.getId()));

                Platform.runLater(() -> {
                    if (albumTabs.get(albumId) == null) {
                        albumTabs.put(albumId, newTab);
                        tabPane.getTabs().add(newTab);
                        tabPane.getSelectionModel().selectLast();
                    }
                    tabPane.getScene().getStylesheets().remove("accentor/stylesheets/wait.css");
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            tabPane.getSelectionModel().select(tab);
        }
    }

    public void openTab(Artist artist) {
        HashMap<String, Tab> artistTabs = model.getArtistTabs();
        String artistId = artist.getId();
        Tab tab = artistTabs.get(artistId);

        if (tab == null) {
            ArtistDetailCompanion artistDetailCompanion
                    = new ArtistDetailCompanion(this, new ArtistDetailModel(artist, model));

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("detail/detail.fxml"));
                fxmlLoader.setController(artistDetailCompanion);
                Tab newTab = fxmlLoader.load();

                newTab.setOnClosed(e -> artistTabs.remove(artist.getId()));

                Platform.runLater(() -> {
                    if (artistTabs.get(artistId) == null) {
                        artistTabs.put(artistId, newTab);
                        tabPane.getTabs().add(newTab);
                        tabPane.getSelectionModel().selectLast();
                    }
                    tabPane.getScene().getStylesheets().remove("accentor/stylesheets/wait.css");
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            tabPane.getSelectionModel().select(tab);
        }
    }

    public void openTab(String id, boolean artist) {
        if (artist) {
            openTab(model.findArtist(id));
        } else {
            openTab(model.findAlbum(id));
        }
    }

    ////////////////////////////////Queue manipulation//////////////////////////////////////////////////////////////////
    public void playSong(Track track){
        model.playSong(track);
    }

    public void playSong(int index) {
        model.playSong(index);
    }

    public void playSong(List<Track> tracks) {
        model.playSong(tracks);
    }

    public void nextSong(Track track) {
        model.nextSong(track);
    }

    public void nextSong(List<Track> tracks) {
        model.nextSong(tracks);
    }

    public void addToQueueEnd(Track track) {
        model.addToQueueEnd(track);
    }

    public void addToQueueEnd(List<Track> tracks) {
        model.addToQueueEnd(tracks);
    }
}
