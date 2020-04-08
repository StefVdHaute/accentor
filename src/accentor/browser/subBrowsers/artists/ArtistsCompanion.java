package accentor.browser.subBrowsers.artists;

import accentor.api.ArtistFinder;
import accentor.browser.BrowseCompanion;
import accentor.browser.subBrowsers.TableCompanion;
import accentor.browser.subBrowsers.cells.PictureCell;
import accentor.domain.Artist;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

public class ArtistsCompanion extends TableCompanion<ArtistsModel, Artist, ArtistFinder.SortOption> {
    private TableColumn<Artist, String> picture = new TableColumn<>();
    private TableColumn<Artist, String> name  = new TableColumn<>();

    private BrowseCompanion superCompanion;

    public ArtistsCompanion(BrowseCompanion superCompanion, ArtistsModel model) {
        super(model);
        this.superCompanion = superCompanion;

        sortMap.put(name.getText(), ArtistFinder.SortOption.BY_NAME);
    }

    @FXML
    public void initialize(){
        super.initialize();

        table.getColumns().add(picture);
        table.getColumns().add(name);

        picture.setMinWidth(110);
        picture.setMaxWidth(120);
        picture.setResizable(false);
        picture.setSortable(false);

        picture.setCellValueFactory( new PropertyValueFactory<>("smallImageURL"));
        name.setCellValueFactory( new PropertyValueFactory<>("name"));

        picture.setCellFactory(column -> new PictureCell<>());
        name.setCellFactory(column -> {
            TableCell<Artist, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String name, boolean empty) {
                    super.updateItem(name, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(name);
                    }
                }
            };

            cell.setAlignment(Pos.CENTER_LEFT);
            cell.setFont(Font.font(20));
            return cell;
        });

        table.setRowFactory(column -> {
            TableRow<Artist> row = new TableRow<>();
            row.setOnMouseClicked(event -> openTab(row.getItem()));
            return row;
        });

        table.getItems().addAll(model.getData());

        updateButtons(1, model.getPages());
        updateLabel();
    }

    private void openTab(Artist artist) {
        if (artist != null) {
            superCompanion.openTab(artist);
        }
    }
}
