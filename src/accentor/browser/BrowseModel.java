package accentor.browser;

import accentor.api.*;
import accentor.browser.subBrowsers.albums.AlbumsModel;
import accentor.browser.subBrowsers.artists.ArtistsModel;
import accentor.browser.subBrowsers.queue.QueueModel;
import accentor.browser.subBrowsers.tracks.TracksModel;
import accentor.domain.Album;
import accentor.domain.Artist;
import accentor.domain.Track;
import javafx.scene.control.Tab;

import java.util.HashMap;
import java.util.List;

public class BrowseModel {
    private ArtistDAO artistDAO;
    private AlbumDAO albumDAO;
    private TrackDAO trackDAO;

    private QueueModel queueModel;
    private ArtistsModel artistsModel;
    private AlbumsModel albumsModel;
    private TracksModel tracksModel;

    private HashMap<String, Tab> artistTabs = new HashMap<>();
    private HashMap<String, Tab> albumTabs = new HashMap<>();
    private HashMap<String, Album> albumCache = new HashMap<>();

    public BrowseModel(DataAccessContext dac){
        artistDAO = dac.getArtistDAO();
        albumDAO  = dac.getAlbumDAO();
        trackDAO  = dac.getTrackDAO();

        queueModel = new QueueModel(this);
        artistsModel = new ArtistsModel(dac.getArtistDAO().list());
        albumsModel = new AlbumsModel(dac.getAlbumDAO().list());
        tracksModel = new TracksModel(dac.getTrackDAO().list(), this);
    }

    public QueueModel getQueueModel() {
        return queueModel;
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
        Artist artist = null;
        try {
            artist = artistDAO.findById(id);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return artist;
    }

    // This is only used for getting albumtitle's so instead of caching objects maybe cache title's
    public Album findAlbum(String id){
        try {
            if (!albumCache.containsKey(id)) {
                albumCache.put(id, albumDAO.findById(id));
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return albumCache.get(id);
    }

    public Track findTrack(String id){
        Track track = null;
        try {
            track = trackDAO.findById(id);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return track;
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

    public HashMap<String, Tab> getArtistTabs() {
        return artistTabs;
    }

    public HashMap<String, Tab> getAlbumTabs() {
        return albumTabs;
    }

    public void playSong(Track track){
        queueModel.setPlaying(track);
    }

    public void nextSong(Track track) {
        queueModel.setNext(track);
    }

    public void nextSongs(List<Track> tracks) {
        queueModel.addListToPlaylist(tracks);
    }
}
