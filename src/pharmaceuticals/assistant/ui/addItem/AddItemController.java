 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.addItem;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pharmaceuticals.assistant.database.DatabaseHandler;

/**
 *
 * @author roger
 */
public class AddItemController implements Initializable {
    
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
    
    @FXML
    private JFXTextField itemId;
    @FXML
    private AnchorPane rootPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       databaseHandler = new DatabaseHandler();
       itemEntryDate.setText(new Date(Calendar.getInstance().getTime().getTime()).toString());
       
       checkData();
    }    

    @FXML
    private void handleSave(ActionEvent event) {
        String itemName = this.itemName.getText();
        String itemPrice = this.itemPrice.getText();
        String itemQuantity = this.itemQuantity.getText();
        String itemDescription = this.itemDescription.getText();
        String  itemEntryDate = this.itemEntryDate.getText();
        String itemId = this.itemId.getText();
        //TODO: automate this process of getting the Id
        //TODO: validate the inputs
        if (itemId.isEmpty() || itemName.isEmpty() || itemPrice.isEmpty() || itemQuantity.isEmpty() ||itemQuantity.isEmpty() || itemDescription.isEmpty() || itemEntryDate.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Make sure all fields are entered");
            alert.showAndWait();
            return;
        }
        /*
         stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                + " id varchar(200) primary key,\n"
                + " name varchar(200),\n"
                + " description varchar(254),\n"
                + " entryDate DATE,\n"
                + " price double precision,\n"
                + " quantity integer,\n"
                + " isAvailable boolean default true"
                + ")");
        */
        String qu = "INSERT INTO MEDICINEITEMS VALUES("
                + "'" + itemId + "',"
                + "'" + itemName + "',"
                + "'" + itemDescription + "',"
                + "'" + itemEntryDate + "',"
                + "" + Double.parseDouble(itemPrice) + ","
                + "" + Integer.parseInt(itemQuantity) + ","
                + "" + "true" + ""
                + ")";
        
        System.out.println(qu);
        if (databaseHandler.execAction(qu))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success");
            alert.showAndWait();
        }else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void checkData() {
       String qu = "SELECT name FROM MEDICINEITEMS";
       ResultSet result = databaseHandler.executeQuery(qu);
        try {
            while(result.next()){
                String itemName = result.getString("name");
                System.out.println(itemName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }

    
}
