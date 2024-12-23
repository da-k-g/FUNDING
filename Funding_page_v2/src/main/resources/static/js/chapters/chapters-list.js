
async function deleteChapters(event) {
	const id= event.target.getAttribute("data-id");
	
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }
    try {
        const csrfToken = document.querySelector('input[name="_csrf"]').value;
 
        const response = await fetch(`/chapters/delete/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            }
        });
        if (response.ok) {
            alert('소설 회차가 성공적으로 삭제되었습니다.');
            location.reload();
			
        } else {
            const error = await response.json();
            alert(`삭제 실패: ${error.message}`);
        }
    } catch (error) {
        console.error('삭제 요청 중 오류 발생:', error);
        alert('삭제 요청 중 오류가 발생했습니다.');
    }
}

