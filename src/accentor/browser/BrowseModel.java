package accentor.browser;

import accentor.api.DataAccessContext;
import accentor.browser.subBrowsers.albums.AlbumsModel;
import accentor.browser.subBrowsers.artists.ArtistsModel;
import accentor.browser.subBrowsers.tracks.TracksModel;

public class BrowseModel {
    private DataAccessContext dac;

    private ArtistsModel artistsModel;
    private AlbumsModel albumsModel;
    private TracksModel tracksModel;

    public BrowseModel(DataAccessContext dac){
        this.dac = dac;
        artistsModel = new ArtistsModel(dac.getArtistDAO());
        albumsModel = new AlbumsModel(dac.getAlbumDAO());
        tracksModel = new TracksModel(dac.getTrackDAO(), dac.getAlbumDAO());
    }

    public ArtistsModel getArtistsModel() {
        return artistsModel;
    }

    public AlbumsModel getAlbumsModel() {
        return albumsModel;
    }

    public TracksModel getTracksModel() {
        return tracksModel;
    }

    public void setAlbumsModel(AlbumsModel albumsModel) {
        this.albumsModel = albumsModel;
    }
}
