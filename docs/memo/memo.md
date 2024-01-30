## 로그인한 경우와 그렇지 않은경우 태그 보일지 말지 설정할 수 있다
````agsl
  <li sec:authorize="isAnonymous()" class="nav-item">
                    <a class="nav-link" href="/blog/login">로그인</a>
                </li>
                <li sec:authorize="isAnonymous()" class="nav-item">
                    <a class="nav-link" href="/blog/member/join">회원가입</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/blog/board/createBoard">글쓰기</a>
                </li>
                <li sec:authorize="isAuthenticated()" class="nav-item">
                    <a class="nav-link" href="/blog/logout">로그아웃</a>
                </li>
````


## input태그에 값을 채우기 위해선 th:value를 써야한다
````agsl
  <label for="memberId"></label>
        <input type="text" id="memberId" th:value="${#authentication.principal.member.id}"/>
        <label for="boardId"></label>
        <input type="text" id="boardId" th:value="*{id}"/>
````

## th:text = | *{} |을 사용할 수 있다.
````agsl
        <p class="card-title" th:text="|글번호: *{id} writer: *{writer}|">writer</p>
````

## 다음과 같이 폼 데이터를 전송할 수 있다. 입력 여부에 따라서 분기할 수 있다.
````agsl
function signin(){
    const email = $("#name").val();
    const password = $("#password").val();

    if(email.trim() == '' || password.trim() == ''){
        alert('아이디 또는 비밀번호를 입력해주세요');
        return false;
    }
    $('form[name="login"]').submit();
}
````

## a가 아닌 곳에 onclick으로 링크 걸기
가장 자주 쓰는 html 태그를 예로 들었을 뿐
tr td뿐만 아니라 a링크 태그가 아닌 모든 속성의 태그에 쓰이는 방법입니다.
section article div p strong u em 뭐 등등등... 전부 다 가능하겠죠.
css로 링크인걸 알려주는 센스정도는 보여주셔야겠죠?

## 타임리프에서 경로를 작성할 때는 @{안에 작성}

## button의 기본 type은 submit이다. 이 경우 브라우저 콘솔 확인하지 못하게 된다.



## 타임리프에서 인증객체에 접근할 수 있는 방법 2가지
````agsl
th:value="${#authentication.principal.member.id}
<div sec:authentication="principal.member.id"></div>

````

## 타임리프에서 쿼리파라미터에 다음과 같이 접근할 수 있다. th:text="${param.message}"
````agsl
<div class="form-group message" th:if="${#strings.toString(param.hasMessage)} == 'true'" th:text="${param.message}">
            아이디와 비밀번호를 확인해주세요
//         
         
         @GetMapping("/login")
    public String login(@RequestParam(required = false) boolean hasMessage,
                        @RequestParam(required = false) String message,
                        @RequestParam(required = false) String returnUrl,
                        HttpServletRequest request,
                        Model model){
        HttpSession session = request.getSession();
        session.setAttribute("returnUrl", returnUrl);

        model.addAttribute("hasMessage", hasMessage);
        model.addAttribute("message", message);
        return "login/loginForm";
    }
````
### 위와 아래의 차이점은 model에 boolean타입 hasMessage객체와 String타입 message객체 담아서 넘겼을 경우 아래와 같이 가능하다.
````agsl
  <div class="message" th:if="${hasMessage == true}" th:text="${message}">
````


## ajax의 리턴타입과 컨트롤러의 리턴 타입 생각하기
````agsl
 @PostMapping("/board/reply/{boardId}")
    @ResponseBody
    public ResponseDto<Integer> addReply(@RequestBody ReplyRequestDto replyRequestDto) {
        boardService.addReply(replyRequestDto.getMemberId(), replyRequestDto.getBoardId(), replyRequestDto.getContent());
        return new ResponseDto<Integer>(HttpStatus.OK, 1);
    }

