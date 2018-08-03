 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
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

/**
 * FXML Controller class
 *
 * @author roger
 */ 
public class LoginController implements Initializable {

    @FXML
    private JFXTextField loginUserName;
    @FXML
    private JFXPasswordField loginPassword;
    @FXML
    private Label titleLabel;
    
    private static String currentUser =  "";
    
    private static String currentUserAccess = "";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleLogin(ActionEvent event) {
        //reset style
        titleLabel.setText("Sales Assistant Login");
        titleLabel.setStyle("-fx-background-color:black;"
                + "-fx-text-fill:white");
        String userName = loginUserName.getText();
        String password = loginPassword.getText();
        
        //validate user
        if(validateUser(userName,password)){
            currentUser = userName;
            closeStage();
            loadMain();
            
        }else {
            //set style to show invalid
            titleLabel.setText("Invalid Details");
            titleLabel.setStyle("-fx-background-color:#D32F2f;"
                    + "-fx-text-fill:white");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        System.exit(1);
    }

    private boolean validateUser(String userName, String password) {
       
        DatabaseHandler handler = DatabaseHandler.getInstance();
        String query = "SELECT * FROM USERTABLE WHERE name = '" + userName+"'";
        ResultSet result = handler.executeQuery(query);
        
        boolean flag = false;
        
        try {
            while(result.next()){
                String databasePassword = result.getString("password");
                currentUserAccess = result.getString("userAccess");
                flag = ((DigestUtils.sha1Hex(password).equals(databasePassword)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    private void closeStage() {
       ((Stage) loginUserName.getScene().getWindow()).close();
    }
    
    void loadMain (){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/pharmaceuticals/assistant/ui/main/Main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(parent));
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentUserAccess() {
        return currentUserAccess;
    }  
}