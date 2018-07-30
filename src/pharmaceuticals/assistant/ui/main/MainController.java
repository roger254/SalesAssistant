/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package pharmaceuticals.assistant.ui.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pharmaceuticals.assistant.database.DatabaseHandler;
import pharmaceuticals.assistant.ui.listItems.ItemListController.MedicineItem;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class MainController implements Initializable {
    
    @FXML
    private HBox medicineInfo;
    @FXML
    private JFXButton checkOutButton;
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
    @FXML
    private Text medicinePriceText;
    @FXML
    private TableColumn<MedicineItem, String> medicineNameColumn;
    @FXML
    private TableColumn<MedicineItem, Double> medicinePriceColumn;
    @FXML
    private TableColumn<MedicineItem, Integer> medicineQuantityColumn;
    @FXML
    private JFXButton sellButton;
    @FXML
    private TableView<MedicineItem> medicineTable;
    
    DatabaseHandler handler ;
    
    private ObservableList<MedicineItem> checkOutList = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        JFXDepthManager.setDepth(medicineInfo, 1);
        JFXDepthManager.setDepth(checkOutButton, 1);
        handler = DatabaseHandler.getInstance();
        initTable();
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
    
    @FXML
    private void loadItemCheckout(ActionEvent event) {
        String medicineName = medicineNameText.getText();
        Double medicinePrice = Double.parseDouble(medicinePriceText.getText());
        int previousMedicineQuantity = Integer.parseInt(medicineQuantityText.getText());
        String userName = "Roger"; //TODO get from log in
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Check out :" + medicineName + "?");
        
        int quantityToSell = 1;//prompt for this input
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK){
            addToCheckOut(medicineName, quantityToSell, previousMedicineQuantity, medicinePrice, userName);
        }else {
            Alert checkOutStoped = new Alert(Alert.AlertType.INFORMATION);
            checkOutStoped.setTitle("Stopped");
            checkOutStoped.setHeaderText(null);
            checkOutStoped.setContentText("Checkout stopped");
            checkOutStoped.showAndWait();
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
            checkOutButton.setDisable(false);
        }
    }
    
    void addToCheckOut(String medicineName, int quantityToSell, int previousMedicineQuantity,Double medicinePrice, String userName){
        //checking if the item is already on check out list
            String checkItem  = "SELECT * FROM CHECKOUT WHERE medicineName ='" + medicineName +"'";
            ResultSet checkQuery = handler.executeQuery(checkItem);
            try {
                if(checkQuery.next()){
                    int previousQuantity = checkQuery.getInt("medicineQuantity");
                    String updateQuery = "UPDATE CHECKOUT SET medicineQuantity = " +(previousQuantity + quantityToSell) + " WHERE medicineName = '" + medicineName + "'";
                    String updateMedicineQuery  = "UPDATE MEDICINEITEMS SET quantity = " + (previousMedicineQuantity - quantityToSell) + " WHERE name = '" + medicineName + "'";
                    if (handler.execAction(updateQuery) && handler.execAction(updateMedicineQuery)){
                        Alert checkOutSuccess = new Alert(Alert.AlertType.INFORMATION);
                        checkOutSuccess.setTitle("Success");
                        checkOutSuccess.setHeaderText(null);
                        checkOutSuccess.setContentText("Checkout Updated");
                        checkOutSuccess.showAndWait();
                        initTable();
                    }else {
                        Alert checkOutSuccess = new Alert(Alert.AlertType.ERROR);
                        checkOutSuccess.setTitle("Failed");
                        checkOutSuccess.setHeaderText(null);
                        checkOutSuccess.setContentText("Checkout Update Failed");
                        checkOutSuccess.showAndWait();
                    }
                }else {
                    //TODO: check on prepared statements
                    String insertQuery = "INSERT INTO CHECKOUT(medicineName,medicinePrice,medicineQuantity,userName) VALUES ("
                            + "'"+ medicineName +"',"
                            + "" + medicinePrice + ","
                            + "" + quantityToSell + ","
                            + "'" + userName + "'"
                            + ")";
                    //TODO: do this after sell
                    int newQuantity = previousMedicineQuantity - quantityToSell;//prompt for this amount
                    String updateMedicineQuery  = "UPDATE MEDICINEITEMS SET quantity = " + newQuantity + " WHERE name = '" + medicineName + "'";
                    
                    System.out.println(insertQuery + " and " + updateMedicineQuery);
                    
                    if(handler.execAction(insertQuery) && handler.execAction(updateMedicineQuery)){
                        Alert checkOutSuccess = new Alert(Alert.AlertType.INFORMATION);
                        checkOutSuccess.setTitle("Success");
                        checkOutSuccess.setHeaderText(null);
                        checkOutSuccess.setContentText("Checkout Done");
                        checkOutSuccess.showAndWait();
                        initTable();
                    }else {
                        Alert checkOutFail = new Alert(Alert.AlertType.ERROR);
                        checkOutFail.setTitle("Failed");
                        checkOutFail.setHeaderText(null);
                        checkOutFail.setContentText("Checkout Failed");
                        checkOutFail.showAndWait();
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @FXML
    private void handleSell(ActionEvent event) {
       
    }
    
    private void initTable(){
         String infoQuery = "SELECT medicineName, medicinePrice, medicineQuantity FROM CHECKOUT";
        ResultSet result = handler.executeQuery(infoQuery);
        try {
            while(result.next()){
                String name = result.getString("medicineName");
                double price = result.getDouble("medicinePrice");
                int quantity = result.getInt("medicineQuantity");
                checkOutList.add(new MedicineItem(name,quantity,price));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
         nameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
       quantityColumn.setCellValueFactory(new PropertyValueFactory<>("medicineQuantity"
        */
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        medicineQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("medicineQuantity"));
        medicinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("medicinePrice"));
        medicineTable.getItems().setAll(checkOutList);
    }
}
