package cz.jiripinkas.jsitemapgenerator;

/**
 *
 * Builder for easier WebPage construction.
 * @deprecated use {@link WebPage.ImageBuilder instead}
 */
@Deprecated
public class ImageBuilder {

	private Image image = new Image();

	public ImageBuilder caption(String caption) {
		image.setCaption(caption);
		return this;
	}

	public ImageBuilder geoLocation(String geoLoation) {
		image.setGeoLocation(geoLoation);
		return this;
	}

	public ImageBuilder license(String license) {
		image.setLicense(license);
		return this;
	}

	public ImageBuilder loc(String loc) {
		image.setLoc(loc);
		return this;
	}

	public ImageBuilder title(String title) {
		image.setTitle(title);
		return this;
	}

	public Image build() {
		return image;
	}

}
