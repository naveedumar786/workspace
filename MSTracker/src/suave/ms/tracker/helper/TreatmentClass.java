package suave.ms.tracker.helper;

import java.util.ArrayList;

public class TreatmentClass {
	int treatmentID;
	String treatmentName;
	public ArrayList<MedicationClass> medicationList = new ArrayList<MedicationClass>();
	public static ArrayList<TreatmentClass> treatmetnList;

	@SuppressWarnings("unchecked")
	public TreatmentClass(int treatmentID, String treatmentName,
			ArrayList<MedicationClass> medicationList) {
		super();
		this.treatmentID = treatmentID;
		this.treatmentName = treatmentName;
		this.medicationList = (ArrayList<MedicationClass>) medicationList
				.clone();
	}

	public int getTreatmentID() {
		return treatmentID;
	}

	public void setTreatmentID(int treatmentID) {
		this.treatmentID = treatmentID;
	}

	public String getTreatmentName() {
		return treatmentName;
	}

	public void setTreatmentName(String treatmentName) {
		this.treatmentName = treatmentName;
	}

	public ArrayList<MedicationClass> getMedicationList() {
		return medicationList;
	}

	public void setMedicationList(ArrayList<MedicationClass> medicationList) {
		this.medicationList = medicationList;
	}
}
