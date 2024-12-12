package com.web.controller;

import com.web.dto.FundingDTO;
import com.web.service.FundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/funding")
public class FundingController {

    @Autowired
    private FundingService fundingService;

    @PostMapping
    public String fundProject(@RequestBody FundingDTO fundingDTO) {
        fundingService.fundProject(fundingDTO.getProjectId(), fundingDTO.getAmount());
        return "펀딩 성공!";
    }
}
