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

package com.sangupta.pry.sitedata;

import java.util.Map;

import org.springframework.data.annotation.Id;

import com.sangupta.pry.site.Site;

/**
 * Holds data from one site that has been downloaded including the
 * response headers, the actual page contents, and information on all
 * assets that may be linked to the page, like images, css and javascript
 * files.
 * 
 * @author sangupta
 *
 */
public class SiteData {
	
	/**
	 * This value is same as {@link Site#getSiteID()}.
	 * 
	 */
	@Id
	private String siteID;
	
	private String url;
	
	private boolean crawled;
	
	private long lastCrawled;
	
	private String contents;

	private Map<String, String> headers;
	
	public void markCrawled() {
		this.crawled = true;
		this.lastCrawled = System.currentTimeMillis(); 
	}
	
	public SiteData() {
		
	}
	
	public SiteData(String url) {
		this.url = url;
	}
	
	// usual accessors follow

	/**
	 * @return the siteID
	 */
	public String getSiteID() {
		return siteID;
	}

	/**
	 * @param siteID the siteID to set
	 */
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * @return the headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * @return the crawled
	 */
	public boolean isCrawled() {
		return crawled;
	}

	/**
	 * @param crawled the crawled to set
	 */
	public void setCrawled(boolean crawled) {
		this.crawled = crawled;
	}

	/**
	 * @return the lastCrawled
	 */
	public long getLastCrawled() {
		return lastCrawled;
	}

	/**
	 * @param lastCrawled the lastCrawled to set
	 */
	public void setLastCrawled(long lastCrawled) {
		this.lastCrawled = lastCrawled;
	}

}
