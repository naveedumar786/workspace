package suave.ms.tracker.helper;

public class MedicationClass {
	int medicationId, userId;
	String medicationName, dosage, indication, reminder, start_date, end_date,
			treatment_type, moreInfo;
	boolean status;
	int treatmentId;

	public MedicationClass(int medicationId, int userId, String medicationName,
			String dosage, String indication, String reminder,
			String start_date, String end_date, String treatment_type,
			String moreInfo, int treatmentId) {
		super();
		this.medicationId = medicationId;
		this.userId = userId;
		this.medicationName = medicationName;
		this.dosage = dosage;
		this.indication = indication;
		this.reminder = reminder;
		this.start_date = start_date;
		this.end_date = end_date;
		this.treatment_type = treatment_type;
		this.moreInfo = moreInfo;
		this.treatmentId = treatmentId;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getMedicationId() {
		return medicationId;
	}

	public void setMedicationId(int medicationId) {
		this.medicationId = medicationId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMedicationName() {
		return medicationName;
	}

	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}

	public String getDosage() {
		return dosage;
	}

	public void setDosage(String dosage) {
		this.dosage = dosage;
	}

	public String getIndication() {
		return indication;
	}

	public void setIndication(String indication) {
		this.indication = indication;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getTreatment_type() {
		return treatment_type;
	}

	public void setTreatment_type(String treatment_type) {
		this.treatment_type = treatment_type;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	public int getTreatmentId() {
		return treatmentId;
	}

	public void setTreatmentId(int treatmentId) {
		this.treatmentId = treatmentId;
	}

}
