package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Patient {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    @SerializedName("lattitude")
    @Expose
    private String lattitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("queueToken")
    @Expose
    private int queueToken;

    @SerializedName("doctorsPhoneNumber")
    @Expose
    private String doctorsPhoneNumber;

    @SerializedName("slot")
    @Expose
    private String slot;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getQueueToken() {
        return queueToken;
    }

    public void setQueueToken(int queueToken) {
        this.queueToken = queueToken;
    }

    public String getDoctorsPhoneNumber() {
        return doctorsPhoneNumber;
    }

    public void setDoctorsPhoneNumber(String doctorsPhoneNumber) {
        this.doctorsPhoneNumber = doctorsPhoneNumber;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
}
