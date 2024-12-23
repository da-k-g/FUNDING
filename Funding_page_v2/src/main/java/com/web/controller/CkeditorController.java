package com.web.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

//CKEditor와 연동하기 위한 컨트롤러 클래스입니다.
//파일 업로드 기능을 제공하여 CKEditor에서 이미지를 삽입할 수 있도록 합니다.
@Controller
@RequestMapping("/ckeditor")
public class CkeditorController {

    
    @Value("${file.upload-dir}") // 이미지 업로드 디렉토리 설정 (application.properties에서 설정한 이미지 경로에서 주입받음)
    private String uploadDir;
   
       
    // 업로드 디렉토리 경로 보정
    // 경로 끝에 '\'가 없다면 추가하여 일관성을 유지.
    private String getUploadDirectory() {
        return uploadDir.endsWith("\\") ? uploadDir : uploadDir + "\\";
    }

    // CKEditor 이미지 업로드 요청 처리
     @PostMapping(value="/uploadImage")
	 @ResponseBody
	 public Map<String, Object> uploadImage(HttpServletRequest request, @RequestParam("upload") MultipartFile upload ) throws Exception{
    	 	// 응답 데이터를 저장할 맵 객체
		     Map<String, Object> response=new HashMap<>();
		    try {
			    // 고유 파일명 생성(UUID + 원본 파일명)
		    	// UUID 전역적으로 고유한 식별자를 생성하기 위한 표준, 즉 같은 이미지를 올리더라도 중복된 경로가 저장되어 충돌되는 것을 방지하고자 사용
	            String fileName = UUID.randomUUID().toString() + "_" + upload.getOriginalFilename();
	            Path uploadPath = Paths.get(getUploadDirectory() + fileName);

	            // 디렉토리 생성 및 파일 저장
	            Files.createDirectories(uploadPath.getParent()); // 디렉토리가 없는 경우 자동 생성
	            Files.write(uploadPath, upload.getBytes()); // 파일 저장

	            // CKEditor가 필요로 하는 JSON 형식의 응답 데이터 생성
	            String fileUrl = "/uploads/" + fileName;
	            response.put("uploaded", 1); // 업로드 성공 플래그
	            response.put("fileName", fileName); // 저장된 파일명
	            response.put("url", fileUrl); // 파일 URL
	            
	            return response; // JSON 응답 반환
		    }catch (Exception e) {
		    	e.printStackTrace(); // 예외 출력
		    	  return null; // 예외 발생시 null 반환(즉, 이미지가 나오지 않음)
			}
		    // 이미지 URL(/uploads/4f9b8b39-12f3-4c56-bd1f_파일명.png)을 사용하여 에디터에 이미지 삽입.
	      
	 }
     
//
//    @PostMapping("/uploadImage")
//    @ResponseBody
//    public Map<String, Object>  uploadImage(@RequestParam("upload") MultipartFile file) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            // 파일명 생성
//            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//            Path uploadPath = Paths.get(getUploadDirectory() + fileName);
//
//            // 디렉토리 생성 및 파일 저장
//            Files.createDirectories(uploadPath.getParent());
//            Files.write(uploadPath, file.getBytes());
//
//            // CKEditor가 필요로 하는 JSON 형식의 응답 데이터 생성
//            String fileUrl = "/uploads/" + fileName;
//            response.put("uploaded", 1);
//            response.put("fileName", fileName);
//            response.put("url", fileUrl);
//
//	        
//            return response;
//        } catch (Exception e) {
//            response.put("uploaded", 0);
//            Map<String, String> error = new HashMap<>();
//            error.put("message", "파일 업로드 실패.");
//            response.put("error", error);
//
//            return response;
//        }
//    }

	
	
}
