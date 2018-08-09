package pharmaceuticals.assistant.ui.listItems;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pharmaceuticals.assistant.alert.AlertMaker;
import pharmaceuticals.assistant.database.DatabaseHandler;
import pharmaceuticals.assistant.database.MedicineHandler;
import pharmaceuticals.assistant.database.MedicineItem;
import pharmaceuticals.assistant.ui.login.LoginController;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class ItemListController implements Initializable
{

    @FXML
    private TableView<MedicineItem> tableView;
    @FXML
    private TableColumn<MedicineItem, String> nameColumn;
    @FXML
    private TableColumn<MedicineItem, Integer> quantityColumn;
    @FXML
    private TableColumn<MedicineItem, Double> priceColumn;
    @FXML
    private TableColumn<MedicineItem, String> descriptionColumn;
    @FXML
    private TableColumn<MedicineItem, Date> entryDateColumn;
    @FXML
    private TableColumn<MedicineItem, Boolean> availabilityColumn;

    @FXML
    private MenuItem deleteMenuItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        initCol();

        loadData();

        deleteMenuItem.setDisable(!LoginController.getCurrentUserAccess().equals("ADMIN"));
    }

    private void initCol()
    {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("medicineQuantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("medicinePrice"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("medicineDescription"));
        entryDateColumn.setCellValueFactory(new PropertyValueFactory<>("medicineEntryDate"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("medicineAvailability"));
    }


    private void loadData()
    {
        tableView.getItems().setAll(MedicineHandler.getMedicineItems());
    }

    @FXML
    private void handleDeleteItem()
    {
        MedicineItem itemSelected = tableView.getSelectionModel().getSelectedItem();
        if (itemSelected == null)
        {
            AlertMaker.showErrorMessage("No item selected", "Please select an item");
            return;
        }
        if (AlertMaker.promptConfirmationMessage("Deleting item", "Deleting", itemSelected.getMedicineName()) == ButtonType.OK)
        {
            if (DatabaseHandler.getInstance().deleteItem(itemSelected))
            {
                AlertMaker.showSimpleAlert(itemSelected.getMedicineName() + " Item Deleted");
                tableView.getItems().remove(itemSelected);
                tableView.refresh();
            } else
                AlertMaker.showErrorMessage("Deletion Failed", itemSelected.getMedicineName() + " could not be deleted");
        } else
            AlertMaker.showInformationMessage("Deletion Cancelled", "Deletion process Cancelled");


    }

}
