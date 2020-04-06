package accentor.browser.subBrowsers.cells;

import javafx.scene.control.TableCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCell<T> extends TableCell<T, LocalDate> {
    private String pattern;

    public DateCell(){
        pattern = "dd/MM/yyyy";
    };

    public DateCell(String datePattern){
        pattern = datePattern;
    };

    @Override
    public void updateItem(LocalDate date, boolean empty){
        super.updateItem(date, empty);

        if (empty) {
            setText(null);
        } else {
            setText(date.format(DateTimeFormatter.ofPattern(pattern)));
        }
    }
}