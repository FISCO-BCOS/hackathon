package org.example.demo.controller;

import org.example.demo.model.bo.*;
import org.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("Organization")
public class OrganizationController {


    @Autowired
    private OrganizationService organization;
    @Autowired
    private ProgramService program;
    @Autowired
    private  DagService dag;

        @GetMapping("addprogram")
        public void addprogram(
                @RequestParam("totalneed")  BigInteger totalneed,
                @RequestParam("title") String title,
                @RequestParam("programType")  String programType,
                @RequestParam("description")  String description
        ) throws Exception {
            if(title==""){
                System.out.println("Empty title!");
                return;
            } else if(description == "") {
                System.out.println(("Empty description!"));
                return;
            }
            OrganizationAddprogramInputBO organizationAddprogramInputBO = new OrganizationAddprogramInputBO(
                    totalneed,
                    title,
                    programType,
                    description);
            organization.addprogram(organizationAddprogramInputBO);
        }


    @GetMapping("changevalue")
    public String changevalue(
            @RequestParam("id") BigInteger id,
            @RequestParam("value") BigInteger value
    ) throws Exception {
        OrganizationChangevalueInputBO organizationChangevalueInputBO = new OrganizationChangevalueInputBO(id, value);

        return organization.changevalue(organizationChangevalueInputBO).getValues();
}


     /*
    @GetMapping("getdevoter")
    public String getdevoter(
            @RequestParam("id") BigInteger id
    ) throws Exception {
        OrganizationGetdevoterInputBO organizationGetdevoterInputBO = new OrganizationGetdevoterInputBO(id);
        return organization.getdevoter(organizationGetdevoterInputBO).getValues();
    }

*/


    @GetMapping("getstring")
    public String getstring(
            @RequestParam("id") BigInteger id
            ) throws Exception {
        OrganizationGetstringinforInputBO organizationGetstringinforInputBO = new OrganizationGetstringinforInputBO(id);
        return organization.getstringinfor(organizationGetstringinforInputBO).getValues();
    }

    @GetMapping("getprocess")
    public String getprocess(
            @RequestParam("id") BigInteger id
    ) throws Exception {
        OrganizationGetprocessInputBO organizationGetprocessInputBO = new OrganizationGetprocessInputBO(id);
        return organization.getprocess(organizationGetprocessInputBO).getValues();
    }


    @GetMapping("implement")
    public String implement(
            @RequestParam("id")  BigInteger id,
            @RequestParam("describe")  String describe
    )throws Exception{
        if(describe == ""){
            System.out.println("Empty description!");
        }
        OrganizationImplementProgramInputBO organizationImplementProgramInputBO =new OrganizationImplementProgramInputBO(id,describe);
        return organization.implementProgram(organizationImplementProgramInputBO).getValues();
    }

}
