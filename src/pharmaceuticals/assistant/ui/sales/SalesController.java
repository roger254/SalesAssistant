/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.sales;

import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import pharmaceuticals.assistant.database.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author roger
 */
public class SalesController implements Initializable
{

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    DatabaseHandler handler;
    @FXML
    private GridPane buttonGrid;
    @FXML
    private AnchorPane pieChartTab;
    @FXML
    private PieChart pieChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        handler = DatabaseHandler.getInstance();
        JFXDepthManager.setDepth(buttonGrid, 2);
        loadData();
    }

    @FXML
    private void displayPieChart()
    {
        pieChart.getData().addAll(pieChartData);
        pieChart.setTitle("Sold Items");
    }

    private void loadData()
    {
        String query = "SELECT * FROM SOLDITEMS";
        ResultSet resultSet = handler.executeQuery(query);
        try
        {
            while (resultSet.next())
            {
                String itemName = resultSet.getString("medicineName");
                double priceSoldAt = resultSet.getDouble("medicinePrice");
                int quantitySold = resultSet.getInt("medicineQuantity");
                pieChartData.add(new PieChart.Data(itemName, (priceSoldAt * quantitySold)));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}