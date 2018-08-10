package pharmaceuticals.assistant.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MedicineHandler
{
    private static MedicineHandler medicineHandler = null;
    private static ObservableList<MedicineItem> medicineItems = FXCollections.observableArrayList();
    private static ObservableList<MedicineItem> checkOutList = FXCollections.observableArrayList();
    private static ObservableList<MedicineItem> sellList = FXCollections.observableArrayList();

    private MedicineHandler()
    {
        DatabaseHandler.getInstance();
        medicineItems = DatabaseHandler.LoadMedicineItems();
        sellList = DatabaseHandler.LoadSoldList();
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

    public static void addItemToCheckOut(MedicineItem item, int quantityToSell)
    {
        boolean isFound = false;
        if (checkOutList.size() > 0)
        {
            for (MedicineItem medicineItem : checkOutList)
            {
                if (medicineItem.checkOutEquals(item))
                {
                    medicineItem.setQuantityToSell(medicineItem.getQuantityToSell() + quantityToSell);
                    item.setMedicineQuantity(item.getMedicineQuantity() - quantityToSell);
                    isFound = true;
                }
            }
        }
        if (!isFound)
            checkOutList.add(item.addToCheckOut(quantityToSell));

    }


    //TODO: fix concurrent modification
    public static void finalizeSelling(boolean finalized)
    {
        if (checkOutList.size() > 0)
        {
            for (MedicineItem checkOutItem : checkOutList)
            {
                for (MedicineItem medicineItem : medicineItems)
                {
                    if (finalized)
                    {
                        if (medicineItem.checkOutEquals(checkOutItem))
                        {
                            medicineItem.addToSold();
                          //  checkOutList.removeIf(p -> p.checkOutEquals(medicineItem));
                            break;
                        }
                    }else
                    {
                        medicineItem.setMedicineQuantity(medicineItem.getPreviousQuantity());
                        medicineItem.setPreviousQuantity(0);
                        medicineItem.setQuantityToSell(0);
                    }
                }
            }
            checkOutList.clear();
        }

        for (MedicineItem item : sellList)
        {
            System.out.println(item.getMedicineName());
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

    public static ObservableList<MedicineItem> getSellList()
    {
        return sellList;
    }

}
