/**
 * 发送评论
 */
function postComment() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content)

}

function comment2target(targetId, type, content) {
    if (content === '') {
        alert('评论内容不能为空');
    } else {
        $.ajax({
            type: "POST",
            url: "/comment",
            contentType: "application/json",
            data: JSON.stringify({
                "parentID": targetId,
                "content": content,
                "type": type
            }),
            success: function (response) {
                if (response.code === 200) {
                    location.reload();
                } else {
                    if (response.code === 2002) {
                        let gotoLogin = confirm(response.message + "，是否前去登录");
                        if (gotoLogin === true) {
                            window.open("https://github.com/login/oauth/authorize?client_id=8c31c05854b01cfe4402&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                            window.localStorage.setItem("closeWindow", true);
                        }
                    } else {
                        alert(response.message);
                    }

                }
            },
            dataType: "json"
        })
    }

}

/**
 * 二级评论
 * @param commentId
 */
function getComment(e) {
    let commentId = e.getAttribute("data-id");
    let content = $("#input-"+commentId).val()
    console.log(commentId,content)
    comment2target(commentId, 2, content)
}

/**
 * 退出登录
 */
function logout() {
    // 让token过期
    document.cookie = 'token=-1; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/';
    location.reload();
}

/**
 * 拉取二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "height":"38px",
                        "width":"38px",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}