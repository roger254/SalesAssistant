/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package pharmaceuticals.assistant.ui.main.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import pharmaceuticals.assistant.database.DatabaseHandler;
import pharmaceuticals.assistant.ui.listItems.ItemListController;
import pharmaceuticals.assistant.ui.listItems.ItemListController.MedicineItem;
import pharmaceuticals.assistant.ui.main.MainController;

/**
 *
 * @author roger
 */
public class EditableTable {
    
    private ObservableList<ItemListController.MedicineItem> currentData = FXCollections.observableArrayList();
    
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_PATTERN);
    
    private TableView<MedicineItem> updateTable;
    
    private TableColumn<MedicineItem, Date> updateEntryDateCol;
    
    private TableColumn<MedicineItem, Double> updatePriceCol;
    
    private TableColumn<MedicineItem, Integer> updateQuantityCol;
    
    private DatabaseHandler handler;
    
    public EditableTable(TableView<MedicineItem> updateTable, TableColumn<MedicineItem, Date> updateEntryDateCol, TableColumn<MedicineItem, Double> updatePriceCol, TableColumn<MedicineItem, Integer> updateQuantityCol, DatabaseHandler handler) {
        this.updateTable = updateTable;
        this.updateEntryDateCol = updateEntryDateCol;
        this.updatePriceCol = updatePriceCol;
        this.updateQuantityCol = updateQuantityCol;
        this.handler = handler;
        initializeDataTable();
    }
    
    private void initializeDataTable(){
        DATE_FORMATTER.setLenient(false);
        updateTable.setItems(currentData);
        populate();
        setUpDateOfEntryColumn();
        setUpPriceColumn();
        setUpQuantityColumn();
        setTableEditable();
    }
    
    //get data fro database
    private void populate(){
        handler = DatabaseHandler.getInstance();
        
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
                
                currentData.add(new ItemListController.MedicineItem(medicineName, medicineQuantity, medicinePrice, medicineDescription, (java.sql.Date) medicineEntryDate, isAvailable));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    //set up the date column
    private void setUpDateOfEntryColumn(){
        //formats the display value to display dats in dd/MM/yyyy format
        updateEntryDateCol
                .setCellFactory(EditCell. <ItemListController.MedicineItem,Date> forTableColumn(
                        new MyDateStringConverter(DATE_PATTERN)));
        //updates the dateOfEnrty field in the MedicineItem object to the committed value
        updateEntryDateCol.setOnEditCommit(event ->{
            final Date value = event.getNewValue() != null ? event.getNewValue() :
                    event.getOldValue();
            ((ItemListController.MedicineItem) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setEntryDate((java.sql.Date) value);
            updateTable.refresh();
        });
    }
    //set up the price column
    private void setUpPriceColumn(){
        //sets the cell factory to use EditCell which will handle key presses and firing commint events
        updatePriceCol.setCellFactory(
                EditCell. <ItemListController.MedicineItem, Double> forTableColumn(
                        new MyDoubleStringConverter()));
        //update the salary field in the MedicineItem object to the committed value
        updatePriceCol.setOnEditCommit(event -> {
            final Double value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            ((ItemListController.MedicineItem) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setMedicinePrice(value);
            updateTable.refresh();
        });
    }
    //set up the quantity column
    private void setUpQuantityColumn(){
        //sets the cell factory to use EditCell which will handle key presses and firing commint events
        updateQuantityCol.setCellFactory(
                EditCell. <ItemListController.MedicineItem, Integer> forTableColumn(
                        new MyIntegerStringConverter()));
        //update the salary field in the MedicineItem object to the committed value
        updateQuantityCol.setOnEditCommit(event -> {
            final int value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            ((ItemListController.MedicineItem) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow()))
                    .setMedicineQuantity(value);
            updateTable.refresh();
        });
    }
    // make table editable
    private void setTableEditable(){
        //allows individual cells to be selected
        updateTable.setEditable(true);
        //start edit when character or numner is pressed
        updateTable.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey() || event.getCode().isDigitKey()){
                editFocusedCell();
            }else if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.TAB){
                updateTable.getSelectionModel().selectNext();
                event.consume();
            }else if (event.getCode() == KeyCode.LEFT){
                /*
                work around due to TableView.getSelectionModel().selectPrevious()
                due to a bug stopping it from working on the first column in the last row of the table
                */
                selectPrevious();
                event.consume();
            }
        });
    }
    
    @SuppressWarnings("unchecked")
    private void editFocusedCell(){
        final TablePosition <ItemListController.MedicineItem, ?> focusedCell = updateTable
                .focusModelProperty().get().focusedCellProperty().get();
        updateTable.edit(focusedCell.getRow(),focusedCell.getTableColumn());
    }
    
    @SuppressWarnings("unchecked")
    private void selectPrevious(){
        if (updateTable.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition<ItemListController.MedicineItem, ?> pos = updateTable.getFocusModel()
                    .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                updateTable.getSelectionModel().select(pos.getRow(),
                        getTableColumn(pos.getTableColumn(), -1));
            } else if (pos.getRow() < updateTable.getItems().size()) {
                // wrap to end of previous row
                updateTable.getSelectionModel().select(pos.getRow() - 1,
                        updateTable.getVisibleLeafColumn(
                                updateTable.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = updateTable.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                updateTable.getSelectionModel().select(updateTable.getItems().size() - 1);
            } else if (focusIndex > 0) {
                updateTable.getSelectionModel().select(focusIndex - 1);
            }
        }
    }
    
    private TableColumn <ItemListController.MedicineItem, ?> getTableColumn(final TableColumn <ItemListController.MedicineItem, ?> column, int offset){
        int columnIndex = updateTable.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return updateTable.getVisibleLeafColumn(newColumnIndex);
    }
    
    public void handleUpdateButton(){
        boolean flag = false , isAvailable = false;
        for (MedicineItem medicineItem : currentData){
            if(medicineItem.getMedicineQuantity() > 0)
                isAvailable = true;
            String updateQuery = "UPDATE MEDICINEITEMS SET price = " + medicineItem.getMedicinePrice() + ","
                    + "quantity = " + medicineItem.getMedicineQuantity() +", isAvailable = " + isAvailable + " WHERE name = '" + medicineItem.getMedicineName() + "'" ;
            flag = handler.execAction(updateQuery);
        }
        if (flag){
            Alert checkOutSuccess = new Alert(Alert.AlertType.INFORMATION);
            checkOutSuccess.setTitle("Success");
            checkOutSuccess.setHeaderText(null);
            checkOutSuccess.setContentText("Updated");
            checkOutSuccess.showAndWait();
        }else {
            Alert checkOutSuccess = new Alert(Alert.AlertType.ERROR);
            checkOutSuccess.setTitle("Failed");
            checkOutSuccess.setHeaderText(null);
            checkOutSuccess.setContentText("Update Failed");
            checkOutSuccess.showAndWait();
        }
    }
}
