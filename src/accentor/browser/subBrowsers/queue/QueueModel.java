package accentor.browser.subBrowsers.queue;

import accentor.Helper;
import accentor.Listener;
import accentor.browser.BrowseModel;
import accentor.browser.subBrowsers.cells.AlbumCellCompatible;
import accentor.browser.subBrowsers.cells.NameListCellCompatible;
import accentor.domain.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueueModel implements AlbumCellCompatible, NameListCellCompatible<Track.TrackArtist> {
    private BrowseModel superBrowser;
    private List<Listener> listeners = new ArrayList<>();
    private List<Track> tracks = new ArrayList<>();
    private int playing = 0;
    private Boolean repeat = false;

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
        fireModelChanged();
    }

    public void setNext(Track track) {
        tracks.add(1, track);
        fireModelChanged();
    }

    public void addListToPlaylist(List<Track> newTracks) {
        tracks.addAll(newTracks);
        fireModelChanged();
    }

    public void addToQueue(Track track) {
        tracks.add(track);
        fireModelChanged();
    }

    public void addToQueue(List<Track> newTracks) {
        tracks.addAll(newTracks);
        fireModelChanged();
    }

    @Override
    public String getNames(List<Track.TrackArtist> ids) {
        return Helper.getArtistsTrack(ids);
    }

    @Override
    public String getAlbumName(String id) {
        return superBrowser.findAlbum(id).getTitle();
    }

    public Track get(int index) {
        return tracks.get(index);
    }

    public Track getCurrent() {
        return tracks.get(playing);
    }

    public Track getNext() {
        Track track = null;

        if (playing + 1 < tracks.size() || repeat) {
            track = tracks.get((playing + 1) % tracks.size());
        }

        return track;
    }

    public void incrementPlaying(int incremental) {
        this.playing = (playing + incremental) % tracks.size();
    }

    public void setPlaying(int playing) {
        this.playing = playing;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }
    public void shuffle() {
        Track currentTrack = tracks.get(playing);
        Collections.shuffle(tracks);
        playing = -1;

        int i = 0;
        while (i < tracks.size() && playing < 0) {
            if (tracks.get(i) == currentTrack) {
                playing = i;
            }

            i++;
        }
        fireModelChanged();
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    private void fireModelChanged() {
        for (Listener listener : listeners) {
            listener.modelHasChanged();
        }
    }
}
