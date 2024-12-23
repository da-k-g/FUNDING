// CKEditor 이미지 업로드 설정
     CKEDITOR.replace('description', {
         filebrowserUploadUrl: '/ckeditor/uploadImage', 
		 height: 450
     });

 
	 
  // 이미지 미리보기
  function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function() {
      const output = document.getElementById('preview');
      output.src = reader.result;
      output.style.display = 'block';  // 이미지 미리보기 표시
    };
    reader.readAsDataURL(event.target.files[0]);
  }