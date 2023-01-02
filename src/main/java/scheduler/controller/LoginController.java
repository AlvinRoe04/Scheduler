package scheduler.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import scheduler.dao.UserDAO;
import scheduler.helper.SceneHelper;
import scheduler.helper.SessionData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller for the login view
 *
 * @author Alvin Roe
 */

public class LoginController implements Initializable {
    /**TextField for the Username*/
    @FXML
    private TextField userName;
    /**Text reference to display the location (zone)*/
    @FXML
    private Text locationLabel;
    /**Title for the view*/
    @FXML
    private Text loginTitle;
    /**Text next to the Username text field, usually says Username, but will change based on language*/
    @FXML
    private Text userText;
    /**Text next to the password text field, usually says Password, but will change based on language settings*/
    @FXML
    private Text passwordText;
    /**Reference to the Login button*/
    @FXML
    private Button loginButton;
    /**Passwordfield for the Password*/
    @FXML
    private PasswordField password;
    /**ResourceBundle used for translations*/
    private ResourceBundle translator;
    /**
     * Method triggered when the "Login" button is pressed. Will go to next scene if Username and Password match.
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    private void onLoginPressed() throws SQLException, IOException {
        String path = "login_activity.txt";
        BufferedWriter logger = new BufferedWriter(new FileWriter(path, true));
        logger.append("Username: " + userName.getText() + " attempted login at: " + ZonedDateTime.now(ZoneOffset.UTC) + " UTC. Result: ");

        if (UserDAO.authenticate(userName.getText(), password.getText())) {
            logger.append("Login Successful");
            SceneHelper.changeScene(SceneHelper.View.SCHEDULE, userName, this);
        }
        else{
            logger.append("Login Failed");
            Alert loginInvalidAlert = new Alert(Alert.AlertType.ERROR);
            loginInvalidAlert.setTitle(translator.getString("Login_Error"));
            loginInvalidAlert.setContentText(translator.getString("User_Not_Found"));
            loginInvalidAlert.showAndWait();
        }
        logger.flush();
        logger.close();
    }
    /**
     * Changes text based on language setting and shows Zone ID.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        translator = ResourceBundle.getBundle("/nat", Locale.getDefault());

           if(Locale.getDefault().getLanguage().equals("fr")) {
               loginButton.setText(translator.getString(loginButton.getText()));
               loginTitle.setText(translator.getString(loginTitle.getText()));
               userText.setText(translator.getString(userText.getText()));
               passwordText.setText(translator.getString(passwordText.getText()));
           }

           locationLabel.setText("Location: " + ZoneId.systemDefault());

    }
}
