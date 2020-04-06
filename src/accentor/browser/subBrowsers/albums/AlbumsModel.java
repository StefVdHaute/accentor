package accentor.browser.subBrowsers.albums;

import accentor.api.*;
import accentor.browser.subBrowsers.TableModel;
import accentor.browser.subBrowsers.cells.NameListCellCompatible;
import accentor.domain.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumsModel extends TableModel<Album> implements NameListCellCompatible<Album.AlbumArtist> {
    private AlbumDAO albumDAO;
    private AlbumFinder finder;

    public AlbumsModel(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
        resetFinder();
    }

    @Override
    public void resetFinder() {
        finder = albumDAO.list();
    }

    @Override
    public void setFilter(String search) {
        finder.setFilter(search);
    }

    public void setSort(AlbumFinder.SortOption sortOn, boolean ascending){
        SortDirection dir = ascending ? SortDirection.ASCENDING : SortDirection.DESCENDING;
        finder = finder.setSortKey(sortOn).setSortDirection(dir);
    }

    @Override
    public void changePage(int increment){
        super.changePage(increment);
        finder = finder.setPage(getPage());
    }

    //TODO: load pictures in cache with a task for quick access
    @Override
    public List<Album> getData() {
        List<Album> albums;

        try {
            PaginatedResult<Album> result = finder.execute();
            setPage(result.getCurrentPage());
            setPages(result.getTotalPages());

            albums = result.getData();
        } catch (DataAccessException e) {
            e.printStackTrace();

            resetFinder();
            albums = new ArrayList<>();
            setPage(1);
            setPages(1);
        }

        return albums;
    }

    public String getNames(List<Album.AlbumArtist> ids){
        String name = "Not found";

        if (ids != null){
            name = ids.parallelStream().map(Album.AlbumArtist::getName).collect(Collectors.joining(", "));
        }

        return name;
    }
}
