package accentor.browser.detail;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class DetailCompanion<M extends DetailModel<T>, T> {
    @FXML public Tab tab;
    @FXML public HBox topview;
    @FXML public VBox details;
    @FXML public VBox info;
    @FXML public Label title;

    private final M model;

    public DetailCompanion(M model){
        this.model = model;
    }

    @FXML
    public void initialize(){
        tab.setText(model.getSubject().toString());
        title.setText(model.getSubject().toString());
    }

    public M getModel() {
        return model;
    }

    abstract public void addSongs();
}
