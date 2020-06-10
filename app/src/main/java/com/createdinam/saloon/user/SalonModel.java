package com.createdinam.saloon.user;

public class SalonModel {

    private String salonId;
    private String salonUniqueId;
    private String salonName;
    private String image;
    private String discount;
    private String image_popup;

    public String getImage_popup() {
        return image_popup;
    }

    public void setImage_popup(String image_popup) {
        this.image_popup = image_popup;
    }

    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public String getSalonUniqueId() {
        return salonUniqueId;
    }

    public void setSalonUniqueId(String salonUniqueId) {
        this.salonUniqueId = salonUniqueId;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
