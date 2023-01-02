package scheduler.helper;
import javafx.scene.control.Alert;

/**
 * Helper method to create and show an Alert
 *
 * @author Alvin Roe
 */
public abstract class AlertHelper {
    /**
     * Creates and shows alert
     * @param alertType The type of alert to create
     * @param title the Title of the window the alert will show
     * @param header the header text for the alert
     * @param content the content text for the alert
     */
    public static void showAlert(Alert.AlertType alertType, String title, String header, String content){
        Alert newAlert = getCommonAlert(alertType, title, header, content);
        newAlert.show();
    }

    /**
     * Creates and returns an Alert
     * @param alertType The type of alert to create
     * @param title the Title of the window the alert will show
     * @param header the header text for the alert
     * @param content the content text for the alert
     * @return Alert based on the parameters
     */
    private static Alert getCommonAlert(Alert.AlertType alertType, String title, String header, String content){
        Alert newAlert = new Alert(alertType);
        newAlert.setTitle(title);
        newAlert.setHeaderText(header);
        newAlert.setContentText(content);
        return newAlert;
    }
}
