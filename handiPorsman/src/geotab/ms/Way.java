package geotab.ms;

import java.util.ArrayList;
import java.util.List;
import org.mapsforge.core.Tag;

import android.util.Log;

public class Way {
	private float[] labelPosition;
	private byte Layer;
	private List<Tag> tags = new  ArrayList<Tag>();
	private float[][] wayNodes;

	public byte getLayer() {
		return Layer;
	}

	public void setLayer(byte layer) {
		Layer = layer;
	}

	public float[] getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(float[] labelPosition) {
		this.labelPosition = labelPosition;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public float[][] getWayNodes() {
		return wayNodes;
	}

	public void setWayNodes(float[][] wayNodes) {
		this.wayNodes = wayNodes;
	}

	public Way() {
	}

	public Way(float[] _labelPosition, byte _layer, float[][] _wayNodes) { //List<Tag> _tags,
		this.labelPosition = _labelPosition;
		this.Layer = _layer;
		this.tags = new ArrayList<Tag>();
		this.wayNodes = _wayNodes;
		
	}
}
