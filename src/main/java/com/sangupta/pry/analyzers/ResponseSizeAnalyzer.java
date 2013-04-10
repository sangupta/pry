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

package com.sangupta.pry.analyzers;

import com.sangupta.pry.analysis.Analysis;
import com.sangupta.pry.analysis.Analyzer;
import com.sangupta.pry.sitedata.SiteData;

/**
 * Runs a query against the response size of the various websites.
 * 
 * @author sangupta
 *
 */
public class ResponseSizeAnalyzer implements Analyzer {
	
	public static final String RESPONSE_SIZE_ATTRIBUTE = "response.size";

	@Override
	public void analyze(Analysis analysis, SiteData siteData) {
		int length = siteData.getContents().length();
		
		analysis.updateInt(RESPONSE_SIZE_ATTRIBUTE, length);
	}

}
