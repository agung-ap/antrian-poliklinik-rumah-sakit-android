package id.developer.rs_thamrin.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class UserApprovalResponse implements Parcelable {
    private String info;
    private String name;
    private String userId;
    private String userStatus;

    public UserApprovalResponse() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public static Creator<UserApprovalResponse> getCREATOR() {
        return CREATOR;
    }

    protected UserApprovalResponse(Parcel in) {
        info = in.readString();
        name = in.readString();
        userId = in.readString();
        userStatus = in.readString();
    }

    public static final Creator<UserApprovalResponse> CREATOR = new Creator<UserApprovalResponse>() {
        @Override
        public UserApprovalResponse createFromParcel(Parcel in) {
            return new UserApprovalResponse(in);
        }

        @Override
        public UserApprovalResponse[] newArray(int size) {
            return new UserApprovalResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(info);
        dest.writeString(name);
        dest.writeString(userId);
        dest.writeString(userStatus);
    }
}
