package suave.ms.tracker.helper;

import java.util.ArrayList;

public class DiagnosisClass {
	int diagnosisCateId;
	String catName;

	public ArrayList<UserDiagnosisClass> userDignosisList = new ArrayList<UserDiagnosisClass>();

	public DiagnosisClass(int diagnosisCateId, String catName,
			ArrayList<UserDiagnosisClass> userDignosisList) {
		super();
		this.diagnosisCateId = diagnosisCateId;
		this.catName = catName;
		this.userDignosisList = (ArrayList<UserDiagnosisClass>) userDignosisList
				.clone();
	}

	public int getDiagnosisCateId() {
		return diagnosisCateId;
	}

	public void setDiagnosisCateId(int diagnosisCateId) {
		this.diagnosisCateId = diagnosisCateId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public ArrayList<UserDiagnosisClass> getUserDignosisList() {
		return userDignosisList;
	}

	public void setUserDignosisList(
			ArrayList<UserDiagnosisClass> userDignosisList) {
		this.userDignosisList = userDignosisList;
	}

	public static ArrayList<DiagnosisClass> diagnosisList;
}
