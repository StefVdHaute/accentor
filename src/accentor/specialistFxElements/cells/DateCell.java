package accentor.specialistFxElements.cells;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCell<T> extends TableCell<T, LocalDate> {

    public DateCell(){
        super();
        setAlignment(Pos.CENTER);
    }

    @Override
    public void updateItem(LocalDate date, boolean empty){
        super.updateItem(date, empty);

        if (empty) {
            setText(null);
        } else {
            setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }
}