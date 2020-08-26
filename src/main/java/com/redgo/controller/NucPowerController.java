package com.redgo.controller;



import com.redgo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;


@Controller
@RequestMapping("/NucPower")
public class NucPowerController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/findAll")
    public  String findAll(String queryString, String page, String powerStation, String unit, String docType, Model model){

        HashMap<String, Object> response = fileService.findAll(queryString, page, powerStation, unit, docType);
        Object sumNumb = response.get("sumNumb");
        Object events = response.get("events");
        Object fileWords = response.get("fileWords");

        double pageSum = Math.ceil(Integer.valueOf(sumNumb.toString())/(float)14);

        model.addAttribute("events",events);
        model.addAttribute("fileWords",fileWords);
        model.addAttribute("queryString",queryString);
        model.addAttribute("sumNumb",Integer.valueOf(sumNumb.toString()));
        model.addAttribute("pageNow",page);
        model.addAttribute("pageSum",(int)pageSum);
        model.addAttribute("powerStation",powerStation);
        model.addAttribute("unit",unit);
        model.addAttribute("docType",docType);

        return "searchEngine";
    }
}
