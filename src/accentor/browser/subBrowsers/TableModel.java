package accentor.browser.subBrowsers;

import accentor.Listener;

import java.util.List;

//TableModel could implement more if sortOptions and finders were generalized :'(
public abstract class TableModel<T, S> {
    private Listener listener;
    private int page = 1;
    private int pages = 1;

    public abstract void resetToOGFinder();
    public abstract void setFilter(String search);
    public abstract void setSort(S s, boolean ascending);

    public abstract List<T> getData();

    public void changePage(int increment) {
        page += increment;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void fireModelHasChanged() {
        listener.modelHasChanged();
    }
}