// 자바스크립트 코드
      replySave: function () {
        let data = {
            memberId: $("#memberId").val(),
            boardId: $("#boardId").val(),
            content: $("#reply-content").val()
        };

        $.ajax({
            type: "POST",
            url: `/blog/board/reply/${data.boardId}`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",

        }).done(function (resp) {
            console.log("성공", resp);
            alert("댓글작성이 완료되었습니다.");
            location.href = `/blog/board/details/${data.boardId}`;
        }).fail(function (error) {
        console.log("실패");
            alert(JSON.stringify(error));
        });
    },
````

## json 타입 변환되는 객체는 기본생성자가 있어야한다.



## jpa :  join은 없으면 아무것도 가져오지 않는다. leftJoin은 null일경우도 일단 왼쪽의 경우 모두 불러온다.
````agsl
 @Override
    public List<ReplyResponseDto> findReplys(Long boardId) {

        QReply reply = QReply.reply;

        List<Tuple> tuples = query.select(reply.content, reply.member.name)
                .from(reply)
                .join(reply.member, QMember.member)
                .where(reply.board.id.eq(boardId))
                .fetch();

        log.info("tuples = {}", tuples);

        List<ReplyResponseDto> replys = new ArrayList<>();
        for (Tuple tuple : tuples) {
            String replyAuthor = tuple.get(reply.member.name);
            String content = tuple.get(reply.content);
            replys.add(new ReplyResponseDto(replyAuthor, content));
        }

        return replys;
    }
````


## ajax로 서버에 post요청시 자바스크립트 객체의 필드명과 자바에서 객체로 받는 필드의 이름이 같아야 매핑이 된다

````agsl
  replySave: function () {
        let data = {
            memberId: $("#memberId").val(), //이부분
            boardId: $("#boardId").val(),
            content: $("#reply-content").val()
        };

        $.ajax({
            type: "POST",
            url: `/blog/board/reply/${data.boardId}`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",

        }).done(function (resp) {
            console.log("성공", resp);
            alert("댓글작성이 완료되었습니다.");
            location.href = `/blog/board/details/${data.boardId}`;
        }).fail(function (error) {
        console.log("실패");
            alert(JSON.stringify(error));
        });
    },
````

