package accentor.browser.subBrowsers.cells;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PictureCell<T> extends TableCell<T, String> {

    public PictureCell() {
        setAlignment(Pos.CENTER);
    }

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
