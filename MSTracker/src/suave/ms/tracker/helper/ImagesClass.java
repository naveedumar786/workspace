package suave.ms.tracker.helper;

import java.util.ArrayList;

public class ImagesClass {
	private int categoryId;
	private String categoryName;
	private int imageId;
	private String imageName;

	public ArrayList<ImagesClass> imagesImagesList;

	public static ArrayList<ImagesClass> imagesCategoryList = new ArrayList<ImagesClass>();

	public static boolean isImages;
	public static int parentIndex = 0;
	public static int childIndex = 0;

	/**
	 * @param categoryId
	 * @param categoryName
	 * @param imageId
	 * @param imageName
	 */

	public ImagesClass(int categoryId, String categoryName,
			ArrayList<ImagesClass> imagesList) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		imagesImagesList = new ArrayList<ImagesClass>(imagesList);

	}

	public ImagesClass(int imageId, String imageName) {
		super();
		this.imageId = imageId;
		this.imageName = imageName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public ArrayList<ImagesClass> getImagesImagesList() {
		return imagesImagesList;
	}

	public void setImagesImagesList(ArrayList<ImagesClass> imagesImagesList) {
		this.imagesImagesList = imagesImagesList;
	}
}
