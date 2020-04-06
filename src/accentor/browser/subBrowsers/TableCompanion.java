package accentor.browser.subBrowsers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public abstract class TableCompanion<M extends TableModel<T>, T> {
    @FXML
    public Label pageNumber;
    @FXML
    public Button prev;
    @FXML
    public Button largePrev;
    @FXML
    public Button next;
    @FXML
    public Button largeNext;
    @FXML // I use a tableView to give the user the option to choose which columns are shown
    public TableView<T> table;

    protected M model;// Need model to much to make this private

    public TableCompanion(M model){
        this.model = model;
    }

    @FXML
    public void initialize(){
        //Initializing buttons
        prev.setUserData(-1);
        largePrev.setUserData(-5);
        next.setUserData(1);
        largeNext.setUserData(5);

        table.setSortPolicy(x -> null);
    }

    /////////////////////////////////////////////////Buttonfunctions////////////////////////////////////////////////////
    protected void changePage(int increment) {
        model.changePage(increment);
        table.getItems().clear();
        table.getItems().addAll(model.getData());

        updateLabel();
        updateButtons(model.getPage(), model.getPages());
    }

    protected void updateButtons(int pageNr, int pages) {
        prev.setDisable(pageNr - 1 < 1);
        largePrev.setDisable(pageNr + (Integer)largePrev.getUserData() < 1);
        next.setDisable(pageNr + 1 > pages);
        largeNext.setDisable(pageNr + (Integer)largeNext.getUserData() > pages);
    }

    public void doPageButton(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        changePage((Integer)sourceButton.getUserData());
    }

    ///////////////////////////////////////////////////////Others///////////////////////////////////////////////////////

    protected void updateLabel() {//TODO: change to listener in model
        pageNumber.setText(model.getPage() + "/" + model.getPages());
    }
}
