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

    /////////////////////////////////////////////Queue-removals/////////////////////////////////////////////////////////
    public void removeSong(int track) {
        if (track < playing) {
            playing --;
        }

        tracks.remove(track);
        fireModelChanged();
    }

    public void removeSong(Track track) {
        if (tracks.indexOf(track) < playing) {
            playing --;
        }

        tracks.remove(track);
        fireModelChanged();
    }

    ////////////////////////////////////////////Queue-additions/////////////////////////////////////////////////////////
    public void setPlaying(Track track) {
        playing = 0;
        tracks = new ArrayList<>();
        tracks.add(track);
        fireModelChanged();
    }

    public void setPlaying(List<Track> newTracks) {
        playing = 0;
        tracks = new ArrayList<>(newTracks);
        fireModelChanged();
    }

    public void addToNext(Track track) {
        if (tracks.contains(track)) {
            removeSong(track);
        }

        tracks.add(playing + 1, track);
        fireModelChanged();
    }

    public void addToNext(List<Track> newTracks) {
        if (tracks.size() >= 1) {
            int i = 1;
            for (Track track : newTracks) {
                tracks.add(playing + i, track);
            }

            fireModelChanged();
        }
        else setPlaying(newTracks);
    }

    public void addToEnd(Track track) {
        if (! tracks.contains(track)) {
            tracks.add(track);
        }

        fireModelChanged();
    }
    public void addToEnd(List<Track> newTracks) {
        for (Track track: newTracks) {
            addToEnd(track);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        Track track = null;

        if (playing < tracks.size()) {
            track = tracks.get(playing);
        }
        return track;
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

    public void reorder(int draggedIndex, int dropIndex) {
        Track draggedTrack = tracks.get(draggedIndex);
        removeSong(draggedIndex);

        tracks.add(dropIndex, draggedTrack);
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
