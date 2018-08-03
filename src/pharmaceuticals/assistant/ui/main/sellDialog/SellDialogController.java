/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package pharmaceuticals.assistant.ui.main.sellDialog;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pharmaceuticals.assistant.ui.main.MainController;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class SellDialogController implements Initializable {
    @FXML
    private JFXTextField amountPaidInput;
    @FXML
    private Text totalAmountText;
    @FXML
    private Text balanceText;
    
    private final Double totalAmount =  MainController.getTotalAmout();
    
    private static boolean confirmed = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        totalAmountText.setText(String.valueOf(totalAmount));
    }
    
    
    @FXML
    private void handleConfirm(ActionEvent event) {
        ((Stage) amountPaidInput.getScene().getWindow()).close();
    }
    
    @FXML
    private void handleCancel(ActionEvent event) {
        confirmed = false;
        ((Stage) amountPaidInput.getScene().getWindow()).close();
    }
    
    public static boolean confirmSell() {
        return confirmed;
    }
    
    @FXML
    private void calculate(ActionEvent event) {
        Double amountPaid = Double.parseDouble( amountPaidInput.getText());
        Double balance = amountPaid - totalAmount;
        balanceText.setText(String.valueOf(balance));
        confirmed = balance > 0;
    }
    
    
}
