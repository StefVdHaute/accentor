package accentor.specialistFxElements.cells;

import javafx.scene.control.TableCell;

public class DurationCell<T> extends TableCell<T, Integer> {
    @Override
    public void updateItem(Integer duration, boolean empty){
        super.updateItem(duration, empty);
        if (empty) {
            setText(null);
        } else {
            int min = duration / 60;
            int sec = duration % 60;

            if (sec >= 10) {
                setText(min + ":" + sec);
            } else {
                setText(min + ":0" + sec);
            }
        }
    }
}