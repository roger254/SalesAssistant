/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.main;

import static com.sun.javafx.scene.control.skin.Utils.getResource;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class MainController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void loadItemCheckout(ActionEvent event) {
    }

    @FXML
    private void loadUpdateItem(ActionEvent event) {
    }

    @FXML
    private void loadAddItem(ActionEvent event) {
        loadWindow("/pharmaceuticals/assistant/ui/addItem/AddItem.fxml", "Add New Item");
    }

    @FXML
    private void loadAddUser(ActionEvent event) {
    }

    @FXML
    private void loadViewItems(ActionEvent event) {
    }

    @FXML
    private void loadViewUsers(ActionEvent event) {
    }

    @FXML
    private void loadSettings(ActionEvent event) {
    }

    @FXML
    private void loadLogout(ActionEvent event) {
    }
    
    void loadWindow(String location, String title){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
