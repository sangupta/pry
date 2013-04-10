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

package com.sangupta.pry;

/**
 * 
 * @author sangupta
 *
 */
public class PryingOptions {
	
	private final boolean downloadZip;
	
	private final boolean extractZip;
	
	private final boolean pushSitesIntoDatabase;
	
	private final boolean downloadSites;
	
	private final boolean runAnalyzers;
	
	private final boolean prepareCharts;
	
	public PryingOptions() {
		this(true, true, true, true, true, true);
	}

	public PryingOptions(boolean downloadZip, boolean extractZip, boolean pushSites, boolean downloadData, boolean analyze, boolean chart) {
		this.downloadZip = downloadZip;
		this.extractZip = extractZip;
		this.pushSitesIntoDatabase = pushSites;
		this.downloadSites = downloadData;
		this.runAnalyzers = analyze;
		this.prepareCharts = chart;
	}
	
	// Usual accessors follow

	/**
	 * @return the downloadZip
	 */
	public boolean isDownloadZip() {
		return downloadZip;
	}

	/**
	 * @return the extractZip
	 */
	public boolean isExtractZip() {
		return extractZip;
	}

	/**
	 * @return the pushSitesIntoDatabase
	 */
	public boolean isPushSitesIntoDatabase() {
		return pushSitesIntoDatabase;
	}

	/**
	 * @return the downloadSites
	 */
	public boolean isDownloadSites() {
		return downloadSites;
	}

	/**
	 * @return the runAnalyzers
	 */
	public boolean isRunAnalyzers() {
		return runAnalyzers;
	}

	/**
	 * @return the prepareCharts
	 */
	public boolean isPrepareCharts() {
		return prepareCharts;
	}
}
