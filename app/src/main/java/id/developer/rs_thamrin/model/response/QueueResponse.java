package id.developer.rs_thamrin.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class QueueResponse implements Parcelable {
    private String message;
    private String poliklinikName;
    private String doctorName;
    private String customerName;
    private String schedule;
    private String rangeTime;
    private String queueCode;

    public QueueResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPoliklinikName() {
        return poliklinikName;
    }

    public void setPoliklinikName(String poliklinikName) {
        this.poliklinikName = poliklinikName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRangeTime() {
        return rangeTime;
    }

    public void setRangeTime(String rangeTime) {
        this.rangeTime = rangeTime;
    }

    public String getQueueCode() {
        return queueCode;
    }

    public void setQueueCode(String queueCode) {
        this.queueCode = queueCode;
    }

    public static Creator<QueueResponse> getCREATOR() {
        return CREATOR;
    }

    protected QueueResponse(Parcel in) {
        message = in.readString();
        poliklinikName = in.readString();
        doctorName = in.readString();
        customerName = in.readString();
        schedule = in.readString();
        rangeTime = in.readString();
        queueCode = in.readString();
    }

    public static final Creator<QueueResponse> CREATOR = new Creator<QueueResponse>() {
        @Override
        public QueueResponse createFromParcel(Parcel in) {
            return new QueueResponse(in);
        }

        @Override
        public QueueResponse[] newArray(int size) {
            return new QueueResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(poliklinikName);
        dest.writeString(doctorName);
        dest.writeString(customerName);
        dest.writeString(schedule);
        dest.writeString(rangeTime);
        dest.writeString(queueCode);
    }
}
