
package pharmaceuticals.assistant.ui.listItems;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import pharmaceuticals.assistant.database.DatabaseHandler;
import pharmaceuticals.assistant.ui.addItem.AddItemController;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class ItemListController implements Initializable {

    @FXML
    private AnchorPane rootPane;
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
    
    
    ObservableList<MedicineItem> medicineList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        
        loadData();
    }    

    private void initCol() {
       nameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
       quantityColumn.setCellValueFactory(new PropertyValueFactory<>("medicineQuantity"));
       priceColumn.setCellValueFactory(new PropertyValueFactory<>("medicinePrice"));
       descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("medicineDescription"));
       entryDateColumn.setCellValueFactory(new PropertyValueFactory<>("medicineEntryDate"));
       availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("medicineAvailability"));
    }
    
   
    private void loadData() {
        DatabaseHandler handler = new DatabaseHandler();
        
          String qu = "SELECT * FROM MEDICINEITEMS";
          ResultSet result = handler.executeQuery(qu);
        try {
            while(result.next()){
                String medicineName = result.getString("name");
                String medicineDescription = result.getString("description");
                Date medicineEntryDate = result.getDate("entryDate");
                double medicinePrice = result.getDouble("price");
                int medicineQuantity = result.getInt("quantity");
                boolean isAvailable = result.getBoolean("isAvailable");
                
                medicineList.add(new MedicineItem(medicineName, medicineQuantity, medicinePrice, medicineDescription, medicineEntryDate, isAvailable));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        tableView.getItems().setAll(medicineList);
    }
    
    public static class MedicineItem{
        private final SimpleStringProperty medicineName;
        private final SimpleIntegerProperty medicineQuantity;
        private final SimpleDoubleProperty medicinePrice;
        private final SimpleStringProperty medicineDescription;
        private final ObjectProperty<Date> medicineEntryDate;
        private final SimpleBooleanProperty medicineAvailability;

        public MedicineItem(String medicineName, int medicineQuantity, double medicinePrice, String medicineDescription, Date medicineEntryDate, boolean medicineAvailability) {
            this.medicineName = new SimpleStringProperty(medicineName);
            this.medicineQuantity = new SimpleIntegerProperty(medicineQuantity);
            this.medicinePrice = new SimpleDoubleProperty(medicinePrice);
            this.medicineDescription = new SimpleStringProperty(medicineDescription);
            this.medicineEntryDate = new SimpleObjectProperty<>(medicineEntryDate);
            this.medicineAvailability =  new SimpleBooleanProperty(medicineAvailability);
        }


        public String getMedicineName() {
            return medicineName.get();
        }

        public Integer getMedicineQuantity() {
            return medicineQuantity.get();
        }

        public Double getMedicinePrice() {
            return medicinePrice.get();
        }

        public String getMedicineDescription() {
            return medicineDescription.get();
        }

        public Date getMedicineEntryDate() {
            return medicineEntryDate.get();
        }

        public Boolean getMedicineAvailability() {
            return medicineAvailability.get();
        }
    }
}
