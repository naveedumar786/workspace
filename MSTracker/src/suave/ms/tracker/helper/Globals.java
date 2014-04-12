package suave.ms.tracker.helper;

import android.provider.SyncStateContract.Constants;

public class Globals {

	// Back Button when pressed
	public static int backButtonId = 0;

	public static String tabedTextColor = "#007eff";
	public static String simpleTextColor = "#121212";
	// Treatment activity IDs
	public static final short AC_TREATMENT = 30;
	public static final short AC_TREATMENT_MEDICATION = 31;
	public static final short AC_TREATMENT_MEDICATION_OPTIONS = 32;

	// Diagonse activity IDs
	public static final short AC_DIAGNOSIS = 20;

	public static int PASSWORD_LENGTH = 6;
	// Initialized on images web service response
	public static String IMAGES_PATH = "http://mslogs.com/ios/mstracker/images/";

	public static String UrlForImagesUpload = "http://mslogs.com/ios/mstracker/upload.php";

	public static int getPASSWORD_LENGTH() {
		return PASSWORD_LENGTH;
	}

}
