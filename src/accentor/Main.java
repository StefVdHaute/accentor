package accentor;
  
import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.Group; 
import javafx.stage.Stage; 

public class Main extends Application {  

    @Override
    public void start(final Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 540, 210);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
