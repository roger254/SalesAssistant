package pharmaceuticals.assistant.database;

import javafx.beans.property.*;

import java.util.Date;
import java.util.Objects;

public class MedicineItem
{

    private transient SimpleStringProperty medicineName;
    private transient SimpleIntegerProperty medicineQuantity;
    private transient SimpleDoubleProperty medicinePrice;
    private transient SimpleStringProperty medicineDescription;
    private transient ObjectProperty<Date> medicineEntryDate;
    private transient SimpleBooleanProperty medicineAvailability;
    private transient SimpleIntegerProperty quantityToSell;
    private int previousQuantity;

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

    void addToSold()
    {
        MedicineHandler.getCheckOutList().add(soldItemClone());
        this.previousQuantity = 0;
        this.quantityToSell.set(0);
    }

   MedicineItem addToCheckOut(int quantityToSell)
    {
        this.quantityToSell.set(quantityToSell);
        previousQuantity = this.medicineQuantity.get();
        int newQuantity = this.previousQuantity - this.quantityToSell.get();
        setMedicineQuantity(newQuantity);
        return checkOutClone();
    }

    private MedicineItem checkOutClone()
    {
        return new MedicineItem(medicineName.get(), medicineQuantity.get(), medicinePrice.get(), medicineDescription.get(), quantityToSell.get());
    }

    private MedicineItem soldItemClone()
    {
        return new MedicineItem(this.medicineName.get(),this.quantityToSell.get(),this.medicinePrice.get());
    }

    boolean checkOutEquals(MedicineItem object)
    {
        if (!this.medicineName.get().equals(object.medicineName.get()))
            return false;
        if (this.medicinePrice.get() != object.medicinePrice.get())
            return false;
        return this.medicineDescription.get().equals(object.medicineDescription.get());
    }

}
