/**
 *
 * pry - Prying what powers top million sites 
 * Copyright (c) 2012, Sandeep Gupta
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

import java.io.File;

import com.sangupta.jerry.util.WebUtils;
import com.sangupta.jerry.util.ZipUtils;

/**
 * This program will analyze (amongst) the top 1 million sites to find
 * what powers these sites, from the server, language, scripts, tools and
 * various frameworks.
 * 
 * The extracted information is then published as HTML pages using Google
 * charts for inspection.
 * 
 * @author sangupta
 *
 */
public class Pry {
	
	private static final String ALEXA_TOP_1M_SITES = "http://s3.amazonaws.com/alexa-static/top-1m.csv.zip";

	/**
	 * The main entry point to this program.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// download the alexa top 1M zip
		File tempZip = WebUtils.downloadToTempFile(ALEXA_TOP_1M_SITES);

		// extract the sites list and find out top 500
		File csvFile = ZipUtils.readFileFromZip(tempZip, "top-1m.csv");
		
		// fetch the home page of each of these sites and store them into the database
		
		// run the analyzers
		
		// prepare the charts
	}

}
