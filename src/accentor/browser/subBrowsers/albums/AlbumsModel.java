package accentor.browser.subBrowsers.albums;

import accentor.api.AlbumDAO;
import accentor.api.DataAccessException;
import accentor.browser.subBrowsers.TableModel;
import accentor.domain.Album;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumsModel extends TableModel<Album> {
    private AlbumDAO albumDAO;

    private List<Album> albums = new ArrayList<>();

    public AlbumsModel(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    @Override
    public void resetFinder() {

    }

    @Override
    public void setFilter(String search) {

    }

    //TODO: load pictures in cache with a task for quick access
    @Override
    public List<Album> getData() {
        try {
            albums = albumDAO.list().execute().getData();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return albums;
    }

    public String getArtistsName(List<Album.AlbumArtist> ids) {
        String name = "Not found";

        if (ids != null){
            name = ids.parallelStream().map(Album.AlbumArtist::getName).collect(Collectors.joining(", "));
        }

        return name;
    }
}
