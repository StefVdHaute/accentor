package accentor.browser.subBrowsers.artists;

import accentor.browser.subBrowsers.TableCompanion;
import accentor.browser.subBrowsers.cells.PictureCell;
import accentor.domain.Artist;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ArtistsCompanion extends TableCompanion<ArtistsModel, Artist> {
    private TableColumn<Artist, String> picture = new TableColumn<>();
    private TableColumn<Artist, String> name  = new TableColumn<>();

    public ArtistsCompanion(ArtistsModel model) {
        super(model);
    }

    @FXML
    public void initialize(){
        super.initialize();

        table.getColumns().add(picture);
        table.getColumns().add(name);

        picture.setCellValueFactory( new PropertyValueFactory<>("smallImageURL"));
        name.setCellValueFactory( new PropertyValueFactory<>("name"));

        picture.setCellFactory(column -> new PictureCell<>() );

        table.getItems().addAll(model.getData());

        updateButtons(1, model.getPages());
        updateLabel();
    }
}
