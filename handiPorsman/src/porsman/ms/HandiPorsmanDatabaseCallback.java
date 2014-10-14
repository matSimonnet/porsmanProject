package porsman.ms;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.core.Tag;
import org.mapsforge.map.reader.MapDatabaseCallback;

import android.util.Log;

public class HandiPorsmanDatabaseCallback implements MapDatabaseCallback {
	public List<PointOfInterest> Pois = new ArrayList<PointOfInterest>();
	public boolean isLanduse;
	public boolean isForest;
	public boolean isBeach;
	public boolean isWater;
	boolean containsR = false;
	boolean containsA = false;
	public String nameLanduse;
	public String nameBeach;
	public String nameWater;
	
	public String getNameBeach() {
		return nameBeach;
	}



	float[][] polygon;
	boolean inLanduse;
	boolean inBeach;
	boolean inWater;
	final List<Way> HighWays = new ArrayList<Way>();
	public Way theway;
	public byte zoomLevel;
	private HandiPorsmanMapView mapView;
	public List<Boolean> listIn = new ArrayList<Boolean>();
	public boolean inCallback = false;

	public HandiPorsmanDatabaseCallback(HandiPorsmanMapView m) {
		this.mapView = m;
	}

	@Override
	public void renderPointOfInterest(byte layer, int latitude, int longitude,List<Tag> tags) {
		//Log.i("pois", "key" + tags.toString());
		//Log.i("POI",""+latitude+" / " + longitude);
		PointOfInterest poi = new PointOfInterest(latitude,longitude);
		
		for (int i = 0; i < tags.size(); i++)
		{
			poi.addTag(tags.get(i).key, tags.get(i).value);
		}
		
		Pois.add(poi); 
	}

	@Override
	public void renderWaterBackground() {
		Log.i("message","water");
	}
	
	
	
