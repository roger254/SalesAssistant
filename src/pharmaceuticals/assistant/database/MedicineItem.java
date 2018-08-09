package pharmaceuticals.assistant.database;

import javafx.beans.property.*;

import java.util.Date;
import java.util.Objects;

public class MedicineItem
{
    private final SimpleStringProperty medicineName;
    private final SimpleIntegerProperty medicineQuantity;
    private final SimpleDoubleProperty medicinePrice;
    private final SimpleStringProperty medicineDescription;
    private final ObjectProperty<Date> medicineEntryDate;
    private final SimpleBooleanProperty medicineAvailability;
    private final SimpleIntegerProperty quantityToSell;
    private int previousQuantity = 0;

    public MedicineItem()
    {
        this.medicineName = new SimpleStringProperty("");
        this.medicineQuantity = new SimpleIntegerProperty(0);
        this.medicinePrice = new SimpleDoubleProperty(0.0);
        this.medicineDescription = new SimpleStringProperty("");
        this.quantityToSell = new SimpleIntegerProperty(0);
        long millis = System.currentTimeMillis();
        Date today = new Date(millis);
        this.medicineEntryDate = new SimpleObjectProperty<>(today);
        this.medicineAvailability = new SimpleBooleanProperty(getMedicineAvailability());
    }

    public MedicineItem(String medicineName)
    {
        this.medicineName = new SimpleStringProperty(medicineName);
        this.medicineQuantity = new SimpleIntegerProperty(0);
        this.medicinePrice = new SimpleDoubleProperty(0.0);
        this.medicineDescription = new SimpleStringProperty("");
        this.quantityToSell = new SimpleIntegerProperty(0);
        long millis = System.currentTimeMillis();
        Date today = new Date(millis);
        this.medicineEntryDate = new SimpleObjectProperty<>(today);
        this.medicineAvailability = new SimpleBooleanProperty(getMedicineAvailability());
    }

    public MedicineItem(String medicineName, int medicineQuantity, double medicinePrice)
    {
        this.medicineName = new SimpleStringProperty(medicineName);
        this.medicineQuantity = new SimpleIntegerProperty(medicineQuantity);
        this.medicinePrice = new SimpleDoubleProperty(medicinePrice);
        this.medicineDescription = new SimpleStringProperty("");
        this.quantityToSell = new SimpleIntegerProperty(0);
        long millis = System.currentTimeMillis();
        Date today = new Date(millis);
        this.medicineEntryDate = new SimpleObjectProperty<>(today);
        this.medicineAvailability = new SimpleBooleanProperty(getMedicineAvailability());
    }

    public MedicineItem(String medicineName, int medicineQuantity, double medicinePrice, String medicineDescription)
    {
        this.medicineName = new SimpleStringProperty(medicineName);
        this.medicineQuantity = new SimpleIntegerProperty(medicineQuantity);
        this.medicinePrice = new SimpleDoubleProperty(medicinePrice);
        this.medicineDescription = new SimpleStringProperty(medicineDescription);
        this.quantityToSell = new SimpleIntegerProperty(0);
        long millis = System.currentTimeMillis();
        Date today = new Date(millis);
        this.medicineEntryDate = new SimpleObjectProperty<>(today);
        this.medicineAvailability = new SimpleBooleanProperty(getMedicineAvailability());
    }

    public MedicineItem(String medicineName, int medicineQuantity, double medicinePrice, String medicineDescription, int quantityToSell)
    {
        this.medicineName = new SimpleStringProperty(medicineName);
        this.medicineQuantity = new SimpleIntegerProperty(medicineQuantity);
        this.medicinePrice = new SimpleDoubleProperty(medicinePrice);
        this.medicineDescription = new SimpleStringProperty(medicineDescription);
        this.quantityToSell = new SimpleIntegerProperty(quantityToSell);
        long millis = System.currentTimeMillis();
        Date today = new Date(millis);
        this.medicineEntryDate = new SimpleObjectProperty<>(today);
        this.medicineAvailability = new SimpleBooleanProperty(getMedicineAvailability());
    }

    public MedicineItem(String medicineName, int medicineQuantity, double medicinePrice, String medicineDescription, Date date)
    {
        this.medicineName = new SimpleStringProperty(medicineName);
        this.medicineQuantity = new SimpleIntegerProperty(medicineQuantity);
        this.medicinePrice = new SimpleDoubleProperty(medicinePrice);
        this.medicineDescription = new SimpleStringProperty(medicineDescription);
        this.quantityToSell = new SimpleIntegerProperty(0);
        this.medicineEntryDate = new SimpleObjectProperty<>(date);
        this.medicineAvailability = new SimpleBooleanProperty(getMedicineAvailability());
    }

    public String getMedicineName()
    {
        return medicineName.get();
    }

    public void setMedicineName(String medicineName)
    {
        this.medicineName.set(medicineName);
    }

    public Integer getMedicineQuantity()
    {
        return medicineQuantity.get();
    }

    public void setMedicineQuantity(int quantity)
    {
        this.medicineQuantity.set(quantity);
    }

    public Double getMedicinePrice()
    {
        return medicinePrice.get();
    }

    public void setMedicinePrice(double price)
    {
        this.medicinePrice.set(price);
    }

    public String getMedicineDescription()
    {
        return medicineDescription.get();
    }

    public Date getMedicineEntryDate()
    {
        return medicineEntryDate.get();
    }

    public Boolean getMedicineAvailability()
    {
        return medicineQuantity.get() > 0;
    }

    @Override
    public int hashCode()
    {
        return 7;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final MedicineItem other = (MedicineItem) obj;
        if (!Objects.equals(this.medicineName, other.medicineName))
        {
            return false;
        }
        return Objects.equals(this.medicinePrice, other.medicinePrice);
    }

    public void setEntryDate(Date date)
    {
        this.medicineEntryDate.set(date);
    }

    public int getPreviousQuantity()
    {
        return previousQuantity;
    }

    public void setPreviousQuantity(int previousQuantity)
    {
        this.previousQuantity = previousQuantity;
    }

    public Integer getQuantityToSell()
    {
        return quantityToSell.get();
    }

    public void setQuantityToSell(int quantityToSell)
    {
        this.quantityToSell.set(quantityToSell);
    }

    static class SoldItem
    {
        private String userName;
        private String itemName;
        private double itemPrice;
        private int itemQuantity;
        private Date checkOutDate;

        public SoldItem(String userName, String itemName, double itemPrice, int itemQuantity, Date checkOutDate)
        {
            this.userName = userName;
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.itemQuantity = itemQuantity;
            this.checkOutDate = checkOutDate;
        }

        public SoldItem(String userName, MedicineItem item)
        {
            this.userName = userName;
            this.itemName = item.getMedicineName();
            this.itemPrice = item.getMedicinePrice();
            this.itemQuantity = item.getMedicineQuantity();
            this.checkOutDate = item.getMedicineEntryDate();
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public String getItemName()
        {
            return itemName;
        }

        public void setItemName(String itemName)
        {
            this.itemName = itemName;
        }

        public double getItemPrice()
        {
            return itemPrice;
        }

        public void setItemPrice(double itemPrice)
        {
            this.itemPrice = itemPrice;
        }

        public int getItemQuantity()
        {
            return itemQuantity;
        }

        public void setItemQuantity(int itemQuantity)
        {
            this.itemQuantity = itemQuantity;
        }

        public Date getCheckOutDate()
        {
            return checkOutDate;
        }

        public void setCheckOutDate(Date checkOutDate)
        {
            this.checkOutDate = checkOutDate;
        }
    }
}