
import java.time.LocalDate;

public class DeliveryDocket {

    // ===== Fields =====
    private int docketId;
    private LocalDate docketDate;
    private int deliveryAreaId;
    private int deliveryPersonId;

    // ===== Constructors =====
    public DeliveryDocket() {
    }

    public DeliveryDocket(LocalDate docketDate, int deliveryAreaId, int deliveryPersonId) {
        this.docketDate = docketDate;
        this.deliveryAreaId = deliveryAreaId;
        this.deliveryPersonId = deliveryPersonId;
    }

    // ===== Validation =====
    public boolean verifyDocketDate(LocalDate docketDate) {
        return docketDate != null;
    }

    public boolean verifyDeliveryAreaId(int deliveryAreaId) {
        return deliveryAreaId >= 1 && deliveryAreaId <= 24;
    }
    

    // ===== Create Delivery Docket =====

    public boolean createDeliveryDocket(LocalDate docketDate, int deliveryAreaId, int deliveryPersonId) {

        if (verifyDocketDate(docketDate) && verifyDeliveryAreaId(deliveryAreaId) && deliveryPersonId > 0) {
            this.docketDate = docketDate;
            this.deliveryAreaId = deliveryAreaId;
            this.deliveryPersonId = deliveryPersonId;
            return true;
        }

        return false;
    }

	public int getDocketId() {
		return docketId;
	}

	public void setDocketId(int docketId) {
		this.docketId = docketId;
	}

	public LocalDate getDocketDate() {
		return docketDate;
	}

	public void setDocketDate(LocalDate docketDate) {
		this.docketDate = docketDate;
	}

	public int getDeliveryAreaId() {
		return deliveryAreaId;
	}

	public void setDeliveryAreaId(int deliveryAreaId) {
		this.deliveryAreaId = deliveryAreaId;
	}

	public int getDeliveryPersonId() {
		return deliveryPersonId;
	}

	public void setDeliveryPersonId(int deliveryPersonId) {
		this.deliveryPersonId = deliveryPersonId;
	}
	

    // ===== Display Docket Details =====

    @Override
    public String toString() {
        return "Docket ID: " + docketId
                + "\nDocket Date: " + docketDate
                + "\nDelivery Area ID: " + deliveryAreaId
                + "\nDelivery Person ID: " + deliveryPersonId;
    }



}
