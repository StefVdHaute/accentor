package accentor.browser.subBrowsers.tracks;

import accentor.api.*;
import accentor.browser.subBrowsers.TableModel;
import accentor.domain.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TracksModel extends TableModel<Track> {
    private TrackDAO trackDAO;
    private AlbumDAO albumDAO;
    private TrackFinder finder;

    private HashMap<String, String> albumcache = new HashMap<>();
    {
        albumcache.put(null, "Not found");
    }

    public TracksModel(TrackDAO trackDAO, AlbumDAO albumDAO) {
        this.trackDAO = trackDAO;
        this.albumDAO = albumDAO;

        resetFinder();
    }

    @Override
    public void resetFinder() {
        finder = trackDAO.list();
    }

    @Override
    public void setFilter(String search){
        finder = finder.setFilter(search);
    }

    public void setSort(TrackFinder.SortOption sortOn, boolean ascending){
        SortDirection dir = ascending ? SortDirection.ASCENDING : SortDirection.DESCENDING;
        finder = finder.setSortKey(sortOn).setSortDirection(dir);
    }

    @Override
    public void changePage(int increment) {
        super.changePage(increment);
        finder = finder.setPage(getPage());
    }

    @Override
    public List<Track> getData() {
        List<Track> tracks;

        try {
            PaginatedResult<Track> result = finder.execute();
            setPage(result.getCurrentPage());
            setPages(result.getTotalPages());

            tracks = result.getData();
        } catch (DataAccessException e) {
            e.printStackTrace();

            resetFinder();
            tracks = new ArrayList<>();
            setPage(1);
            setPages(1);
        }

        return tracks;
    }

    public String getArtistsName(List<Track.TrackArtist> ids){
        String name = "Not found";

        if (ids != null){
            name = ids.parallelStream().map(Track.TrackArtist::getName).collect(Collectors.joining(", "));
        }

        return name;
    }

    public String getAlbumName(String id){
        if (!albumcache.containsKey(id)) {
            try {
                albumcache.put(id, albumDAO.findById(id).getTitle());
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        return albumcache.get(id);
    }
}
