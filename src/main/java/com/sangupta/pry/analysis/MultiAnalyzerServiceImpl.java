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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sangupta.pry.site.Site;
import com.sangupta.pry.sitedata.SiteData;
import com.sangupta.pry.sitedata.SiteDataService;

/**
 * 
 * @author sangupta
 *
 */
public class MultiAnalyzerServiceImpl implements AnalyzerService {
	
	private List<Analyzer> analyzers;
	
	@Autowired
	private SiteDataService siteDataService;

	@Override
	public Analysis analyze(List<Site> sites) {
		final Analysis analysis = new Analysis(sites.size());

		// start aggregating results for each site
		for(Site site : sites) {
			// get site data
			SiteData siteData = this.siteDataService.get(site.getSiteID());
			
			// run analyzers
			for(Analyzer analyzer : this.analyzers) {
				analyzer.analyze(analysis, siteData);
			}
		}
		
		return analysis;
	}
	
	// Usual accessors follow

	/**
	 * @return the analyzers
	 */
	public List<Analyzer> getAnalyzers() {
		return analyzers;
	}

	/**
	 * @param analyzers the analyzers to set
	 */
	public void setAnalyzers(List<Analyzer> analyzers) {
		this.analyzers = analyzers;
	}

	/**
	 * @return the siteDataService
	 */
	public SiteDataService getSiteDataService() {
		return siteDataService;
	}

	/**
	 * @param siteDataService the siteDataService to set
	 */
	public void setSiteDataService(SiteDataService siteDataService) {
		this.siteDataService = siteDataService;
	}

}
