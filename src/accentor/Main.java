package accentor;

import accentor.api.DataAccessContext;
import accentor.api.DataAccessException;
import accentor.api.HttpDataAccessProvider;
import accentor.browser.BrowseCompanion;
import accentor.browser.BrowseModel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main extends Application {
    private Properties accProperties;
    private HttpDataAccessProvider dap;
    private DataAccessContext dac;
    private FXMLLoader fxmlLoader;

    @Override
    public void init() {
        try (InputStream input = new FileInputStream("resources/accentor/accentor.properties")) {
            accProperties = new Properties(0);
            accProperties.load(input);

            dap = new HttpDataAccessProvider(accProperties);
            dac = dap.getDataAccessContext();
        } catch (IOException | DataAccessException e) {
            //TODO: catch properly
            e.printStackTrace();
        }
    }

    @Override
    public void start(final Stage primaryStage) throws IOException{
        fxmlLoader = new FXMLLoader(getClass().getResource("browser/browse.fxml"));
        fxmlLoader.setController(new BrowseCompanion(new BrowseModel(dac)));
        BorderPane root = fxmlLoader.load();
        Scene scene = new Scene(root);

        //.ico support would be nice :'(
        //This also doesn't seem to work properly with Docky on linux mint
        primaryStage.getIcons().add(new Image("accentor/images/rippoffyX256.png"));
        primaryStage.getIcons().add(new Image("accentor/images/rippoffyX128.png"));
        primaryStage.getIcons().add(new Image("accentor/images/rippoffyX64.png"));
        primaryStage.setTitle("Rippoffy, the best, worst music player.");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){//TODO: init with arg instead of properties.
        launch(args);
    }
}
