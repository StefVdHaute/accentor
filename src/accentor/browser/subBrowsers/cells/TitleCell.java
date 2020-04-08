package accentor.browser.subBrowsers.cells;

import accentor.domain.Album;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class TitleCell extends TableCell<Album, String> {//Maybe for when pictures can be loaded in smoothly :')

    @Override
    public void updateItem(String title, boolean empty){
        super.updateItem(title, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText("\t" + title);
            if (getTableRow().getItem().hasImage()) {
                AnchorPane image = new AnchorPane(new ImageView(new Image(getTableRow().getItem().getSmallImageURL())));
                image.setPrefWidth(100);

                setGraphic(image);
            }
        }
    }
}