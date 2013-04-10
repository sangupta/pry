/**
 *
 * pry - Prying what powers the Internet 
 * Copyright (c) 2012-2013, Sandeep Gupta
 * 
 * http://www.sangupta/projects/pry
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.pry.analysis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author sangupta
 *
 */
public class Analysis {
	
	private final long totalSites;
	
	private long sitesAnalyzed = 0;
	
	private Map<String, Long> values;
	
	public Analysis(int totalSites) {
		this.totalSites = totalSites;
		this.values = new ConcurrentHashMap<String, Long>();
	}
	
	public void incrementSiteCount() {
		this.sitesAnalyzed++;
	}
	
	public long getInt(String name) {
		if(this.values.containsKey(name)) {
			return this.values.get(name);
		}
		
		return -1;
	}
	
	public void updateInt(String name, int delta) {
		long value;
		if(this.values.containsKey(name)) {
			value = this.values.get(name);
			value += delta;
		} else {
			value = delta;
		}
		
		this.values.put(name, value);
	}
	
	// Usual accessors follow

	/**
	 * @return the totalSites
	 */
	public long getTotalSites() {
		return totalSites;
	}

	/**
	 * @return the sitesAnalyzed
	 */
	public long getSitesAnalyzed() {
		return sitesAnalyzed;
	}

}
