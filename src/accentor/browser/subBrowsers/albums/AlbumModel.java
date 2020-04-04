package accentor.browser.subBrowsers.albums;

import accentor.api.AlbumDAO;
import accentor.api.DataAccessException;
import accentor.domain.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumModel {
    private AlbumDAO albumDAO;

    private List<Album> albums = new ArrayList<>();

    public AlbumModel(AlbumDAO albumDAO) {
        this.albumDAO = albumDAO;
    }

    public List<Album> getAlbums() {
        try {
            albums = albumDAO.list().execute().getData();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return albums;
    }
}
