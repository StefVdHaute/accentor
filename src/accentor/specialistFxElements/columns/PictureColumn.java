package accentor.specialistFxElements.columns;

import javafx.scene.control.TableColumn;

public class PictureColumn<T> extends TableColumn<T, String> {
    public PictureColumn() {
        super();
        init();
    }
    public PictureColumn(String name) {
        super(name);
        init();
    }

    private void init() {
        visibleProperty().addListener(iv -> {
            if (this.isVisible()) {
                this.getStyleableParent().getStyleClass().add("picture-table");
            } else {
                this.getStyleableParent().getStyleClass().remove("picture-table");
            }
        });
    }
}
