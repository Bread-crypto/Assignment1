public class DeliveryArea {

    // ===== Fields =====
    private int deliveryAreaId;
    private String areaName;
    private int deliveryPersonId;


    // ===== Constructors =====

    public DeliveryArea() {
    }

    public DeliveryArea(String areaName) {
        this.areaName = areaName;
    }


    // ===== Validation =====

    public boolean verifyAreaName(String areaName) {

        return areaName != null
                && !areaName.trim().isEmpty()
                && areaName.length() <= 30;
    }


    public boolean verifyDeliveryAreaId(int deliveryAreaId) {

        return deliveryAreaId >= 1
                && deliveryAreaId <= 24;
    }


    public boolean createDeliveryArea(String areaName) {

        if (verifyAreaName(areaName)) {

            this.areaName = areaName;

            return true;
        }

        return false;
    }


    // ===== Getters and Setters =====

    public int getDeliveryAreaId() {
        return deliveryAreaId;
    }

    public void setDeliveryAreaId(int deliveryAreaId) {
        this.deliveryAreaId = deliveryAreaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(int deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }


    @Override
    public String toString() {

        return "Delivery Area ID: " + deliveryAreaId
                + "\nArea Name: " + areaName
                + "\nDelivery Person ID: " + deliveryPersonId;
    }
}