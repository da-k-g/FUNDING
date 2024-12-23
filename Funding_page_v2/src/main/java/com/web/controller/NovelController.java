package com.web.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.domain.Novel;
import com.web.domain.User;
import com.web.dto.LikeFormDTO;
import com.web.dto.NovelDTO;
import com.web.dto.NovelSearchDTO;
import com.web.dto.ProjectDTO;
import com.web.dto.ResponseDTO;
import com.web.dto.ResponseVoteDTO;
import com.web.service.LikeService;
import com.web.service.NovelService;
import com.web.service.ProjectService;
import com.web.service.UserService;
import com.web.utils.PageMaker;
import com.web.utils.UploadSetting;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
// 소설 관련 작업을 처리하는 컨트롤러
@Controller
@RequestMapping("/novels") // "/novel"로 시작하는 요청을 처리
@RequiredArgsConstructor // Lombok을 사용하여 생성자를 통한 의존성 주입
public class NovelController {
    
    private final NovelService novelService; // 소설 관련 서비스

    private final UserService userService; // 사용자 관련 서비스
    
    private final LikeService likeService; // 좋아요 관련 서비스
    
    private final ProjectService projectService; // 프로젝트 관련 서비스
    
    
    @Value("${file.upload-dir}") // 업로드 디렉토리 설정
    private String uploadDir;
   
       

    /**
     * 소설 인기작품 목록
     * @param searchDTO 검색 조건
     * @param pageMaker 페이지네이션 설정 객체
     * @param model 뷰(.html)로 데이터를 전달하기 위한 객체
     * @return "novels/popular-list" 탬플릿
     */
    @GetMapping("/popular")
    public String popularNovels(NovelSearchDTO searchDTO, PageMaker pageMaker,Model model) { 
    	 // 신작순 소설 목록 가져오기
    	 searchDTO.setOrderBy("createDateDesc");
         Page<Novel> items1 =novelService.popularNovels(searchDTO,  pageMaker.getPageable(pageMaker, 5));               
         List<Novel> createDateDescNovels= items1.getContent();		 
         
         // 좋아요순 소설 목록 가져오기
      	 searchDTO.setOrderBy("likeScoreDesc");
         Page<Novel> items2 =novelService.popularNovels(searchDTO,  pageMaker.getPageable(pageMaker, 5));               
         List<Novel> likeScoreDescNovels = items2.getContent();	
         
         //조회순 소설 목록 가져오기
      	 searchDTO.setOrderBy("viewCountDesc");
         Page<Novel> items3 =novelService.popularNovels(searchDTO,  pageMaker.getPageable(pageMaker, 5));               
         List<Novel> viewCountDescNovels = items3.getContent();	
         
         // 모델에 데이터 추가
    	 model.addAttribute("createDateDescNovels",createDateDescNovels);
    	 model.addAttribute("likeScoreDescNovels",likeScoreDescNovels);   
    	 model.addAttribute("viewCountDescNovels",viewCountDescNovels);  
    	 model.addAttribute("menuActive", "/novels/popular");
        return "novels/popular-list"; // 탬플릿 반환
    }
    
    
    /**
     * 인기작품 더보기
     * @param searchDTO 검색 조건 DTO
     * @param pageMaker 페이지네이션 설정 객체
     * @param model 뷰로 데이터를 전달하기 위한 객체
     * @return "novels/popular-more-list" 템플릿
     */
    @GetMapping("/popular/{moreOrderBy}")
    public String popularNovelsMore(@PathVariable String moreOrderBy,   NovelSearchDTO searchDTO, PageMaker pageMaker,Model model) {     	 
    	searchDTO.setOrderBy(moreOrderBy); // 정렬 기준 설정
    	
        Page<Novel> items =novelService.listNovels(searchDTO,  pageMaker.getPageable(pageMaker, 12)); // 페이지 처리 화면에 등록된 소설이 12개면 다음 페이지로 넘어가야 볼 수 있음
        String pagination = pageMaker.pageObject(items, pageMaker.currentPage(pageMaker), 12, 5, "/novels/popular/"+moreOrderBy, "href");
        List<Novel> content = items.getContent();		 
   		
        String menuTitle="신작순";
        if(moreOrderBy.equals("likeScoreDesc")) {
        	menuTitle="좋아요순";
        }else if(moreOrderBy.equals("viewCountDesc")) {
        	menuTitle="조회순";
        }
        
        // 모델에 데이터 추가
   	    model.addAttribute("novels",content);    
   	    model.addAttribute("searchDTO", searchDTO);
   	    model.addAttribute("pagination", pagination); 	
   	    model.addAttribute("menuTitle", menuTitle); 	
   	       	    
        return "novels/popular-more-list"; // 인기작품 더보기 탬플릿 반환
    }
 
    
    
    

