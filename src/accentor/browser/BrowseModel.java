package accentor.browser;

import accentor.api.DataAccessContext;
import accentor.browser.subBrowsers.albums.AlbumModel;
import accentor.browser.subBrowsers.artists.ArtistModel;
import accentor.browser.subBrowsers.tracks.TracksModel;

public class BrowseModel {
    private DataAccessContext dac;

    private ArtistModel artistsModel;
    private AlbumModel albumsModel;
    private TracksModel tracksModel;

    public BrowseModel(DataAccessContext dac){
        this.dac = dac;
        artistsModel = new ArtistModel(dac.getArtistDAO());
        albumsModel = new AlbumModel(dac.getAlbumDAO());
        tracksModel = new TracksModel(dac.getTrackDAO(), dac.getAlbumDAO());
    }

    public ArtistModel getArtistsModel() {
        return artistsModel;
    }

    public AlbumModel getAlbumsModel() {
        return albumsModel;
    }

    public TracksModel getTracksModel() {
        return tracksModel;
    }

    public void setAlbumsModel(AlbumModel albumsModel) {
        this.albumsModel = albumsModel;
    }
}
