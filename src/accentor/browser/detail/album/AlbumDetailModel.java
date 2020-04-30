package accentor.browser.detail.album;

import accentor.browser.BrowseModel;
import accentor.browser.detail.DetailModel;
import accentor.browser.subBrowsers.tracks.TracksModel;
import accentor.domain.Album;

public class AlbumDetailModel extends DetailModel<Album> {
    private final TracksModel tracksModel;

    public AlbumDetailModel(Album album, BrowseModel browseModel){
        super(album, browseModel);

        tracksModel = new TracksModel(browseModel.getTracks(album), browseModel);
    }

    public TracksModel getTracksModel() {
        return tracksModel;
    }
}
