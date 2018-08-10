package pharmaceuticals.assistant.database;

import javafx.beans.property.*;

import java.io.*;
import java.util.Date;
import java.util.Objects;

public class MedicineItem implements Serializable
{

    private transient SimpleStringProperty medicineName;
    private transient SimpleIntegerProperty medicineQuantity;
    private transient SimpleDoubleProperty medicinePrice;
    private transient SimpleStringProperty medicineDescription;
    private transient ObjectProperty<Date> medicineEntryDate;
    private transient SimpleBooleanProperty medicineAvailability;
    private transient SimpleIntegerProperty quantityToSell;
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

    public MedicineItem(String medicineName, int medicineQuantity, double medicinePrice, Date date)
    {
        this.medicineName = new SimpleStringProperty(medicineName);
        this.medicineQuantity = new SimpleIntegerProperty(medicineQuantity);
        this.medicinePrice = new SimpleDoubleProperty(medicinePrice);
        this.medicineDescription = new SimpleStringProperty("");
        this.quantityToSell = new SimpleIntegerProperty(0);
        this.medicineEntryDate = new SimpleObjectProperty<>(date);
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

    public final String getMedicineName()
    {
        return medicineName.get();
    }

    public final void setMedicineName(String medicineName)
    {
        this.medicineName.set(medicineName);
    }

    public final Integer getMedicineQuantity()
    {
        return medicineQuantity.get();
    }

    public final void setMedicineQuantity(int quantity)
    {
        this.medicineQuantity.set(quantity);
    }

    public final Double getMedicinePrice()
    {
        return medicinePrice.get();
    }

    public final void setMedicinePrice(double price)
    {
        this.medicinePrice.set(price);
    }

    public final String getMedicineDescription()
    {
        return medicineDescription.get();
    }

    public final Date getMedicineEntryDate()
    {
        return medicineEntryDate.get();
    }

    public final Boolean getMedicineAvailability()
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

    public final void setEntryDate(Date date)
    {
        this.medicineEntryDate.set(date);
    }

    public final int getPreviousQuantity()
    {
        return previousQuantity;
    }

    public final void setPreviousQuantity(int previousQuantity)
    {
        this.previousQuantity = previousQuantity;
    }

    public final Integer getQuantityToSell()
    {
        return quantityToSell.get();
    }

    public final void setQuantityToSell(int quantityToSell)
    {
        this.quantityToSell.set(quantityToSell);
    }

    static class SoldItem extends MedicineItem
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

        public void switchQuantities(boolean isSold)
        {
            if (isSold)
            {

            }
        }
    }

   public MedicineItem addToCheckOut(int quantityToSell)
    {
        this.quantityToSell.set(quantityToSell);
        int previousQuantity = this.medicineQuantity.get();
        int newQuantity = previousQuantity - this.quantityToSell.get();
        setMedicineQuantity(newQuantity);
        return checkOutClone(this);
    }

    void confirmSell(boolean confirm)
    {
        if (confirm)
        {
            this.previousQuantity = 0;
            this.quantityToSell.set(0);
        }
        else
        {
            this.medicineQuantity.set(previousQuantity);
            this.previousQuantity = 0;
            this.quantityToSell.set(0);
        }
    }

    private  MedicineItem checkOutClone(MedicineItem object)
    {
        return new MedicineItem(medicineName.get(),medicineQuantity.get(),medicinePrice.get(),medicineDescription.get(),quantityToSell.get());
    }

    private void writeObject(ObjectOutputStream o) throws IOException
    {
        /**
         * private transient SimpleStringProperty medicineName;
         *     private transient SimpleIntegerProperty medicineQuantity;
         *     private transient SimpleDoubleProperty medicinePrice;
         *     private transient SimpleStringProperty medicineDescription;
         *     private transient ObjectProperty<Date> medicineEntryDate;
         *     private transient SimpleBooleanProperty medicineAvailability;
         *     private transient SimpleIntegerProperty quantityToSell;
         *     private int previousQuantity = 0;
         */
        o.defaultWriteObject();
        o.writeUTF(getMedicineName());
        o.writeInt(getMedicineQuantity());
        o.writeDouble(getMedicinePrice());
        o.writeUTF(getMedicineDescription());
        o.writeObject(getMedicineEntryDate());
        o.writeBoolean(getMedicineAvailability());
        o.writeInt(getQuantityToSell());
    }

    private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException
    {
        /*
        rivate transient SimpleStringProperty medicineName;
    private transient SimpleIntegerProperty medicineQuantity;
    private transient SimpleDoubleProperty medicinePrice;
    private transient SimpleStringProperty medicineDescription;
    private transient ObjectProperty<Date> medicineEntryDate;
    private transient SimpleBooleanProperty medicineAvailability;
    private transient SimpleIntegerProperty quantityToSell;
    private int previousQuantity = 0;
         */
        medicineName = new SimpleStringProperty(i.readUTF());
        medicineQuantity = new SimpleIntegerProperty(i.readInt());
        medicinePrice = new SimpleDoubleProperty(i.readDouble());
        medicineDescription = new SimpleStringProperty(i.readUTF());
        medicineEntryDate = new SimpleObjectProperty<>((Date) i.readObject());
        medicineAvailability = new SimpleBooleanProperty(i.readBoolean());
        quantityToSell = new SimpleIntegerProperty(i.readInt());
    }


}
