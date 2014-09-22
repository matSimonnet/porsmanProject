package porsman.ms;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.core.Tag;

public class PointOfInterest {

	private double Latitude;
	private double Longitude;
	private List<Tag> tags;

	public PointOfInterest() {
	}

	public PointOfInterest(double latitude, double longitude) {
		this.Latitude = latitude;
		this.Longitude = longitude;
		this.tags = new ArrayList<Tag>();
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public void addTag(String key, String value)
	{
		tags.add(new Tag(key,value));	
	}
}
