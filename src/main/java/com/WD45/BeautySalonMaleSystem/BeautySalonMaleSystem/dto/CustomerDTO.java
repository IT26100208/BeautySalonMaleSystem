package com.WD45.BeautySalonMaleSystem.dto;


public class CustomerDTO extends UserDTO {

    private String address;

    private String loyaltyNumber;

    public CustomerDTO() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLoyaltyNumber() {
        return loyaltyNumber;
    }

    public void setLoyaltyNumber(String loyaltyNumber) {
        this.loyaltyNumber = loyaltyNumber;
    }
}
