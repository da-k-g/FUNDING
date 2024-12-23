package com.web.controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.domain.Chapter;
import com.web.domain.Novel;
import com.web.domain.User;
import com.web.dto.ChapterDTO;
import com.web.dto.NovelDTO;
import com.web.service.ChapterService;
import com.web.service.NovelService;
import com.web.service.UserService;
import com.web.utils.PageMaker;
import com.web.utils.UploadSetting;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// 회차 관련 작업을 처리하는 컨트롤러 클래스
@Controller
@RequestMapping("/chapters") // "/chapters"로 시작하는 URL 매핑
@RequiredArgsConstructor // 생성자를 통한 의존성 주입
public class ChapterController {

    private final NovelService novelService; // 소설 관련 서비스

    private final UserService userService; // 사용자 관련 서비스
       
    private final ChapterService chapterService; // 회차 관련 서비스
    
    
    @Value("${file.upload-dir}") 
    private String uploadDir; // 파일 업로드 디렉토리 생성
   

    
    // 회차 등록 폼 메서드
    @GetMapping("/{novelId}/new")
    public String showCreateChapterForm(@PathVariable Long novelId, Authentication authentication, Model model) {
        Novel novel = novelService.findById(novelId); // 소설 정보 조회   
        ChapterDTO chapterDTO =ChapterDTO.builder().paid(false).build(); // 초기 DTO 설정
    	model.addAttribute("chapterDTO", chapterDTO); // 뷰로 전당
        
        model.addAttribute("novel", NovelDTO.toDto(novel)); // 소설 데이터 DTO로 변환 후 전환
        return "chapters/create"; // 회차 등록 폼 탬플릿 반환
    }

    
    
    // 회차 등록 처리
    @PostMapping("/{novelId}")
    public String createChapter(@PathVariable Long novelId, 
    		@ModelAttribute @Valid ChapterDTO chapterDTO, BindingResult bindingResult, 
    		Authentication authentication, Model model) throws Exception{
    	
    	Novel novel = novelService.findById(novelId); // 소설 정보 조회
    	model.addAttribute("novel", NovelDTO.toDto(novel)); // 뷰로 소설 데이터 전달
    	
       	User user=userService.findByEmail(authentication.getName()); // 현재 로그인 된 사용자 이름가져와서 사용자 정보 조회
    	// user email이 null인 경우
       	if (user == null) {
    	        throw new IllegalAccessException("로그인된 사용자 정보를 찾을 수 없습니다."); // 사용자 인증 실패 예외 처리 메세지
    	}
    	
     	if(bindingResult.hasErrors()) {    		
     		model.addAttribute("errors", bindingResult.getAllErrors()); // 유효성 검증 오류 전달
     		return "chapters/write"; // 오류 발생시 등록 페이지로 이동
     	}
     	
     	try {    		
    		if(!chapterDTO.getCoverImage().isEmpty()) { // 커버 이미가 있는 경우 파일 저장 처리
    			String fileName =UUID.randomUUID().toString() +"_" +chapterDTO.getCoverImage().getOriginalFilename(); // 고유 파일명 생성(같은 이미지 업로드 충돌 방지)
    			Path path=Paths.get(UploadSetting.directory(uploadDir)+fileName);  // 저장 경로 설정 ("/uploads/UUID_파일명.확장자")
    			Files.createDirectories(path.getParent());// 경로 없으면 생성
    			Files.write(path,chapterDTO.getCoverImage().getBytes());// 파일 저장
    			
    			String fileUrl = "/uploads/" + fileName; // 파일 출력용 URL 생성
    			chapterDTO.setThumbnailImageUrl(fileUrl); // DTO에 파일 URL 저장    	
    		}    	
    	
    		//회차저장
    		chapterService.save(novelId, chapterDTO, user); 
    		
    	}catch (Exception e) {
    		 bindingResult.reject("fileUploadError", "파일 업로드 중 문제가 발생했습니다."); // 업로드 오류 처리
    		 return "novels/write"; // 소설 등록(write)페이지로 이동
		}    	
     	
        
        return "redirect:/chapters/" + novelId; // 회차 목록 페이지로 리다이렉트
    }

    
    
    
    // 회차 목록 보기
    @GetMapping("/{novelId}")
    public String listChapters(@PathVariable Long novelId, PageMaker pageMaker,Model model) {
        Novel novel = novelService.findById(novelId); // 소설 ID로 소설 조회
        
        
        Page<Chapter> items =chapterService.listChapters(novelId , pageMaker.getPageable(pageMaker, 6)); // 페이지네이션 처리
        String pagination = pageMaker.pageObject(items, pageMaker.currentPage(pageMaker), 6, 5, "/chapters/"+novel.getId(), "href");//  페이지네이션 탬플릿 생성
        List<Chapter> content = items.getContent();	// 회차 데이터 가져오기
        
        
        model.addAttribute("novel", novel); // 소설 데이터 전달
        model.addAttribute("chapters", content); // 회차 목록 전달
   	 	model.addAttribute("pagination", pagination); // 페이지네이숀 데이터 전달
        return "chapters/list"; // 목록 탬플릿 반환
    }