    /**
     * 소설 목록
     * @param searchDTO
     * @param pageMaker
     * @param model
     * @return
     */
    @GetMapping
    public String listNovels(NovelSearchDTO searchDTO, PageMaker pageMaker,Model model) { 
    	 
         Page<Novel> items = novelService.listNovels(searchDTO,  pageMaker.getPageable(pageMaker, 12)); // 소설 목록 가져오기    
         String pagination = pageMaker.pageObject(items, pageMaker.currentPage(pageMaker), 12, 5, "/novels", "href");
         List<Novel> content = items.getContent();		 
    			 
    	 model.addAttribute("novels",content); // 소설 데이터 추가    
    	 model.addAttribute("searchDTO", searchDTO); // 검색 조건 추가
    	 model.addAttribute("pagination", pagination); // 페이지네이션 데이터 추가
        return "novels/list"; // 탬플릿 반환
    }
    
    
    

    /** 소설 등록폼
     * 
     * @param model 뷰로 데이터를 전달하기 위한 객체
     * @param authentication 사용자 인증 객체
     * @return
     * @throws Exception
     */
    @GetMapping("/new")
    public String showWriteForm(Model model, Authentication authentication) throws Exception {
    	NovelDTO novelDTO = NovelDTO.builder().paid(false).build(); // 초기값 설정
    	model.addAttribute("novelDTO", novelDTO); // 모델에 추가
        return "novels/write"; // 탬플릿 반환
    }

    
    

    /**
     * 소설 등록처리
     * @param novelDTO 소설 등록 폼 데이터
     * @param bindingResult 유효성 검사 결과
     * @param authentication 사용자 인증 객체
     * @param model 뷰로 데이터를 전달하기 위한 객체
     * @return
     * @throws Exception
     */
    @PostMapping
    public String createNovel(@ModelAttribute @Valid  NovelDTO novelDTO,  BindingResult bindingResult, 
    		Authentication authentication, Model model) throws Exception {     
       	User user = userService.findByEmail(authentication.getName()); // 사용자 정보 조회
    	if (user == null) {
    	        throw new IllegalAccessException("로그인된 사용자 정보를 찾을 수 없습니다."); // 인증 실패
    	}
    	
    	
    	if(bindingResult.hasErrors()) { // 유효성 검사 실패처리    		
    		 model.addAttribute("errors", bindingResult.getAllErrors());
    		 return "novels/write"; //  탬플릿 반환
    	}
    	
    	try {    		
    		if(!novelDTO.getCoverImage().isEmpty()) { // 커버 이미지가 있으면 처리
    			String fileName =UUID.randomUUID().toString() +"_" +novelDTO.getCoverImage().getOriginalFilename();
    			Path path=Paths.get(UploadSetting.directory(uploadDir)+fileName); 
    			Files.createDirectories(path.getParent());// 경로 없으면 생성
    			Files.write(path,novelDTO.getCoverImage().getBytes());// 파일 저장
    			
    			String fileUrl = "/uploads/" + fileName; // 파일 출력용 URL 생성
    	        novelDTO.setCoverImageUrl(fileUrl);    	
    		}    		      	 
    		novelService.save(novelDTO, user); // 소설 저장  		
    	}catch (Exception e) {
    		 bindingResult.reject("fileUploadError", "파일 업로드 중 문제가 발생했습니다."); // 업로드 실패 처리
    		 return "novels/write"; // 탬플릿 반환
		}    	
    	return "redirect:/novels"; // 성공시 목록 페이지로 리다이렉트
    }
    
    
 

    /**
     * 좋아요 상태
     * @param priDetails
     * @param bno
     * @return
     */
    @GetMapping(value = "/{novelId}/like", produces = "application/json")
    public ResponseEntity<?> getVoteState(@PathVariable  Long novelId,   Authentication authentication) {
    	// 인증 정보가 없는 경우 오류 반환
    	if(authentication==null) return ResponseEntity.badRequest().body("Authorization error");
    	User user=userService.findByEmail(authentication.getName()); // 사용자 정보 조회
        // 좋아요 상태 조회
        ResponseVoteDTO result= likeService.getVoteState(novelId, user.getId());
        // 성공 응답 반환
        return ResponseEntity.ok(ResponseDTO.builder()
                .code(1)
                .message("success")
                .data(result)
                .build()) ;
    }
    
    
    
    
    /**  /novels/like
     * 추천/비추천 - 등록 처리
     * @param authentication 사용자 인증 객체
     * @param likeInvestmentFormDTO 좋아요 요청 데이터
     * @return 처리 결과를 jSON 형태로 반환
     */
    @PostMapping(value = "/like", produces = "application/json")
    public ResponseEntity<?> updteLike(Authentication authentication, @RequestBody LikeFormDTO likeFormDTO) {
        // 인증 정보가 없으면 오류 반환
    	if (authentication==null) return ResponseEntity.badRequest().body("Authorization error");    
     	User user = userService.findByEmail(authentication.getName()); // 사용자 정보 조회
     	
        likeFormDTO.setUid(user.getId()); // 요청 데이터에 사용자 ID 설정
        ResponseVoteDTO result= likeService.updateLike(likeFormDTO); // 좋아요 상태 업데이트
        // 성공 응답 반환
        return ResponseEntity.ok(ResponseDTO.builder()
                .code(1)
                .message("success")
                .data(result)
                .build()) ;
    }

       
    

