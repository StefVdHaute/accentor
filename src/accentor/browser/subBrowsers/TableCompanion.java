package accentor.browser.subBrowsers;

import accentor.Listener;
import accentor.browser.BrowseCompanion;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

public abstract class TableCompanion<M extends TableModel<T, S>, T, S> implements Listener {
    @FXML public TextField searchString;
    @FXML public Label pageNumber;
    @FXML public Button prev;
    @FXML public Button largePrev;
    @FXML public Button next;
    @FXML public Button largeNext;
    @FXML // I use a tableView to give the user the option to choose which columns are shown
    public TableView<T> table;

    // Needed to much to make these private
    protected M model;
    protected HashMap<String, S> sortMap = new HashMap<>();// Do not use getId since it results in unexpected sorting
    protected BrowseCompanion superCompanion;

    public TableCompanion(BrowseCompanion superCompanion, M model){
        this.model = model;
        this.superCompanion = superCompanion;
        model.setListener(this);
    }

    @FXML
    public void initialize(){
        //Initializing buttons
        prev.setUserData(-1);
        largePrev.setUserData(-5);
        next.setUserData(1);
        largeNext.setUserData(5);

        searchString.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                search();
                event.consume();
            }
        });

        table.setSortPolicy(table -> {
            ObservableList<TableColumn<T, ?>> columns = table.getSortOrder();

            if (!columns.isEmpty()) {
                String sortOn = columns.get(0).getText();
                boolean ascending = columns.get(0).getSortType() == TableColumn.SortType.ASCENDING;

                model.setSort(sortMap.get(sortOn), ascending);
                modelHasChanged();
            }

            return true;
        });

    }

    /////////////////////////////////////////////////Buttonfunctions////////////////////////////////////////////////////
    public void search(){
        model.resetToOGFinder();
        model.setFilter(searchString.getText());

        modelHasChanged();
    }

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

    @Override
    public void modelHasChanged(){
        table.getItems().clear();
        table.getItems().addAll(model.getData());

        updateLabel();
        updateButtons(model.getPage(), model.getPages());
    }
}
