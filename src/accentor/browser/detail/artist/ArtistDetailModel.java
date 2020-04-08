package accentor.browser.detail.artist;

import accentor.browser.BrowseModel;
import accentor.browser.detail.DetailModel;
import accentor.browser.subBrowsers.albums.AlbumsModel;
import accentor.browser.subBrowsers.tracks.TracksModel;
import accentor.domain.Artist;

public class ArtistDetailModel extends DetailModel<Artist> {
    private AlbumsModel albumsModel;
    private TracksModel tracksModel;

    public ArtistDetailModel(Artist artist, BrowseModel browseModel) {
        super(artist, browseModel);

        albumsModel = new AlbumsModel(browseModel.getAlbums(artist));
        tracksModel = new TracksModel(browseModel.getTracks(artist),browseModel);
    }

    public TracksModel getTracksModel() {
        return tracksModel;
    }

    public AlbumsModel getAlbumsModel() {
        return albumsModel;
    }
}
