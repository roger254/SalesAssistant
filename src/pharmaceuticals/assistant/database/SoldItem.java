package pharmaceuticals.assistant.database;

import java.util.Date;

public class SoldItem extends MedicineItem
{
    private String userName;
    private String itemName;
    private double itemPrice;
    private int itemQuantity;
    private Date itemDate;

    public SoldItem(String userName, String itemName, double itemPrice, int itemQuantity, Date itemDate)
    {
        this.userName = userName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemDate = itemDate;
    }

    public SoldItem(String medicineName, String userName, String itemName, double itemPrice, int itemQuantity, Date itemDate)
    {
        super(medicineName);
        this.userName = userName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemDate = itemDate;
    }

    public SoldItem(String medicineName, int medicineQuantity, double medicinePrice, String userName, String itemName, double itemPrice, int itemQuantity, Date itemDate)
    {
        super(medicineName, medicineQuantity, medicinePrice);
        this.userName = userName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemDate = itemDate;
    }

    public SoldItem(String medicineName, int medicineQuantity, double medicinePrice, String medicineDescription, String userName, String itemName, double itemPrice, int itemQuantity, Date itemDate)
    {
        super(medicineName, medicineQuantity, medicinePrice, medicineDescription);
        this.userName = userName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemDate = itemDate;
    }

    public SoldItem(String medicineName, int medicineQuantity, double medicinePrice, String medicineDescription, int quantityToSell, String userName, String itemName, double itemPrice, int itemQuantity, Date itemDate)
    {
        super(medicineName, medicineQuantity, medicinePrice, medicineDescription, quantityToSell);
        this.userName = userName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemDate = itemDate;
    }

    public SoldItem(String medicineName, int medicineQuantity, double medicinePrice, String medicineDescription, Date date, String userName, String itemName, double itemPrice, int itemQuantity, Date itemDate)
    {
        super(medicineName, medicineQuantity, medicinePrice, medicineDescription, date);
        this.userName = userName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemDate = itemDate;
    }

}
