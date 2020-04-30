package accentor.browser.subBrowsers.artists;

import accentor.api.ArtistFinder;
import accentor.browser.BrowseCompanion;
import accentor.browser.subBrowsers.TableCompanion;
import accentor.domain.Artist;
import accentor.specialistFxElements.cells.PictureCell;
import accentor.specialistFxElements.columns.PictureColumn;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;

public class ArtistsCompanion extends TableCompanion<ArtistsModel, Artist, ArtistFinder.SortOption> {
    private final TableColumn<Artist, String> picture = new PictureColumn<>();
    private final TableColumn<Artist, String> name  = new TableColumn<>();

    public ArtistsCompanion(BrowseCompanion superCompanion, ArtistsModel model) {
        super(superCompanion, model);

        sortMap.put(name.getText(), ArtistFinder.SortOption.BY_NAME);
    }

    @FXML
    public void initialize(){
        super.initialize();

        table.getStyleClass().add("picture-table");

        table.getColumns().add(picture);
        table.getColumns().add(name);

        picture.setMinWidth(110);
        picture.setMaxWidth(120);
        picture.setResizable(false);
        picture.setSortable(false);

        picture.setCellValueFactory( new PropertyValueFactory<>("smallImageURL"));
        name.setCellValueFactory( new PropertyValueFactory<>("name"));

        Image placeholder = new Image("accentor/images/artist_placeholder_small.png");
        picture.setCellFactory(column -> new PictureCell<>(placeholder));
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
            row.setOnMouseClicked(event -> {
                Platform.runLater(() -> openTab(row.getItem()));
                event.consume();
            });
            row.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    Platform.runLater(() -> openTab(row.getItem()));
                    keyEvent.consume();
                }
            });
            return row;
        });

        renewData();
    }

    private void openTab(Artist artist) {
        if (artist != null) {
            superCompanion.openTab(artist);
        }
    }
}
