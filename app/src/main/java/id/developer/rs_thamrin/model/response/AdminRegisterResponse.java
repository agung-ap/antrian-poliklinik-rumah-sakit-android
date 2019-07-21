package id.developer.rs_thamrin.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class AdminRegisterResponse implements Parcelable {
    private String info;
    private String createdDate;
    private String name;
    private String adminId;
    private String password;

    public AdminRegisterResponse() {
    }

    protected AdminRegisterResponse(Parcel in) {
        info = in.readString();
        createdDate = in.readString();
        name = in.readString();
        adminId = in.readString();
        password = in.readString();
    }

    public static final Creator<AdminRegisterResponse> CREATOR = new Creator<AdminRegisterResponse>() {
        @Override
        public AdminRegisterResponse createFromParcel(Parcel in) {
            return new AdminRegisterResponse(in);
        }

        @Override
        public AdminRegisterResponse[] newArray(int size) {
            return new AdminRegisterResponse[size];
        }
    };

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
        dest.writeString(createdDate);
        dest.writeString(name);
        dest.writeString(adminId);
        dest.writeString(password);
    }
}
