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
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    
    @FXML
    private JFXTextField itemId;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       databaseHandler = new DatabaseHandler();
       itemEntryDate.setText(new Date(Calendar.getInstance().getTime().getTime()).toString());
    }    

    @FXML
    private void handleSave(ActionEvent event) {
        String itemName = this.itemName.getText();
        String itemPrice = this.itemPrice.getText();
        String itemQuantity = this.itemQuantity.getText();
        String itemDescription = this.itemDescription.getText();
        String  itemEntryDate = this.itemEntryDate.getText();
        String itemId = this.itemId.getText();
        //TODO: automate this process
        
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
        System.exit(1);
    }

    
}
