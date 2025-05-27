package com.example.kayakquest.Operations;

import java.util.HashMap;
import java.util.Map;

public class FloatPlan
{
    private String kayakerName;
    private String gender;
    private String phoneNumber;
    private int age;
    private String address;
    private String city;
    private String state;
    private String emergencyContact;
    private String emergencyPhone;
    private String kayakMake;
    private String kayakModel;
    private String kayakLength;
    private String kayakColor;
    private String safetyEquipmentNotes;
    private String vehicleMake;
    private String vehicleColor;
    private String vehicleModel;
    private String plateNumber;
    private String departureDate;
    private String departureTime;
    private String putInLocation;
    private String takeOutLocation;
    private String returnTime;
    private String tripNotes;
    private String userId;
    private String pdfUrl;

    public FloatPlan()
    {
        this.kayakerName = "";
        this.gender = "";
        this.phoneNumber = "";
        this.age = 0;
        this.address = "";
        this.city = "";
        this.state = "";
        this.emergencyContact = "";
        this.emergencyPhone = "";
        this.kayakMake = "";
        this.kayakModel = "";
        this.kayakLength = "";
        this.kayakColor = "";
        this.safetyEquipmentNotes = "";
        this.vehicleMake = "";
        this.vehicleColor = "";
        this.vehicleModel = "";
        this.plateNumber = "";
        this.departureDate = "";
        this.departureTime = "";
        this.putInLocation = "";
        this.takeOutLocation = "";
        this.returnTime = "";
        this.tripNotes = "";
        this.userId = "";
        this.pdfUrl = null;
    }

    public FloatPlan(String kayakerName, String gender, String phoneNumber, int age, String address,
                     String city, String state, String emergencyContact, String emergencyPhone,
                     String kayakMake, String kayakModel, String kayakLength, String kayakColor,
                     String safetyEquipmentNotes, String vehicleMake, String vehicleColor, String vehicleModel,
                     String plateNumber, String departureDate, String departureTime, String putInLocation,
                     String takeOutLocation, String returnTime, String tripNotes, String userId,
                     String pdfUrl)
    {
        this.kayakerName = kayakerName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address;
        this.city = city;
        this.state = state;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
        this.kayakMake = kayakMake;
        this.kayakModel = kayakModel;
        this.kayakLength = kayakLength;
        this.kayakColor = kayakColor;
        this.safetyEquipmentNotes = safetyEquipmentNotes;
        this.vehicleMake = vehicleMake;
        this.vehicleColor = vehicleColor;
        this.vehicleModel = vehicleModel;
        this.plateNumber = plateNumber;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.putInLocation = putInLocation;
        this.takeOutLocation = takeOutLocation;
        this.returnTime = returnTime;
        this.tripNotes = tripNotes;
        this.userId = userId;
        this.pdfUrl = pdfUrl;
    }

    // Getters and setters
    public String getKayakerName() { return kayakerName; }
    public void setKayakerName(String kayakerName) { this.kayakerName = kayakerName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }
    public String getKayakMake() { return kayakMake; }
    public void setKayakMake(String kayakMake) { this.kayakMake = kayakMake; }
    public String getKayakModel() { return kayakModel; }
    public void setKayakModel(String kayakModel) { this.kayakModel = kayakModel; }
    public String getKayakLength() { return kayakLength; }
    public void setKayakLength(String kayakLength) { this.kayakLength = kayakLength; }
    public String getKayakColor() { return kayakColor; }
    public void setKayakColor(String kayakColor) { this.kayakColor = kayakColor; }
    public String getSafetyEquipmentNotes() { return safetyEquipmentNotes; }
    public void setSafetyEquipmentNotes(String safetyEquipmentNotes) { this.safetyEquipmentNotes = safetyEquipmentNotes; }
    public String getVehicleMake() { return vehicleMake; }
    public void setVehicleMake(String vehicleMake) { this.vehicleMake = vehicleMake; }
    public String getVehicleColor() { return vehicleColor; }
    public void setVehicleColor(String vehicleColor) { this.vehicleColor = vehicleColor; }
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
    public String getDepartureDate() { return departureDate; }
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getPutInLocation() { return putInLocation; }
    public void setPutInLocation(String putInLocation) { this.putInLocation = putInLocation; }
    public String getTakeOutLocation() { return takeOutLocation; }
    public void setTakeOutLocation(String takeOutLocation) { this.takeOutLocation = takeOutLocation; }
    public String getReturnTime() { return returnTime; }
    public void setReturnTime(String returnTime) { this.returnTime = returnTime; }
    public String getTripNotes() { return tripNotes; }
    public void setTripNotes(String tripNotes) { this.tripNotes = tripNotes; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("kayakerName", kayakerName);
        map.put("gender", gender);
        map.put("phoneNumber", phoneNumber);
        map.put("age", age);
        map.put("address", address);
        map.put("city", city);
        map.put("state", state);
        map.put("emergencyContact", emergencyContact);
        map.put("emergencyPhone", emergencyPhone);
        map.put("kayakMake", kayakMake);
        map.put("kayakModel", kayakModel);
        map.put("kayakLength", kayakLength);
        map.put("kayakColor", kayakColor);
        map.put("vehicleModel", vehicleModel);
        map.put("plateNumber", plateNumber);
        map.put("safetyEquipmentNotes", safetyEquipmentNotes);
        map.put("vehicleMake", vehicleMake);
        map.put("vehicleColor", vehicleColor);
        map.put("departureDate", departureDate);
        map.put("departureTime", departureTime);
        map.put("putInLocation", putInLocation);
        map.put("takeOutLocation", takeOutLocation);
        map.put("returnTime", returnTime);
        map.put("tripNotes", tripNotes);
        map.put("userId", userId);
        map.put("pdfUrl", pdfUrl);
        return map;
    }
}