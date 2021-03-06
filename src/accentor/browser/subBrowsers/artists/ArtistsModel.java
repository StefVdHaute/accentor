package accentor.browser.subBrowsers.artists;

import accentor.api.*;
import accentor.browser.subBrowsers.TableModel;
import accentor.domain.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistsModel extends TableModel<Artist, ArtistFinder.SortOption> {
    private final ArtistFinder ogFinder;
    private ArtistFinder finder;

    public ArtistsModel(ArtistFinder finder) {
        this.ogFinder = finder;
        resetToOGFinder();
    }

    @Override
    public void resetToOGFinder() {
        finder = ogFinder;
    }

    @Override
    public void setFilter(String search) {
        finder = finder.setFilter(search);
    }

    @Override
    public void setSort(ArtistFinder.SortOption sortOn, boolean ascending){
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
    public List<Artist> getData() {
        List<Artist> artists;

        try {
            PaginatedResult<Artist> result = finder.execute();
            setPage(result.getCurrentPage());
            setPages(result.getTotalPages());

            artists = result.getData();
        } catch (DataAccessException e) {
            e.printStackTrace();

            resetToOGFinder();
            artists = new ArrayList<>();
            setPage(1);
            setPages(1);
        }

        fireModelHasChanged();
        return artists;
    }
}
