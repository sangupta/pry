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

package com.sangupta.pry.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.sangupta.jerry.batch.MultiThreadableOperation;
import com.sangupta.jerry.batch.MultiThreadedTaskExecutor;
import com.sangupta.jerry.util.UriUtils;
import com.sangupta.jerry.util.WebUtils;
import com.sangupta.jerry.util.ZipUtils;
import com.sangupta.pry.PryingOptions;
import com.sangupta.pry.analysis.Analysis;
import com.sangupta.pry.analysis.AnalyzerService;
import com.sangupta.pry.chart.ChartService;
import com.sangupta.pry.download.SiteDownloadService;
import com.sangupta.pry.site.Site;
import com.sangupta.pry.site.SiteService;

/**
 * 
 * @author sangupta
 *
 */
public class DefaultPryServiceImpl implements PryService {
	
	private static final String FILE_TO_READ = "top-1m.csv";
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private SiteDownloadService siteDownloadService;
	
	@Autowired
	private AnalyzerService analyzerService;
	
	@Autowired
	private ChartService chartService;
	
	private final File workFolder;
	
	public DefaultPryServiceImpl() {
		this.workFolder = new File("work");
		if(!this.workFolder.exists() || !this.workFolder.isDirectory()) {
			this.workFolder.mkdirs();
		}
	}

	@Override
	public void startPrying(final String url, final int numSitesToAnalyze, final PryingOptions pryingOptions) {
		File zipFile = new File(this.workFolder, UriUtils.extractFileName(url));
		File csvFile = new File(this.workFolder, FILE_TO_READ);
		
		if(pryingOptions.isDownloadZip()) {
			downloadFile(url, zipFile);
		}
		
		// extract the sites list and find out top 500
		if(pryingOptions.isExtractZip()) {
			extractZip(zipFile, csvFile);
		}
		
		// read the top 100 sites and
		// push all sites into the database
		List<Site> sites;
		if(pryingOptions.isPushSitesIntoDatabase()) {
			sites = pushToDatabase(csvFile, numSitesToAnalyze);
		} else {
			sites = this.siteService.getAllEntities();
		}
		
		// fetch the home page of each of these sites and store them into the database
		if(pryingOptions.isDownloadSites()) {
			downloadAllSites(sites);
		}
		
		// run the analyzers
		Analysis analysis = analyze(sites);
		
		// prepare the charts
		if(pryingOptions.isPrepareCharts()) {
			chart(analysis, sites);
		}
	}
	
	/**
	 * Create charts based on analysis and sites.
	 * @param analysis 
	 * 
	 * @param sites
	 */
	private void chart(Analysis analysis, List<Site> sites) {
		System.out.print("Starting to build charts...");
		this.chartService.chart(analysis, sites);
		System.out.println("done!");
	}

	/**
	 * Analyze the list of sites.
	 * 
	 * @param sites
	 */
	private Analysis analyze(List<Site> sites) {
		System.out.print("Starting site analysis...");
		
		Analysis analysis = this.analyzerService.analyze(sites);
		
		System.out.println("done!");
		
		return analysis;
	}

	private void downloadAllSites(List<Site> sites) {
		System.out.println("Downloading sites...");
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
		System.out.print("Pushing site data to database...");
		this.siteService.deleteAllEntities();
		
		List<Site> sites = new ArrayList<Site>();

		// read the file line by line
		FileReader fileReader = null;
		BufferedReader reader = null;
		try {
			fileReader = new FileReader(csvFile);
			reader = new BufferedReader(fileReader);
			
			String line;
			for(int index = 0; index < numSites; index++) {
				line = reader.readLine();
				if(line == null) {
					break;
				}
				
				int comma = line.indexOf(',');
				String url = line.substring(comma + 1);
				
				Site site = this.siteService.getOrAdd(UriUtils.normalizeUrl(url));
				if(site != null) {
					sites.add(site);
				}
			}
		} catch(FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
			IOUtils.closeQuietly(fileReader);
		}
		
		System.out.println("done!");
		return sites;
	}

	/**
	 * Extract the zip file to a temporary location.
	 * 
	 * @param zipFile
	 * @return
	 */
	private void extractZip(final File zipFile, final File csvFile) {
		System.out.print("Extracting file list...");
		
		try {
			File tempFile = ZipUtils.readFileFromZip(zipFile, FILE_TO_READ);
			FileUtils.copyFile(tempFile, csvFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("done!");
	}

	/**
	 * Download the file from URL to a temporary location.
	 * 
	 * @param url
	 * @return
	 */
	private void downloadFile(final String url, File zipFile) {
		// download the alexa top 1M zip
		System.out.print("Downloading the Alexa top Million sites list...");
		
		try {
			WebUtils.downloadToFile(url, zipFile);
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("done!");
	}
	
	// Usual accessors follow

	/**
	 * @return the siteService
	 */
	public SiteService getSiteService() {
		return siteService;
	}

	/**
	 * @param siteService the siteService to set
	 */
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	/**
	 * @return the siteDownloadService
	 */
	public SiteDownloadService getSiteDownloadService() {
		return siteDownloadService;
	}

	/**
	 * @param siteDownloadService the siteDownloadService to set
	 */
	public void setSiteDownloadService(SiteDownloadService siteDownloadService) {
		this.siteDownloadService = siteDownloadService;
	}

	/**
	 * @return the analyzerService
	 */
	public AnalyzerService getAnalyzerService() {
		return analyzerService;
	}

	/**
	 * @param analyzerService the analyzerService to set
	 */
	public void setAnalyzerService(AnalyzerService analyzerService) {
		this.analyzerService = analyzerService;
	}

	/**
	 * @return the chartService
	 */
	public ChartService getChartService() {
		return chartService;
	}

	/**
	 * @param chartService the chartService to set
	 */
	public void setChartService(ChartService chartService) {
		this.chartService = chartService;
	}

}
