package scheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scheduler.dao.JDBC;

import java.io.IOException;

/**
 * The Main Application class for this app.
 *
 * @author Alvin Roe
 */
public class Main extends Application {
    /**
     * Starts the first scene and opens the stage that it goes on
     * @param stage The window that everything takes place in.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/LoginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 371, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The Main method. The beginning and end of the application.
     * @param args
     */
    public static void main(String[] args){
        JDBC.openConnection();

        launch();

        JDBC.closeConnection();
    }
}