
$(document).ready(function() {
    getVoteState();

    // 추천 버튼 클릭 이벤트
    $('#upvote-btn').click(function() {		
        voteAction(this, "UP");
    });

    // 비추천 버튼 클릭 이벤트
    $('#downvote-btn').click(function() {
        voteAction(this,"DOWN");
    });

});


function voteAction(e, voteType){
	
		  
    const novelId=$("#novelId").val();
    const username=$("#loginUserEmail").val();
    if(!username){
        alert('로그인후 이용 가능합니다.');
        return;
    }

	const token = $('meta[name="_csrf"]').attr('content');
    const header = $('meta[name="_csrf_header"]').attr('content');

		
    $.ajax({
        url: `/novels/like`,
        type: "post",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({novelId, voteType}),
		beforeSend: function (xhr) {
		                xhr.setRequestHeader(header, token);
		},
				
        success: function(result) {
        console.log("* 반환 처리값 :" ,result);

            if(result.code ===1) {
                $('#upvote-count').text(result.data.upvoteCount);
                $('#downvote-count').text(result.data.downvoteCount);
                $('.vote-container .btn').removeClass('active');

                if(result.data.message==="create"){
                    $(e).addClass('active');	
					upDownAlert(voteType);

                }else if(result.data.message==="update"){
                    $(e).addClass('active');
					upDownAlert(voteType)

                }else if(result.data.message==="delete"){
                    $(e).removeClass('active');					
					tWarning("취소 하였습니다." ,"알림");
                }

            } else{
                alert("추천 오류 입니다.");
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}





function upDownAlert(voteType){
	if(voteType==="UP"){
		tInfo("추천 하였습니다.", "알림");						
	}else{
		tError("비추천 하였습니다.", "알림");
	}									
	
}


function getVoteState(){

	const token = $('meta[name="_csrf"]').attr('content');
	const header = $('meta[name="_csrf_header"]').attr('content');
    const novelId=$("#novelId").val();
	const email=$("#loginUserEmail").val();
	if(!email)return;
	
	
	
    $.ajax({
        url: `/novels/${novelId}/like`,
        type: "GET",
        //data: {email},
		beforeSend: function (xhr) {
			 xhr.setRequestHeader(header, token);
		},		
        success: function(result) {
			console.log("*좋야요 상태 :" ,result);
			
            if(result.code ===1) {
                $('#upvote-count').text(result.data.upvoteCount);
                $('#downvote-count').text(result.data.downvoteCount);
                $('.vote-container .btn').removeClass('active');
				
                if(result.data.myVoted==="UP"){
                    $("#upvote-btn").addClass('active');
                }else if(result.data.myVoted==="DOWN"){
                    $("#downvote-btn").addClass('active');
                }
				
				
            } else{
                alert("추천 오류 입니다.");
            }
        },
        error: function(error) {
            console.log(error);
        }
    });
}



