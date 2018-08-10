package pharmaceuticals.assistant.database;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pharmaceuticals.assistant.ui.addItem.AddItemController;
import pharmaceuticals.assistant.ui.login.LoginController;

import javax.swing.*;
import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author roger
 */
public final class DatabaseHandler
{

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static DatabaseHandler handler = null;
    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler()
    {
        createConnection();
        setupMedicineItemTable();
        setupUserTable();
        setUpCheckoutTable();
        setUpSoldTable();
    }

    public static DatabaseHandler getInstance()
    {
        if (handler == null)
            handler = new DatabaseHandler();
        return handler;
    }

    //create connection to database
    private void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex)
        {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    //create table for the medicineItem
    private void setupMedicineItemTable()
    {
        String TABLE_NAME = "MEDICINE_ITEMS_TABLE";
        try
        {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet table = dbm.getTablePrivileges(null, TABLE_NAME.toUpperCase(), null);
            if (table.next())
            {
                System.out.println("Table" + TABLE_NAME + "already exists. Ready for go!");
            } else
            {
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
        } catch (SQLException ex)
        {
            System.err.println(ex.getMessage() + " --- setup Medicine Item table");
        }
    }

    //create user table
    private void setupUserTable()
    {
        String TABLE_NAME = "USER_TABLE";
        try
        {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet table = dbm.getTablePrivileges(null, TABLE_NAME.toUpperCase(), null);
            if (table.next())
            {
                System.out.println("Table" + TABLE_NAME + "already exists. Ready for go!");
            } else
            {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + " userId varchar(200) not null,\n"
                        + " name varchar(200) primary key,\n"
                        + " password varchar(254),\n"
                        + " userAccess varchar (254)"
                        + ")");
            }
        } catch (SQLException ex)
        {
            System.err.println(ex.getMessage() + " --- setup User table");
        }
    }

    //create checkout table
    private void setUpCheckoutTable()
    {
        String TABLE_NAME = "CHECKOUT_TABLE";

        try
        {
            stmt = conn.createStatement();
            DatabaseMetaData dmd = conn.getMetaData();

            ResultSet table = dmd.getTables(null, null, TABLE_NAME, null);
            if (table.next())
                System.out.println("Table " + TABLE_NAME + " already exists.");
            else
            {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + " medicineName varchar(200) primary key,\n"
                        + " medicinePrice double precision,\n"
                        + " medicineQuantity integer,\n"
                        + " userName varchar(200),\n"
                        + " checkOutTime timestamp default CURRENT_TIMESTAMP,\n"
                        + " FOREIGN KEY (medicineName) REFERENCES MEDICINE_ITEMS_TABLE(name),\n"
                        + " FOREIGN KEY (userName) REFERENCES USER_TABLE(name)"
                        + ")");
            }
        } catch (SQLException e)
        {
            System.err.println(e.getMessage() + " --- setup CheckOut Table");
        }
    }

    private void setUpSoldTable()
    {
        String TABLE_NAME = "SOLD_ITEMS_TABLE";

        try
        {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet table = dbm.getTablePrivileges(null, TABLE_NAME.toUpperCase(), null);
            if (table.next())
            {
                System.out.println("Table" + TABLE_NAME + "already exists. Ready for go!");
            } else
            {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + " userName varchar(200),\n"
                        + " medicineName varchar(200),\n"
                        + " medicinePrice double precision,\n"
                        + " medicineQuantity integer,\n"
                        + " soldTime timestamp default CURRENT_TIMESTAMP"
                        + ")");
            }
        } catch (SQLException ex)
        {
            System.err.println(ex.getMessage() + " --- setup Sold Items Table");
        }
    }

    //execute Query and execAction
    public ResultSet executeQuery(String query)
    {
        ResultSet result;
        try
        {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex)
        {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        return result;
    }

    public boolean execAction(String qu)
    {
        try
        {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex)
        {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /*
    Deletes the selected item from the database
     */

    public boolean deleteItem(MedicineItem item)
    {
        try
        {
            String deleteStmt = "DELETE FROM MEDICINE_ITEMS_TABLE WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStmt);
            stmt.setString(1, item.getMedicineName());
            if (stmt.executeUpdate() == 1)
                return true;
        } catch (SQLException ex)
        {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /*
    Retrieve all current medicine data in stock
     */

    public static ObservableList<MedicineItem> LoadMedicineItems()
    {
        ObservableList<MedicineItem> medicineItems = FXCollections.observableArrayList();
        String qu = "SELECT * FROM MEDICINE_ITEMS_TABLE";
        ResultSet result = handler.executeQuery(qu);
        try
        {
            while (result != null && result.next())
            {
                String medicineName = result.getString("name");
                String medicineDescription = result.getString("description");

                java.util.Date medicineEntryDate = result.getDate("entryDate");
                System.out.println(medicineEntryDate);
                double medicinePrice = result.getDouble("price");
                int medicineQuantity = result.getInt("quantity");

                medicineItems.add(new MedicineItem(medicineName, medicineQuantity, medicinePrice, medicineDescription, medicineEntryDate));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(AddItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return medicineItems;
    }

    /*
    Retrieve sold items data from the database
     */

    public static ObservableList<MedicineItem.SoldItem> LoadSoldList()
    {
        ObservableList<MedicineItem.SoldItem> soldItems = FXCollections.observableArrayList();
        String qu = "SELECT * FROM SOLD_ITEMS_TABLE";
        ResultSet result = handler.executeQuery(qu);
        try
        {
            while (result != null && result.next())
            {
                String medicineName = result.getString("medicineName");
                Date medicineEntryDate = result.getDate("soldTime");
                double medicinePrice = result.getDouble("medicinePrice");
                int medicineQuantity = result.getInt("medicineQuantity");

                soldItems.add(new MedicineItem.SoldItem(LoginController.getCurrentUser(), medicineName, medicinePrice, medicineQuantity, medicineEntryDate));
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(AddItemController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return soldItems;
    }

    /*
    TODO: Create individual tables for all users
    TODO: Update each sell to the current user
     */

    public static void handleSell()
    {
        ObservableList<MedicineItem.SoldItem> soldItemsList = MedicineHandler.getSellList();
        ResultSet result;
        boolean flag;

        for (MedicineItem.SoldItem soldItem : soldItemsList)
        {
            String itemsName = soldItem.getItemName();
            //check if item is in SOLD TABLE
            String checkQuery = "SELECT * FROM SOLD_ITEMS_TABLE WHERE medicineName ='" + itemsName + "'";
            result = handler.executeQuery(checkQuery);

            String soldItemQuery = null;
            try
            {
                if (result != null && result.next())
                {
                    //update Quantity
                    int databaseQuantity = result.getInt("medicineQuantity");
                  //  if(databaseQuantity!= soldItem.getItemQuantity())
                        soldItemQuery = "UPDATE SOLD_ITEMS_TABLE SET medicineQuantity = " + (soldItem.getItemQuantity() + databaseQuantity) + " WHERE medicineName ='" + itemsName + "'";

                } else
                {
                    //if not add to sell data
                    soldItemQuery = "INSERT INTO SOLD_ITEMS_TABLE(userName,medicineName,medicinePrice,medicineQuantity) VALUES("
                            + "'" + LoginController.getCurrentUser() + "',"
                            + "'" + soldItem.getItemName() + "',"
                            + "" + soldItem.getItemPrice() + ","
                            + "" + soldItem.getItemQuantity() + ""
                            + ")";
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            flag = handler.execAction(soldItemQuery);
            if (flag)
                MedicineHandler.getSellList().clear();
        }
    }
}
