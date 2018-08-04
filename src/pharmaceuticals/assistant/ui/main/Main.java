package pharmaceuticals.assistant.ui.main;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pharmaceuticals.assistant.database.DatabaseHandler;
import pharmaceuticals.assistant.ui.login.LoginController;
import pharmaceuticals.assistant.util.SalesAssistantUtil;

/**
 *
 * @author roger
 */
public class Main extends Application {
    
    DatabaseHandler handler;
     
    @Override
    public void start(Stage stage) throws Exception {
        //database is initialized in different thread//
        //new Thread(DatabaseHandler::getInstance).start();  
        handler = DatabaseHandler.getInstance();  
        checkForUsers();
        
        Parent root = FXMLLoader.load(getClass().getResource("/pharmaceuticals/assistant/ui/login/login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("LOGIN PAGE");
        stage.show();
        
        
        //set the icon
        SalesAssistantUtil.setStageIcon(stage);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    void checkForUsers(){
        try {
            ResultSet result;
            
            String checkForUsers  = "SELECT * FROM USERTABLE";
            result = handler.executeQuery(checkForUsers);
            if(!result.next()){
                
                try {
                    
                    Parent parent = FXMLLoader.load(getClass().getResource("/pharmaceuticals/assistant/ui/addUser/addUser.fxml"));
                    Stage stage = new Stage(StageStyle.DECORATED);
                    stage.setTitle("Add New User");
                    stage.setScene(new Scene(parent));
                    stage.showAndWait();
                    
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