    /**
     *  회차 상세보기
     * @param chapterId
     * @param model
     * @return
     */
    @GetMapping("/detail/{chapterId}")
    public String chapterDetail(@PathVariable Long chapterId, Model model) {
        Chapter chapter = chapterService.chapterDetail(chapterId); // 회차 상세 정보 조회
               
        Novel novel = chapter.getNovel(); // 소설 정보 조회
        model.addAttribute("novel", novel); // 소설 데이터 전달
        model.addAttribute("chapter", chapter); // 회차 데이터 전달
        return "chapters/view"; // 상세보기 탬플릿(HTML화면) 반환
    }
    
    
    
    
    /**
     *  회차 수정 폼
     * @param chapterId
     * @param model
     * @return
     */
    @GetMapping("/modify/{chapterId}")
    public String showmodifyChapterForm(@PathVariable Long chapterId, Model model) {
        Chapter chapter = chapterService.findById(chapterId); // 수정할 회차 데이터 조회
        Novel novel = chapter.getNovel(); //  소설 정보 조회
        model.addAttribute("novel", NovelDTO.toDto(novel)); // 소설 데이터 전달
        model.addAttribute("chapterDTO", ChapterDTO.toDto(chapter)); //회차 데이터 전달
        return "chapters/modify"; // 회차 수정 폼 템플릿
    }
    
    

    // 회차 수정 처리
    @PostMapping("/modify/{chapterId}")
    public String modifyChapter(@PathVariable Long chapterId, 
    		@ModelAttribute @Valid ChapterDTO chapterDTO, BindingResult bindingResult, 
    		Authentication authentication, Model model) throws Exception {   
    	chapterDTO.setId(chapterId); // DTO에 ID 설정
    	Chapter chapter = chapterService.findById(chapterId); // 기존 회차 정보 조회
    	Novel novel = chapter.getNovel(); // 소설 정보 조회
    	

    	model.addAttribute("novel", NovelDTO.toDto(novel)); // 뷰로 소설 데이터 전달
    	
       	User user=userService.findByEmail(authentication.getName()); // 현재 사용자 정보 조회
       	// user의 emaill이 null값이 경우 "메세지" 출력
    	if (user == null) {
    	        throw new IllegalAccessException("로그인된 사용자 정보를 찾을 수 없습니다."); // 사용자 인증 실패시 예외처리
    	}
    	
     	if(bindingResult.hasErrors()) {    		
     		model.addAttribute("errors", bindingResult.getAllErrors()); // 유효성 검증 오류 전달
     		return "chapters/modify"; // 오류 발생시 수정 폼으로 이동
     	}
     	
     	try {    		
    		if(!chapterDTO.getCoverImage().isEmpty()) { // 커버 이미지가 있으면 파일 저장 처리, 없는 경우는 오류 남
    			String fileName =UUID.randomUUID().toString() +"_" +chapterDTO.getCoverImage().getOriginalFilename(); // 고유의 파일명 생성
    			Path path=Paths.get(UploadSetting.directory(uploadDir)+fileName); // 저장경로 설정(db에 저장되는 경로)
    			Files.createDirectories(path.getParent());// 경로 없으면 생성
    			Files.write(path,chapterDTO.getCoverImage().getBytes());// 파일 저장
    			
    			String fileUrl = "/uploads/" + fileName; // 파일 출력용 URL 생성
    			chapterDTO.setThumbnailImageUrl(fileUrl); // DTO에 파일 URL저장   	
    		}    	
    	
    		// 수정한 회차 저장
    		chapterService.modifyChapter(novel, chapterDTO, user); 
    		
    	}catch (Exception e) {
    		 e.printStackTrace();
    		 bindingResult.reject("fileUploadError", "파일 업로드 중 문제가 발생했습니다."); // 업로드 오류 처리
    		 return "chapters/modify"; // 회차수정 탬플릿 반환
		}    	
     	
        return "redirect:/chapters/" + novel.getId(); // 성공 시 목록 페이지로 리다이렉트
    	
    	
    }

    
    /**
     * 회차 삭제 처리
     * @param chapterId
     * @return
     */
    @DeleteMapping("/delete/{chapterId}")
    public ResponseEntity<?> deleteChapter(@PathVariable Long chapterId) {
        chapterService.deleteChapter(chapterId); // 회차 삭제
        return ResponseEntity.ok().build(); // 성공 응답 반환(삭제 완료)
    }

}
