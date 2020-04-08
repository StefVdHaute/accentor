package accentor.browser.subBrowsers.albums;

import accentor.Helper;
import accentor.api.AlbumFinder;
import accentor.api.DataAccessException;
import accentor.api.PaginatedResult;
import accentor.api.SortDirection;
import accentor.browser.subBrowsers.TableModel;
import accentor.browser.subBrowsers.cells.NameListCellCompatible;
import accentor.domain.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumsModel extends TableModel<Album, AlbumFinder.SortOption> implements NameListCellCompatible<Album.AlbumArtist> {
    private AlbumFinder ogFinder;
    private AlbumFinder finder;

    public AlbumsModel(AlbumFinder finder) {
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
    public void setSort(AlbumFinder.SortOption sortOn, boolean ascending){
        SortDirection dir = ascending ? SortDirection.ASCENDING : SortDirection.DESCENDING;
        finder = finder.setSortKey(sortOn).setSortDirection(dir);
    }

    @Override
    public void changePage(int increment){
        super.changePage(increment);
        finder = finder.setPage(getPage());
    }

    @Override //TODO: load pictures in cache with a task for quick access
    public List<Album> getData() {
        List<Album> albums;

        try {
            PaginatedResult<Album> result = finder.execute();
            setPage(result.getCurrentPage());
            setPages(result.getTotalPages());

            albums = result.getData();
        } catch (DataAccessException e) {
            e.printStackTrace();

            resetToOGFinder();
            albums = new ArrayList<>();
            setPage(1);
            setPages(1);
        }

        return albums;
    }

    @Override
    public String getNames(List<Album.AlbumArtist> ids){
        return Helper.getArtistsAlbum(ids);
    }
}
