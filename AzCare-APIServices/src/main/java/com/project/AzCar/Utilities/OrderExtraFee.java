package com.project.AzCar.Utilities;

public class OrderExtraFee {
    private float deliveryFee;
    private float cleanFee;
    private float smellFee;
    private float insurance;

    public OrderExtraFee() {
        // Constructor mặc định
    }

    public OrderExtraFee(float deliveryFee, float cleanFee, float smellFee) {
        this.deliveryFee = deliveryFee;
        this.cleanFee = cleanFee;
        this.smellFee = smellFee;
        this.insurance = 200;
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

	public float getInsurance() {
		return insurance;
	}

	public void setInsurance(float insurance) {
		this.insurance = insurance;
	}
    
    
}
