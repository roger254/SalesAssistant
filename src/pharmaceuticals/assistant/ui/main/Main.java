/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmaceuticals.assistant.ui.main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pharmaceuticals.assistant.database.DatabaseHandler;

/**
 *
 * @author roger
 */
public class Main extends Application {
    
     
    @Override
    public void start(Stage stage) throws Exception {
        //database is initialized in different thread//
        //new Thread(DatabaseHandler::getInstance).start();  
        DatabaseHandler.getInstance();  
        
        Parent root = FXMLLoader.load(getClass().getResource("/pharmaceuticals/assistant/ui/login/login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("LOGIN PAGE");
        stage.show();
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
