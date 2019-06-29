package id.developer.rs_thamrin.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class QueueListResponse implements Parcelable {
    private int id;
    private String userId;
    private String username;
    private String paymentType;
    private String poliklinik;

    public QueueListResponse() {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPoliklinik() {
        return poliklinik;
    }

    public void setPoliklinik(String poliklinik) {
        this.poliklinik = poliklinik;
    }

    public static Creator<QueueListResponse> getCREATOR() {
        return CREATOR;
    }

    protected QueueListResponse(Parcel in) {
        id = in.readInt();
        userId = in.readString();
        username = in.readString();
        paymentType = in.readString();
        poliklinik = in.readString();
    }

    public static final Creator<QueueListResponse> CREATOR = new Creator<QueueListResponse>() {
        @Override
        public QueueListResponse createFromParcel(Parcel in) {
            return new QueueListResponse(in);
        }

        @Override
        public QueueListResponse[] newArray(int size) {
            return new QueueListResponse[size];
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
        dest.writeString(username);
        dest.writeString(paymentType);
        dest.writeString(poliklinik);
    }
}
