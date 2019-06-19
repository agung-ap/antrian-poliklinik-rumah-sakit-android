package id.developer.rs_thamrin.model.response;

import android.os.Parcel;
import android.os.Parcelable;

public class DoctorDataResponse implements Parcelable {
    private String message;
    private String createdDate;
    private String dokterId;
    private String password;
    private String name;
    private String specialization;

    public DoctorDataResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDokterId() {
        return dokterId;
    }

    public void setDokterId(String dokterId) {
        this.dokterId = dokterId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public static Creator<DoctorDataResponse> getCREATOR() {
        return CREATOR;
    }

    protected DoctorDataResponse(Parcel in) {
        message = in.readString();
        createdDate = in.readString();
        dokterId = in.readString();
        password = in.readString();
        name = in.readString();
        specialization = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(createdDate);
        dest.writeString(dokterId);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(specialization);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DoctorDataResponse> CREATOR = new Creator<DoctorDataResponse>() {
        @Override
        public DoctorDataResponse createFromParcel(Parcel in) {
            return new DoctorDataResponse(in);
        }

        @Override
        public DoctorDataResponse[] newArray(int size) {
            return new DoctorDataResponse[size];
        }
    };
}
