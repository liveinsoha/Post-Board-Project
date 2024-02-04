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
## ajax로 서버에  JSON.stringify()를 하면 "" 가 붙는다 -> 그냥 string을 보내자
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
public class PagingBoardDto extends PagingDto {

    public PagingBoardDto(Page<BoardResponseDto> responses) {
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
                            <button id="btn-reply-delete" th:onclick="'index.replyDelete(' + ${board.id} + ', ' + ${reply.id} + ')'" class="delete-btn">Delete</button>
                        </td>
                    </th:block>
                </tr>
````

## 태그 속성에서 타임리프 문법을 사용할 경우 th:onclick을 사용해야 한다.
## javaScript함수로 넘길경우 ''로 감싸서 넘겨야 한다.
````agsl
<td th:if="${#authentication.principal.member.id == reply.replyAuthorId}" class="actions">
                            <button id="btn-reply-delete" th:onclick="'index.replyDelete(' + ${board.id} + ', ' + ${reply.id} + ')'" class="delete-btn">Delete</button>
                        </td>
                        
//여기서 작은따옴표(')는 JavaScript 문자열의 따옴표를 나타내며, ${board.id}의 값을 문자열에 삽입하기 위해 사용됩니다. 
// 예를 들어, board.id가 123이라면 이 부분은 JavaScript에서 문자열로 변환되어 '123'으로 대체됩니다.                        
           
                        
````

## 정렬방법을 바꿀 수 있고 셀렉트 박스에서 쿼리파라미터에 따라 동적으로 선택된 값을 변경할 수 있다
````agsl
$(document).ready(function () {
    var urlParams = new URLSearchParams(window.location.search);
    var selectedSortOrder = urlParams.get("sort");

    if (selectedSortOrder) {
        if (selectedSortOrder === 'createdTime,asc') {
            $("#sortOrder").val("asc").prop('selected', true);
        } else {
            $("#sortOrder").val("desc").prop('selected', true);
        }
    }
});
````

## 셀렉트 박스에서 고른값으로 url쿼리파라미터에 담아 정렬을 할 수 있다. 정렬될 경우 셀렉트 박스에서 선택된 값으로 selected처리할 수 있다.
## 
````agsl


$(document).ready(function () {
    var urlParams = new URLSearchParams(window.location.search);
    var selectedSortOrder = urlParams.get("sort");

    if (selectedSortOrder) {
        if (selectedSortOrder === 'createdTime,asc') {
            $("#sortOrder").val("asc").prop('selected', true);
        } else {
            $("#sortOrder").val("desc").prop('selected', true);
        }
    }
});
````
## 다시 정렬하고자 할경우 검색 조건이 증발하지 않으려면 현재 페이지에 담겨있는 검색 조건 정보를 담고, 정렬 파라미터를 추가해서 요청한다.
````agsl
function changeSortOrder() {
    var selectedSearchBy = document.getElementById("searchBy").value;
    var searchQuery = document.getElementById("searchQuery").value;

    // 현재 셀렉트 박스의 값과 검색어를 URL에 추가
    var urlParams = `?searchBy=${selectedSearchBy}&searchQuery=${searchQuery}`;
    var selectedSortOrder = $("#sortOrder").val();

    // 동적으로 href 생성
    var dynamicHref = urlParams + "&sort=" + selectedSortOrder;

    // 페이지 이동
    window.location.href = dynamicHref;
}
````


## 페이지 파라미터가 2개 이상일 경우 접두어로 구분
- 파라미터에 {접두어}_+"xxx" 로 설정
- 예) /members?member_page=0&order_page=1
- 
컨트롤러에서는 @Qualifier 어노테이션으로 접두어를 설정하여 구분. 하나의 요쳥으로 두 개 이상의 Pageable 또는 Sort 객체를 생성하는 경우에, @Qualifier 어노테이션을 사용해서 하나씩 구별할 수 있다. 그리고 요청 파라미터는 ${qualifier}_. 접두사가 있어야한다. 아래와 같은 경우 foo_page, bar_page 등의 파라미터가 있어야한다.

String showUsers(Model model,
@Qualifier("foo") Pageable first,
@Qualifier("bar") Pageable second) { … }
- 
````agsl
  @GetMapping("/member/myPage")
    public String myPage(Model model,
                         @Qualifier("board") @PageableDefault(size = 5, sort = "createdTime", direction = Sort.Direction.DESC) Pageable boardPageable,
                         @Qualifier("reply") @PageableDefault(sort = "createdTime", direction = Sort.Direction.DESC) Pageable replyPageable,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        Page<BoardResponseDto> boardResponses = memberService.findBoardByMember(memberId, boardPageable).map(BoardResponseDto::new);
        Page<ReplyResponseDto> replyResponses = memberService.findReplyByMember(memberId, replyPageable);
        PagingBoardDto pagingBoardDto = new PagingBoardDto(boardResponses);
        PagingReplyDto pagingReplyDto = new PagingReplyDto(replyResponses);

        System.out.println("pagingBoardDto = " + pagingBoardDto);
        System.out.println("pagingReplyDto = " + pagingReplyDto);

        model.addAttribute("boards", boardResponses);
        model.addAttribute("replys",replyResponses);
        model.addAttribute("pagingBoard", pagingBoardDto);
        model.addAttribute("pagingReply", pagingReplyDto);
        return "/member/myPage";
    }

````

## 모델로 넘어온 객체에 접근해서 selectBox를 selected처리를 할 수도 있고, 쿼리파라미터에 접근해서 할 수도 있고.
````agsl
<div class="form-inline justify-content-center" th:object="${boardSearchDto}">
                <form action="/blog" method="get">
                    <select th:field="*{searchBy}" class="form-control" style="width:auto;"> //boardSearchDto객체 접근
                        <option value="author">writer</option>
                        <option value="content">content</option>
                    </select>
                    <input th:field="*{searchQuery}" type="text" class="form-control" placeholder="검색어를 입력해주세요">
                    <button id="searchBtn" type="submit" class="btn btn-primary">검색</button>
                </form>
            </div>

