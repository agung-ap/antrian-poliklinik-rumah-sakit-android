package id.developer.rs_thamrin.model.request;

import java.math.BigDecimal;

public class CustomerRegisterRequest {
    private String firstName;
    private String lastName;
    private String birthPlace;
    private String birthDate;
    private String identityCard;
    private String typeOfIdentityCard;
    private String familyName;
    private String typeFamilyName;
    private String gender;
    private String mariedStatus;
    private String education;
    private String phoneNumber;
    private String job;
    private String typeOfPayment;
    private String imageIdentityCard;
    //    address
    private String country;
    private long province;
    private long regencie;
    private long district;
    private BigDecimal village;
    private String address;
    private String typeOfAddress;

    public CustomerRegisterRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getTypeOfIdentityCard() {
        return typeOfIdentityCard;
    }

    public void setTypeOfIdentityCard(String typeOfIdentityCard) {
        this.typeOfIdentityCard = typeOfIdentityCard;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getTypeFamilyName() {
        return typeFamilyName;
    }

    public void setTypeFamilyName(String typeFamilyName) {
        this.typeFamilyName = typeFamilyName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMariedStatus() {
        return mariedStatus;
    }

    public void setMariedStatus(String mariedStatus) {
        this.mariedStatus = mariedStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(String typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }

    public String getImageIdentityCard() {
        return imageIdentityCard;
    }

    public void setImageIdentityCard(String imageIdentityCard) {
        this.imageIdentityCard = imageIdentityCard;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getProvince() {
        return province;
    }

    public void setProvince(long province) {
        this.province = province;
    }

    public long getRegencie() {
        return regencie;
    }

    public void setRegencie(long regencie) {
        this.regencie = regencie;
    }

    public long getDistrict() {
        return district;
    }

    public void setDistrict(long district) {
        this.district = district;
    }

    public BigDecimal getVillage() {
        return village;
    }

    public void setVillage(BigDecimal village) {
        this.village = village;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypeOfAddress() {
        return typeOfAddress;
    }

    public void setTypeOfAddress(String typeOfAddress) {
        this.typeOfAddress = typeOfAddress;
    }
}
