package accentor.browser.subBrowsers.artists;

import accentor.api.*;
import accentor.browser.subBrowsers.TableModel;
import accentor.domain.Artist;

import java.util.ArrayList;
import java.util.List;

public class ArtistsModel extends TableModel<Artist> {
    private ArtistDAO artistDAO;
    private ArtistFinder finder;

    public ArtistsModel(ArtistDAO artistDAO) {
        this.artistDAO = artistDAO;
        resetFinder();
    }

    @Override
    public void resetFinder() {
        finder = artistDAO.list();
    }

    @Override
    public void setFilter(String search) {
        finder = finder.setFilter(search);
    }

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
    public List<Artist> getData() {
        List<Artist> artists;

        try {
            PaginatedResult<Artist> result = finder.execute();
            setPage(result.getCurrentPage());
            setPages(result.getTotalPages());

            artists = result.getData();
        } catch (DataAccessException e) {
            e.printStackTrace();

            resetFinder();
            artists = new ArrayList<>();
            setPage(1);
            setPages(1);
        }

        return artists;
    }
}
