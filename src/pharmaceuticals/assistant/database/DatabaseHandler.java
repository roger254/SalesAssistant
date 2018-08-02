
package pharmaceuticals.assistant.database;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author roger
 */
public final class DatabaseHandler {
    
    private static DatabaseHandler handler = null;
    
    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    
    private DatabaseHandler() {
        createConnection();
        setupMedicineItemTable();
        setupUserTable();
        setUpCheckoutTable();
        setUpSoldTable();
    }
    
    public static DatabaseHandler getInstance(){
        if (handler == null)
            handler = new DatabaseHandler();
        return handler;
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
                        + " id varchar(200) not null ,\n"
                        + " name varchar(200) primary key,\n"
                        + " description varchar(254),\n"
                        + " entryDate timestamp default CURRENT_TIMESTAMP,\n"
                        + " price double precision,\n"
                        + " quantity integer,\n"
                        + " isAvailable boolean default true"
                        + ")");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + " --- setup Medicine Item table");
        }finally{
            
        }
    }
    
    //create user table
    void setupUserTable(){
        String TABLE_NAME = "USERTABLE";
        try{
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet table = dbm.getTablePrivileges(null, TABLE_NAME.toUpperCase(), null);
            if (table.next()){
                System.out.println("Table" + TABLE_NAME + "already exists. Ready for go!");
            }else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + " userId varchar(200) not null,\n"
                        + " name varchar(200) primary key,\n"
                        + " password varchar(254),\n"
                        + " userAccess varchar (254)"
                        + ")");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + " --- setup User table");
        }finally{
            
        }
    }
    
    //create checkout table
    void setUpCheckoutTable(){
        String TABLE_NAME = "CHECKOUT";
        
        try{
            stmt = conn.createStatement();
            DatabaseMetaData dmd = conn.getMetaData();
            
            ResultSet table = dmd.getTables(null, null, TABLE_NAME, null);
            if(table.next())
                System.out.println("Table " + TABLE_NAME + " already exists.");
            else{
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + " medicineName varchar(200) primary key,\n"
                        + " medicinePrice double precision,\n"
                        + " medicineQuantity integer,\n"
                        + " userName varchar(200),\n"
                        + " checkOutTime timestamp default CURRENT_TIMESTAMP,\n"
                        + " FOREIGN KEY (medicineName) REFERENCES MEDICINEITEMS(name),\n"
                        + " FOREIGN KEY (userName) REFERENCES USERTABLE(name)"
                        + ")");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage() + " --- setup CheckOut Table");
        }
    }
    
    void setUpSoldTable(){
        String TABLE_NAME = "SOLDITEMS";
        
        try{
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet table = dbm.getTablePrivileges(null, TABLE_NAME.toUpperCase(), null);
            if (table.next()){
                System.out.println("Table" + TABLE_NAME + "already exists. Ready for go!");
            }else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + " userName varchar(200),\n"
                        + " medicineName varchar(200),\n"
                        + " medicinePrice double precision,\n"
                        + " medicineQuantity integer,\n"
                        + " soldTime timestamp default CURRENT_TIMESTAMP"
                        + ")");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + " --- setup Sold Items Table");
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
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
}
