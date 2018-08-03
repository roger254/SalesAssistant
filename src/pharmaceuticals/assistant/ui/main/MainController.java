package pharmaceuticals.assistant.ui.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pharmaceuticals.assistant.database.DatabaseHandler;
import pharmaceuticals.assistant.ui.listItems.ItemListController.MedicineItem;
import pharmaceuticals.assistant.ui.login.LoginController;
import pharmaceuticals.assistant.ui.main.sellDialog.SellDialogController;
import pharmaceuticals.assistant.ui.main.utils.EditableTable;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class MainController implements Initializable {
    
    /**
     * FXML 
     * VARIABLES
     */
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
    @FXML
    private TableView<MedicineItem> updateTable;
    @FXML
    private TableColumn<MedicineItem, Double> updatePriceCol;
    @FXML
    private TableColumn<MedicineItem, Integer> updateQuantityCol;
    @FXML
    private TableColumn<MedicineItem, Date> updateEntryDateCol;
    @FXML
    private JFXTextField totalAmountField;
    @FXML
    private Menu currentUserMenu;
    @FXML
    private StackPane rootPane;
    
    /**
     * Class Variables
     */
    DatabaseHandler handler ;
    
    private String currentUser = null;
    
    private final ObservableList<MedicineItem> checkOutList = FXCollections.observableArrayList();
    
    EditableTable editableTable;
    
    private boolean isRestricted;
    @FXML
    private Tab updateItemTab;
    @FXML
    private JFXButton cancelSellButton;
    @FXML
    private Button addUserButton;
    @FXML
    private Button viewUserButton;
    
    private static double totalAmount = 0;
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentUser = LoginController.getCurrentUser();
        isRestricted = !LoginController.getCurrentUserAccess().equals("ADMIN");
        // TODO
        if(currentUser == null){
            Alert checkOutSuccess = new Alert(Alert.AlertType.ERROR);
            checkOutSuccess.setTitle("Login Error");
            checkOutSuccess.setHeaderText(null);
            checkOutSuccess.setContentText("Please make sure you're logged in!");
            checkOutSuccess.showAndWait();
            loadWindow("/pharmaceuticals/assistant/ui/login/login.fxml", "LOGIN");
        }
        currentUserMenu.setText(currentUser + " : " + (isRestricted ? "User" : "ADMIN"));
        JFXDepthManager.setDepth(medicineInfo, 1);
        JFXDepthManager.setDepth(checkOutButton, 1);
        handler = DatabaseHandler.getInstance();
        
        //restrict access
        updateItemTab.setDisable(isRestricted);
        addUserButton.setDisable(isRestricted);
        viewUserButton.setDisable(isRestricted);
        
        initCheckOutListTable();
        editableTable = new EditableTable(updateTable, updateEntryDateCol, updatePriceCol, updateQuantityCol, handler);
    }
    
    /**
     * Methods for loading other FXML pages
     */
    
    @FXML
    private void loadAddItem() {
        loadWindow("/pharmaceuticals/assistant/ui/addItem/AddItem.fxml", "Add New Item");
    }
    
    @FXML
    private void loadAddUser() {
        loadWindow("/pharmaceuticals/assistant/ui/addUser/addUser.fxml", "Add New User");
    }
    
    @FXML
    private void loadViewItems() {
        loadWindow("/pharmaceuticals/assistant/ui/listItems/ItemList.fxml", "View Items");
    }
    
    @FXML
    private void loadViewUsers() {
        loadWindow("/pharmaceuticals/assistant/ui/listUsers/listUsers.fxml", "View Users");
    }
    
    @FXML
    private void loadSalesPage() {
        loadWindow("/pharmaceuticals/assistant/ui/sales/sales.fxml", "Sales Page");
    }
    
    /**
     * FXML Button handlers
     */
    
    @FXML
    private void handleLogoutUser() {
        //reset current user
        currentUser = null;
        //close current pane
        ((Stage) rootPane.getScene().getWindow()).close();
        //redirect to login page
        loadWindow("/pharmaceuticals/assistant/ui/login/login.fxml", "LOGIN");
    }
  
    @FXML
    private void loadUpdateItem() {
        editableTable.handleUpdateButton();
    }
    
    @FXML
    private void handleCancelSell() {
        String deleteQuery = "DELETE FROM CHECKOUT";
        String delete = "DELETE FROM USERTABLE";
        if(handler.execAction(deleteQuery) && handler.execAction(delete)){
            System.out.println("Deleted");
        }
        checkOutList.clear();
        initCheckOutListTable();
    }
    
    @FXML
    private void loadLogout() {
        checkOutList.clear();
        medicineTable.refresh();
        System.exit(1);
    }
    
    @FXML
    private void loadMedicineInfo() {
        //get search input
        String medicineSearchName = medicineNameInput.getText();
        
        //check from databas
        String query = "SELECT * FROM MEDICINEITEMS WHERE name = '" + medicineSearchName + "'";
        ResultSet result = handler.executeQuery(query);
        
        Boolean flag = false;
        
        try {
            while(result.next()){
                //collect the medicine details
                String medicineName = result.getString("name");
                Double medicinePrice = result.getDouble("price");
                int medicineQuantity = result.getInt("quantity");
                String medicineDescription = result.getString("description");
                Date medicineEntryDate = result.getDate("entryDate");
                boolean medicineAvailability = result.getBoolean("isAvailable");
                
                //display details
                displayMedicineDetails(medicineName, medicinePrice, medicineQuantity, medicineDescription, medicineEntryDate, medicineAvailability);
                flag = true;
            }
            
            //if not details, send null
            if (!flag)
                displayMedicineDetails(null, null, 0, null, null, false);
            
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void loadItemCheckout() {
        //details to collect for checkout
        String medicineName = medicineNameText.getText();
        Double medicinePrice = Double.parseDouble(medicinePriceText.getText());
        int previousMedicineQuantity = Integer.parseInt(medicineQuantityText.getText());
        String userName = currentUser; //TODO get from log in
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Check out :" + medicineName + "?");
        
        //prompt quantity to sell
        int quantityToSell = quantityPrompt(medicineName, previousMedicineQuantity);
        Optional<ButtonType> response = alert.showAndWait();
        
        //get response
        if (response.get() == ButtonType.OK){
            //add to checkout
            addToCheckOut(medicineName, quantityToSell, previousMedicineQuantity, medicinePrice, userName);
            
            //update total amoun field
            double currentTotalAmount = 0;
            for (MedicineItem item : checkOutList) {
                //get total amount
                currentTotalAmount += (item.getMedicinePrice() * item.getMedicineQuantity());
            }
            
            //show total amount
            totalAmountField.setText(String.valueOf(currentTotalAmount));
            //set total amount
            totalAmount = currentTotalAmount;
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
        
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/pharmaceuticals/assistant/ui/main/sellDialog/sellDialog.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Sell Confirmation");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
            
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //check if checkout list is not empty
        //also confirm payment
        if(checkOutList.size() > 0 && SellDialogController.confirmSell()){
            
            //loop through check out list
            for (MedicineItem medicineItem : checkOutList){
                
                try {
                    
                    //get required details(name and quantity
                    final String itemsName = medicineItem.getMedicineName();
                    final int currentQuantity = medicineItem.getMedicineQuantity();
                    
                    //get items quantity from database
                    int quantity = 0;
                    String query = "SELECT quantity FROM MEDICINEITEMS WHERE name  = '"+ itemsName +"'";
                    ResultSet result = handler.executeQuery(query);
                    
                    //get the quantity
                    if(result.next()){
                        quantity = result.getInt("quantity");
                    }
                    
                    //get new quantity after selling
                    int newQuantity = quantity - currentQuantity;
                    String updateQuery = "";
                    
                    //check if new quantity is less than 0
                    if (newQuantity < 1){//make item not available
                        updateQuery = "UPDATE MEDICINEITEMS SET quantity = " + 0 + ", isAvailable = false WHERE name = '" + itemsName + "'";
                    }else if(newQuantity > 0)//set to new quantity
                        updateQuery = "UPDATE MEDICINEITEMS SET quantity = " + newQuantity + " WHERE name = '" + itemsName + "'";
                    
                    //check if item is in SOLDTABLE
                    String checkQuery = "SELECT * FROM SOLDITEMS WHERE medicineName ='" + itemsName + "'";
                    result = handler.executeQuery(checkQuery);
                    
                    String soldItemQuery = "";
                    if(result.next()){
                        
                        //updateQuantity if it is
                        int databaseQuantity = result.getInt("medicineQuantity");
                        soldItemQuery = "UPDATE SOLDITEMS SET medicineQuantity = "+ (currentQuantity + databaseQuantity)+ " WHERE medicineName ='" + itemsName + "'";
                        
                    }else{
                        
                        //if not add to sell data
                        soldItemQuery = "INSERT INTO SOLDITEMS(userName,medicineName,medicinePrice,medicineQuantity) VALUES("
                                + "'"+ currentUser + "',"
                                + "'"+ medicineItem.getMedicineName()+"',"
                                + ""+ medicineItem.getMedicinePrice()+","
                                + ""+ medicineItem.getMedicineQuantity() +""
                                + ")";
                        
                    }
                    
                    //empty checkOut list
                    String deleteQuery = "DELETE FROM CHECKOUT WHERE medicineName = '" + medicineItem.getMedicineName() + "'";
                    
                    flag = handler.execAction(updateQuery) && handler.execAction(soldItemQuery) && handler.execAction(deleteQuery);
                } catch (SQLException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        //show confirmation of selling
        if (flag){
            Alert checkOutStoped = new Alert(Alert.AlertType.INFORMATION);
            checkOutStoped.setTitle("Sold");
            checkOutStoped.setHeaderText(null);
            checkOutStoped.setContentText("Items sold");
            checkOutStoped.showAndWait();
            
            //reset data
            checkOutList.clear();
            initCheckOutListTable();
            totalAmountField.setText("00 : 00");
        }else{
            Alert checkOutStoped = new Alert(Alert.AlertType.ERROR);
            checkOutStoped.setTitle("Failed");
            checkOutStoped.setHeaderText(null);
            checkOutStoped.setContentText("Selling update failed");
            checkOutStoped.showAndWait();
        }
    }
    
    /**
     * Class helper methods
    */
    
    //create the checkoutlist table
    private void initCheckOutListTable(){
        //clear previous checkout list
        checkOutList.clear();
        //load new checkout list from database
        String infoQuery = "SELECT medicineName, medicinePrice, medicineQuantity FROM CHECKOUT";
        ResultSet result = handler.executeQuery(infoQuery);
        
        try {
            while(result.next()){
                String name = result.getString("medicineName");
                double price = result.getDouble("medicinePrice");
                int quantity = result.getInt("medicineQuantity");
                //load the checkoutlist
                checkOutList.add(new MedicineItem(name,quantity,price));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //initialize check out table
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        medicineQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("medicineQuantity"));
        medicinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("medicinePrice"));
        medicineTable.getItems().setAll(checkOutList);
    }
    
    //load other windows
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
    
    //prompt quantity to be sold
    int quantityPrompt(String medicineName, int medicineQuantity){
        TextInputDialog dialog = new TextInputDialog("Amount");
        dialog.setTitle("Enter Amount");
        dialog.setHeaderText("There are " + medicineQuantity + " " + medicineName + " left");
        dialog.setContentText("Please the amount:");
        
        int quantity = 1;//default quantity to 1
        Optional<String> amountInput = dialog.showAndWait();
        
        if (amountInput.isPresent())
            quantity = Integer.parseInt(amountInput.get());
        
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
    
    //add items to checkout list
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
                    initCheckOutListTable();
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
                        + "'" + currentUser+ "'"
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
                    initCheckOutListTable();
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
    
    public static double getTotalAmout() {
        return totalAmount;
    }
}

