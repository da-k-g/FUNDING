document.addEventListener("DOMContentLoaded", () => {
    const orderBySelect = document.getElementById("orderBy");

    if (orderBySelect) {
        orderBySelect.addEventListener("change", () => {
            // orderBy 선택 변경 시 폼 자동 제출
            const form = orderBySelect.closest("form");
            if (form) {
                form.submit();
            }
        });
    }
});



async function deleteNovel(event) {
	const id= event.target.getAttribute("data-id");
	
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }
    try {
        const csrfToken = document.querySelector('input[name="_csrf"]').value;
 
        const response = await fetch(`/novels/delete/${id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            }
        });
        if (response.ok) {
            alert('소설이 성공적으로 삭제되었습니다.');
            location.href="/novels";
        } else {
            const error = await response.json();
            alert(`삭제 실패: ${error.message}`);
        }
    } catch (error) {
        console.error('삭제 요청 중 오류 발생:', error);
        alert('삭제 요청 중 오류가 발생했습니다.');
    }
}

