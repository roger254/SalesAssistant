
package pharmaceuticals.assistant.ui.addUser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import pharmaceuticals.assistant.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class AddUserController implements Initializable {
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField userPassword;
    @FXML
    private JFXPasswordField confirmPassword;
    @FXML
    private JFXTextField userID;
    @FXML
    private JFXComboBox<String> userAccess;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    
    private DatabaseHandler handler;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userAccess.getItems().add("ADMIN");
        userAccess.getItems().add("Regular User");
        
        handler = DatabaseHandler.getInstance();
    }
    
    @FXML
    private void handleSave(ActionEvent event) {
        String userName = this.userName.getText();
        String userPassword = this.userPassword.getText();
        String confirmPassword = this.confirmPassword.getText();
        String userID = this.userID.getText();
        String userAccess = this.userAccess.getValue();
        
        Boolean flag = userName.isEmpty() || userPassword.isEmpty() || confirmPassword.isEmpty() || userID.isEmpty() || userAccess.isEmpty() ;
        if (flag){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Make sure all fields are entered");
            alert.showAndWait();
            return;
        }
        if (!userPassword.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("The password fields do not match");
            alert.showAndWait();
            return;
        }
        
        String stmt = "INSERT INTO USERTABLE VALUES("
                + "'" + userID + "',"
                + "'" + userName + "',"
                + "'" + DigestUtils.sha1Hex(userPassword) + "',"
                + "'" + userAccess + "'"
                + ")";
        
        System.out.println(stmt);
        if(handler.execAction(stmt)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("User Saved");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed to save user");
            alert.showAndWait();
        }
        closeStage();
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        closeStage();
    }
    
    private void closeStage(){
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
}
