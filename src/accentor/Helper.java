package accentor;

import accentor.domain.Album;
import accentor.domain.Track;

import java.util.List;

public class Helper {
    public static String getArtistsTrack(List<Track.TrackArtist> ids){
        String name = "Not found";

        if (ids != null && !ids.isEmpty()){
            String[] names = new String[ids.size()];

            for (Track.TrackArtist id : ids) {
                names[id.getOrder() - 1] = id.getName();
            }

            name = String.join(", ", names);
        }

        return name;
    }

    public static String getArtistsAlbum(List<Album.AlbumArtist> ids){
        String name = "Not found";

        if (ids != null && !ids.isEmpty()){
            String[] names = new String[ids.size()];

            for (Album.AlbumArtist id : ids) {
                names[id.getOrder() - 1] = id.getName() + id.getSeparator();
            }

            name = String.join("", names);
        }

        // I think the substring method is the easiest, it's either this or comparing everything for null
        return name.substring(0, name.length() - 4);
    }
}
