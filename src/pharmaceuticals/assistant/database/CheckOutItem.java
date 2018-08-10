package pharmaceuticals.assistant.database;

import java.util.Date;

public class CheckOutItem extends MedicineItem
{
    private String currentUser;
    private int quantityToSell;

    public CheckOutItem(String medicineName, int medicineQuantity, double medicinePrice, String medicineDescription, String currentUser, int quantityToSell)
    {
        super(medicineName, medicineQuantity, medicinePrice, medicineDescription);
        this.currentUser = currentUser;
        this.quantityToSell = quantityToSell;
    }

}
