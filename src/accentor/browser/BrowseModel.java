package accentor.browser;

import accentor.api.*;
import accentor.browser.subBrowsers.albums.AlbumsModel;
import accentor.browser.subBrowsers.artists.ArtistsModel;
import accentor.browser.subBrowsers.tracks.TracksModel;
import accentor.domain.Album;
import accentor.domain.Artist;
import accentor.domain.Track;

public class BrowseModel {
    private ArtistDAO artistDAO;
    private AlbumDAO albumDAO;
    private TrackDAO trackDAO;

    private ArtistsModel artistsModel;
    private AlbumsModel albumsModel;
    private TracksModel tracksModel;

    public BrowseModel(DataAccessContext dac){
        artistDAO = dac.getArtistDAO();
        albumDAO  = dac.getAlbumDAO();
        trackDAO  = dac.getTrackDAO();

        artistsModel = new ArtistsModel(dac.getArtistDAO().list());
        albumsModel = new AlbumsModel(dac.getAlbumDAO().list());
        tracksModel = new TracksModel(dac.getTrackDAO().list(), this);
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

    public Artist findArtist(String id){
        try {
            return artistDAO.findById(id);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Album findAlbum(String id){
        try {
            return albumDAO.findById(id);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Track findTrack(String id){
        try {
            return trackDAO.findById(id);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public AlbumFinder getAlbums(Artist artist) {
        return artistDAO.albums(artist);
    }

    public TrackFinder getTracks(Artist artist) {
        return artistDAO.tracks(artist);
    }

    public TrackFinder getTracks(Album album) {
        return albumDAO.tracks(album);
    }
}
