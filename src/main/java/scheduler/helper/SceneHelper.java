package scheduler.helper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Helper class for changing scenes
 *
 * @author Alvin Roe
 */
public abstract class SceneHelper {
    /**Enum utilized for tracking what scene is currently being used*/
    public enum View {CUSTOMER, ADD_CUSTOMER, UPDATE_CUSTOMER, SCHEDULE, ADD_APPOINTMENT, UPDATE_APPOINTMENT, LOGIN, REPORT}
    public static View view = View.LOGIN;

    /**
     * Changes to the next scene based on the "View" provided
     * @param nextView the next scene to go to
     * @param nodeInScene a node in the current scene, utilized for getting the stage
     * @param caller the instance that called the method, utilized for finding the root
     * @throws IOException
     */
    public static void changeScene(View nextView, Node nodeInScene, Object caller) throws IOException {
        String viewFileLocation;
        String title;
        double sceneWidth;
        double sceneHeight;

        switch(nextView) {
            case SCHEDULE:
                viewFileLocation = "/view/ScheduleForm.fxml";
                title = "Schedule";
                sceneWidth = 1139;
                sceneHeight = 906;
                view = View.SCHEDULE;
                break;
            case ADD_APPOINTMENT:
                viewFileLocation = "/view/AddAppointmentForm.fxml";
                title = "Add Appointment";
                sceneWidth = 650;
                sceneHeight = 484;
                view = View.ADD_APPOINTMENT;
                break;
            case UPDATE_APPOINTMENT:
                viewFileLocation = "/view/UpdateAppointmentForm.fxml";
                title = "Update Appointment";
                sceneWidth = 650;
                sceneHeight = 484;
                view = View.UPDATE_APPOINTMENT;
                break;
            case CUSTOMER:
                viewFileLocation = "/view/CustomerForm.fxml";
                title = "Customers";
                sceneWidth = 650;
                sceneHeight = 784;
                view = View.CUSTOMER;
                break;
            case ADD_CUSTOMER:
                viewFileLocation = "/view/AddCustomerForm.fxml";
                title = "Add Customer";
                sceneWidth = 550;
                sceneHeight = 450;
                view = View.ADD_CUSTOMER;
                break;
            case UPDATE_CUSTOMER:
                viewFileLocation = "/view/UpdateCustomerForm.fxml";
                title = "Update Customer";
                sceneWidth = 550;
                sceneHeight = 450;
                view = View.UPDATE_CUSTOMER;
                break;
            case REPORT:
                viewFileLocation = "/view/ReportForm.fxml";
                title = "Reports";
                sceneWidth = 931;
                sceneHeight = 836;
                break;
            default:
                viewFileLocation = "/view/LoginForm.fxml";
                title = "Login";
                sceneWidth = 371;
                sceneHeight = 400;
                view = View.LOGIN;
                break;
        }
        Parent root = FXMLLoader.load(caller.getClass().getResource(viewFileLocation));

        Stage stage = (Stage) nodeInScene.getScene().getWindow();


        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }
}
