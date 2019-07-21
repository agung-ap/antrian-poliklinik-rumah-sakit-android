package id.developer.rs_thamrin.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class AdminData implements Parcelable {
    private long id;
    private String userId;
    private String name;
    private String firstName;
    private String lastName;
    private String userRole;
    private String userStatus;
    private String birthPlace;
    private String birthDate;
    private String address;

    public AdminData() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Creator<AdminData> getCREATOR() {
        return CREATOR;
    }

    protected AdminData(Parcel in) {
        id = in.readLong();
        userId = in.readString();
        name = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        userRole = in.readString();
        userStatus = in.readString();
        birthPlace = in.readString();
        birthDate = in.readString();
        address = in.readString();
    }

    public static final Creator<AdminData> CREATOR = new Creator<AdminData>() {
        @Override
        public AdminData createFromParcel(Parcel in) {
            return new AdminData(in);
        }

        @Override
        public AdminData[] newArray(int size) {
            return new AdminData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(userRole);
        dest.writeString(userStatus);
        dest.writeString(birthPlace);
        dest.writeString(birthDate);
        dest.writeString(address);
    }
}
