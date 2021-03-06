package accentor.browser.detail.album;

import accentor.Helper;
import accentor.browser.BrowseCompanion;
import accentor.browser.BrowseModel;
import accentor.browser.detail.DetailCompanion;
import accentor.browser.subBrowsers.tracks.TracksCompanion;
import accentor.domain.Album;
import accentor.specialistFxElements.PlaceHolderImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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
        Album album = getModel().getSubject();

        Image albumImage = null;

        if (album.hasImage()) {
            albumImage = new Image(album.getMediumImageURL(), true);
        }

        //For consistent image-holder size
        StackPane imagePane = new StackPane(new PlaceHolderImageView(
                albumImage,
                new Image("accentor/images/album_placeholder_medium.png")
        ));
        imagePane.setAlignment(Pos.CENTER);
        imagePane.setMinWidth(260);
        imagePane.setMaxWidth(260);
        imagePane.setMinHeight(260);
        imagePane.setMaxHeight(260);

        topview.getChildren().add(0, imagePane);

        //Threading the buttonFunctions in order to add a loading cursor is not worth the effort
        ButtonBar playlistBtns = new ButtonBar();

        Button playSongsBtn = new Button("Play all songs now");
        playSongsBtn.setOnAction(x -> playSongs());

        Button nextSongsBtn = new Button("Play all songs next");
        nextSongsBtn.setOnAction(x -> playSongsNext());

        Button addSongsBtn = new Button("Add all songs to your playlist");
        addSongsBtn.setOnAction(x -> addSongs());

        playlistBtns.getButtons().addAll(playSongsBtn, nextSongsBtn, addSongsBtn);
        details.getChildren().add(playlistBtns);

        tab.setText(album.getTitle());
        title.setText(album.getTitle());
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