            <div class="form-inline justify-content-end">
                <label for="sortOrder" class="mr-2">Sort Order:</label>
                <select id="sortOrder" onchange="changeSortOrder()">
//쿼리파라미터 접근    <option  th:selected="${#strings.toString(param.sort)} == 'createdTime,desc'" id="desc" value="createdTime,desc">Latest</option>
                    <option  th:selected="${#strings.toString(param.sort)} == 'createdTime,asc'" id="asc" value="createdTime,asc">Oldest</option>
                </select>
            </div>
````

## 각 테이블 요소에 th:onclick, 폼 요소애 th:action으로 링크를 걸어 이동할 수 있다
````agsl

<form th:action="@{/board/edit/{boardId}(boardId=${editForm.id})}" method="post">
        <div th:object="${editForm}">
            <div class="form-group">
                <div class="form-group geeting">글 수정하기</div>
 //               
   <tbody>
            <tr th:each="board:${boards}" style="cursor:pointer;color:#blue;">
                <td th:onclick="|location.href='@{/board/details/{id}(id=${board.id})}'|" th:text="${board.title}">
                    John
                </td>
                <td th:onclick="|location.href='@{/board/details/{id}(id=${board.id})}'|"
                    th:text="${#temporals.format(board.createdTime, 'yyyy-MM-dd')}">2022-12-12
                </td>

                <td>
                    <button class="edit-button"
                            th:onclick="|location.href='@{/board/edit/{boardId}(boardId=${board.id})}'|"
                            title="Edit">Edit
                    </button>
                </td>
                </th:block>
            </tr>
````

## details에서 Board정보를 가져올 때 로그인 인증 객체와 id비교를 하려면 Board writerId가 필요하다. writer정보가 필요하다.
- 아래와 같이 @Query기능을 통해 Member와 조인하여 조회한다
````agsl
  @Query("select b from Board b join fetch b.member m where b.id = :boardId")
    Optional<Board> findByIdWithMember(Long boardId);
````

````agsl
  <div class="d-flex justify-content-between">
            <p class="card-title" th:text="|글번호: *{id} writer: *{writer}|">writer</p>
            <th:block sec:authorize="isAuthenticated()">
                <div th:if="${#authentication.principal.member.id == board.writerId}" class="actions">
                    <button class="btn btn-secondary"
                            th:onclick="|location.href='@{/board/edit/{boardId}(boardId=${board.id})}'|">수정
                    </button>
                </div>
            </th:block>
        </div>

````

## URL을 통한 수정 접근을 막기 위해 인증 인터셉터 만든다. 
## 이와 같이 인증객체에 접근할 수 있다.
````agsl
 PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("principal = " + principal);
````

## 이와 같이 url경로변수에 접근할 수 있다
````agsl

            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            System.out.println("pathVariables = " + pathVariables);
            Long boardId = Long.parseLong((String) pathVariables.get("boardId"));

