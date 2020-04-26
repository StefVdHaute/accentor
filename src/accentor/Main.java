package accentor;

import accentor.api.DataAccessContext;
import accentor.api.DataAccessException;
import accentor.api.HttpDataAccessProvider;
import accentor.browser.BrowseCompanion;
import accentor.browser.BrowseModel;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Main extends Application {
    private Properties accProperties;
    private HttpDataAccessProvider dap;
    private DataAccessContext dac;

    @Override
    public void init() {
        List<String> argList = getParameters().getRaw();

        String propertiesPath = "resources/accentor/accentor.properties";

        if (!argList.isEmpty()){
            propertiesPath = argList.get(0);
        }

        try (InputStream input = new FileInputStream(propertiesPath)) {
            accProperties = new Properties(0);
            accProperties.load(input);

            dap = new HttpDataAccessProvider(accProperties);
            dac = dap.getDataAccessContext();
        } catch (IOException | DataAccessException e) {
            //Can really only be used for the console or trying to connect again
            e.printStackTrace();
        }
    }

    @Override
    public void start(final Stage primaryStage) throws IOException{
        if (dac == null) {
            error("Something went wrong", "Something went wrong\nwhile trying to acces the server.");
            System.exit(1);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("browser/browse.fxml"));
        fxmlLoader.setController(new BrowseCompanion(new BrowseModel(dac)));
        BorderPane root = fxmlLoader.load();
        Scene scene = new Scene(root);

        //.ico support would be nice :'(
        //This also doesn't seem to work properly with Docky on linux mint, Docky's fault?
        primaryStage.getIcons().add(new Image("accentor/images/rippoffyX256.png"));
        primaryStage.getIcons().add(new Image("accentor/images/rippoffyX128.png"));
        primaryStage.getIcons().add(new Image("accentor/images/rippoffyX64.png"));
        primaryStage.setTitle("Ripoffy, the best, worst music player.");

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
    }

    public static void main(String[] args){
        launch(args);
    }

    public void error(String header, String context) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(header);
        errorAlert.setContentText(context);

        Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("accentor/images/rippoffyX64.png"));

        errorAlert.showAndWait();
    }
}
