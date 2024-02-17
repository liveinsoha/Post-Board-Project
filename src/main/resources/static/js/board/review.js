$(function (){
    $('.review-btn').on("click", function (){
        const starRating = $('input[name=rating]:checked').val();
        const content = $('.comment').val();

        const result = validateForm(starRating, content);
        if(result == false){
            return false;
        }

        const bookId = $(this).data('book');
        const data = {
            bookId : bookId,
            content : content,
            starRating : starRating
        }
        callAjax("post", "/review", data);
    });

    $(document).on("click", '.update-confirm', function (){
        const starRating = $('input[name=rating]:checked').val();
        const content = $('.comment').val();
        validateForm(starRating, content);

        const reviewId = $(this).data('review');
        const data = {
            content : content,
            starRating : starRating
        }

        callAjax("patch", "/review/"+reviewId, data);
    });

    $(".btn-reply-delete").on("click", function (){
        const result = confirm('삭제하면 복구할 수 없습니다. 삭제하시겠습니까?');
        if(result == false){
            return false;
        }
        const replyId = $(this).data('deleteReplyId');

        callAjax("delete", "/reply/"+replyId);
    });

    $(document).on("click", ".comment-submit", function (){
        const replyId = $(this).closest('.review_wrap').find('.comment-btn').data('rrr');//여기서 data-rrr값을 사용한다.
        const content = $(this).closest('.review_wrap').find('textarea.comment-textarea').val();

        if(content.trim() == ''){
            alert('댓글을 남겨주세요');
            return false;
        }

        if(content.length > 2000){
            alert('2000자 미만의 댓글을 남겨주세요.');
            return false;
        }

        const data = {
            replyId : replyId,
            content : content
        }

        callAjax("post", "/blog/rereply", data);
    })

    $(document).on("click", ".delete-reReply", function (){
        const result = confirm("정말 삭제하시겠습니까?");

        if(result == false){
            return false;
        }

        const reReplyId = $(this).data("rere"); //여기서 th:data-reReply 값을 사용한다
        console.log(reReplyId);
        callAjax("delete", "/blog/rereply/"+reReplyId, null);
    })

    $('.like-btn').on("click", function (){
        const replyId = $(this).data('review');
        const data = {
            replyId : replyId
        }
        callAjaxNoAlert('post', '/blog/like', data);
    });

    // 댓글 토글
    let isCommentVisible = false;
    const textarea = $('<textarea>', {
        class: 'form-control mt-2 comment-textarea',
        placeholder: '댓글을 입력하세요.'
    });

    const button = $('<button>', {
        class: 'btn btn-primary mt-2 comment-submit',
        text: '댓글 작성'
    });

    $('.comment-btn').on("click", function () {
      if(isCommentVisible) {
        $('.review_wrap .comment-textarea, .review_wrap .comment-submit').remove();
        isCommentVisible = false;
      } else{
        $(this).closest('.review_wrap').append(textarea);
        $(this).closest('.review_wrap').append(button);
        isCommentVisible = true;
      }
    })
})