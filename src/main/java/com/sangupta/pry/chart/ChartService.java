package com.sangupta.pry.chart;

import java.util.List;

import com.sangupta.pry.analysis.Analysis;
import com.sangupta.pry.site.Site;

public interface ChartService {

	public void chart(Analysis analysis, List<Site> sites);

}
