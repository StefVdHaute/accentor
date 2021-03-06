package accentor.browser.detail;

import accentor.browser.BrowseModel;

public abstract class DetailModel<T> {
    private final T subject;
    private final BrowseModel browseModel;

    public DetailModel(T subject, BrowseModel browseModel){
        this.browseModel = browseModel;
        this.subject = subject;
    }


    public T getSubject() {
        return subject;
    }
}
