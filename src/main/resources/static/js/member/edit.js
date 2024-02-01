$(function () {
    // 정규표현식 활용
    const getIdCheck = RegExp(/^[a-zA-Z0-9]{4,14}$/);
    const getPwCheck = RegExp(/([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])/);
    const getNameCheck = RegExp(/^[가-힣]+$/);

    // 입력값 중 하나라도 만족하지 못한다면 회원 가입 처리를 막기 위한 논리형 변수 선언
    let chk1 = false, chk2 = false, chk3 = false, chk4 = false;

    // 1. ID 입력값 검증
    $('#account').keyup(function () {
        if ($(event.target).val() === '') {
            $(event.target).css('background', 'pink');
            $('#idChk').html('<b style="font-size: 14px; color:blue">[아이디는 필수 정보입니다.]</b>');
            chk1 = false;
        } else if (!getIdCheck.test($(event.target).val())) {
            $(event.target).css('background', 'pink');
            $('#idChk').html('<b style="font-size: 14px; color:blue">[영문과 숫자 조합으로 4~14자 조합해주세요.]</b>');
            chk1 = false;
        } else {
            const checkId = $(event.target).val();

            // AJAX 호출
            $.ajax({
                type: 'POST',
                url: '/blog/member/checkId',
                data: checkId,
                contentType: "application/json; charset=utf-8;",
                success: function (result) {
                    console.log('통신 성공 ' + result);
                    if (result === 'Available') {
                        $('#user_id').css('background', 'aqua');
                        $('#idChk').html('<b style="font-size: 14px; color:skyblue">[작성하신 아이디는 사용 가능합니다.]</b>');
                        chk1 = true;
                    } else {
                        $('#user_id').css('background', 'pink');
                        $('#idChk').html('<b style="font-size: 14px; color:blue">[아이디가 중복되었습니다.]</b>');
                        chk1 = false;
                    }
                },
                error: function (status, error) {
                    console.log('통신 실패');
                    console.log(status, error);
                }
            });
        }
    });


    // 2. PW 입력값 검증
    $('#password').keyup(function () {
        if ($(event.target).val() === '') {
            $(event.target).css('background', 'pink');
            $('#pwChk1').html('<b style="font-size: 14px; color:blue">[비밀번호를 입력해주세요]</b>');
            chk2 = false;
        } else if (!getPwCheck.test($(event.target).val()) || $(event.target).val().length < 8) {
            $(event.target).css('background', 'pink');
            $('#pwChk1').html('<b style="font-size: 14px; color:blue">[비밀번호는 특수문자 포함 8자 이상입니다.]</b>');
            chk2 = false;
        } else {
            $(event.target).css('background', 'aqua');
            $('#pwChk1').html('<b style="font-size: 14px; color:skyblue">[비밀번호 입력 확인 완료.]</b>');
            chk2 = true;
        }
    });

    // 3. PW 확인란 검증
    $('#password_check').keyup(function () {
        if ($(event.target).val() === '') {
            $(event.target).css('background', 'pink');
            $('#pwChk2').html('<b style="font-size: 14px; color:blue">[비밀번호 확인은 필수 정보 입니다.]</b>');
            chk3 = false;
        } else if ($(event.target).val() !== $('#password').val()) {
            $(event.target).css('background', 'pink');
            $('#pwChk2').html('<b style="font-size: 14px; color:blue">[입력한 비밀번호가 일치하지 않습니다.]</b>');
            chk3 = false;
        } else {
            $(event.target).css('background', 'aqua');
            $('#pwChk2').html('<b style="font-size: 14px; color:skyblue">[비밀번호 입력 확인 완료.]</b>');
            chk3 = true;
        }
    });

    // 4. 이름 입력값 검증
    $('#name').keyup(function () {
        if ($(event.target).val() === '') {
            $(event.target).css('background', 'pink');
            $('#nameChk').html('<b style="font-size: 14px; color:blue">[이름은 필수 입력값입니다.]</b>');
            chk4 = false;
        } else if (!getNameCheck.test($(event.target).val())) {
            $(event.target).css('background', 'pink');
            $('#nameChk').html('<b style="font-size: 14px; color:blue">[이름은 한글만 작성가능합니다.]</b>');
            chk4 = false;
        } else {
            $(event.target).css('background', 'aqua');
            $('#nameChk').html('<b style="font-size: 14px; color:skyblue">[이름 입력 확인 완료.]</b>');
            chk4 = true;
        }
    });

    // 사용자가 회원 가입 버튼을 눌렀을 때의 이벤트 처리
    // 사용자가 입력하는 4가지 데이터 중 단 하나라도 문제가 있으면 회원가입 처리하면 안됨
    $('#signup-btn').click(function () {
        if (chk1 && chk2 && chk3 && chk4) {

            const memberId = $('#memberId').val();
            const account = $('#account').val();
            const currentPw = $('#currentPw').val();
            const pw = $('#password').val();
            const name = $('#name').val();
            const email = $('#email').val();


            const user = {
                'account': account,
                'currentPw': currentPw,
                'password': pw,
                'name': name,
                'email': email
            };

            $.ajax({
                type: 'POST',
                url: '/blog/member/edit/' + memberId,
                contentType: "application/json; charset=utf-8;",
                data: JSON.stringify(user),
                success: function (result) {
                    if (result) {
                        console.log('통신 성공 : ' + result);
                        alert("회원정보가 수정되었습니다.");
                        location.href = '/blog/member/details/'+memberId;
                    } else {
                        alert("비밀번호가 올바르지 않습니다.");
                        $('#currentPw').focus();
                    }
                },
                error: function (error) {
                    alert(JSON.stringify(error));
                }
            });
        } else {
            alert('입력 정보를 다시 확인하세요.');
        }
    });
});