package suave.ms.tracker.helper;

import java.util.ArrayList;

public class InjectionTrackerClass {
	/* Table body part */
	int bodyPartId;
	String bodyPartName;

	// Table injection
	int injectionId;
	String injectedDrug;
	String injectedDate;
	public static int selectedIndex;
	public static int selectedChildIndex = -1;
	public static int Right_Arm = 0, Left_Arm = 1, Right_Abdomen = 2,
			Left_Abdomen = 3, Right_Thigh = 4, Left_Thigh = 5,
			Left_Buutock = 6, Right_Buutock = 7;

	public ArrayList<InjectionTrackerClass> injectionList;
	public static ArrayList<InjectionTrackerClass> bodyPartList = new ArrayList<InjectionTrackerClass>();

	/**
	 * @param bodyPartId
	 * @param bodyPartName
	 */
	public InjectionTrackerClass(int bodyPartId, String bodyPartName,
			ArrayList<InjectionTrackerClass> injectionList) {
		super();
		this.bodyPartId = bodyPartId;
		this.bodyPartName = bodyPartName;
		this.injectionList = new ArrayList<InjectionTrackerClass>(injectionList);

	}

	/**
	 * @param injectionId
	 * @param injectedDrug
	 * @param injectedDate
	 */

	public InjectionTrackerClass(int injectionId, String injectedDrug,
			String injectedDate) {
		super();
		this.injectionId = injectionId;
		this.injectedDrug = injectedDrug;
		this.injectedDate = injectedDate;
	}

	public int getBodyPartId() {
		return bodyPartId;
	}

	public void setBodyPartId(int bodyPartId) {
		this.bodyPartId = bodyPartId;
	}

	public String getBodyPartName() {
		return bodyPartName;
	}

	public void setBodyPartName(String bodyPartName) {
		this.bodyPartName = bodyPartName;
	}

	public int getInjectionId() {
		return injectionId;
	}

	public void setInjectionId(int injectionId) {
		this.injectionId = injectionId;
	}

	public String getInjectedDrug() {
		return injectedDrug;
	}

	public void setInjectedDrug(String injectedDrug) {
		this.injectedDrug = injectedDrug;
	}

	public String getInjectedDate() {
		return injectedDate;
	}

	public void setInjectedDate(String injectedDate) {
		this.injectedDate = injectedDate;
	}

	public ArrayList<InjectionTrackerClass> getInjectionList() {
		return injectionList;
	}

	public void setInjectionList(ArrayList<InjectionTrackerClass> injectionList) {
		this.injectionList = injectionList;
	}

	public static ArrayList<InjectionTrackerClass> getBodyPartList() {
		return bodyPartList;
	}

	public static void setBodyPartList(
			ArrayList<InjectionTrackerClass> bodyPartList) {
		InjectionTrackerClass.bodyPartList = bodyPartList;
	}

	public static void clearInjectionsList() {
		for (int i = 0; i < bodyPartList.size(); i++) {
			bodyPartList.get(i).injectionList.clear();
		}
	}
}
