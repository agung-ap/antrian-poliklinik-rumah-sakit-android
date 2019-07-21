package id.developer.rs_thamrin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DoctorData implements Parcelable {
    private int id;
    private String userId;
    private String name;
    private String firsName;
    private String lastName;
    private String specialization;
    private String specializationCode;
    private int specializationId;
    private String address;

    public DoctorData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirsName() {
        return firsName;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getSpecializationCode() {
        return specializationCode;
    }

    public void setSpecializationCode(String specializationCode) {
        this.specializationCode = specializationCode;
    }

    public int getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(int specializationId) {
        this.specializationId = specializationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Creator<DoctorData> getCREATOR() {
        return CREATOR;
    }

    protected DoctorData(Parcel in) {
        id = in.readInt();
        userId = in.readString();
        name = in.readString();
        firsName = in.readString();
        lastName = in.readString();
        specialization = in.readString();
        specializationCode = in.readString();
        specializationId = in.readInt();
        address = in.readString();
    }

    public static final Creator<DoctorData> CREATOR = new Creator<DoctorData>() {
        @Override
        public DoctorData createFromParcel(Parcel in) {
            return new DoctorData(in);
        }

        @Override
        public DoctorData[] newArray(int size) {
            return new DoctorData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(firsName);
        dest.writeString(lastName);
        dest.writeString(specialization);
        dest.writeString(specializationCode);
        dest.writeInt(specializationId);
        dest.writeString(address);
    }
}