            Board board = boardRepository.findById(boardId).get(); //id는 가지고 있으므로 지연로딩 하지 않는다.

````

### 로그 찍어본 결과
principal = PrincipalDetails(member=Member(id=1, name=kimkim1, password=$2a$10$0fRSMTimY.Wakupom7D52ejrrPyDAxZUmFi.g5tM0zRB1GpeJTbK., email=aaa, role=ROLE_MEMBER))
pathVariables = {boardId=3}

## ajax로 폼의 요소들을 자바스크립트 객체로 생성하여 서버로 요청할 수도 있지만, 
## 폼의 submit 기능을 이용하도록 onclick 자바스크립트 함수를 매핑할 수도 있다.
```agsl
 <button type="button" class="btn btn-primary" onclick="signin()">Submit</button>
//
 function signin(){
    const account = $("#account").val();
    const password = $("#password").val();

    if(account.trim() == '' || password.trim() == ''){
        alert('아이디 또는 비밀번호를 입력해주세요');
        return false;
    }
    $('form[name="login"]').submit();
}


```

## 인증 아이디로 account필드를 쓸 경우 여기를 수정해야 한다
````agsl
   @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getAccount();  // 이 곳 수정
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

````


## enum타입으로 폼데이터를 받을 수 있다. 
````agsl


 <select name="gender" id="gender" class="form-select p-3 ms-1 mb-1">
        <option value="MEN">남성</option>
        <option value="WOMEN">여성</option>
        <option value="SECRET">비공개</option>
      </select>
      
      
      @Getter
@AllArgsConstructor
@ToString
public class RegisterRequest {
    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private String birthYear;
    private Gender gender;
    private String phoneNo;
}
````

## 여러 파라미터로 멤버를 조회할 수 있는 메서드를 만든다.

````agsl
public Member findMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("해당 이메일로 회원을 찾을 수 없습니다. 이메일을 다시 확인해주세요."));
    }

    public Member findMember(Principal principal) {
        if(principal == null){
            throw new NotLoginException("로그인 하셔야 이용할 수 있습니다.");
        }

        String email = principal.getName();
        return findMember(email);
    }

    public Member findMember(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 아이디로 회원을 찾을 수 없습니다. 회원 아이디를 다시 확인해주세요."));
    }
````

## 프론트에서 검증했지만, 벡엔드에서도 검증하는 메서드를 만든다.

````agsl
  private void validatePhoneNo(String phoneNo) {
        if(phoneNo == null || phoneNo.trim().equals("")){
            throw new IllegalArgumentException("핸드폰 번호를 올바르게 입력해주세요.");
        }

        if(phoneNo.length() != 11){
            throw new IllegalArgumentException("핸드폰 번호를 올바르게 입력해주세요.");
        }
    }

    private void validateEmail(String email) {
        if(email == null || email.trim().equals("")){
            throw new IllegalArgumentException("이메일을 올바르게 입력해주세요.");
        }

        String[] splitEmail = email.split("@");
        if(splitEmail[0].length() < 4 || splitEmail[0].length() > 24){
            throw new IllegalArgumentException("이메일을 올바르게 입력해주세요.");
        }
    }
````

## 일대다 컬렉션 조회를 할 때 페치조인을 하면 

컬렉션을 fetch join하는 순간 페이징이 불가능해진다.

컬렉션을 페치 조인하면 1 : 다 조인이 발생하므로 데이터가 예측할 수 없이 증가한다.
1 : 다에서 1을 기준으로 페이징을 하는 것이 목적인데 데이터는 다(N)를 기준으로 row(행)가 생성된다.
Order를 기준으로 페이징 하고 싶은데, 다(N)인 OrderItem을 조인하면 OrderItem이 기준이 되어 버린다.
이 경우 하이버네이트는 경고 로그를 남기고 모든 DB 데이터를 읽어서 메모리에서 페이징을 시도하게 되고 최악의 경우 장애가 발생한다.
( 1 : 1 관계는 fetch join을 해도 문제가 발생하지 않는다. )


지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size 또는 @BatchSize 를 적용한다.

