package accentor.browser.subBrowsers.cells;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

import java.util.List;

public class NameListCell<T, A> extends TableCell<T, List<A>> {
    private NameListCellCompatible<A> model;

    public NameListCell(NameListCellCompatible<A> model){
        super();
        this.model = model;

        setAlignment(Pos.CENTER_LEFT);
    }

    @Override
    public void updateItem(List<A> ids, boolean empty){
        super.updateItem(ids, empty);

        if (empty) {
            setText(null);
        } else {
            setText(model.getNames(ids));
        }
    }
}
