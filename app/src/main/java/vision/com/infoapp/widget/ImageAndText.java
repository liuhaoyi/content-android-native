package vision.com.infoapp.widget;

import android.graphics.drawable.Drawable;

public class ImageAndText implements java.io.Serializable {
		private String id;
	    private String imageUrl;
	    private String text;
	    private String brief;
	    private String catalog;
	    private String modifyDateTime;
	    private Drawable drawable;

	    public ImageAndText(String id, String imageUrl, String text, String brief, String catalog, String modifyDateTime) {
	    	this.id = id;
	        this.imageUrl = imageUrl;
	        this.text = text;
	        this.brief = brief;
	        this.catalog = catalog;
	        this.modifyDateTime = modifyDateTime;
	    }
	    
	    public String getBrief() {
			return brief;
		}

		public void setBrief(String brief) {
			this.brief = brief;
		}

		public String getImageUrl() {
	        return imageUrl;
	    }
	    public String getText() {
	        return text;
	    }
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public void setText(String text) {
			this.text = text;
		}

		public String getCatalog() {
			return catalog;
		}

		public void setCatalog(String catalog) {
			this.catalog = catalog;
		}

		public String getModifyDateTime() {
			return modifyDateTime;
		}

		public void setModifyDateTime(String modifyDateTime) {
			this.modifyDateTime = modifyDateTime;
		}

		public Drawable getDrawable() {
			return drawable;
		}

		public void setDrawable(Drawable drawable) {
			this.drawable = drawable;
		}

}
