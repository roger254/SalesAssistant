package pharmaceuticals.assistant.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;
import pharmaceuticals.assistant.database.DatabaseHandler;
import pharmaceuticals.assistant.ui.main.MainController;
import pharmaceuticals.assistant.util.SalesAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class LoginController implements Initializable
{

    private static String currentUser = "";
    private static String currentUserAccess = "";
    @FXML
    private JFXTextField loginUserName;
    @FXML
    private JFXPasswordField loginPassword;
    @FXML
    private Label titleLabel;
    private DatabaseHandler handler;

    public static String getCurrentUser()
    {
        return currentUser;
    }

    public static String getCurrentUserAccess()
    {
        return currentUserAccess;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        handler = DatabaseHandler.getInstance();
        checkForUsers();
    }

    @FXML
    private void handleLogin()
    {
        //reset style
        titleLabel.setText("Sales Assistant Login");
        titleLabel.setStyle("-fx-background-color:black;"
                + "-fx-text-fill:white");
        String userName = loginUserName.getText();
        String password = loginPassword.getText();

        //validate user
        if (validateUser(userName, password))
        {
            currentUser = userName;
            closeStage();
            loadMain();

        } else
        {
            //set style to show invalid
            titleLabel.setText("Invalid Details");
            titleLabel.setStyle("-fx-background-color:#D32F2f;"
                    + "-fx-text-fill:white");
        }
    }

    @FXML
    private void handleLogout()
    {
        System.exit(1);
    }

    private boolean validateUser(String userName, String password)
    {
        boolean flag = false;
        try
        {


            String query = "SELECT * FROM USER_TABLE WHERE name = '" + userName + "'";
            ResultSet result = handler.executeQuery(query);

            while (result != null && result.next())
            {
                String databasePassword = result.getString("password");
                currentUserAccess = result.getString("userAccess");
                flag = ((DigestUtils.sha1Hex(password).equals(databasePassword)));
            }

        } catch (SQLException ex)
        {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    private void closeStage()
    {
        ((Stage) loginUserName.getScene().getWindow()).close();
    }

    private void loadMain()
    {
        try
        {
            Parent parent = FXMLLoader.load(getClass().getResource("/pharmaceuticals/assistant/ui/main/Main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(parent));
            stage.show();
            //set the icon
            SalesAssistantUtil.setStageIcon(stage);
        } catch (IOException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void checkForUsers()
    {
        try
        {
            ResultSet result;

            String checkForUsers = "SELECT * FROM USER_TABLE";
            result = handler.executeQuery(checkForUsers);
            if (!(result != null && result.next()))
            {
                try
                {
                    Parent parent = FXMLLoader.load(getClass().getResource("/pharmaceuticals/assistant/ui/addUser/addUser.fxml"));
                    Stage stage = new Stage(StageStyle.DECORATED);
                    stage.setTitle("Dashboard");
                    stage.setScene(new Scene(parent));
                    stage.show();
                } catch (IOException ex)
                {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}