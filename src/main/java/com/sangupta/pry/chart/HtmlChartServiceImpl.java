package com.sangupta.pry.chart;

import java.util.List;

import com.sangupta.pry.analysis.Analysis;
import com.sangupta.pry.analyzers.ResponseSizeAnalyzer;
import com.sangupta.pry.site.Site;

public class HtmlChartServiceImpl implements ChartService {

	@Override
	public void chart(Analysis analysis, List<Site> sites) {
		// generate the main chart
		System.out.print("Average response size: ");
		System.out.println(analysis.getInt(ResponseSizeAnalyzer.RESPONSE_SIZE_ATTRIBUTE));
	}

}
