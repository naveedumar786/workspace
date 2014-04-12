package suave.ms.tracker.helper;

public class UserDiagnosisClass {
	int diagnosisId, diagnosisCatId, userId;
	String diagnosisDate, diagnosisPlace, doctorName;
	boolean labImageDone, status;

	public UserDiagnosisClass(int diagnosisId, int diagnosisCatId, int userId,
			String diagnosisDate, String diagnosisPlace, String doctorName,
			boolean labImageDone, boolean status) {
		super();
		this.diagnosisId = diagnosisId;
		this.diagnosisCatId = diagnosisCatId;
		this.userId = userId;
		this.diagnosisDate = diagnosisDate;
		this.diagnosisPlace = diagnosisPlace;
		this.doctorName = doctorName;
		this.labImageDone = labImageDone;
		this.status = status;
	}

	public int getDiagnosisCatId() {
		return diagnosisCatId;
	}

	public void setDiagnosisCatId(int diagnosisCatId) {
		this.diagnosisCatId = diagnosisCatId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDiagnosisDate() {
		return diagnosisDate;
	}

	public void setDiagnosisDate(String diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	public String getDiagnosisPlace() {
		return diagnosisPlace;
	}

	public void setDiagnosisPlace(String diagnosisPlace) {
		this.diagnosisPlace = diagnosisPlace;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public boolean isLabImageDone() {
		return labImageDone;
	}

	public void setLabImageDone(boolean labImageDone) {
		this.labImageDone = labImageDone;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(int diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

}
