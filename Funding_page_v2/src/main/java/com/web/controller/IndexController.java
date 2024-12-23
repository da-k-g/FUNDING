package com.web.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import com.web.domain.Novel;
import com.web.dto.NovelSearchDTO;
import com.web.service.NovelService;
import com.web.utils.PageMaker;

import lombok.RequiredArgsConstructor;

// 로그인을 하지 않아도 맨처음으로 나오는 페이지를 인기작품의 내용을 불러오도록 설정한 index 컨트롤러
@Controller
@RequiredArgsConstructor
public class IndexController {
	
    private final NovelService novelService;
    
    
    @Value("${file.upload-dir}") // 업로드 디렉토리 설정
    private String uploadDir;
   

    @GetMapping("/")
    public String popularNovels(NovelSearchDTO searchDTO, PageMaker pageMaker,Model model) { 
    	 
    	 searchDTO.setOrderBy("createDateDesc");
         Page<Novel> items1 = novelService.popularNovels(searchDTO,  pageMaker.getPageable(pageMaker, 5));               
         List<Novel> createDateDescNovels= items1.getContent();		 
         
         
      	 searchDTO.setOrderBy("likeScoreDesc");
         Page<Novel> items2 =novelService.popularNovels(searchDTO,  pageMaker.getPageable(pageMaker, 5));               
         List<Novel> likeScoreDescNovels = items2.getContent();	
         
         
      	 searchDTO.setOrderBy("viewCountDesc");
         Page<Novel> items3 =novelService.popularNovels(searchDTO,  pageMaker.getPageable(pageMaker, 5));               
         List<Novel> viewCountDescNovels = items3.getContent();	
         
    			 
    	 model.addAttribute("createDateDescNovels",createDateDescNovels);
    	 model.addAttribute("likeScoreDescNovels",likeScoreDescNovels);   
    	 model.addAttribute("viewCountDescNovels",viewCountDescNovels);  
    	 model.addAttribute("menuActive", "/novels/popular");
        return "novels/popular-list";
    }
}

	// 로그인 전 페이지
//    @GetMapping("/")
//    public String index(Authentication authentication) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            return "redirect:/dashboard"; // 로그인 상태면 대시보드로 리다이렉트
//        }
//        return "novels/popular-list"; // 로그인 전 페이지
//    }
//}

    // 로그인 후 페이지 (대시보드)
//    @GetMapping("/dashboard")
//    public String dashboard(Model model, Authentication authentication) {
//        String username = authentication.getName(); // 로그인한 사용자 이름 가져오기
//        model.addAttribute("username", username);
//        return "dashboard"; // 로그인 후 전용 홈 페이지
//    }
//}
