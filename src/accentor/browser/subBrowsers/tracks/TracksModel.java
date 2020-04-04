package accentor.browser.subBrowsers.tracks;

import accentor.api.*;
import accentor.domain.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TracksModel {
    private TrackDAO trackDAO;
    private AlbumDAO albumDAO;

    private List<Track> tracks = new ArrayList<>();
    private HashMap<String, String> albumcache = new HashMap<>();

    public TracksModel(TrackDAO trackDAO, AlbumDAO albumDAO) {
        this.trackDAO = trackDAO;
        this.albumDAO = albumDAO;
    }

    public List<Track> getTracks() {
        try {
            tracks = trackDAO.list().execute().getData();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return tracks;
    }

    public String getAlbumName(String id){
        if (id == null){
            return "Not found";
        }

        if (!albumcache.containsKey(id)) {
            try {
                albumcache.put(id, albumDAO.findById(id).getTitle());
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        return albumcache.get(id);
    }

    public String getArtistsName(List<Track.TrackArtist> ids){
        if (ids == null || ids.size() == 0){
            return "Not found";
        }

        return ids.parallelStream().map(Track.TrackArtist::getName).collect(Collectors.joining());
    }
}
