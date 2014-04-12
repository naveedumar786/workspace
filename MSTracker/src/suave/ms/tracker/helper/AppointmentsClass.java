package suave.ms.tracker.helper;

import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentsClass implements Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int scheduleId;
	int scheduleCatId;
	int userId;
	boolean status;
	String doctorName;
	String visitTiming;
	String contactInfo;
	String staffName;

	public AppointmentsClass(int scheduleId, int scheduleCatId, int userId,
			boolean status, String doctorName, String visitTiming,
			String contactInfo, String staffName) {
		super();
		this.scheduleId = scheduleId;
		this.scheduleCatId = scheduleCatId;
		this.userId = userId;
		this.status = status;
		this.doctorName = doctorName;
		this.visitTiming = visitTiming;
		this.contactInfo = contactInfo;
		this.staffName = staffName;
	}

	public AppointmentsClass(Parcel in) {
		readFromParcel(in);
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public int getScheduleCatId() {
		return scheduleCatId;
	}

	public int getUserId() {
		return userId;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public String getVisitTiming() {
		return visitTiming;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public String getStaffName() {
		return staffName;
	}

	@Override
	public String toString() {
		return "AppointmentsClass [scheduleId=" + scheduleId
				+ ", scheduleCatId=" + scheduleCatId + ", userId=" + userId
				+ ", status=" + status + ", doctorName=" + doctorName
				+ ", visitTiming=" + visitTiming + ", contactInfo="
				+ contactInfo + ", staffName=" + staffName + "]";
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.doctorName);
		dest.writeString(this.contactInfo);
		dest.writeString(this.staffName);
		dest.writeInt(this.status ? 1 : 0);
		dest.writeString(this.visitTiming);
	}

	private void readFromParcel(Parcel in) {
		doctorName = in.readString();
		contactInfo = in.readString();
		staffName = in.readString();
		status = in.readInt() == 1 ? true : false;
		visitTiming = in.readString();
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public AppointmentsClass createFromParcel(Parcel in) {
			return new AppointmentsClass(in);
		}

		public AppointmentsClass[] newArray(int size) {
			return new AppointmentsClass[size];
		}
	};

}
