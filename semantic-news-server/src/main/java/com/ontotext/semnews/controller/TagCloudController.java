package com.ontotext.semnews.controller;

import com.ontotext.ffnews.model.HeatMapModel;
import com.ontotext.ffnews.model.TagCloudModel;
import com.ontotext.ffnews.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class TagCloudController {

	@Autowired
	private QueryService queryService;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("from", "");
		model.addAttribute("catA", "All");
		return "tagcloud";
	}

	@RequestMapping(value = "/tag-cloud", method = RequestMethod.GET)
	@ResponseBody
	public List<TagCloudModel> getTagCloud(ModelMap model, @RequestParam("from") String from,
										   @RequestParam("hidden") String hidden,
										   @RequestParam("industry") String industry,
										   @RequestParam(value = "relPop", required = false, defaultValue = "false") Boolean relPopularity) {


		List<TagCloudModel> tagCloud;
		tagCloud = queryService.getWordCloudResutlts(from, hidden, industry, relPopularity);



		return tagCloud;
	}

	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	@ResponseBody
	public Set<String> getCategories() {
		return getPubCategory();

	}

	@RequestMapping(value = "/tag-cloud/results", method = RequestMethod.GET)
	public String getReuslts(HttpServletRequest request, @RequestParam("from") String from,
							 @RequestParam("industry") String industry,
							 @RequestParam(value = "relPop", required = false, defaultValue = "false") Boolean relPopularity,
							 ModelMap model) {
		model.addAttribute("fromA", from);
//		model.addAttribute("toA", to);
		model.addAttribute("catA", industry);

		model.addAttribute("repativePop", relPopularity);


		return "tagcloud";

	}

	@RequestMapping(value = "/heat-map", method = RequestMethod.GET)
	@ResponseBody
	public List<HeatMapModel> getHeatMap(HttpServletRequest request, @RequestParam("from") String from, ModelMap model) {

		List<HeatMapModel> heatMapModels = new ArrayList<>();
		heatMapModels = queryService.getHeatMap(from);

		return heatMapModels;
	}

	private Map<String, String> getIndustry(){
		Map<String, String> industry = new HashMap<>();
		industry.put("all", "all");
		industry.put("Agriculture", "http://dbpedia.org/resource/Agriculture");
		industry.put("Biotechnology", "http://dbpedia.org/resource/Biotechnology");
		industry.put("Electronics", "http://dbpedia.org/resource/Electronics");
		industry.put("Telecommunications", "http://dbpedia.org/resource/Telecommunications");
		industry.put("Real estate", "http://dbpedia.org/resource/Real_estate");
		industry.put("Internet", "http://dbpedia.org/resource/Internet");
		industry.put("Software", "http://dbpedia.org/resource/Software");
		industry.put("Transport", "http://dbpedia.org/resource/Transport");
		industry.put("Manufacturing", "http://dbpedia.org/resource/Manufacturing");
		industry.put("Construction", "http://dbpedia.org/resource/Construction");
		industry.put("Mining", "http://dbpedia.org/resource/Mining");
		industry.put("Energy", "http://dbpedia.org/resource/Energy");
		industry.put("Entertainment", "http://dbpedia.org/resource/Entertainment");
		industry.put("Mass media", "http://dbpedia.org/resource/Mass_media");
		industry.put("Education", "http://dbpedia.org/resource/Education");
		industry.put("Finance", "http://dbpedia.org/resource/Finance");
		industry.put("Automotive", "http://dbpedia.org/resource/Automotive");
		industry.put("Healthcare", "http://dbpedia.org/resource/Healthcare");
		industry.put("Publishing", "http://dbpedia.org/resource/Publishing");
		industry.put("Hospitality", "http://dbpedia.org/resource/Hospitality");
		industry.put("Retail", "http://dbpedia.org/resource/Retail");
		industry.put("Oil and gas", "http://dbpedia.org/resource/Oil_and_gas");
		industry.put("Food and Beverage", "http://dbpedia.org/resource/Food_and_Beverage");

		return industry;
	}

	public Set<String> getPubCategory() {
		Set<String> pubCategory = new HashSet();
		pubCategory.add("All");
		pubCategory.add("Business");
		pubCategory.add("Sports");
		pubCategory.add("International");
		pubCategory.add("Science and Technology");
		pubCategory.add("Lifestyle");

		return pubCategory;
	}

	private Map<String, String> getContinents() {
		Map<String, String> continents = new HashMap<>();
		continents.put("Africa", "http://dbpedia.org/resource/Africa");
		continents.put("Antarctica", "http://dbpedia.org/resource/Antarctica");
		continents.put("Asia", "http://dbpedia.org/resource/Asia");
		continents.put("Europe", "http://dbpedia.org/resource/Europe");
		continents.put("North America", "http://dbpedia.org/resource/North_America");
		continents.put("South America", "http://dbpedia.org/resource/South_America");
		continents.put("Australia (continent)", "http://dbpedia.org/resource/Australia_(continent)");

		return continents;
	}




}