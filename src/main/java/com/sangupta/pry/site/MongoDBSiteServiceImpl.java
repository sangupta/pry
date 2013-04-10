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

package com.sangupta.pry.site;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.sangupta.jerry.mongodb.MongoTemplateBasicOperations;

/**
 * 
 * @author sangupta
 *
 */
public class MongoDBSiteServiceImpl extends MongoTemplateBasicOperations<Site, String> implements SiteService {

	@Override
	public Site getOrAdd(String url) {
		Query query = new Query(Criteria.where("url").is(url));
		Site site = this.mongoTemplate.findOne(query, Site.class);
		if(site != null) {
			return site;
		}
		
		site = new Site(url);
		this.mongoTemplate.insert(site);
		return site;
	}

}