    /**
     * 소설 상세 보기
     * @param id 소설 id
     * @param model 뷰로 데이터를 전달하기 위한 객체
     * @param authentication 사용자 인증 객체
     * @return
     */
    @GetMapping("/{id}")
    public String viewNovel(@PathVariable Long id, Model model, Authentication authentication) {
        NovelDTO novelDTO = novelService.viewNovel(id); // 소설 정보 조회
        System.out.println("novelDTO.id: " + novelDTO.getId()); // 값 확인
        model.addAttribute("novelDTO", novelDTO); // 모델에 소설 정보 추가
        return "novels/view"; // 상세 보기 탬플릿 반환
    }

    
    
    /**
     * 소설수정폼
     * @param id 소설 id
     * @param model 뷰로 데이터를 전달하기 위한 객체
     * @return
     */
    @GetMapping("/modify/{id}")
    public String showmodifyForm(@PathVariable Long id, Model model) {
        NovelDTO novelDTO = novelService.viewNovel(id); // 소설 정보 id로 조회
        model.addAttribute("novelDTO", novelDTO); // 모델에 소설 정보 추가
        return "novels/modify";// 수정 페이지 탬플릿 반환
    }

    
    
    /**
     * 소설 수정처리
     * @param id 소설 id
     * @param novelDTO 소설 수정 데이터
     * @param bindingResult 유효성 검사 결과
     * @param model 뷰로 데이터를 전달하기 위한 객체
     * @param authentication 사용자 인증 객체
     * @return
     * @throws Exception 예외 처리
     */
    @PostMapping("/modify/{id}")
    public String updateNovel(@PathVariable Long id, @ModelAttribute @Valid NovelDTO novelDTO, BindingResult bindingResult, Model model,
    		Authentication authentication
    		) throws Exception {
    	// 사용자 인증 정보 확인
       	User user = userService.findByEmail(authentication.getName());
    	if (user == null) {
    	        throw new IllegalAccessException("로그인된 사용자 정보를 찾을 수 없습니다.");
    	}
    	
    	// 유효성 검사 오류 처리
    	if(bindingResult.hasErrors()) {
    		 System.out.println("Errors: " + bindingResult.getAllErrors());
    		 model.addAttribute("errors", bindingResult.getAllErrors());
    		 return "novels/modify";
    	}
    	
    	try {    
    		// 커버 이미지가 있으면 파일 저장 처리
    		if(!novelDTO.getCoverImage().isEmpty()) {
    			String fileName =UUID.randomUUID().toString() +"_" +novelDTO.getCoverImage().getOriginalFilename();
    			Path path=Paths.get(UploadSetting.directory(uploadDir)+fileName); 
    			Files.createDirectories(path.getParent());// 경로 없으면 생성
    			Files.write(path,novelDTO.getCoverImage().getBytes());// 파일 저장
    			
    			String fileUrl = "/uploads/" + fileName; // 파일 출력용 URL 생성
    	        novelDTO.setCoverImageUrl(fileUrl);    	
    		}    		      	 
    		
    	}catch (Exception e) {
    		 bindingResult.reject("fileUploadError", "파일 업로드 중 문제가 발생했습니다.");
    		 return "novels/modify";
		} 
    	
    	
    	if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "novels/modify";
        }
    	// 소설 수정 데이트 업데이트
    	novelDTO.setId(id);
    	
    	//더티 체킹
    	novelService.updateNovel(novelDTO, user);

        return "redirect:/novels"; // 수정성공시 목록 페이지로 리다이렉트
    }

    
    /**
     * 소설 삭제 처리
     * @param id 소설 id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNovel(@PathVariable Long id) {
    	// 소설 삭제 처리 id값으로
        novelService.deleteNovel(id);
        return ResponseEntity.ok().build(); // 성공 메세지 반환
    }
    
    @GetMapping("/{id}/related-project")
    public String getRelatedProject(@PathVariable Long id, Model model, Authentication authentication) {
        // Novel 정보를 가져옴
        NovelDTO novelDTO = novelService.viewNovel(id);
        model.addAttribute("novelDTO", novelDTO);

        // 관련 프로젝트를 찾음
        Optional<ProjectDTO> project = projectService.getProjectByTitle(novelDTO.getTitle());

        // 프로젝트 ID와 사용자 ID를 모델에 추가
        if (project.isPresent()) {
            model.addAttribute("projectId", project.get().getId());
        } else {
            model.addAttribute("errorMessage", "관련 프로젝트를 찾을 수 없습니다.");
        }

        if (authentication != null) {
            model.addAttribute("userId", authentication.getName());
        }

        return "fundingPage"; // `projects/funding.html` 파일로 이동
    }








    
    
    
    
    
}