이 옵션들을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size IN 키워드를 사용해 조회한다.- hibernate.default_batch_fetch_size: 글로벌 설정 ( 주로 사용 )

default_batch_fetch_size의 작동원리
이러한 결과를 통해 알 수 있는 것은,
JPA는 지연로딩할 객체를 만났을 때,
default_batch_fetch_size개수만큼 모일 때까지 쌓아두었다가,
해당 개수가 다 모이면 쿼리를 보낸다는 것을 알 수 있다!
https://velog.io/@jadenkim5179/Spring-defaultbatchfetchsize%EC%9D%98-%EC%9E%91%EB%8F%99%EC%9B%90%EB%A6%AC


## 지연로딩 프록시 기술을 이용하기 위해 다음과 같으 기본 생성자를 protected로
````agsl
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //지연로딩 프록시 기술을 위함
public class ReplyLikeLog {
````

## JPA를 사용하면서 네이티브 쿼리를 다음과 같이 사용할 수 있고, 데이터베이스의 데이터에 영향을 준다면 @Mopdify를 붙인다.
nativeQuery=true -> 네이티브 쿼리를 쓴다.

## 엔티티 생성시 불필요한 Select문을 없애기 위해, 다음과 같이 getReferenceById를 사용.
우리가 흔히 사용하던 findById()은 eager방식의 조회기법이라면 getOne()은 lazy방식으로 조회된다.
레퍼런스를 확인해보면 주어진 식별자를 가지고있는 엔티티의 참조를 반환한다. JPA에서 lazy loading을 사용할때 엔티티 매니저에서 getReference()를 사용하는데 getOne()도 내부적으로 이 메서드를 사용한다. 프록시를 사용하기 때문에 실제 필드에 접근할 때 db에서 조회해온다.  getOne()을 사용하고 필드에 접근하는 예시를 보자.

@Transactional
public Comment saveComment(CommentRequestDto requestDto) {
Post post = postRepository.getReferenceById(requestDto.getPostId());
System.out.println(post.getContent()); //필드 값 접근
Comment comment = Comment.builder()
.post(post)
.content(requestDto.getContent())
.build();
return commentRepository.save(comment);
}
이런 상황이라면 필드에 접근했으므로 아래와 같이 select문이 동작한다. 만약, 식별자가 db에 존재하지 않는다면 필드 접근 시점에서 예외가 발생한다.

## 테스트 실행시 쿼리를 제대로 확인하려면 영속성 컨텍스트 초기화가 필요하다
````agsl
   @Test
    void test() {
        Member member1 = memberService.findById(1L);

        Board board = boardService.findById(1L);
        System.out.println("-------------------");
        Reply reply = boardService.addReply(member1.getId(), board.getId(), "replyContent");
        System.out.println("-------------------");
        Member member2 = memberService.findById(2L);
        System.out.println("-------------------");
        ReReply reReply = boardService.addReReply(member2.getId(), reply.getId(), "reReplyContent");
        System.out.println("reReply = " + reReply);
        em.flush();
        em.clear();
        System.out.println("-------------------");
        boardService.deleteReReply(reReply.getId());
        System.out.println("-------------------");
    }
````

## 다음 테스트 코드에서 영속성 전이 쿼리를 확인할 수 있다. 연관관계 부모객체 삭제시 -> 자식객체 삭제.
````agsl
 @Test
    void test() {
        Member member1 = memberService.findById(1L);

        Board board = boardService.findById(1L);
        System.out.println("-------------------");
        Reply reply = boardService.addReply(member1.getId(), board.getId(), "replyContent");
        System.out.println("-------------------");
        Member member2 = memberService.findById(2L);
        System.out.println("-------------------");
        ReReply reReply1 = boardService.addReReply(member2.getId(), reply.getId(), "reReplyContent1");
        ReReply reReply2 = boardService.addReReply(member2.getId(), reply.getId(), "reReplyContent2");
        System.out.println("reReply = " + reReply1);
        System.out.println("reReply = " + reReply2);
        em.flush();
        em.clear();
        System.out.println("-------------------");
        boardService.deleteReply(reply.getId());
        System.out.println("-------------------");
    }
    } //대댓글 삭제 쿼리 나간 후 댓글 삭제 쿼리 나간다. -> 근데 대댓글이 2개라서 delete문이 2개 나간다.. 대댓글이 1억개라면..? -> 개선이 필요.
````

