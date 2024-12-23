/**
 * 
 */
function usernameCheck(){
	
	const username=document.getElementById("username").value;
	
	if(!username){
		alert("아이디를 입력하세요");
		return;
	}
	
	fetch(`/check-username?username=${encodeURIComponent(username)}`, {
		method:"GET"
	})
	.then(response=>response.json())
	.then(data=>{
		if (data.exists){
			alert("이미 사용중인 아이디입니다.")
		} else{
			alert("사용 가능한 아이디 입니다")
		}
	})
	.catch(error=>{
		console.error("Error checking username",error);
		alert("아이디 중복 확인 중 오류가 발생했습니다.");
	});
	

}