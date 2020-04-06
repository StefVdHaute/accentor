package accentor.browser.subBrowsers.artists;

import accentor.api.ArtistDAO;
import accentor.browser.subBrowsers.TableModel;
import accentor.domain.Artist;

import java.util.List;

public class ArtistsModel extends TableModel<Artist> {
    public ArtistsModel(ArtistDAO artistDAO) {
    }

    @Override
    public void resetFinder() {

    }

    @Override
    public void setFilter(String search) {

    }

    @Override
    public List<Artist> getData() {
        return null;
    }
}