## Board삭제시 단계적으로 영속성 전이 발생 확인
````agsl
  @Test
    void test() {
        Member member1 = memberService.findById(1L);

        Board board = boardService.findById(1L);
        System.out.println("-------------------");
        Reply reply = boardService.addReply(member1.getId(), board.getId(), "replyContent");
        System.out.println("-------------------");
        Member member2 = memberService.findById(2L);
        System.out.println("-------------------");
        ReReply reReply1 = boardService.addReReply(member2.getId(), reply.getId(), "reReplyContent1");
        ReReply reReply2 = boardService.addReReply(member2.getId(), reply.getId(), "reReplyContent2");
        System.out.println("reReply = " + reReply1);
        System.out.println("reReply = " + reReply2);
        em.flush();
        em.clear();
        System.out.println("-------------------");
        boardService.deleteBoard(board.getId());
        System.out.println("-------------------");
    } //대댓글 삭제쿼리 2개 -> 댓글 삭제쿼리 1개 -> Board삭제 쿼리 1개.
````

## 기존 Repostory에서 메서드 네이밍을 이용한 exists방식은 조인을 사용해 성능이슈가 있으므로 아래와 같이 Querydsl로 개선
````agsl
 @Override
    public boolean existsByReplyId(Long replyId) {
        Integer fetchFirst = query.selectOne()
                .from(reReply)
                .where(reReply.reply.id.eq(replyId))
                .fetchFirst();//댓글Id에 해당하는 값이 없으면 null 반환.
        return fetchFirst != null;

    }
````

## 대댓글 작성자의 이름을 불러올 때, Response에 어떻게 내보내는지 확인하자.// -> tuple을 쓰는 것도 방법이지만, -> 페이조인으로 연관관계 필드 불러온 후 getter로 할당. 

````agsl

        // 이곳은 ReplyRepositoryCustomImpl
    @Override
    public List<Reply> findReplyByBoard(Long boardId) {

        List<Reply> replies = query.select(reply)
                .from(reply)
                .join(reply.member, QMember.member)
                .join(reply.board, QBoard.board)
                .where(reply.board.id.eq(boardId))
                .fetch();

        return replies; //dto는 컨트롤러가서 바꾸자
    }
    
    //
    //이곳은 ReplyResponseDto 생성자.
        public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.boardTitle = reply.getBoard().getTitle();
        this.boardId = reply.getBoard().getId();
        this.replyAuthorId = reply.getMember().getId();
        this.replyAuthor = reply.getMember().getAccount();
        this.content = reply.getContent();
        this.createdTime = reply.getCreatedTime();
    }
    
````

## 다대일 양방향 연관관계를 사용할 경우 질문이 있습니다
 아래와 같이 Reply 엔티티를 생성할 때 Board엔티티의 List<Reply> replies에 추가를 해주어야 하기 떄문에,
 엔티티 생성시 필요한 엔티티에 대한 select문은 getReferenceById를 사용해서 없앨 수 있었고 Reply엔티티 생성도 할 수 있었다.
````agsl
    @Transactional
    public Reply addReply(Long memberId, Long boardId, String content) {

        Member member = memberRepository.getReferenceById(memberId); //엔티티 생성에 불필요한 select문 없다.
        Board board = boardRepository.getReferenceById(boardId);
        System.out.println("===================");
        Reply reply = Reply.createReply(member, board, content);
        System.out.println("===================");
        return replyRepository.save(reply);
    }
````
 다만 다대일 양방향 연관관계의 경우 댓글을 저장할 때마다, Board에 대한 Select쿼리가 발생한다.
 
````agsl
    public static Reply createReply(Member member, Board board, String content) {
        Reply reply = new Reply();
        reply.member = member;
        reply.board = board;
        reply.content = content;
        reply.likeCount = 0;
        reply.isDeleted = false;
        log.info("board select 발생 넣기 --");
        board.addReply(reply);
        log.info("board select 발생 넣기 --");
        return reply;
    }
````

이렇게 댓글을 저장할 때 마다 board에 대한 select쿼리가 발생하다 보니 다대일 양방향 관계를 하지 말아야 하는 생각이 드는데요..
지금 프로젝트에서는 양방향 연관관계의 장점은 영속성 전이와 orphanremoval을 사용헤서 게시글 삭제시 댓글이 모두 삭제되는 이점을 얻는다는 것이 있습니다.
물론 board삭제할 경우 replyrepository에서 boardId를 통한 댓글 삭제가 가능할 것으로 생각 되는데요.

