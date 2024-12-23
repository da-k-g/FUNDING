package com.web.utils;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Data
@ToString
public class PageMaker {
    // 페이지 관련된 설정 및 계산을 관리하는 클래스

    private Integer page; // 현재 페이지 번호
    private int pageSize = 10; // 한 페이지에 보여줄 데이터 개수
    private int pageStart; // 현재 페이지 시작 인덱스
    private int totalCount; // 전체 데이터 개수
    private int startPage; // 페이지 네비게이션에서의 시작 페이지 번호
    private int endPage; // 페이지 네비게이션에서의 끝 페이지 번호
    private boolean prev; // 이전 버튼 활성화 여부
    private boolean next; // 다음 버튼 활성화 여부
    private boolean last; // 마지막 페이지 여부
    private int displayPageNum = 10; // 하단 네비게이션에 표시할 페이지 개수
    private int tempEndPage; // 계산된 전체 페이지 수
    private String searchQuery; // 검색 쿼리

    Page<?> pageObject; // Spring Data의 페이지 객체로, 데이터 페이징 처리를 담당

    // 페이지 네비게이션 데이터를 계산하는 메서드
    private void calcData() {
        endPage = (int) (Math.ceil(page / (double) displayPageNum) * displayPageNum); 
        // 현재 페이지가 속한 블록의 마지막 페이지 계산
        startPage = (endPage - displayPageNum) + 1; 
        // 블록의 시작 페이지 계산

        if (endPage >= tempEndPage) endPage = tempEndPage; 
        // 계산된 끝 페이지가 실제 전체 페이지 수를 초과하면 조정
        prev = startPage == 1 ? false : true; 
        // 시작 페이지가 1이면 이전 버튼 비활성화
        next = endPage * pageSize >= totalCount ? false : true; 
        // 끝 페이지가 전체 데이터를 다 포함하면 다음 버튼 비활성화
    }

    // 현재 페이지를 반환, 페이지가 null인 경우 기본값 0 반환
    public int currentPage(PageMaker pageMaker) {
        return pageMaker.getPage() == null ? 0 : pageMaker.getPage();
    }

    // Spring Data의 PageRequest를 생성
    public PageRequest getPageable(PageMaker pageMaker, Integer rowNumber) {
        int pageInt = currentPage(pageMaker); 
        // 현재 페이지 번호 계산
        PageRequest pageable = PageRequest.of(pageInt, rowNumber); 
        // PageRequest 객체 생성
        return pageable;
    }

    /**
     * 페이징 관련 HTML 또는 JavaScript를 생성하는 메서드
     * @param pageObject 페이징 처리된 데이터
     * @param pageInt 현재 페이지 번호
     * @param pageSize 한 페이지 데이터 개수
     * @param displayPageNum 네비게이션에 표시할 페이지 개수
     * @param pageUrl 페이지 이동 URL
     * @param type 네비게이션 방식(JS, HREF, PATHVARIABLE)
     * @return HTML 또는 JavaScript 코드
     */
    public String pageObject(Page<?> pageObject, Integer pageInt, Integer pageSize, Integer displayPageNum, String pageUrl, String type) {
        this.pageObject = pageObject;
        this.page = pageInt == 0 ? 1 : pageInt + 1;
        if (pageSize != null) {
            this.pageSize = pageSize;
        }
        this.tempEndPage = pageObject.getTotalPages(); 
        // 전체 페이지 수
        if (displayPageNum != null) {
            this.displayPageNum = displayPageNum;
        } else this.displayPageNum = 10;

        this.totalCount = Math.toIntExact(pageObject.getTotalElements()); 
        // 전체 데이터 개수
        calcData(); // 페이지 데이터 계산

        if (StringUtils.hasText(pageUrl)) {
            if (type.equalsIgnoreCase("JS")) {
                return paginationJs(pageUrl);
            } else if (type.equalsIgnoreCase("HREF")) {
                return paginationHref(pageUrl);
            } else if (type.equalsIgnoreCase("PATHVARIABLE")) {
                return paginationPathVariable(pageUrl);
            }
        }
        return null;
    }

