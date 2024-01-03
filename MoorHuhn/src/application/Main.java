package application;
	

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;	



public class Main extends Application {
	@Override
    public void start(Stage stage) {
		Group root = new Group();

        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        double gameWidth = screenWidth * 0.8;
        double gameHeight = screenHeight * 0.8;

        // Kurzor
        Image cursorImage = null;
        try {
        	URL resource = Game.class.getResource("/images/scope.png");
            String path = resource.getPath();
            cursorImage = new Image(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, gameWidth, gameHeight);
        scene.setCursor(new ImageCursor(cursorImage, cursorImage.getWidth() / 2, cursorImage.getHeight() / 2));
        new Game((int) gameWidth, (int) gameHeight, root, scene);

        stage.setScene(scene);
        stage.show();
		
    }

    public static void main(String[] args) {
        launch();
    }
}
