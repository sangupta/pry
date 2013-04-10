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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sangupta.jerry.batch.MultiThreadableOperation;
import com.sangupta.jerry.batch.MultiThreadedTaskExecutor;
import com.sangupta.jerry.util.WebUtils;
import com.sangupta.jerry.util.ZipUtils;
import com.sangupta.pry.download.SiteDownloadService;
import com.sangupta.pry.site.Site;
import com.sangupta.pry.site.SiteService;

/**
 * 
 * @author sangupta
 *
 */
public class DefaultPryServiceImpl implements PryService {
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private SiteDownloadService siteDownloadService;

	@Override
	public void startPrying(final String url, final int numSitesToAnalyze) {
		File tempZip = downloadFile(url);
		
		// extract the sites list and find out top 500
		File csvFile = extractZip(tempZip);
		
		// read the top 100 sites and
		// push all sites into the database
		List<Site> sites = pushToDatabase(csvFile, numSitesToAnalyze);
		
		// fetch the home page of each of these sites and store them into the database
		downloadAllSites(sites);
		
		// run the analyzers
		
		// prepare the charts
	}
	
	private void downloadAllSites(List<Site> sites) {
		MultiThreadableOperation<Site> operation = new MultiThreadableOperation<Site>() {
			
			@Override
			public void runWithArguments(Site site) {
				siteDownloadService.downloadSite(site);
			}
		};
		
		MultiThreadedTaskExecutor executor = new MultiThreadedTaskExecutor("downloadSites", operation, 10);
		
		for(Site site : sites) {
			executor.addInvocation(site);
		}
		
		try {
			executor.waitForCompletion();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Downloaded all sites.");
	}

	/**
	 * Read the file and get objects for the first N sites.
	 * 
	 * @param csvFile
	 * @param numSites
	 * @return
	 */
	private List<Site> pushToDatabase(File csvFile, int numSites) {
		this.siteService.deleteAllEntities();
		
		List<Site> sites = new ArrayList<Site>();

		// read the file line by line
		FileReader fileReader = null;
		BufferedReader reader = null;
		try {
			fileReader = new FileReader(csvFile);
			reader = new BufferedReader(fileReader);
			
			for(int index = 0; index < numSites; index++) {
				Site site = this.siteService.getOrAdd(reader.readLine());
				if(site != null) {
					sites.add(site);
				}
			}
		} catch(FileNotFoundException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(fileReader);
		}
		
		return sites;
	}

	/**
	 * Extract the zip file to a temporary location.
	 * 
	 * @param zipFile
	 * @return
	 */
	private File extractZip(final File zipFile) {
		System.out.print("Extracting file list...");
		
		File csvFile = null;
		try {
			csvFile = ZipUtils.readFileFromZip(zipFile, "top-1m.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("done!");
		return csvFile;
	}

	/**
	 * Download the file from URL to a temporary location.
	 * 
	 * @param url
	 * @return
	 */
	private File downloadFile(final String url) {
		// download the alexa top 1M zip
		System.out.print("Downloading the Alexa top Million sites list...");
		
		File tempZip = null;
		try {
			tempZip = WebUtils.downloadToTempFile(url);
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("done!");
		return tempZip;
	}

}
