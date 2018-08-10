package pharmaceuticals.assistant.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pharmaceuticals.assistant.ui.login.LoginController;
import pharmaceuticals.assistant.ui.main.MainController;

public class MedicineHandler
{
    private  MainController mainController;
    private static MedicineHandler medicineHandler = null;
    private static ObservableList<MedicineItem> medicineItems = FXCollections.observableArrayList();
    private static ObservableList<MedicineItem> checkOutList = FXCollections.observableArrayList();
    private static ObservableList<MedicineItem.SoldItem> sellList = FXCollections.observableArrayList();

    private MedicineHandler()
    {
        DatabaseHandler.getInstance();
        medicineItems =  DatabaseHandler.LoadMedicineItems();
        sellList = DatabaseHandler.LoadSoldList();
    }

    public MedicineHandler(MainController mainController)
    {
        this.mainController = mainController;
        MedicineHandler.getInstance();
    }

    public static MedicineHandler getInstance()
    {
        if (medicineHandler == null)
            medicineHandler = new MedicineHandler();
        return medicineHandler;
    }

    public static void emptyCheckOut()
    {
        checkOutList.clear();
    }

    public static void addItemToCheckOut(MedicineItem item)
    {
        if (checkOutList.size() > 0)
        {
            for (MedicineItem medicineItem: checkOutList)
            {
                if (medicineItem.equals(item))
                {
                    medicineItem.setQuantityToSell(item.getPreviousQuantity());
                }
            }
        }
        boolean isFound = false;
        if (checkOutList.size() > 0)
        {
            for (MedicineItem medicineItem : checkOutList)
            {
                if (medicineItem.getMedicineName().equals(item.getMedicineName()))
                {
                    medicineItem.setQuantityToSell(medicineItem.getQuantityToSell() + item.getQuantityToSell());
                    updateItemQuantity(item);
                    isFound = true;
                }
            }
            if (!isFound)
            {
                checkOutList.add(item);
                updateItemQuantity(item);
            }
        } else
        {
            checkOutList.add(item);
            updateItemQuantity(item);
        }
    }

    private static void updateItemQuantity(MedicineItem item)
    {
        if (medicineItems.size() > 0)
        {
            for (MedicineItem medicineItem : medicineItems)
            {
                if (medicineItem.getMedicineName().equals(item.getMedicineName()))
                {
                    medicineItem.setPreviousQuantity(medicineItem.getMedicineQuantity());
                    medicineItem.setMedicineQuantity(medicineItem.getMedicineQuantity() - item.getQuantityToSell());
                }
            }
        }
    }

    public static void finalizeSelling(boolean finalized)
    {
        if (checkOutList.size() > 0)
        {
            ItemLoop:
            for (MedicineItem itemList : medicineItems)
            {
                for (MedicineItem checkOutListItem : checkOutList)
                {
                    if (itemList.getMedicineName().equals(checkOutListItem.getMedicineName()))
                    {
                        if (finalized)
                        {
                            itemList.setQuantityToSell(0);
                            itemList.setPreviousQuantity(0);

                            //add too sell list
                            checkOutListItem.setMedicineQuantity(checkOutListItem.getQuantityToSell());
                            sellList.add(new MedicineItem.SoldItem(LoginController.getCurrentUser(), checkOutListItem));
                            System.out.println("Added to sellList");
                            break ItemLoop;
                        } else
                        {
                            itemList.setMedicineQuantity(itemList.getMedicineQuantity() + itemList.getQuantityToSell());
                            itemList.setQuantityToSell(0);
                            itemList.setPreviousQuantity(0);
                        }
                    }
                }
            }
            if (finalized)
            {
                checkOutList.clear();
                System.out.println("Cleared");
               // medicineHandler.mainController.reloadMiniTable();
            }
        }
    }

    public static ObservableList<MedicineItem> getMedicineItems()
    {
        return medicineItems;
    }

    public static ObservableList<MedicineItem> getCheckOutList()
    {
        return checkOutList;
    }

    public static ObservableList<MedicineItem.SoldItem> getSellList()
    {
        return sellList;
    }

}