댓글을 작성할 때마다 Board select쿼리가 나가는 것은 성능상의 이슈로 봐야 할까요?? -> 다대일 단방향 연관관계로 바꾸는게 맞을까요?
아니면 양방향 연관관계의 경우 JPA를 사용하지 않을시 객체지향적 코드를 보장한다는 이야기를 들었는데 현재 프로젝트 진행 중엔 와닿지가 않아서
어떤 선택을 해야할 지 질문드립니다!!
어떤 선택을 해야 할까요?

아래는 발생 쿼리 로그입니다.
```agsl
===================
2024-02-03T14:15:00.936+09:00  INFO 2612 --- [    Test worker] cos.blog.web.model.entity.Reply          : board select 발생 넣기 --
2024-02-03T14:15:00.937+09:00 DEBUG 2612 --- [    Test worker] org.hibernate.SQL                        : 
    select
        b1_0.board_id,
        b1_0.content,
        b1_0.created_time,
        b1_0.last_modified_time,
        b1_0.member_id,
        b1_0.title 
    from
        board b1_0 
    where
        b1_0.board_id=?
2024-02-03T14:15:00.938+09:00  INFO 2612 --- [    Test worker] p6spy                                    : #1706937300938 | took 0ms | statement | connection 74| url jdbc:h2:tcp://localhost/~/blog
select b1_0.board_id,b1_0.content,b1_0.created_time,b1_0.last_modified_time,b1_0.member_id,b1_0.title from board b1_0 where b1_0.board_id=?
select b1_0.board_id,b1_0.content,b1_0.created_time,b1_0.last_modified_time,b1_0.member_id,b1_0.title from board b1_0 where b1_0.board_id=31;
2024-02-03T14:15:00.939+09:00  INFO 2612 --- [    Test worker] cos.blog.web.model.entity.Reply          : board select 발생 넣기 --
===================
2024-02-03T14:15:00.940+09:00 DEBUG 2612 --- [    Test worker] org.hibernate.SQL                        : 
    insert 
    into
        reply
        (board_id, content, created_time, is_deleted, last_modified_time, like_count, member_id, reply_id) 
    values
        (?, ?, ?, ?, ?, ?, ?, default)
2024-02-03T14:15:00.942+09:00  INFO 2612 --- [    Test worker] p6spy                                    : #1706937300942 | took 0ms | statement | connection 74| url jdbc:h2:tcp://localhost/~/blog
insert into reply (board_id,content,created_time,is_deleted,last_modified_time,like_count,member_id,reply_id) values (?,?,?,?,?,?,?,default)
insert into reply (board_id,content,created_time,is_deleted,last_modified_time,like_count,member_id,reply_id) values (31,'replyreply','2024-02-03T14:15:00.939+0900',false,'2024-02-03T14:15:00.939+0900',0,12,default);
===================
```


## bookId로 댓글을 조회하고 그에 딸린 대댓글까지 조회할 수 있다 -> 이 방법은 페이징이 불가하다.(V3의 방법)

````agsl

  * JPA 2편 4. API개발 고급 V3에 해당한다.
     * 페이징 불가하다는 단점이 있다 (ToOne관계만 페치조인 할 경우에는 페이징이 가능하다.)
     * 조인을 하면 다를 기준으로 로우가 형성되는데 distinct를 작성하면 JPA가 orderItem id가 같은 애들은 중복이라 여겨 없앤다
 @Query(
            "select distinct r " +
                    "from Review r " +
                    "join fetch r.member " +
                    "join fetch r.book " +
                    "left join fetch r.reviewComments rc " + //대댓글이 없을 수도 있으므로..
                    "where r.book.id = :bookId and r.isHidden = false and (rc.isHidden = false or rc is null) " +
                    "order by r.likesCount desc, r.createdTime asc "
    )
    List<Review> findReviews(@Param("bookId") Long bookId);
````


## 컬렉션 조회시 페이징을 하고 최적화 하는 강력한 기능
````agsl

@Override
    public Optional<Board> findBoardWithMember(Long boardId) {
        Board findBoard = query.select(board).distinct()
                .from(board)
                .join(board.member, QMember.member).fetchJoin()
                .where(board.id.eq(boardId))
                .fetchOne();
        return Optional.ofNullable(findBoard);
    }
    ToOne관계인 멤버는 페치조인으로 가져오고 나머지는 LAzyLoading시 In쿼리가 발생한다.
    

