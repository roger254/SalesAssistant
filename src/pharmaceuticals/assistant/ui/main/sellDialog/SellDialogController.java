package pharmaceuticals.assistant.ui.main.sellDialog;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pharmaceuticals.assistant.database.MedicineHandler;
import pharmaceuticals.assistant.database.MedicineItem;
import pharmaceuticals.assistant.ui.main.MainController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class SellDialogController implements Initializable
{
    private final Double totalAmount = getItemsTotal();
    @FXML
    private JFXTextField amountPaidInput;
    @FXML
    private Text totalAmountText;
    @FXML
    private Text balanceText;

    private boolean confirmedSale = false;

    private MainController mainController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        totalAmountText.setText(String.valueOf(totalAmount));
    }

    @FXML
    private void handleConfirm()
    {
        if (confirmedSale)
        {
            MedicineHandler.finalizeSelling(true);
            mainController.reloadMiniTable();
        }
        ((Stage) amountPaidInput.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel()
    {
        ((Stage) amountPaidInput.getScene().getWindow()).close();
    }

    @FXML
    private void calculate()
    {
        Double amountPaid = Double.parseDouble(amountPaidInput.getText());
        Double balance = amountPaid - totalAmount;
        balanceText.setText(String.valueOf(balance));
        confirmedSale = balance > 0;
    }

    private double getItemsTotal()
    {
        int total = 0;
        for (MedicineItem item : MedicineHandler.getCheckOutList())
        {
             total += item.getQuantityToSell() * item.getMedicinePrice();
        }
        return total;
    }

    public void setMain(MainController mainController)
    {
        this.mainController = mainController;
    }
}