## <script src="https://code.jquery.com/jquery-3.5.1.js"></script> 메인 레이아웃에만 추가하면 되나??
네
## ajax로 서버에 데이터 전송시 객체가 아닌 String을 전송할 때는 JSON.stringify()를 하면 "" 가 붙는다 -> 그냥 string을 보내자
````agsl
 $.ajax({
                type: 'POST',
                url: '/blog/member/checkId',
                data: checkId, //-> data로 그냥 String을 보냄
                contentType: "application/json; charset=utf-8;",
                success: function (result) {
                    console.log('통신 성공 ' + result);
                    if (result === 'Available') {
                        $('#user_id').css('background', 'aqua');
                        $('#idChk').html('<b style="font-size: 14px; color:skyblue">[작성하신 아이디는 사용 가능합니다.]</b>');
                        chk1 = true; 
              
       //
               @PostMapping("/member/checkId")
    @ResponseBody
    public String checkId(HttpEntity<String> httpEntity) {
        System.out.println("httpEntity.getBody() = " + httpEntity.getBody());
        return "Available";
    }          
       //헤더 바디  httpEntity.getBody() = aaa1
````


## callAjax를 이용하지 않는 경우 다음과 같이 코드를 작성. hidden의 input태크에있는 id="boardId"를 참조하여 url을 생성
````agsl

  replySave: function () {
        let data = {
            memberId: $("#memberId").val(),
            boardId: $("#boardId").val(),
            content: $("#reply-content").val(),
        };

        $.ajax({
            type: "POST",
            url: `/blog/board/reply/${data.boardId}`,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",

        }).done(function (resp) {
            console.log("성공", resp);
            alert("댓글작성이 완료되었습니다.");
            location.href = `/blog/board/details/${data.boardId}`;
        }).fail(function (error) {
        console.log("실패");
            alert(JSON.stringify(error));
        });
    },

````

## callAjax를 메소드 분리하여 파라미터로  전송 방식 ,targetUrl,  data객체를 넘겨준다.
- 주의할 점은 다음과 같이 Content-type을 설정하지 않을 경우  `application/x-www-form-urlencoded`로 기본 설정 되어 서버로 전송되고,
- 컨트롤러에서 받기 위해서는 @RequestBody가 있어서는 안된다. -> 그래야 @ModelAttribute, @RequstParam를 적용하여 객체 또는 파라미터로 받을 수 있다
- 밑에 코드를 보면 성공시 반환되는 객체가 success : function의 data로 넘어오는데 message를 넘겨주어 alert를 사용할 수 있다.
````agsl
replySave: function () {
		let data = {
			memberId: $("#memberId").val(),
			boardId: $("#boardId").val(),
			content: $("#reply-content").val(),
		};

		callAjax("POST", "/blog/board/reply/${data.boardId}",data)
	},
	

function callAjax(method, url, data) {

    $.ajax({
        method: method,
        url: url,
        data: data,
        success: function (data) {
            console.log(data);
            alert(data.message)
            location.reload();
        },
        error: function (data) {
            if(data.status == 401){
                const returnUrl = window.location.href;
                location.href = '/blog/login?returnUrl='+returnUrl;
                return false;
            }
            if(data.status == 403){
                const returnUrl = window.location.href;
                location.href = '/blog/login?returnUrl='+returnUrl;
                return false;
            }
            const response = data.responseJSON;
            console.log(response);
            alert(response.message);
            location.reload();
        }
    });
}
````

## 따라서 응답객체를 다음과 같이 정의하자 BaseResponse
- restController의 경우 응답객체를 ResponseEntity를 리턴하되 응답객체 BaseResoponse를 body에 담아서 상태코드와 함꼐 보낸다.
````agsl

````

## ajax로 인증이 필요한 페이지 접근시 403 발생시키기 위한 코드 등록
- LoginUrlAuthenticationEntryPoint에서 기본적으로 인증이 안 되었을 경우 302 발생후 로그인을 위한 곳으로 리다이렉트한다.
- 다음 코드등록을 통해 ajax요청시 403에러코드를 발생시켜 예상루트로 흘러갈 수 있다
```agsl

public class AjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public AjaxAwareAuthenticationEntryPoint(String loginUrl) {
        super(loginUrl);
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
        if (isAjax) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Ajax REquest Denied (Session Expired)");
        } else {
            super.commence(request, response, authException);
        }

    }
}
// 다음 코드블록은 등록 부분
``
         .permitAll()
                )
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(ajaxAwareAuthenticationEntryPoint("/login")));

        return http.build();
```

## proxy객체를 로딩하기 위해 다음 라이브러리가 필요하다.
````agsl
 /**
     * 프록시 객체를 로딩하기 위한 라이브러리
     * @return
     */
    @Bean
    Hibernate5JakartaModule hibernate5Module() {
        Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
        hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
        return hibernate5JakartaModule;
    }

````

## 페이징 처리를 위한 객체를 만들었다
````agsl
@Getter
public class PagingDto {
    // 페이지 그룹에 최대로 담길 수 있는 페이지 수량
    protected final int MAXIMUM_PAGE_NUMBER_IN_PAGE_GROUP = 5;

    // 페이지 그룹에 담길 수 있는 페이지 수량
    protected int pageGroupSize;

    // 몇 개의 페이지 그룹이 있는지 수량
    protected int totalPageGroups;

    // 페이지 그룹의 번호
    protected int pageGroupNumber;
    // 현재 페이지 그룹 번호에서 시작하는 페이지 번호
    protected int startPageNumberInThisPageGroup;
    // 현재 페이지 그룹 번호에서 끝나는 페이지 번호
    protected int lastPageNumberInThisPageGroup;

    // 이전 페이지 번호
    protected int prevPageNumber;
    // 다음 페이지 번호
    protected int nextPageNumber;


    protected int calculateTotalPageGroups(int totalSearchResultCount) {
        // 총 블록 개수는 = 총 검색 결과 / 블록 사이즈 -> 소수점은 무조건 반올림 처리
        return (int) Math.ceil(totalSearchResultCount * 1.0 / pageGroupSize);
    }

    protected int calculatePageGroupNumber(int number) {
        // double형이 아닌, int형이므로 소수점은 자동 제거
        return number / pageGroupSize;
    }

    protected int calculateStartPageNumber() {
        return (pageGroupNumber) * pageGroupSize + 1;
    }

    protected int calculateLastPageNumber(int totalPages, int startPageNumberInThisPageGroup) {
        int tempLastPageNumber = startPageNumberInThisPageGroup + pageGroupSize - 1;
        return tempLastPageNumber < totalPages ? tempLastPageNumber : totalPages;
    }
}

//생성자를 보면 Page<BoarResponseDto> 페이지 객체를 받아서 PagingDto를 생성하는 것을 확인할 수 있다.
public class PagingBoardsDto extends PagingDto {

    public PagingBoardsDto(Page<BoardResponseDto> responses) {
        // Page 인터페이스의 페이지 총 수량 구함
        int totalPages = responses.getTotalPages();

        pageGroupSize = MAXIMUM_PAGE_NUMBER_IN_PAGE_GROUP;

        totalPageGroups = calculateTotalPageGroups(totalPages);
        pageGroupNumber = calculatePageGroupNumber(responses.getNumber());

        startPageNumberInThisPageGroup = calculateStartPageNumber();
        lastPageNumberInThisPageGroup = calculateLastPageNumber(totalPages, startPageNumberInThisPageGroup);

        prevPageNumber = responses.getNumber() - 1;
        nextPageNumber = responses.getPageable().getPageNumber() + 1;
    }
}

````

## 다음은 뷰에서 PagingDto와 List<Dto>를 활용하여 페이징을 나타낸 것. 타임리프에서 링크에 쿼리파라미터를 작성할 때 다음과 같이 괄호안에 작성할 수 있다.
````agsl
 <section th:if="${responses.totalElements ne 0}" class="mt-2 d-flex justify-content-center">
                <nav aria-label="Page navigation">
                    <ul class="pagination">
                        <li class="page-item" th:class="${responses.first} ? 'disabled'">
                            <a class="page-link" th:href="@{/(page=${paging.prevPageNumber})}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        <th:block th:with="start = ${paging.startPageNumberInThisPageGroup}, end = ${paging.lastPageNumberInThisPageGroup}">
                            <li class="page-item" th:each="num : ${#numbers.sequence(start, end)}" th:class="${responses.pageable.pageNumber eq num - 1} ? 'active' : ''">
                                <a class="page-link" th:href="@{/(page=${num} - 1)}" th:text="${num}">1</a>
                            </li>
                        </th:block>
                        <li class="page-item" th:class="${responses.last} ? 'disabled' : ''">
                            <a class="page-link" th:href="@{/(page=${paging.nextPageNumber})}" aria-label="Previous">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </section>
````

## th:if를 활용하여 인증객체의 id와 댓글 작성자의 id가 같은 경우 댓글 삭제버튼을 노출할 수 있다
## 그리고 타임리프를 사용하여 LocalDateTime객체 포맷팅을 할 수 있다.
````agsl
 <tr th:each=" reply : ${board.replys}">
                    <input type="hidden" id="replyId" th:value="${reply.replyAuthorId}" />
                    <td th:text="${reply.replyAuthor}">John</td>
                    <td th:text="${reply.content}">Doe</td>
                    <td class="date" th:text="${#temporals.format(reply.createdTime, 'yyyy-MM-dd HH:mm')}">Doe</td>
                    <th:block sec:authorize="isAuthenticated()">
                        <td th:if="${#authentication.principal.member.id == reply.replyAuthorId}" class="actions">
                            <button id="btn-reply-delete" onclick="index.replyDelete(${board.id}, ${reply.id})" class="delete-btn">Delete</button>
                        </td>
                    </th:block>
                </tr>
````
