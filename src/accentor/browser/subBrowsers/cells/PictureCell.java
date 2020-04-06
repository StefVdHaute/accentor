package accentor.browser.subBrowsers.cells;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PictureCell<T> extends TableCell<T, String> {
    @Override
    public void updateItem(String url, boolean empty){
        super.updateItem(url, empty);

        if (empty || url == null) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic(new ImageView(new Image(url)));
        }
    }
}