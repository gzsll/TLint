Jockey.on('addThreadInfo',
function(payload) {
    addThreadContent(payload);
});

Jockey.on('addLightTitle',
function(payload) {
    addLightTitle(payload);
});

Jockey.on('addLightPost',
function(payload) {
    addLightPost(payload);
});

Jockey.on('addReplyTitle',
function(payload) {
    addReplyTitle(payload);
});

Jockey.on('addReply',
function(payload) {
    addReply(payload);
    reloadStuff();
});

Jockey.on('addLight',
function(payload) {
    lightSuccess(payload);
});

var addThreadContent = function(threadEntity) {
    var threadContent = "<header class=\"article-title\">" + "<h1 class=\"headline\">" + threadEntity.title + "</h1>" + "<div class=\"article-info\">" + "<span class=\"times\">" + threadEntity.time + "</span>" + "<span class=\"post-board\">" + "<a href=\"kanqiu://bbs/board/" + threadEntity.fid + "\" target=\"_blank\">" + threadEntity.forumName + "</a>" + "</span>" + "</div>" + "<div class=\"line\"></div>" + "</header>" + "<article class=\"article-content\">" + "<div class=\"article-author clearfix\">" + "<img src=\"" + threadEntity.userImg + "\" class=\"author-icon\">" + "<a href=\"kanqiu://people/" + threadEntity.author_puid + "\" target=\"_blank\"><font color=\"#326ca6\">" + threadEntity.username + "</font></a>" + "<span class=\"mod-mask mask\"></span>" + "</div>" + "<div class=\"article-content\">" + threadEntity.content + "</div>" + "</article>";
    $('#detail-content').html(threadContent);
};

var addLightTitle = function(title) {
    $('#bright-reply').html("<div class=\"title\">" + "<h2>" + title + "</h2>" + "</div>");
};

var addLightPost = function(replyEntity) {
    var author = "";
    var quoteContent = "";
    if (replyEntity.quoteContent) {
        quoteContent = "<div class=\"reply-quote-content\">" + " <div class=\"reply-quote-hd\">" + replyEntity.quoteHeader + "</div>";
        if (replyEntity.quoteToggle) {
            quoteContent = quoteContent + "<div class=\"short-quote-content\">" + replyEntity.quoteToggle + "</div>" + "<div class=\"reply-quote-bd J_allContent\">" + replyEntity.quoteContent + "</div>" + "<div class=\"button-open-inner\">" + "<a class=\"button-open J_buttonOpenAll\" title=\"展开\">显示全部 <s class=\"arrow\"></s></a> </div>";
        } else {
            quoteContent = quoteContent + "<div class=\"short-quote-content\">" + replyEntity.quoteContent + "</div>";
        }
        quoteContent = quoteContent + " </div>"
    }
    var content = "<div class=\"reply-inner\">" + "<dl class=\"reply-list\" data-pid=\"" + replyEntity.pid + "\" data-uid=\"" + replyEntity.puid + "\" data-area=\"0\" data-index=\"" + replyEntity.index + "\">" + "<dd class=\"operations-user\">" + "<a href=\"kanqiu://people/" + replyEntity.puid + "\" class=\"user-avatar\" >" + "<img src=\"" + replyEntity.userImg + "\" alt=\"" + replyEntity.userName + "\">" + "<span class=\"mod-mask mask\"></span>" + "</a>" + "<div class=\"user-info\">" + "<div class=\"user-name clearfix\">" + "<span class=\"fl ellipsis\"><a href=\"kanqiu://people/" + replyEntity.puid + "\" target=\"_blank\"><font color=\"#326ca6\">" + replyEntity.userName + "</font></a></span>" + author + "</div>" + "<div class=\"source-left\">" + "<span class=\"postdate\">" + replyEntity.time + "</span>" + "</div>" + "</div>" + "<span class=\"reply-light\" id=\"hupu_" + replyEntity.pid + "_0\">亮了(" + replyEntity.light_count + ")</span>" + "</dd>" + "<dt class=\"reply-content\">" + quoteContent + "<div class=\"current-content\">" + "<span class=\"short-content\">" + replyEntity.content + "</span>" + "</div>" + "</dt>" + "</dl>" + "</div>";
    var html = $('#bright-reply').html();
    $('#bright-reply').html(html + "" + content);
};