public Board findBoardWithRepliesCanPaging(Long boardId) {
        Board board = boardRepository.findBoardWithMember(boardId).orElseThrow(NoSuchElementException::new);
        return board;
    }
    
 @Test
    void 컬렉션조회_batch(){
        Board board = boardService.findBoardWithRepliesCanPaging(1L); //ToOne관계만 페치조인.
        System.out.println("board = " + board); 
        //댓글들은 where reply.board.id = boardId
        //대댓글들은 in쿼리
        //대댓글의 작성자 member도 in쿼리 -> toOne관계도 페치조인하지않고 LazyLoading전략일 경우 BatchSize의 영향을 받아 in쿼리를 가져온다
        // -> 하지만네트워크를 더 많이 타므로 TOOne관계는 fetchJoin으로 가져오자

        /**
         * V3방법은 모드 페치 조인으로 다루면 JPA에서 distinct처리를 하기 전에 많은 양의 데이터 중봅된 것이 넘어온다 -> 데이터 전송량이 많다는 단점이 있다.
         * V3의 방법보다 Batch를 활용하면 쿼리는 3개 나가도 테이블 단위로 in으로 팍팍팍 찍어서 가져오기 때문에 데이터 전송량이 줄어든다/
         */
    }
````

## 객체 지연로딩의 경우 . 컬렉션 지연로딩의 경우 강제 초기화를 해주어야 프록시 객체가 아닌, 진짜 객체로 초기화 된다
````agsl
만약 EAGER 로딩을 사용한다면 Jpql 을 사용할 때 예상하지 못한 쿼리가 나가는 문제가 발생합니다.
따라서 기본적으로 Lazy 로딩으로 세팅을 한 뒤에 성능 최적화를 시작합니다.
한편, Open In View 옵션을 true 로 해두었다면, JPA 영속성 관리를 벗어난 시점에도 트랜젝션을 끊지 않기 때문에 Lazy 로딩으로 인해, 로딩 되지 않은 Entity 도 데이터를 당겨올 수 있습니다.
그러나 Open In View 는 그만큼 커넥션을 풀에 반환하지 않고 가지고 있다는 것을 의미하기에, 잘못하면 커넥션 풀이 말라버리는 문제가 발생할 수 있고 따라서 Open In View 옵션을 끄는 것을 권장합니다.
그런데 이 옵션을 끄면 트랜젝션을 관리하는 서비스 계층에서 필요한 데이터를 모두 로딩을 해놔야 함을 의미합니다.
에초에 JPQL 을 사용하여 DTO 로 반환하는 경우 문제가 될 것이 없겠지만 엔티티를 사용한다면 강제 초기화를 해주어야 합니다.
JPA 초기화에는 다음과 같은 두가지 방법이 있습니다.


-트랜잭션 내에서 초기화..
   @Test
    void initTest() {
        List<Reply> replies = replyRepository.findReplyWithBoardAndMember(1L);
       assertThat(emf.getPersistenceUnitUtil().isLoaded( replies.get(0).getReReplies())).isFalse();
        System.out.println("======초기화 되기 전=========");
       
        System.out.println("replies.get(0).getReReplies() = " + replies.get(0).getReReplies());
        System.out.println("======print로인한 초기화 IN쿼리 나간다=========");
        assertThat(emf.getPersistenceUnitUtil().isLoaded( replies.get(0).getReReplies().get(0))).isTrue();
    }

````

### th:data-rrr의 값을 받아서 자바스크립트에서 사용하였다.

```agsl
<div class="pt-1 pb-1 w-100 text-end">
                            <button class="btn btn-primary comment-btn" th:data-rrr="${reply.id}">댓글 <span
                                    th:text="${reply.reReplyCount}"></span></button>
                            <button class="btn btn-outline-grey like-btn" th:data-review="${reply.id}">좋아요<span
                                    th:text="${reply.likeCount}"></span></button>
                        </div>
                        
 // 아래는 자바스크립트 코드
                         $(document).on("click", ".comment-submit", function (){
        const replyId = $(this).closest('.review_wrap').find('.comment-btn').data('rrr'); //여기서 th:data-rrr값 사용.
        const content = $(this).closest('.review_wrap').find('textarea.comment-textarea').val();

        if(content.tr
```