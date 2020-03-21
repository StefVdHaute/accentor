package accentor;
  
import accentor.api.DataAccessContext;
import accentor.api.DataAccessException;
import accentor.api.HttpDataAccessProvider;
import javafx.application.Application;
import javafx.scene.Scene; 
import javafx.scene.Group; 
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main extends Application {
    private Properties accProperties;
    private HttpDataAccessProvider DAP;
    private DataAccessContext DAC;

    @Override
    public void init() {
        try (InputStream input = new FileInputStream("resources/accentor/accentor.properties")) {
            accProperties = new Properties(0);
            accProperties.load(input);

            DAP = new HttpDataAccessProvider(accProperties);
            DAC = DAP.getDataAccessContext();
        } catch (IOException | DataAccessException e) {
            //TODO: catch
            e.printStackTrace();
        }
    }

    @Override
    public void start(final Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 540, 210);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
