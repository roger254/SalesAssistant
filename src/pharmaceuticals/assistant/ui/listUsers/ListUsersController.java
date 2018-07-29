/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.listUsers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
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
public class ListUsersController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, Integer> userIdColumn;
    @FXML
    private TableColumn<User, String> userAccessColumn;
    
     ObservableList<User> userList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }    

    private void initCol() {
       nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
       userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
       userAccessColumn.setCellValueFactory(new PropertyValueFactory<>("userAccess"));
    }

    private void loadData() {
        DatabaseHandler handler = new DatabaseHandler();
        
          String qu = "SELECT * FROM USERTABLE";
          ResultSet result = handler.executeQuery(qu);
        try {
            while(result.next()){
                String userName = result.getString("name");
                 String databaseUserId = result.getString("userId");
                 String userAccess = result.getString("userAccess");
                int userId = Integer.parseInt(databaseUserId);
                 
                 userList.add(new User(userName, userId, userAccess));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        tableView.getItems().setAll(userList);
    }
    
    
     
    public static class User{
        private final SimpleStringProperty userName;
        private final SimpleIntegerProperty userId;
        private final SimpleStringProperty userAccess;
       

        public User(String userName, int userId, String userAccess) {
            this.userName = new SimpleStringProperty(userName);
            this.userId = new SimpleIntegerProperty(userId);   
            this.userAccess = new SimpleStringProperty(userAccess);
        }


        public String getUserName() {
            return userName.get();
        }

        public Integer getUserId() {
            return userId.get();
        }
        
        public String getUserAccess() {
            return userAccess.get();
        }
    }
}
