package com.web.service;


public interface FundingService {
    // 프로젝트 펀딩
    void fundProject(Long projectId, int amount);
}
