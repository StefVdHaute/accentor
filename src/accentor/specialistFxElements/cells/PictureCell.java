package accentor.specialistFxElements.cells;

import accentor.specialistFxElements.PlaceHolderImageView;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PictureCell<T> extends TableCell<T, String> {

    private Image placeholder;

    public PictureCell() {
        setAlignment(Pos.CENTER);
    }

    public PictureCell(Image placeholder) {
        this.placeholder = placeholder;
        setAlignment(Pos.CENTER);
    }

    @Override
    public void updateItem(String url, boolean empty){
        super.updateItem(url, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            Image image = null;

            if (url != null) {
                image = new Image(url, true);
            }

            ImageView imageView = new PlaceHolderImageView(image, placeholder);
            setGraphic(imageView);
        }
    }
}
