package com.example.json_test;

import com.mapquest.android.maps.BoundingBox;
import com.mapquest.android.maps.GeoPoint;

public class GuidanceRoute {
	BoundingBox boundingBox;

	public GuidanceRoute() {
		// TODO Auto-generated constructor stub
	}
	
	public GuidanceRoute(GuidanceData d) {
		GeoPoint ul = new GeoPoint(d.boundingBox.ul.lat, d.boundingBox.ul.lng);
		GeoPoint lr = new GeoPoint(d.boundingBox.lr.lat, d.boundingBox.lr.lng);
		
		boundingBox = new BoundingBox(ul, lr);
	}
	
	class GuidanceEvent {
		String info;
		GeoPoint location;
		int maneuverType;
		int[] linkIds;
	}
}
