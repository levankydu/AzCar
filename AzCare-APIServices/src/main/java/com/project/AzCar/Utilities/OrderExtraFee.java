package com.project.AzCar.Utilities;

public class OrderExtraFee {
    private float deliveryFee;
    private float cleanFee;
    private float smellFee;

    public OrderExtraFee() {
        // Constructor mặc định
    }

    public OrderExtraFee(float deliveryFee, float cleanFee, float smellFee) {
        this.deliveryFee = deliveryFee;
        this.cleanFee = cleanFee;
        this.smellFee = smellFee;
    }

    public float getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(float deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public float getCleanFee() {
        return cleanFee;
    }

    public void setCleanFee(float cleanFee) {
        this.cleanFee = cleanFee;
    }

    public float getSmellFee() {
        return smellFee;
    }

    public void setSmellFee(float smellFee) {
        this.smellFee = smellFee;
    }
}
