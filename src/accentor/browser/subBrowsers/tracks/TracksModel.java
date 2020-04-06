package accentor.browser.subBrowsers.tracks;

import accentor.Helper;
import accentor.api.*;
import accentor.browser.BrowseModel;
import accentor.browser.subBrowsers.TableModel;
import accentor.browser.subBrowsers.cells.AlbumCellCompatible;
import accentor.browser.subBrowsers.cells.NameListCellCompatible;
import accentor.domain.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TracksModel
        extends TableModel<Track>
        implements AlbumCellCompatible, NameListCellCompatible<Track.TrackArtist> {
    private BrowseModel superBrowser;
    private TrackFinder ogFinder;
    private TrackFinder finder;

    private HashMap<String, String> albumcache = new HashMap<>();
    {
        albumcache.put(null, "Not found");
    }

    public TracksModel(TrackFinder finder, BrowseModel superBrowser) {
        this.ogFinder = finder;
        this.superBrowser = superBrowser;

        resetToOGFinder();
    }

    @Override
    public void resetToOGFinder() {
        finder = ogFinder;
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

            resetToOGFinder();
            tracks = new ArrayList<>();
            setPage(1);
            setPages(1);
        }

        return tracks;
    }

    @Override
    public String getNames(List<Track.TrackArtist> ids){
        return Helper.getArtistsTrack(ids);
    }

    @Override
    public String getAlbumName(String id){
        if (!albumcache.containsKey(id)) {
            albumcache.put(id, superBrowser.findAlbum(id).getTitle());
        }

        return albumcache.get(id);
    }
}
