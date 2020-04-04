package accentor;
  
import accentor.api.DataAccessContext;
import accentor.api.DataAccessException;
import accentor.api.HttpDataAccessProvider;
import accentor.browser.BrowseModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Group; 
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import accentor.browser.BrowseCompanion;
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
            //TODO: catch
            e.printStackTrace();
        }
    }

    @Override
    public void start(final Stage primaryStage) throws IOException{
        fxmlLoader = new FXMLLoader(getClass().getResource("browser/browse.fxml"));
        fxmlLoader.setController(new BrowseCompanion(new BrowseModel(dac)));
        Group root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
