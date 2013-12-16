/*  MultiWii EZ-GUI
    Copyright (C) <2012>  Bartosz Szczygiel (eziosoft)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nav;

import android.R.integer;

import com.google.android.gms.maps.model.LatLng;

public class WaypointNav implements Comparable<WaypointNav> {

	// mission_step.number = read8();
	// mission_step.pos[LAT] = read32();
	// mission_step.pos[LON] = read32();
	// mission_step.action = read8();
	// mission_step.parameter = read16();
	// mission_step.altitude = read32();
	// mission_step.flag = read8();

	public static final int WP_ACTION_WAYPOINT = 1; // Set waypoint
	public static final int WP_ACTION_HOLD_UNLIM = 2; // Poshold unlimited
	public static final int WP_ACTION_HOLD_TIME = 3; // Hold for a predetermined
														// time
	public static final int WP_ACTION_RTH = 4; // Return to HOME
	public static final int WP_ACTION_SET_POI = 5; // Set POINT of interest (not
													// implemented jet)
	public static final int WP_ACTION_JUMP = 6; // jump to the given WP and
												// (number of times)

	public static final int MISSION_FLAG_END = 0xa5;

	public static final int ERROR_ERROR = 1;
	public static final int ERROR_CRC = 2;

	public static final String[] WP_ACTION_NAMES = { "---", "WAYPOINT", "HOLD_UNLIM", "HOLD_TIME", "RTH", "SET_POI", "JUMP" };

	public boolean ShowMarkerForThisWP() {
		boolean show = true;
		switch (Action) {
		case WP_ACTION_RTH:
			show = false;
			Lat = 0;
			Lon = 0;

			break;
		case WP_ACTION_JUMP:
			show = false;
			break;

		default:
			show = true;
			break;
		}
		return show;
	}

	public int Number = 0;
	public int Lat = 0;
	public int Lon = 0;
	public int Action = 1;
	public int Parameter = 0;
	public int Altitude = 25;
	public int Flag = 0;
	public String MarkerId = "";
	public int Error = 0;

	public String toString() {
		return "[" + getMarkerTitle() + "]\n" + getMarkerSnippet() + String.valueOf(getLatLng().latitude) + "x" + String.valueOf(getLatLng().longitude) + " F" + Integer.toHexString(Flag);

	}

	/**
	 * 
	 * @param number
	 * @param lat
	 * @param lon
	 * @param alt
	 *            altitude (cm)
	 * @param heading
	 *            heading (deg)
	 * @param timeToStay
	 *            time to stay (ms)
	 * @param navFlag
	 */
	public WaypointNav(int number, int lat, int lon, int action, int parameter, int altitude, int flag) {
		Number = number;
		Lat = lat;
		Lon = lon;
		Action = action;
		Parameter = parameter;
		Altitude = altitude; // to set altitude (cm)
		Flag = flag;
	}

	/**
	 * 
	 * @param number
	 * @param lat
	 * @param lon
	 * @param alt
	 *            altitude (cm)
	 * @param heading
	 *            heading (deg)
	 * @param timeToStay
	 *            time to stay (ms)
	 * @param navFlag
	 */
	public WaypointNav(int number, LatLng latLng, int action, int parameter, int altitude, int flag) {
		Number = number;
		Lat = (int) (latLng.latitude * 1e7);
		Lon = (int) (latLng.longitude * 1e7);
		Action = action;
		Parameter = parameter;
		Altitude = altitude; // to set altitude (cm)
		Flag = flag;
	}

	public WaypointNav(int number, LatLng latLng, int action, int parameter, int altitude, int flag, String MarkerID) {
		Number = number;
		Lat = (int) (latLng.latitude * 1e7);
		Lon = (int) (latLng.longitude * 1e7);
		Action = action;
		Parameter = parameter;
		Altitude = altitude; // to set altitude (cm)
		Flag = flag;
		MarkerId = MarkerID;
	}

	public WaypointNav() {

	}

	public LatLng getLatLng() {
		return new LatLng(Lat / 1e7, Lon / 1e7);

	}

	public void setLatLng(LatLng position) {
		Lat = (int) (position.latitude * 1e7);
		Lon = (int) (position.longitude * 1e7);
	}

	public String getMarkerTitle() {
		String t = "";
		t += "WP" + String.valueOf(Number);
		return t;
	}

	public String getMarkerSnippet() {
		String t = "";
		t += "Action:" + WP_ACTION_NAMES[Action] + "\n";
		t += "Parameter:" + String.valueOf(Parameter) + "\n";
		t += "Altitude:" + String.valueOf(Altitude) + "\n";
		return t;
	}

	public static int getActionNumberFromString(String action) {
		int i = 0;
		for (i = 0; i < WP_ACTION_NAMES.length; i++) {
			if (action.equals(WP_ACTION_NAMES[i]))
				return i;
		}
		return 0;
	}

	@Override
	public int compareTo(WaypointNav another) {
		if (Number < another.Number)
			return -1;
		if (Number > another.Number)
			return 1;
		if (Number == another.Number)
			return 0;
		return 0;
	}

}
