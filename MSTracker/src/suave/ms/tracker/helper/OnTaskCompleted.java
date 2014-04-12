package suave.ms.tracker.helper;

import org.json.JSONObject;

public interface OnTaskCompleted {

	public void onSuccessWebservice(JSONObject json, String action);

	void onFailureWebservice(boolean satus);
}