	/**
	 * Converts a longitude value into an X coordinate on the current tile.
	 * 
	 * @param longitude
	 *            the longitude value.
	 * @return the X coordinate on the current tile.
	 */
	@Override
	public void renderWay(byte layer, float[] labelPosition, List<Tag> tags, float[][] wayNodes) {
		inCallback = true;
		
		//for (int ita = 0 ; ita < tags.size() ; ita++) Log.i("me", "key " + tags.get(ita).key.toString() + " value " + tags.get(ita).value.toString());
		
		for (Tag tt : tags) {
			if (tt.key.equals("highway") && ( tt.value.equals("residential") 
											|| tt.value.equals("service") 
											 || tt.value.equals("track")) )
			{
				for (int i = 0; i < wayNodes[0].length; i = i + 2) {
					double x1 = wayNodes[0][i] * Math.pow(10, -6);
					double y1 = wayNodes[0][i + 1] * Math.pow(10, -6);
					double x2 = mapView.thread.geopointReal.getLongitude();
					double y2 = mapView.thread.geopointReal.getLatitude();
					
					if (Math.abs(x1 - x2) < 0.00005) //0.00015
					{
						//Log.i("way x1 - x2", "" + tt.value.toString());
						if (Math.abs(y1 - y2) < 0.00005) 
						{
							//Log.i("way y1 - y2", "----" + tt.value.toString());
							if (tt.key.equals("highway")&& ( tt.value.equals("residential") 
														   || tt.value.equals("service") 
															|| tt.value.equals("track")) )
							{
								mapView.thread.isHighway = true;								
								Log.i("NODE-TAG", ""+ tt.key.toString() + " : " + tt.value.toString());
							}
						}
					}
				}
			}
			
				if (tt.key.equals("highway") && tt.value.equals("footway"))
				{
					for (int i = 0; i < wayNodes[0].length; i = i + 2) {
						double x1 = wayNodes[0][i] * Math.pow(10, -6);
						double y1 = wayNodes[0][i + 1] * Math.pow(10, -6);
						double x2 = mapView.thread.geopointReal.getLongitude();
						double y2 = mapView.thread.geopointReal.getLatitude();	
						if (Math.abs(x1 - x2) < 0.000055) { //0.0001
							//Log.i("way x1 - x2", "" + tt.value.toString());
							if (Math.abs(y1 - y2) < 0.000055) 
							{
								//Log.i("way y1 - y2", "----" + tt.value.toString());
								if (tt.key.equals("highway")&& tt.value.equals("footway"))
								{
									mapView.thread.isFootway = true;
									Log.i("NODE-TAG", ""+ tt.key.toString() + " : " + tt.value.toString());
								}
							}
						}
					}
				}
				
				if (tt.key.equals("natural") && tt.value.equals("coastline"))
				{
					for (int i = 0; i < wayNodes[0].length; i = i + 2) {
						double x1 = wayNodes[0][i] * Math.pow(10, -6);
						double y1 = wayNodes[0][i + 1] * Math.pow(10, -6);
						double x2 = mapView.thread.geopointReal.getLongitude();
						double y2 = mapView.thread.geopointReal.getLatitude();	
						if (Math.abs(x1 - x2) < 0.0001) {
							//Log.i("way x1 - x2", "" + tt.value.toString());
							if (Math.abs(y1 - y2) < 0.0001) 
							{
								//Log.i("way y1 - y2", "----" + tt.value.toString());
								if (tt.key.equals("natural")&& tt.value.equals("coastline"))
								{
									mapView.thread.isCoast = true;
									Log.i("NODE-TAG", ""+ tt.key.toString() + " : " + tt.value.toString());
								}
							}
						}
					}
				}
				
				if (tt.key.equals("highway") )
				{
					for (int i = 0; i < wayNodes[0].length; i = i + 2) {
						double x1 = wayNodes[0][i] * Math.pow(10, -6);
						double y1 = wayNodes[0][i + 1] * Math.pow(10, -6);
						double x2 = mapView.thread.geopointReal.getLongitude();
						double y2 = mapView.thread.geopointReal.getLatitude();	
						if (Math.abs(x1 - x2) < 0.0001) {
							//Log.i("way x1 - x2", "" + tt.value.toString());
							if (Math.abs(y1 - y2) < 0.0001) 
							{
								//Log.i("way y1 - y2", "----" + tt.value.toString());
									if (tt.key.equals("highway"))
									{
										//mapView.thread.isHighway = true;
										Log.i("NODE-TAG", ""+ tt.key.toString() + " : " + tt.value.toString());
										}
							}
						}
					}
				}
			}// end of for (Tag tt : tags) 

		isLanduse = false;
		
		for (Tag t : tags) {

			if (t.key.equals("landuse") && t.value.equals("residential")) 
			{
				isLanduse = true;
			}
		}

		// LANDUSE
		if (isLanduse ) {
			for (Tag t : tags) {
				if (t.key.equals("name")) {
					nameLanduse = t.value;
					Log.v("speak1 : ", nameLanduse);
				} else
					nameLanduse = null;
				Log.v("TagMyWay : ", t.key + " : " + t.value);
			}
			Log.v("TagMyWay : ", "nbre Noeuds : " + wayNodes[0].length);

			inLanduse = false;

			int j = wayNodes[0].length - 2;
			for (int i = 0; i < wayNodes[0].length; i = i + 2) {
				float polyXi = (float) (wayNodes[0][i] * Math.pow(10, -6));
				float polyYi = (float) (wayNodes[0][i + 1] * Math.pow(10, -6));
				float polyXj = (float) (wayNodes[0][j] * Math.pow(10, -6));
				float polyYj = (float) (wayNodes[0][j + 1] * Math.pow(10, -6));
				float x = (float) this.mapView.thread.geopointReal.getLongitude();
				float y = (float) this.mapView.thread.geopointReal.getLatitude();
				if (polyYi < y && polyYj >= y || polyYj < y && polyYi >= y) {
					if (polyXi + (y - polyYi) / (polyYj - polyYi)
							* (polyXj - polyXi) < x)
						inLanduse = !inLanduse;
				}

				j = i;
				//Log.i("TagMyWay : ", "nbre Noeuds+++++ : " + "latitude : "+ (wayNodes[0][i + 1] * Math.pow(10, -6))+ " longitude : " + (wayNodes[0][i] * Math.pow(10, -6)));
			}
			if (inLanduse)
				this.mapView.thread.trouve = true;
			if (inLanduse == isLanduse)
				mapView.thread.Landuse = isLanduse;
			//Log.i("WAY-TAG", "islanduse");
			listIn.add(inLanduse);
			Log.v("TagMyWay : ", "intersec : " + inLanduse);
		}
	
		
		// BEACH
		
		isBeach = false;
		
		for (Tag t : tags) {

			if (t.key.equals("natural") && t.value.equals("sea")) 
			{
				
				isWater = true;
				Log.v("WATER = ", isWater + " = TRUE !!!!!!");
				
			}
			
			if (t.key.equals("natural") && t.value.equals("beach")) 
			{
				
				isBeach = true;
			}
			

			
		}


		if (isBeach) {
			for (Tag t : tags) {
				if (t.key.equals("name")) {
					mapView.thread.wayName  = t.value;
					//Log.i("speak2 : ", "" + mapView.thread.wayName);
				} else
					nameBeach = null;
				Log.v("TagMyWay : ", t.key + " : " + t.value);
			}
			Log.v("TagMyWay BEACH: ", "nbre Noeuds : " + wayNodes[0].length);

			inBeach = false;

			int j = wayNodes[0].length - 2;
			for (int i = 0; i < wayNodes[0].length; i = i + 2) {
				float polyXi = (float) (wayNodes[0][i] * Math.pow(10, -6));
				float polyYi = (float) (wayNodes[0][i + 1] * Math.pow(10, -6));
				float polyXj = (float) (wayNodes[0][j] * Math.pow(10, -6));
				float polyYj = (float) (wayNodes[0][j + 1] * Math.pow(10, -6));
				float x = (float) this.mapView.thread.geopointReal.getLongitude();
				float y = (float) this.mapView.thread.geopointReal.getLatitude();
				if (polyYi < y && polyYj >= y || polyYj < y && polyYi >= y) {
					if (polyXi + (y - polyYi) / (polyYj - polyYi)
							* (polyXj - polyXi) < x)
						inBeach = !inBeach;
				}

				j = i;
				//Log.i("TagMyWay : ", "nbre Noeuds+++++ : " + "latitude : "+ (wayNodes[0][i + 1] * Math.pow(10, -6))+ " longitude : " + (wayNodes[0][i] * Math.pow(10, -6)));
			}
			if (inBeach)
				this.mapView.thread.trouve = true;
			if (inBeach == isBeach)
				mapView.thread.Beach = isBeach;  
			//Log.i("WAY-TAG", "isBeach");
			listIn.add(inBeach);
			Log.v("TagMyWay : ", "intersec : " + inBeach);
		}
		
		
		
		// water
		
		isWater = false;
		
		
		
//		for (Tag t : tags) {
//			Log.v("isWater : ", " COUCOU for tag");
//			if (t.key.equals("water") && t.value.equals("sea")) 
//			{
//				
//				isWater = true;
//				Log.v("WATER = ", isWater + "");
//				
//			}
//		}
		
		if (isWater) {
			for (Tag t : tags) {
				if (t.key.equals("name")) {
					mapView.thread.wayName  = t.value;
					//Log.i("speak2 : ", "" + mapView.thread.wayName);
				} else
					nameWater = null;
				Log.v("TagMyWay : ", t.key + " : " + t.value);
			}
			Log.v("WATER TagMyWay : ", "nbre Noeuds water: " + wayNodes[0].length);

			inWater = false;

			int j = wayNodes[0].length - 2;
			for (int i = 0; i < wayNodes[0].length; i = i + 2) {
				float polyXi = (float) (wayNodes[0][i] * Math.pow(10, -6));
				float polyYi = (float) (wayNodes[0][i + 1] * Math.pow(10, -6));
				float polyXj = (float) (wayNodes[0][j] * Math.pow(10, -6));
				float polyYj = (float) (wayNodes[0][j + 1] * Math.pow(10, -6));
				float x = (float) this.mapView.thread.geopointReal.getLongitude();
				float y = (float) this.mapView.thread.geopointReal.getLatitude();
				if (polyYi < y && polyYj >= y || polyYj < y && polyYi >= y) {
					if (polyXi + (y - polyYi) / (polyYj - polyYi)
							* (polyXj - polyXi) < x)
						inWater = !inWater;
				}

				j = i;
				//Log.i("TagMyWay : ", "nbre Noeuds+++++ : " + "latitude : "+ (wayNodes[0][i + 1] * Math.pow(10, -6))+ " longitude : " + (wayNodes[0][i] * Math.pow(10, -6)));
			}
			if (inWater)
				this.mapView.thread.trouve = true;
			if (inWater == isWater)
				mapView.thread.Water = isWater;  
			Log.i("WAY-TAG", "isWATER");
			listIn.add(inWater);
			Log.v("WATER TagMyWay : ", "intersec : " + inWater);
		}
		
		
		
		
		
	}
}
