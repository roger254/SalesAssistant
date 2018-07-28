 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.addItem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pharmaceuticals.assistant.database.DatabaseHandler;

/**
 *
 * @author roger
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    
    DatabaseHandler databaseHandler;
    @FXML
    private JFXTextField itemName;
    @FXML
    private JFXTextField itemPrice;
    @FXML
    private JFXTextField itemQuantity;
    @FXML
    private JFXTextField itemDescription;
    @FXML
    private JFXTextField itemEntryDate;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       databaseHandler = new DatabaseHandler();
    }    

    @FXML
    private void handleSave(ActionEvent event) {
    }

    @FXML
    private void handleCancel(ActionEvent event) {
    }

    
}
