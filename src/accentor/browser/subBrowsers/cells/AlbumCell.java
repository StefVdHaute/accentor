package accentor.browser.subBrowsers.cells;

import javafx.scene.control.TableCell;

public class AlbumCell<T> extends TableCell<T, String> {
    private AlbumCellCompatible model;

    public AlbumCell(AlbumCellCompatible model){
        this.model = model;
    }

    @Override
    public void updateItem(String id, boolean empty){
        super.updateItem(id, empty);

        if (empty) {
            setText(null);
        } else {
            setText(model.getAlbumName(id));
        }
    }
}
