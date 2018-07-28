
package pharmaceuticals.assistant.database;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author roger
 */
public final class DatabaseHandler {
    
    private static DatabaseHandler handler;
    
    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    public DatabaseHandler() {
        createConnection();
        setupMedicineItemTable();
    }
    
    
    
    
   //create connection to database
    void createConnection(){
        try{
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
    
    //create table for the medicineItem
    void setupMedicineItemTable(){
        String TABLE_NAME = "MEDICINEITEMS";
        try{
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet table = dbm.getTablePrivileges(null, TABLE_NAME.toUpperCase(), null);
            if (table.next()){
                System.out.println("Table" + TABLE_NAME + "already exists. Ready for go!");
            }else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                + " id varchar(200) primary key ,\n"
                + " name varchar(200),\n"
                + " description varchar(254),\n"
                + " entryDate DATE,\n"
                + " price double precision,\n"
                + " quantity integer,\n"
                + " isAvailable boolean default true"
                + ")");
            }
        } catch (SQLException ex) {
           System.err.println(ex.getMessage() + " --- setup Database");
        }finally{
            
        }
    }
    
    //execute Query and execAction
    public ResultSet executeQuery(String query){
        ResultSet result;
        try{
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        return result;
    }
    
    public boolean execAction(String qu){
        try{
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error:" + ex.getMessage(), "Error Occured",JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execAction:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
    }
}
