package accentor.specialistFxElements;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlaceHolderImageView extends ImageView {

    //Parameters allowed to be null
    public PlaceHolderImageView(Image primaryImage, Image placeholder) {
        super(placeholder);

        if (primaryImage != null) {
            primaryImage.progressProperty().addListener(iv -> {
                if (primaryImage.getProgress() == 1) {
                    setImage(primaryImage);
                }
            });
        }
    }
}
