function signin(){
    const account = $("#account").val();
    const password = $("#password").val();

    if(account.trim() == '' || password.trim() == ''){
        alert('아이디 또는 비밀번호를 입력해주세요');
        return false;
    }
    $('form[name="login"]').submit();
}

