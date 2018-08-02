package pharmaceuticals.assistant.ui.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pharmaceuticals.assistant.database.DatabaseHandler;
import pharmaceuticals.assistant.ui.listItems.ItemListController.MedicineItem;
import pharmaceuticals.assistant.ui.main.utils.EditableTable;

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
    private TableView<MedicineItem> medicineTable;
    
    DatabaseHandler handler ;
    
    private String currentUser = "Roger";
    
    private final ObservableList<MedicineItem> checkOutList = FXCollections.observableArrayList();
    @FXML
    private TableView<MedicineItem> updateTable;
    @FXML
    private TableColumn<MedicineItem, Double> updatePriceCol;
    @FXML
    private TableColumn<MedicineItem, Integer> updateQuantityCol;
    @FXML
    private TableColumn<MedicineItem, Date> updateEntryDateCol;
    
    EditableTable editableTable;
   
    @FXML
    private JFXTextField totalAmountField;
    @FXML
    private TableColumn<?, ?> updateNameCol;
    @FXML
    private TableColumn<?, ?> updateDescriptionCol;
    @FXML
    private JFXButton cancelSellButton;
    @FXML
    private JFXButton sellButton1;
    
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
        editableTable = new EditableTable(updateTable, updateEntryDateCol, updatePriceCol, updateQuantityCol, handler);
    }
    
    @FXML
    private void loadUpdateItem(ActionEvent event) {
        editableTable.handleUpdateButton();
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
        checkOutList.clear();
        medicineTable.refresh();
        System.exit(1);
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
                displayMedicineDetails(medicineName, medicinePrice, medicineQuantity, medicineDescription, medicineEntryDate, medicineAvailability);
                flag = true;
            }
            if (!flag)
                displayMedicineDetails(null, null, 0, null, null, false);
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void loadItemCheckout(ActionEvent event) {
        //details to collect for checkout
        String medicineName = medicineNameText.getText();
        Double medicinePrice = Double.parseDouble(medicinePriceText.getText());
        int previousMedicineQuantity = Integer.parseInt(medicineQuantityText.getText());
        String userName = currentUser; //TODO get from log in
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Check out :" + medicineName + "?");
        
        int quantityToSell = quantityPrompt(medicineName, previousMedicineQuantity);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK){
            addToCheckOut(medicineName, quantityToSell, previousMedicineQuantity, medicinePrice, userName);
            double totalAmount = 0;
            for(MedicineItem item : checkOutList){
                totalAmount += (item.getMedicinePrice() * item.getMedicineQuantity());
            }
            totalAmountField.setText(String.valueOf(totalAmount));
        }else {
            Alert checkOutStoped = new Alert(Alert.AlertType.INFORMATION);
            checkOutStoped.setTitle("Stopped");
            checkOutStoped.setHeaderText(null);
            checkOutStoped.setContentText("Checkout stopped");
            checkOutStoped.showAndWait();
        }
        
    }
    
    @FXML
    private void handleSell() {
        boolean flag = false;
        if(checkOutList.size() > 0 ){
            for (MedicineItem medicineItem : checkOutList){
                String query = "SELECT quantity FROM MEDICINEITEMS WHERE name  = '"+ medicineItem.getMedicineName() +"'";
                ResultSet result = handler.executeQuery(query);
                int quantity = 0;
                try {
                    while(result.next()){
                        quantity = result.getInt("quantity");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
                int newQuantity = quantity - medicineItem.getMedicineQuantity();
                String updateQuery = "";
                if (newQuantity < 1){
                     updateQuery = "UPDATE MEDICINEITEMS SET quantity = " + 0 + ", isAvailable = false WHERE name = '" + medicineItem.getMedicineName() + "'";
                }else if(newQuantity > 0)
                     updateQuery = "UPDATE MEDICINEITEMS SET quantity = " + newQuantity + " WHERE name = '" + medicineItem.getMedicineName() + "'";
                String insertToSold = "INSERT INTO SOLDITEMS(userName,medicineName,medicinePrice,medicineQuantity) VALUES("
                        + "'"+ currentUser + "',"
                        + "'"+ medicineItem.getMedicineName()+"',"
                        + ""+ medicineItem.getMedicinePrice()+","
                        + ""+ medicineItem.getMedicineQuantity() +""
                        + ")";
                String deleteQuery = "DELETE FROM CHECKOUT WHERE medicineName = '" + medicineItem.getMedicineName() + "'";
                flag = handler.execAction(updateQuery) && handler.execAction(insertToSold) && handler.execAction(deleteQuery);
            }
        }
        if (flag){
            System.out.println("Success in selling");
            checkOutList.clear();
            initTable();
            totalAmountField.setText("00 : 00");
        }
    }
    
    private void initTable(){
        checkOutList.clear();
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
        
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        medicineQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("medicineQuantity"));
        medicinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("medicinePrice"));
        medicineTable.getItems().setAll(checkOutList);
    }
    
    //prompt quantity to be sold
    int quantityPrompt(String medicineName, int medicineQuantity){
        TextInputDialog dialog = new TextInputDialog("Amount");
        dialog.setTitle("Enter Amount");
        dialog.setHeaderText("There are " + medicineQuantity + " " + medicineName + " left");
        dialog.setContentText("Please the amount:");
        int quantity = 1;
        Optional<String> amountInput = dialog.showAndWait();
        if (amountInput.isPresent()) {
            quantity = Integer.parseInt(amountInput.get());
        }
        return quantity;
    }
    
    //display medicine details after search
    void displayMedicineDetails(String medicineName, Double medicinePrice, int medicineQuantity, String medicineDescription, Date medicineEntryDate, boolean medicineAvailability){
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
            boolean flag = medicineQuantity > 0;
            medicineAvailabilityText.setText(flag ? "Available" : "Not available");
            checkOutButton.setDisable(!flag);
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

    private void addToCheckOutList(MedicineItem medicineItem) {
        boolean flag = true;
        for(MedicineItem item : checkOutList){
            if(medicineItem.getMedicineName().equals(item.getMedicineName())){
                int previousQuantity = item.getMedicineQuantity();
                int newQuantity = previousQuantity + medicineItem.getMedicineQuantity();
                item.setMedicineQuantity(newQuantity);
                System.out.println("Updated in checkOut");
                flag = false;
            }
        }
        if (flag)
            checkOutList.add(medicineItem);
    }

    @FXML
    private void handleCancelSell(ActionEvent event) {
        String deleteQuery = "DELETE FROM CHECKOUT";
        if(handler.execAction(deleteQuery)){
            System.out.println("Deleted");
        }
        checkOutList.clear();
        initTable();
    }
    
}
