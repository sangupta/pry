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

package com.sangupta.pry.download;

import org.springframework.beans.factory.annotation.Autowired;

import com.sangupta.jerry.http.WebInvoker;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.pry.site.Site;
import com.sangupta.pry.sitedata.MongoDBSiteDataServiceImpl;
import com.sangupta.pry.sitedata.SiteData;

/**
 * 
 * @author sangupta
 *
 */
public class HttpSiteDownloadService implements SiteDownloadService {
	
	@Autowired
	private MongoDBSiteDataServiceImpl siteDataService;

	@Override
	public void downloadSite(Site site) {
		if(site == null) {
			return;
		}
		
		// store the url that is being worked upon
		final String url = site.getUrl();
		
		// initialize data object, as needed
		SiteData siteData = this.siteDataService.get(site.getSiteID());
		if(siteData == null) {
			siteData = new SiteData(url);
		}
		
		WebResponse response = WebInvoker.getResponse(url);
		siteData.markCrawled();
		if(response == null) {
			// this is an error
			siteData.setContents(null);
			siteData.setHeaders(null);
		} else {
			// we received a response
			// let's save the data in the database
			siteData.setHeaders(response.getHeaders());
			siteData.setContents(response.getContent());
		}
		
		this.siteDataService.addOrUpdate(siteData);
	}

}
