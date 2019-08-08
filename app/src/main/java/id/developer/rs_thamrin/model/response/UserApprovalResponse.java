package id.developer.rs_thamrin.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class UserApprovalResponse implements Parcelable {
    private String info;

    public UserApprovalResponse() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static Creator<UserApprovalResponse> getCREATOR() {
        return CREATOR;
    }

    protected UserApprovalResponse(Parcel in) {
        info = in.readString();
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
    }
}
