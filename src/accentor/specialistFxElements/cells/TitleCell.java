package accentor.specialistFxElements.cells;

import accentor.specialistFxElements.PlaceHolderImageView;
import accentor.domain.Album;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class TitleCell extends TableCell<Album, String> {

    private final Image placeholder;

    public TitleCell(Image placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void updateItem(String title, boolean empty){
        super.updateItem(title, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText("\t" + title);
            if (getTableRow().getItem().hasImage()) {
                AnchorPane image = new AnchorPane(
                        new PlaceHolderImageView(
                                new Image(getTableRow().getItem().getSmallImageURL(), true)
                                , placeholder
                        )
                );
                image.setPrefWidth(100);

                setGraphic(image);
            }
        }
    }
}