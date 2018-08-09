 /*
  * To change this license header, choose License Headers in Project Properties.
  * To change this template file, choose Tools | Templates
  * and open the template in the editor.
  */
 package pharmaceuticals.assistant.ui.addItem;

 import com.jfoenix.controls.JFXDatePicker;
 import com.jfoenix.controls.JFXTextField;
 import javafx.fxml.FXML;
 import javafx.fxml.Initializable;
 import javafx.scene.layout.AnchorPane;
 import javafx.stage.Stage;
 import pharmaceuticals.assistant.alert.AlertMaker;
 import pharmaceuticals.assistant.database.DatabaseHandler;

 import java.net.URL;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.time.LocalDate;
 import java.util.ResourceBundle;
 import java.util.logging.Level;
 import java.util.logging.Logger;

 /**
  * @author roger
  */
 public class AddItemController implements Initializable
 {

     private DatabaseHandler databaseHandler;
     @FXML
     private JFXTextField itemName;
     @FXML
     private JFXTextField itemPrice;
     @FXML
     private JFXTextField itemQuantity;
     @FXML
     private JFXTextField itemDescription;
     @FXML
     private JFXDatePicker itemEntryDate;
     @FXML
     private JFXTextField itemId;
     @FXML
     private AnchorPane rootPane;


     @Override
     public void initialize(URL url, ResourceBundle rb)
     {
         databaseHandler = DatabaseHandler.getInstance();

         itemEntryDate.setValue(LocalDate.now());
         itemId.setText(String.valueOf(getNextID()));

     }

     @FXML
     private void handleSave()
     {
         //get medicine details
         String itemName = this.itemName.getText();
         String itemPrice = this.itemPrice.getText();
         String itemQuantity = this.itemQuantity.getText();
         String itemDescription = this.itemDescription.getText();
         //  String itemEntryDate = formatter.format(this.itemEntryDate.getValue());
         String itemId = this.itemId.getText();

         //TODO: automate this process of getting the Id
         //TODO: validate the inputs
         if (itemId.isEmpty() || itemName.isEmpty() || itemPrice.isEmpty() || itemQuantity.isEmpty() || itemDescription.isEmpty())
         {
             AlertMaker.showErrorMessage(null, "Make sure all fields are entered");
             return;
         }

         String qu = "INSERT INTO MEDICINE_ITEMS_TABLE (id,name,description,price,quantity,isAvailable) VALUES("
                 + "'" + itemId + "',"
                 + "'" + itemName + "',"
                 + "'" + itemDescription + "',"
                 + "" + Double.parseDouble(itemPrice) + ","
                 + "" + Integer.parseInt(itemQuantity) + ","
                 + "" + "true" + ""
                 + ")";

         //show details posted
         System.out.println(qu);

         if (databaseHandler.execAction(qu))
             AlertMaker.showInformationMessage(null, "Success");
         else
             AlertMaker.showErrorMessage(null, "Failed");

     }

     @FXML
     private void handleCancel()
     {
         Stage stage = (Stage) rootPane.getScene().getWindow();
         stage.close();
     }

     private int getNextID()
     {
         String qu = "SELECT id from MEDICINE_ITEMS_TABLE";
         ResultSet result = databaseHandler.executeQuery(qu);

         int id = 0;

         try
         {
             while (result != null && result.next())
             {

                 id = Integer.parseInt(result.getString("id"));
                 System.out.println(id);
             }
         } catch (SQLException ex)
         {
             Logger.getLogger(AddItemController.class.getName()).log(Level.SEVERE, null, ex);
         }

         return ++id;
     }


 }
