toastr.options = {
  "closeButton": false,         // 알림 메시지에 닫기 버튼을 표시할지 여부
  "debug": false,               // 디버그 모드 설정 (보통 false로 설정)
  "newestOnTop": false,         // 새로운 알림을 화면의 상단에 표시할지 여부
  "progressBar": false,         // 알림 지속 시간 동안 진행 표시줄을 표시할지 여부
  "positionClass": "toast-top-right", // 알림 메시지의 위치 (화면의 오른쪽 상단)
  "preventDuplicates": false,   // 중복된 알림 메시지 방지 여부
  "onclick": null,              // 알림 메시지를 클릭했을 때 실행할 함수 (현재 null)
  "showDuration": "300",        // 알림이 나타나는 데 걸리는 시간 (밀리초 단위)
  "hideDuration": "1000",       // 알림이 사라지는 데 걸리는 시간 (밀리초 단위)
  "timeOut": "3000",            // 알림이 화면에 유지되는 시간 (밀리초 단위)
  "extendedTimeOut": "1000",    // 사용자가 마우스를 알림에 올렸을 때 추가로 유지되는 시간 (밀리초 단위)
  "showEasing": "swing",        // 알림이 표시될 때의 애니메이션 효과
  "hideEasing": "linear",       // 알림이 사라질 때의 애니메이션 효과
  "showMethod": "fadeIn",       // 알림이 나타날 때의 애니메이션 방법
  "hideMethod": "fadeOut"       // 알림이 사라질 때의 애니메이션 방법
}

// 경고 메시지를 표시하는 함수
function tWarning(title, content){
    toastr.remove();            // 기존의 알림 메시지를 제거
    toastr.clear();             // 알림 큐를 초기화

    if(title==""){              // 제목이 없는 경우 내용만 표시
      toastr.warning(content);  
    }else{                      // 제목과 내용을 모두 표시
      toastr.warning(title, content);   
    }           
}

// 정보 메시지를 표시하는 함수
function tInfo(title, content){
    toastr.remove();            // 기존의 알림 메시지를 제거
    toastr.clear();             // 알림 큐를 초기화
    if(title=="")               // 제목이 없는 경우 내용만 표시
        toastr.info(content);  
    else 
        toastr.info(title, content);           
}

// 성공 메시지를 표시하는 함수
function tSuccess(title, content){
    toastr.remove();            // 기존의 알림 메시지를 제거
    toastr.clear();             // 알림 큐를 초기화
    if(title=="")               // 제목이 없는 경우 내용만 표시
        toastr.success(content);
    else 
        toastr.success(title, content);        
}

// 오류 메시지를 표시하는 함수
function tError(title, content){
    toastr.remove();            // 기존의 알림 메시지를 제거
    toastr.clear();             // 알림 큐를 초기화
    if(title=="")               // 제목이 없는 경우 내용만 표시
        toastr.error(content); 
    else 
        toastr.error(title, content);          
}

// 확인 메시지를 표시하는 함수
function tConfirm(content){
    toastr.remove();            // 기존의 알림 메시지를 제거
    toastr.clear();             // 알림 큐를 초기화     
    toastr.warning(
        "<div class='text-center'><button type='button' id='confirmationRevertYes' class='mb-xs mt-xs mr-xs btn btn-sm btn-danger'>예</button>" +
        "&nbsp;&nbsp;&nbsp;<button type='button' id='confirmationRevertNo' class='mb-xs mt-xs mr-xs btn btn-sm btn-info'>아니오</button><div>",
        content,                
        {
          closeButton: false,   // 닫기 버튼을 표시하지 않음
          allowHtml: true,      // HTML 콘텐츠 허용
          onShown: function (toast) { // 알림이 표시된 후 실행될 동작 정의
              $("#confirmationRevertYes").click(function(){
                  alert("ok");  // "예" 버튼 클릭 시 실행
              });

              $("#confirmationRevertNo").click(function(){
                  alert("no");  // "아니오" 버튼 클릭 시 실행
              });             
            }
        }
    );
}
