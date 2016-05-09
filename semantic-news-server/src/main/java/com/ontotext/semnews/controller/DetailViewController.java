package com.ontotext.semnews.controller;

import com.ontotext.semnews.service.SemanticNewsMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Boyan on 24-Mar-16.
 */
@Controller
public class DetailViewController {

    @Autowired
    private SemanticNewsMapService semanticNewsMapService;

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public String getDetailView(ModelMap model, @RequestParam(value = "uri") String entitiUri,
                                @RequestParam("from") String from,
                                @RequestParam("industry") String industry) {
        model.addAttribute("newsMenEnt", semanticNewsMapService.getNewsMentioningEntitie(from, industry, entitiUri));
        model.addAttribute("newsMenRelEnt", semanticNewsMapService.getNewsMentioningRelEntitie(from, industry, entitiUri));

        return "details";
    }

}
