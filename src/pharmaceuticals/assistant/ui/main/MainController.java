/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.main;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import static com.sun.javafx.scene.control.skin.Utils.getResource;
import de.jensd.fx.glyphs.icons525.Icons525View;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pharmaceuticals.assistant.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class MainController implements Initializable {

    @FXML
    private HBox medicineInfo;
    @FXML
    private Icons525View checkOutButton;
    @FXML
    private Text medicineNameText;
    @FXML
    private Text medicineQuantityText;
    @FXML
    private Text medicineDescriptionText;
    @FXML
    private Text medicineEntryDateText;
    @FXML
    private Text medicineAvailabilityText;
    @FXML
    private JFXTextField medicineNameInput;
    
    DatabaseHandler handler ;
    @FXML
    private Text medicinePriceText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        JFXDepthManager.setDepth(medicineInfo, 1);
        JFXDepthManager.setDepth(checkOutButton, 1);
        handler = DatabaseHandler.getInstance();
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
         loadWindow("/pharmaceuticals/assistant/ui/addUser/addUser.fxml", "Add New User");
    }

    @FXML
    private void loadViewItems(ActionEvent event) {
         loadWindow("/pharmaceuticals/assistant/ui/listItems/ItemList.fxml", "View Items");
    }

    @FXML
    private void loadViewUsers(ActionEvent event) {
         loadWindow("/pharmaceuticals/assistant/ui/listUsers/listUsers.fxml", "View Users");
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

    @FXML
    private void loadMedicineInfo(ActionEvent event) {
        String medicineSearchName = medicineNameInput.getText();
        String query = "SELECT * FROM MEDICINEITEMS WHERE name = '" + medicineSearchName + "'";
        ResultSet result = handler.executeQuery(query);
        Boolean flag = false;
        try {
            while(result.next()){
                String medicineName = result.getString("name");
                Double medicinePrice = result.getDouble("price");
                int medicineQuantity = result.getInt("quantity");
                String medicineDescription = result.getString("description");
                Date medicineEntryDate = result.getDate("entryDate");
                boolean medicineAvailability = result.getBoolean("isAvailable");
                showMedicineDetails(medicineName, medicinePrice, medicineQuantity, medicineDescription, medicineEntryDate, medicineAvailability);
                flag = true;
            }
            if (!flag)
                showMedicineDetails(null, null, 0, null, null, false);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void showMedicineDetails(String medicineName, Double medicinePrice, int medicineQuantity, String medicineDescription, Date medicineEntryDate, boolean medicineAvailability){
        
        if((medicineName == null || medicineName.isEmpty() || medicineName.equals(""))){
            medicineNameText.setText("N/A");
            medicinePriceText.setText("N/A");
            medicineQuantityText.setText("N/A");
            medicineDescriptionText.setText("N/A");
            medicineEntryDateText.setText("N/A");
            medicineAvailabilityText.setText("Not available");
        }else{
            medicineNameText.setText(medicineName);
            medicinePriceText.setText(medicinePrice.toString());
            medicineQuantityText.setText(String.valueOf(medicineQuantity));
            medicineDescriptionText.setText(medicineDescription);
            medicineEntryDateText.setText(medicineEntryDate.toString());
            medicineAvailabilityText.setText(medicineAvailability ? "Available" : "Not available");
        }
    }
}