    // JavaScript 기반 페이지 네비게이션 생성
    public String paginationJs(String url) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("<ul class='pagination justify-content-center'>");
        if (prev) {
            sBuffer.append("<li class='page-item'><a class='page-link' onclick='javascript:page(0)'>처음</a></li>");
        }
        if (prev) {
            sBuffer.append("<li class='page-item'><a class='page-link' onclick='javascript:page(" + (startPage - 2) + ")'>&laquo;</a></li>");
        }

        String active = "";
        for (int i = startPage; i <= endPage; i++) {
            active = (page == i) ? "class='page-item active'" : "class='page-item'";
            sBuffer.append("<li " + active + ">");
            sBuffer.append("<a class='page-link' onclick='javascript:page(" + (i - 1) + ")'>" + i + "</a></li>");
        }

        if (next && endPage > 0 && endPage <= tempEndPage) {
            sBuffer.append("<li class='page-item'><a class='page-link' onclick='javascript:page(" + (endPage) + ")'>&raquo;</a></li>");
        }
        if (next && endPage > 0 && !isLast()) {
            sBuffer.append("<li class='page-item'><a class='page-link' onclick='javascript:page(" + (tempEndPage - 1) + ")'>마지막</a></li>");
        }
        sBuffer.append("</ul>");
        return sBuffer.toString();
    }

    // 검색 쿼리를 포함한 링크 생성
    public String makeSearch(int page) {
        UriComponents uriComponents =
                UriComponentsBuilder.newInstance()
                        .queryParam("searchQuery", searchQuery)
                        .queryParam("page", page)
                        .build();
        return uriComponents.toUriString();
    }

    // HREF 방식의 페이지 네비게이션 생성
    public String paginationHref(String url) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("<ul class='pagination justify-content-center'>");
        if (prev) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + makeSearch(1) + "'>처음</a></li>");
        }
        if (prev) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + makeSearch(startPage - 2) + "'>&laquo;</a></li>");
        }

        String active = "";
        for (int i = startPage; i <= endPage; i++) {
            if (page == i) {
                active = "class='page-item active'";
                sBuffer.append("<li " + active + ">");
                sBuffer.append("<a class='page-link' href='javascript:void(0)'>" + i + "</a></li>");
            } else {
                active = "class='page-item'";
                sBuffer.append("<li " + active + ">");
                sBuffer.append("<a class='page-link' href='" + url + makeSearch(i - 1) + "'>" + i + "</a></li>");
            }
        }

        if (next && endPage > 0 && endPage <= tempEndPage) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + makeSearch(endPage) + "'>&raquo;</a></li>");
        }
        if (next && endPage > 0 && !isLast()) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + makeSearch(tempEndPage - 1) + "'>마지막</a></li>");
        }
        sBuffer.append("</ul>");
        return sBuffer.toString();
    }

    // PathVariable 기반 페이지 네비게이션 생성
    public String paginationPathVariable(String url) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("<ul class='pagination justify-content-center'>");
        if (prev) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + (1) + "'>처음</a></li>");
        }
        if (prev) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + (startPage - 2) + "'>&laquo;</a></li>");
        }

        String active = "";
        for (int i = startPage; i <= endPage; i++) {
            if (page == i) {
                active = "class='page-item active'";
                sBuffer.append("<li " + active + ">");
                sBuffer.append("<a class='page-link' href='javascript:void(0)'>" + i + "</a></li>");
            } else {
                active = "class='page-item'";
                sBuffer.append("<li " + active + ">");
                sBuffer.append("<a class='page-link' href='" + url + (i - 1) + "'>" + i + "</a></li>");
            }
        }

        if (next && endPage > 0 && endPage <= tempEndPage) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + (endPage) + "'>&raquo;</a></li>");
        }
        if (next && endPage > 0 && !isLast()) {
            sBuffer.append("<li class='page-item'><a class='page-link' href='" + url + (tempEndPage - 1) + "'>마지막</a></li>");
        }
        sBuffer.append("</ul>");
        return sBuffer.toString();
    }
}