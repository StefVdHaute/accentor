package accentor.browser.subBrowsers.queue;

import accentor.Helper;
import accentor.Listener;
import accentor.browser.BrowseModel;
import accentor.browser.subBrowsers.cells.AlbumCellCompatible;
import accentor.browser.subBrowsers.cells.NameListCellCompatible;
import accentor.domain.Track;

import java.util.ArrayList;
import java.util.List;

public class QueueModel implements AlbumCellCompatible, NameListCellCompatible<Track.TrackArtist> {
    private BrowseModel superBrowser;
    private Listener listener;
    private List<Track> tracks = new ArrayList<>();
    private int playing = 0;

    public QueueModel(BrowseModel superBrowser) {
        this.superBrowser = superBrowser;
    }

    public List<Track> getData() {
        return tracks;
    }

    public void setPlaying(Track track) {
        playing = 0;
        tracks = new ArrayList<>();
        tracks.add(track);
        listener.modelHasChanged();
    }

    public void setNext(Track track) {
        tracks.add(1, track);
        listener.modelHasChanged();
    }

    public void setNext(List<Track> newTracks) {
        tracks.addAll(1, newTracks);
        listener.modelHasChanged();
    }

    public void addToQueue(Track track) {
        tracks.add(track);
        listener.modelHasChanged();
    }

    public void addToQueue(List<Track> newTracks) {
        tracks.addAll(newTracks);
        listener.modelHasChanged();
    }

    @Override
    public String getNames(List<Track.TrackArtist> ids) {
        return Helper.getArtistsTrack(ids);
    }

    @Override
    public String getAlbumName(String id) {
        return superBrowser.findAlbum(id).getTitle();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public Track getNext() {
        Track track = null;

        if (playing + 1 < tracks.size()) {
            track = tracks.get(playing + 1);
        }

        return track;
    }

    public void incrementPlaying(int incremental) {
        this.playing += incremental;
    }

    public void setPlaying(int playing) {
        this.playing = playing;
    }
}
