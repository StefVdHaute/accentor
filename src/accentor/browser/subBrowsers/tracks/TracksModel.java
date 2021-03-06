package accentor.browser.subBrowsers.tracks;

import accentor.Helper;
import accentor.api.*;
import accentor.browser.BrowseModel;
import accentor.browser.subBrowsers.TableModel;
import accentor.specialistFxElements.cells.AlbumCellCompatible;
import accentor.specialistFxElements.cells.NameListCellCompatible;
import accentor.domain.Track;

import java.util.ArrayList;
import java.util.List;

public class TracksModel extends TableModel<Track, TrackFinder.SortOption>
        implements AlbumCellCompatible, NameListCellCompatible<Track.TrackArtist> {
    private final BrowseModel superBrowser;

    private final TrackFinder ogFinder;
    private TrackFinder finder;

    public TracksModel(TrackFinder finder, BrowseModel superBrowser) {
        this.superBrowser = superBrowser;

        this.ogFinder = finder;
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

    @Override
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
    public void resetPage() {
        super.resetPage();
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

        fireModelHasChanged();
        return tracks;
    }

    public List<Track> getAllData() {
        List<Track> tracks = null;
        TrackFinder tmpFinder = finder.setPerPage(Integer.MAX_VALUE);

        try {
            PaginatedResult<Track> result = tmpFinder.execute();
            setPage(result.getCurrentPage());
            setPages(result.getTotalPages());

            tracks = result.getData();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return tracks;
    }

    @Override
    public String getNames(List<Track.TrackArtist> ids){
        return Helper.getArtistsTrack(ids);
    }

    @Override
    public String getAlbumName(String id){
        return superBrowser.findAlbum(id).getTitle();
    }
}
