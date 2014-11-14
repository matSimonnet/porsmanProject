package porsman.ms;

import org.mapsforge.applications.android.geotablet.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Help extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem Item){
		switch (Item.getItemId()) {
		case R.id.item_map:
			Intent handiPorsmanMapViewerIntent = new Intent(Help.this,HandiPorsmanMapViewer.class);
			startActivity(handiPorsmanMapViewerIntent);
			break;
		default:
			break;
		}
		return false;
	}
	
	
}
