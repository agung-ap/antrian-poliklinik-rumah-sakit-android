package id.developer.rs_thamrin.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerRegisterResponse implements Parcelable {
    private String info;
    private String RmId;
    private String password;
    private String createdDate;
    private String name;

    public CustomerRegisterResponse() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRmId() {
        return RmId;
    }

    public void setRmId(String rmId) {
        RmId = rmId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public static Creator<CustomerRegisterResponse> getCREATOR() {
        return CREATOR;
    }

    protected CustomerRegisterResponse(Parcel in) {
        info = in.readString();
        RmId = in.readString();
        password = in.readString();
        createdDate = in.readString();
        name = in.readString();
    }

    public static final Creator<CustomerRegisterResponse> CREATOR = new Creator<CustomerRegisterResponse>() {
        @Override
        public CustomerRegisterResponse createFromParcel(Parcel in) {
            return new CustomerRegisterResponse(in);
        }

        @Override
        public CustomerRegisterResponse[] newArray(int size) {
            return new CustomerRegisterResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
        dest.writeString(RmId);
        dest.writeString(password);
        dest.writeString(createdDate);
        dest.writeString(name);
    }
}