var addReplyTitle = function(title) {
    $('#general-reply').html("<div class=\"title\">" + "<h2>" + title + "</h2>" + "</div>");
};
var addReply = function(replyEntity) {
    var author = "";
    var quoteContent = "";
     if (replyEntity.quoteContent) {
            quoteContent = "<div class=\"reply-quote-content\">" + " <div class=\"reply-quote-hd\">" + replyEntity.quoteHeader + "</div>";
            if (replyEntity.quoteToggle) {
                quoteContent = quoteContent + "<div class=\"short-quote-content\">" + replyEntity.quoteToggle + "</div>" + "<div class=\"reply-quote-bd J_allContent\">" + replyEntity.quoteContent + "</div>" + "<div class=\"button-open-inner\">" + "<a class=\"button-open J_buttonOpenAll\" title=\"展开\">显示全部 <s class=\"arrow\"></s></a> </div>";
            } else {
                quoteContent = quoteContent + "<div class=\"short-quote-content\">" + replyEntity.quoteContent + "</div>";
            }
            quoteContent = quoteContent + " </div>"
        }
    var content = "<div class=\"reply-inner\">" + "<dl class=\"reply-list\" data-pid=\"" + replyEntity.pid + "\" data-uid=\"" + replyEntity.puid + "\" data-area=\"1\" data-index=\"" + replyEntity.floor + "\">" + "<dd class=\"operations-user\">" + "<a href=\"kanqiu://people/" + replyEntity.puid + "\" class=\"user-avatar\" >" + "<img src=\"" + replyEntity.userImg + "\" alt=\"" + replyEntity.userName + "\">" + "<span class=\"mod-mask mask\"></span>" + "</a>" + "<div class=\"user-info\">" + "<div class=\"user-name clearfix\">" + "<span class=\"fl ellipsis\"><a href=\"kanqiu://people/" + replyEntity.puid + "\" target=\"_blank\"><font color=\"#326ca6\">" + replyEntity.userName + "</font></a></span>" + author + "</div>" + "<div class=\"source-left\">" + "<span class=\"floor\">" + replyEntity.floor + "楼</span>" + " " + "<span class=\"postdate\">" + replyEntity.time + "</span>" + "</div>" + "</div>" + "<span  class=\"reply-light\" id=\"hupu_" + replyEntity.pid + "_1\">亮了(" + replyEntity.light_count + ")</span>" + "</dd>" + "<dt class=\"reply-content\">" + quoteContent + "<div class=\"current-content\">" + "<span class=\"short-content\">" + replyEntity.content + "</span>" + "</div>" + "</dt>" + "</dl>" + "</div>";
    var html = $('#general-reply').html();
    $('#general-reply').html(html + "" + content);

};

var getHTML = function() {
    return $('body').html();
};

var clientX = -1;
var clientY = -1;

document.ontouchstart = function() {
    window.getSelection().removeAllRanges();
    clientX = event.touches[0].clientX;
    clientY = event.touches[0].pageY;
};

var lightSuccess = function(addLightEntity) {
       $("#hupu_" + addLightEntity.pid + "_0").text("亮了(" + addLightEntity.light + ")");
       $("#hupu_" + addLightEntity.pid + "_1").text("亮了(" + addLightEntity.light + ")");
};

var $imageList = [];
var reloadStuff = function() {
    $imageList = [];
    var i = 0;
    $('img').each(function(e) {
        var $this = $(this);
        var url = $this.attr('data_url');
        if (url && url.length > 0) {
            $this.attr('data-index', i);
            i++;
            $this.attr('class', 'normal_img');
            $imageList.push(url);
        }
    });
    $('.normal_img').off('tap').on('tap',
    function(e) {
        e.stopPropagation();
        e.preventDefault();
        var index = $(this).attr('data-index'),
        param = {
            index: parseInt(index),
            imgs: $imageList
        };

        Jockey.send('showImg', param);
    });

    $('a').off('click').on('click',
    function(e) {
        e.stopPropagation();
        e.preventDefault();
        var url = $(this).attr('href');
        if (url && url.length > 0) {
            var param = {
                "url": url
            };
            Jockey.send('showUrl', param);
        }
    });

    $('.user-avatar').off('tap').on('tap',
    function(e) {
        e.stopPropagation();
        e.preventDefault();
        var uid = $(this).attr('data_uid');
        if (uid) {
            var param = {
                "uid": uid
            };
            Jockey.send('showUser', param);
        }
    });

    $('.reply-list').off('tap').on('tap',
    function(e) {
        e.stopPropagation();
        e.preventDefault();
        var area = $(this).attr('data-area');
        var index = $(this).attr('data-index');
        if (area && index) {
            showTip();
            $('.reply-tips .reply-ico').off('tap').on('tap',
            function(e) {
                e.stopPropagation();
                e.preventDefault();
                var param = {
                    "area": area,
                    "index": index,
                    "type": "reply"
                };
                Jockey.send('showMenu', param);
                hideTip();

            });

            $('.reply-tips .report-ico').off('tap').on('tap',
            function(e) {
                e.stopPropagation();
                e.preventDefault();
                var param = {
                    "area": area,
                    "index": index,
                    "type": "report"
                };
                Jockey.send('showMenu', param);
                hideTip();
            });

            $('.reply-tips .light-ico').off('tap').on('tap',
            function(e) {
                e.stopPropagation();
                e.preventDefault();
                var param = {
                    "area": area,
                    "index": index,
                    "type": "light"
                };
                Jockey.send('showMenu', param);
                hideTip();
            });

            $('.reply-tips .rulight-ico').off('tap').on('tap',
            function(e) {
                e.stopPropagation();
                e.preventDefault();
                var param = {
                    "area": area,
                    "index": index,
                    "type": "rulight"
                };
                Jockey.send('showMenu', param);
                hideTip();
            });

        }

    });

};



$(window).on('scroll',
function() {
    hideTip();
});

var showTip = function() {
    $('.m-reply .reply-tips').offset({
        top: clientY
    }).show();
}

var hideTip = function() {
    $('.m-reply .reply-tips').hide();
}

var nightMode = function() {
    $('body').attr('class', 'night');
};