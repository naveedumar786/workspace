package suave.ms.tracker.helper;

import java.util.ArrayList;

/********************* class ScheduleCategory start *****************************/
// Template class for schedule categories
public class ScheduleCategoryClass {
	private String title;
	private int id;
	ArrayList<ScheduleCategoryClass> ScheduleList;

	public ScheduleCategoryClass(String title, int id) {
		super();
		this.title = title;
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public int getId() {
		return id;
	}

}

/******************** class ScheduleCategory end *******************************/
