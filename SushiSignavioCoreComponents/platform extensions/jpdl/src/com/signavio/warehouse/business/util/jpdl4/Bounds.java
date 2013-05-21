/*******************************************************************************
 * Signavio Core Components
 * Copyright (C) 2012  Signavio GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.signavio.warehouse.business.util.jpdl4;

import java.io.StringWriter;

import org.json.JSONException;
import org.json.JSONObject;

public class Bounds {
	private int ulx = 0;
	private int uly = 0;
	private int height = 80;
	private int width = 100;
	
	public void setUlx(int ulx) {
		this.ulx = ulx;
	}
	public void setUly(int uly) {
		this.uly = uly;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getUlx() {
		return ulx;
	}
	public int getUly() {
		return uly;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public Bounds() {
		
	}
	public Bounds(JSONObject bounds) {
		try {
			JSONObject upperLeft = bounds.getJSONObject("upperLeft");
			JSONObject lowerRight = bounds.getJSONObject("lowerRight");
			this.ulx = upperLeft.getInt("x");
			this.uly = upperLeft.getInt("y");
			this.width = lowerRight.getInt("x") - ulx;
			this.height = lowerRight.getInt("y") - uly;
		} catch (JSONException e) {}
	}
	
	public Bounds(String[] bounds) {
		if(bounds.length == 4) {
			this.ulx = Integer.parseInt(bounds[0]);
			this.uly = Integer.parseInt(bounds[1]);
			this.width = Integer.parseInt(bounds[2]);
			this.height = Integer.parseInt(bounds[3]);
		}
	}
	
	public String toJpdl() {
		StringWriter jpdl = new StringWriter();
		jpdl.write(" g=\"");
		jpdl.write(ulx + ",");
		jpdl.write(uly + ",");
		jpdl.write(width + ",");
		jpdl.write(height + "\"");
		return jpdl.toString();
	}
	
	public JSONObject toJson() throws JSONException {

		JSONObject lowerRight = new JSONObject();
		lowerRight.put("x", ulx + width);
		lowerRight.put("y", uly + height);
		
		JSONObject upperLeft = new JSONObject();
		upperLeft.put("x", ulx);
		upperLeft.put("y", uly);
		
		JSONObject bounds = new JSONObject();
		bounds.put("lowerRight", lowerRight);
		bounds.put("upperLeft", upperLeft);
		
		return bounds;
	}
	
}
