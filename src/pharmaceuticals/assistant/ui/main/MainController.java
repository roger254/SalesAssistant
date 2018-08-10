package pharmaceuticals.assistant.ui.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pharmaceuticals.assistant.alert.AlertMaker;
import pharmaceuticals.assistant.database.DatabaseHandler;
import pharmaceuticals.assistant.database.MedicineHandler;
import pharmaceuticals.assistant.database.MedicineItem;
import pharmaceuticals.assistant.ui.login.LoginController;
import pharmaceuticals.assistant.ui.main.sellDialog.SellDialogController;
import pharmaceuticals.assistant.ui.main.utils.EditableTable;
import pharmaceuticals.assistant.util.SalesAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class MainController implements Initializable
{

    private static double totalAmount = 0;
    private final ObservableList<MedicineItem> checkOutList = FXCollections.observableArrayList();
    /**
     * *@FXMl-VARIABLES
     */
    public TableView<MedicineItem> miniCheckOutTable;
    public JFXButton toCheckOutViewButton;
    public JFXButton removeAll;
    public JFXButton pushAllToCheckOut;
    public JFXButton cancelSellButton;
    public TableColumn<MedicineItem, String> itemNameCol;
    public TableColumn<MedicineItem, Integer> itemQuantityCol;
    public TableColumn<MedicineItem, Double> itemPriceCol;
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
    private DatabaseHandler handler;
    private String currentUser = null;
    private EditableTable editableTable;
    @FXML
    private Tab updateItemTab;
    @FXML
    private Button addUserButton;
    @FXML
    private Button viewUserButton;
    private boolean isFound = false;

    private MedicineHandler medicineHandler;

    public static double getTotalAmount()
    {
        return totalAmount;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        currentUser = LoginController.getCurrentUser();
        boolean isRestricted = !LoginController.getCurrentUserAccess().equals("ADMIN");
        // TODO
        if (currentUser == null)
        {
            Alert checkOutSuccess = new Alert(Alert.AlertType.ERROR);
            checkOutSuccess.setTitle("Login Error");
            checkOutSuccess.setHeaderText(null);
            checkOutSuccess.setContentText("Please make sure you're logged in!");
            checkOutSuccess.showAndWait();
            loadWindow("/pharmaceuticals/assistant/ui/login/login.fxml", "LOGIN");
        }
        //show user on menu bar
        currentUserMenu.setText(currentUser + " : " + (isRestricted ? "User" : "ADMIN"));
        JFXDepthManager.setDepth(medicineInfo, 1);
        //   JFXDepthManager.setDepth(checkOutButton, 1);
        handler = DatabaseHandler.getInstance();
        medicineHandler = MedicineHandler.getInstance();

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
    private void loadAddItem()
    {
        loadWindow("/pharmaceuticals/assistant/ui/addItem/AddItem.fxml", "Add New Item");
    }

    @FXML
    private void loadAddUser()
    {
        loadWindow("/pharmaceuticals/assistant/ui/addUser/addUser.fxml", "Add New User");
    }

    @FXML
    private void loadViewItems()
    {
        loadWindow("/pharmaceuticals/assistant/ui/listItems/ItemList.fxml", "View Items");
    }

    @FXML
    private void loadViewUsers()
    {
        loadWindow("/pharmaceuticals/assistant/ui/listUsers/listUsers.fxml", "View Users");
    }

    @FXML
    private void loadSalesPage()
    {
        loadWindow("/pharmaceuticals/assistant/ui/sales/sales.fxml", "Sales Page");
    }

    /**
     * FXML Button handlers
     */

    @FXML
    private void addToCheckOut()
    {
        if (isFound)
        {
            MedicineItem item = loadMedicineInfo();
            int quantityToSell = quantityPrompt(item != null ? item.getMedicineName() : null, Objects.requireNonNull(item).getMedicineQuantity());
            MedicineHandler.addItemToCheckOut(item,quantityToSell);
        }
        setMiniCheckOutTable();
    }

    private void setMiniCheckOutTable()
    {
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        itemQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityToSell"));
        itemPriceCol.setCellValueFactory(new PropertyValueFactory<>("medicinePrice"));
        miniCheckOutTable.getItems().setAll(MedicineHandler.getCheckOutList());
    }

    @FXML
    private void handleLogoutUser()
    {
        //reset current user
        currentUser = null;
        //close current pane
        ((Stage) rootPane.getScene().getWindow()).close();
        //redirect to login page
        loadWindow("/pharmaceuticals/assistant/ui/login/login.fxml", "LOGIN");
    }

    @FXML
    private void loadUpdateItem()
    {
        editableTable.handleUpdateButton();
    }

    @FXML
    private void handleCancelSell()
    {
        String deleteQuery = "DELETE FROM CHECKOUT";
        if (handler.execAction(deleteQuery))
        {
            System.out.println("Checkout Cleared");
        }
        checkOutList.clear();
        initCheckOutListTable();
    }

    @FXML
    private void loadLogout()
    {
        checkOutList.clear();
        medicineTable.refresh();
        System.exit(1);
    }

    @FXML
    private MedicineItem loadMedicineInfo()
    {
        //get search input
        String medicineSearchName = medicineNameInput.getText();

        isFound = false;

        for (MedicineItem item : MedicineHandler.getMedicineItems())
        {
            if (item.getMedicineName().equals(medicineSearchName))
            {
                //display details
                displayMedicineDetails(item);
                isFound = true;
                return item;
            }
        }

        //if not details, send null
        if (!isFound)
            displayMedicineDetails(null);

        return null;
    }
//display medicine details after search

    private void displayMedicineDetails(MedicineItem item)
    {
        if (item == null)
        {
            medicineNameText.setText("N/A");
            medicinePriceText.setText("N/A");
            medicineQuantityText.setText("N/A");
            medicineDescriptionText.setText("N/A");
            medicineEntryDateText.setText("N/A");
            medicineAvailabilityText.setText("Not available");
        } else
        {
            medicineNameText.setText(item.getMedicineName());
            medicinePriceText.setText(String.valueOf(item.getMedicinePrice()));
            medicineQuantityText.setText(String.valueOf(item.getMedicineQuantity()));
            medicineDescriptionText.setText(item.getMedicineDescription());
            medicineEntryDateText.setText(item.getMedicineEntryDate().toString());
            boolean flag = item.getMedicineQuantity() > 0;
            medicineAvailabilityText.setText(flag ? "Available" : "Not available");
        }
    }

    @FXML
    private void loadItemCheckout()
    {
        //details to collect for checkout
        String medicineName = medicineNameText.getText();
        Double medicinePrice = Double.parseDouble(medicinePriceText.getText());
        int previousMedicineQuantity = Integer.parseInt(medicineQuantityText.getText());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Check out :" + medicineName + "?");

        //prompt quantity to sell
        int quantityToSell = quantityPrompt(medicineName, previousMedicineQuantity);
        Optional<ButtonType> response = alert.showAndWait();

        //get response
        if (response.isPresent() && response.get() == ButtonType.OK)
        {
            //add to checkout
            addToCheckOut(medicineName, quantityToSell, previousMedicineQuantity, medicinePrice);

            //update total amount field
            double currentTotalAmount = 0;
            for (MedicineItem item : checkOutList)
            {
                //get total amount
                currentTotalAmount += (item.getMedicinePrice() * item.getMedicineQuantity());
            }

            //show total amount
            totalAmountField.setText(String.valueOf(currentTotalAmount));
            //set total amount
            totalAmount = currentTotalAmount;
        } else
        {
            Alert checkOutStopped = new Alert(Alert.AlertType.INFORMATION);
            checkOutStopped.setTitle("Stopped");
            checkOutStopped.setHeaderText(null);
            checkOutStopped.setContentText("Checkout stopped");
            checkOutStopped.showAndWait();
        }
    }

    @FXML
    private void handleSell()
    {
    }

    /**
     * Class helper methods
     */

    //create the checkOutList table
    private void initCheckOutListTable()
    {
        //clear previous checkout list
        checkOutList.clear();
        //load new checkout list from database
        String infoQuery = "SELECT medicineName, medicinePrice, medicineQuantity FROM CHECKOUT_TABLE";
        ResultSet result = handler.executeQuery(infoQuery);

        try
        {
            if (result != null)
            {
                while (result.next())
                {
                    String name = result.getString("medicineName");
                    double price = result.getDouble("medicinePrice");
                    int quantity = result.getInt("medicineQuantity");
                    //load the checkUutList
                    checkOutList.add(new MedicineItem(name, quantity, price));
                }
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //initialize check out table
        medicineNameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        medicineQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("medicineQuantity"));
        medicinePriceColumn.setCellValueFactory(new PropertyValueFactory<>("medicinePrice"));
        medicineTable.getItems().setAll(checkOutList);
    }

    //load other windows
    private void loadWindow(String location, String title)
    {
        try
        {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            //set the icon
            SalesAssistantUtil.setStageIcon(stage);
        } catch (IOException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //prompt quantity to be sold
    private int quantityPrompt(String medicineName, int medicineQuantity)
    {
        TextInputDialog dialog = new TextInputDialog("Amount");
        dialog.setTitle("Enter Amount");
        dialog.setHeaderText("There are " + medicineQuantity + " " + medicineName + " left");
        dialog.setContentText("Please the amount:");

        int quantity = 1;//default quantity to 1
        Optional<String> amountInput = dialog.showAndWait();

        if (amountInput.isPresent())
            quantity = Integer.parseInt(amountInput.get());
        //TODO: CHECK WHY QUANTITY SOLD IS DOUBLING AFTER SELLING
        return quantity;
    }

    //add items to checkout list
    private void addToCheckOut(String medicineName, int quantityToSell, int previousMedicineQuantity, Double medicinePrice)
    {
        //checking if the item is already on check out list
        String checkItem = "SELECT * FROM CHECKOUT_TABLE WHERE medicineName ='" + medicineName + "'";
        ResultSet checkQuery = handler.executeQuery(checkItem);
        try
        {
            if (checkQuery != null && checkQuery.next())
            {
                int previousQuantity = checkQuery.getInt("medicineQuantity");

                String updateQuery = "UPDATE CHECKOUT_TABLE SET medicineQuantity = " + (previousQuantity + quantityToSell) + " WHERE medicineName = '" + medicineName + "'";
                String updateMedicineQuery = "UPDATE MEDICINE_ITEMS_TABLE SET quantity = " + (previousMedicineQuantity - quantityToSell) + " WHERE name = '" + medicineName + "'";

                if (handler.execAction(updateQuery) && handler.execAction(updateMedicineQuery))
                {
                    AlertMaker.showInformationMessage("Success", "Checkout Updated");
                    initCheckOutListTable();
                } else
                    AlertMaker.showErrorMessage("Failed", "Checkout Update Failed");
            } else
            {
                //TODO: check on prepared statements
                String insertQuery = "INSERT INTO CHECKOUT_TABLE(medicineName,medicinePrice,medicineQuantity,userName) VALUES ("
                        + "'" + medicineName + "',"
                        + "" + medicinePrice + ","
                        + "" + quantityToSell + ","
                        + "'" + currentUser + "'"
                        + ")";
                //TODO: do this after sell
                int newQuantity = previousMedicineQuantity - quantityToSell;//prompt for this amount
                String updateMedicineQuery = "UPDATE MEDICINE_ITEMS_TABLE SET quantity = " + newQuantity + " WHERE name = '" + medicineName + "'";

                System.out.println(insertQuery + " and " + updateMedicineQuery);

                if (handler.execAction(insertQuery) && handler.execAction(updateMedicineQuery))
                {
                    AlertMaker.showInformationMessage("Success", "Checkout Done");
                    initCheckOutListTable();
                } else
                    AlertMaker.showErrorMessage("Failed", "Checkout Failed");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeAllFromCheckOut()
    {
        MedicineHandler.finalizeSelling(false);
        MedicineHandler.emptyCheckOut();
        setMiniCheckOutTable();
    }

    @FXML
    private void pushToSell()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/pharmaceuticals/assistant/ui/main/sellDialog/sellDialog.fxml"));
            Parent parent = loader.load();
          //  Parent parent = FXMLLoader.load(getClass().getResource("/pharmaceuticals/assistant/ui/main/sellDialog/sellDialog.fxml"));

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Sell Confirmation");
            SellDialogController sellDialogController = loader.getController();
            sellDialogController.setMain(this);
            stage.setScene(new Scene(parent));
            stage.show();
            //set the icon
            SalesAssistantUtil.setStageIcon(stage);
        } catch (IOException ex)
        {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        setMiniCheckOutTable();
    }

    public void reloadMiniTable()
    {
        setMiniCheckOutTable();
    }
}

