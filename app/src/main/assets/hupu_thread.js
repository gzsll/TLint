var hupu_title;
var hupu_date;

var getImageURLs = function(){
    var images = document.getElementsByTagName("img");
    return "" + images.length;
};

var imageURLAtIndex = function(position){
    var images = document.getElementsByTagName("img");
    return "" + images[position].getAttribute('data-url');
};

var addPostInfo = function(title, postInfo, date, uname, position){
    hupu_title = title;
    hupu_date = date;
    var mainPost = "<div class=\"title\"><h1>" + title + "</h1><div><span class=\"date\">" + date + "</span><span class=\"info\" id=\"jdb_postInfo\">" + postInfo + "</div></div><div class=\"about\"><span class=\"name\">" + uname + "</span> <span class=\"time\">" + position + "</span> </div>";
    document.getElementById("hupu_mainPost").innerHTML = mainPost;
    return "1";
};

document.ontouchstart = function() {
    
    window.getSelection().removeAllRanges();
    clientX = event.touches[0].clientX;
    clientY = event.touches[0].pageY - window.scrollY;
};

var addThreadInfo = function(threadEntity) {
    var obj = JSON.parse(decodeURIComponent(threadEntity));
    var threadInfo = "<header class=\"artical-title\">"+
    "<h1 class=\"headline\">"+obj.title+"</h1>"+
    "<div class=\"artical-info\">"+
    "<span class=\"times\">"+ obj.time +"</span>"+
    "</div>"+
    "<div class=\"line\"></div>"+
    "</header>";
    document.getElementById("detail-content").innerHTML = threadInfo;
};


var addThreadContent = function(threadEntity) {
    var obj = JSON.parse(decodeURIComponent(threadEntity));
    var threadContent = "<header class=\"artical-title\">"+
    "<h1 class=\"headline\">"+obj.title+"</h1>"+
    "<div class=\"artical-info\">"+
    "<span class=\"times\">"+ obj.time +"</span>"+
    "</div>"+
    "<div class=\"line\"></div>"+
    "</header>"+
    "<article class=\"article-content\">"+
    "<div class=\"article-author clearfix\">"+
    "<img src=\""+ obj.userImg+"\" class=\"author-icon\">"+
    "<a href=\"http://my.hupu.com/"+obj.puid+"\" target=\"_blank\"><font color=\"#326ca6\">"+obj.username+"</font></a>"+
    "<span class=\"mod-mask mask\"></span>"+
    "</div>"+
    "<div class=\"article-content\">"+ obj.content +
    "</div>"+
    "</article>";
    $('#detail-content').html(threadContent);
};

var clearLightReply = function() {
    $('#bright-reply').html("");
};

var clearReply = function() {
    $('#general-reply').html("");
};

var clearPost = function(threadEntity){
    var obj = JSON.parse(decodeURIComponent(threadEntity));
    var threadInfo = "<header class=\"artical-title\">"+
    "<h1 class=\"headline\">"+obj.title+"</h1>"+
    "<div class=\"artical-info\">"+
    "<span class=\"times\">"+ obj.time +"</span>"+
    "</div>"+
    "<div class=\"line\"></div>"+
    "</header>";
    $('#detail-content').html(threadInfo);
    $('#bright-reply').html("");
    $('#general-reply').html("");
};

var addLightTitle = function(title){
    $('#bright-reply').html("<div class=\"title\">"+"<h2>"+title+"</h2>"+"</div>");
};

var addLightPost = function(replyEntity, index, isAuthor) {
    var obj = JSON.parse(decodeURIComponent(replyEntity));
    var author = isAuthor? "<i class=\"fr\">楼主</i>" : "";
    var content = "<dl class=\"reply-list\" data-pid=\""+obj.pid+"\" data-uid=\""+obj.puid+"\" data-area=\"0\" data-index=\""+index+"\">"+
    "<dd class=\"operations-user\">"+
    "<span class=\"user-avatar\" data_url=\"http://my.hupu.com/"+ obj.puid +"\">"+
    "<img src=\""+obj.userImg+"\" alt=\""+obj.userName+"\">"+
    "<span class=\"mod-mask mask\"></span>"+
    "</span>"+
    "<div class=\"user-info\">"+
    "<div class=\"user-name clearfix\">"+
    "<span class=\"fl ellipsis\"><a href=\"http://my.hupu.com/"+obj.puid+"\" target=\"_blank\"><font color=\"#326ca6\">"+obj.userName+"</font></a></span>"+ author +
    "</div>"+
    "<div class=\"source-left\">"+
    "<span class=\"postdate\">"+obj.time+"</span>"+
    "</div>"+
    "</div>"+
    "<span class=\"reply-light\" id=\"hupu_" + obj.pid + "_0\">亮了("+obj.light_count+")</span>"+
    "</dd>"+
    "<dt class=\"reply-content\">"+ obj.quoteContent +
    "<div class=\"current-content\">"+
    "<span class=\"short-content\">"+ obj.content +
    "</span>"+
    "</div>"+
    "</dt>"+
    "</dl>";
    var html = $('#bright-reply').html();
    $('#bright-reply').html(html + "" + content);
};

var addReplyTitle = function(title) {
    $('#general-reply').html("<div class=\"title\">"+"<h2>"+title+"</h2>"+"</div>");
};

var addReply = function(replyEntity, index, isAuthor) {
    
    var obj = JSON.parse(decodeURIComponent(replyEntity));
    var author = isAuthor? "<i class=\"fr\">楼主</i>" : "";
    
    var content = "<dl class=\"reply-list\" data-pid=\""+obj.pid+"\" data-uid=\""+obj.puid+"\" data-area=\"1\" data-index=\""+index+"\">"+
    "<dd class=\"operations-user\">"+
    "<span class=\"user-avatar\" data_url=\"http://my.hupu.com/"+ obj.puid +"\">"+
    "<img src=\""+obj.userImg+"\" alt=\""+obj.userName+"\">"+
    "<span class=\"mod-mask mask\"></span>"+
    "</span>"+
    "<div class=\"user-info\">"+
    "<div class=\"user-name clearfix\">"+
    "<span class=\"fl ellipsis\"><a href=\"http://my.hupu.com/"+obj.puid+"\" target=\"_blank\"><font color=\"#326ca6\">"+obj.userName+"</font></a></span>"+author+
    "</div>"+
    "<div class=\"source-left\">"+
    "<span class=\"floor\">"+ obj.floor +"楼</span>"+ " "+
    "<span class=\"postdate\">"+obj.time+"</span>"+
    "</div>"+
    "</div>"+
    "<div class=\"reply-light\" id=\"hupu_" + obj.pid + "_1\">亮了("+obj.light_count+")</div>"+
    "</dd>"+
    "<dt class=\"reply-content\">"+ obj.quoteContent +
    "<div class=\"current-content\">"+
    "<span class=\"short-content\">"+ obj.content +
    "</span>"+
    "</div>"+
    "</dt>"+
    "</dl>";
    var html = $('#general-reply').html();
    $('#general-reply').html(html + "" + content);
};


var isMoving = false;

var imgUri = "";
var videoUri = "";
var uri = "";

var clientX = -1;
var clientY = -1;


var replyOnTouchUp = function(index, pid, area){
    event.stopPropagation();
	event.preventDefault();
	if(isMoving){
		isMoving = false;
		return;
	}
    if(window.event.srcElement.tagName == "A" && uri == window.event.srcElement.href){
        window.location = "open:a";
        return;
    }
//	if(window.event.srcElement.tagName == "A"){
//        uri = window.event.srcElement.href;
//        window.location = "open:a";
//		return;
//	}
    if(window.event.srcElement.tagName == "BUTTON"){
        //button handle it's owe events
        return;
    }
    if(window.event.srcElement.tagName == "IMG"){
        var elem = window.event.srcElement;
        var index = elem.getAttribute("data-index"),
        param = {
        index: parseInt(index),
        imgs: $imageList
        };
        
        window.location = "zen://showImage?" + encodeURIComponent(JSON.stringify(param));
        return;
    }
    if(window.getSelection().rangeCount == 0){
        window.location = "clientX:" + clientX + ":clientY:" + clientY + ":index:" + index + ":area:" + area;
    }
};

var findPos = function(pid) {
    var obj = document.getElementById("hupu_" + pid + "_1");
    var curleft = curtop = 0;
    if (obj.offsetParent) {
        do {
            curleft += obj.offsetLeft;
            curtop += obj.offsetTop;
        } while (obj = obj.offsetParent);
    }
    return curtop;
};

var scrollToVisible = function(pid) {
    var obj = document.getElementById("hupu_" + pid + "_1");
    var curleft = curtop = 0;
    if (obj.offsetParent) {
        do {
            curleft += obj.offsetLeft;
            curtop += obj.offsetTop;
        } while (obj = obj.offsetParent);
    }
    var totalH = document.body.scrollHeight;
    var windowH = window.innerHeight;
    var offset = curtop;
    
    if (offset > windowH) {
        if ((offset - windowH/3) < (totalH - windowH)) {
            window.scrollTo(0, offset - windowH/3);
        }
        else {
            window.scrollTo(0, totalH - windowH);
        }
    }
    
};

var getHTML = function() {
    return $('body').html();
};

//function getRectForSelectedWord() {
//    var selection = window.getSelection();
//    var range = selection.getRangeAt(0);
//    var rect = range.getBoundingClientRect();
//    return "{{" + rect.left + "," + rect.top + "}, {" + rect.width + "," + rect.height + "}}";
//}

var lightSuccess = function(light, pid) {
    $("#hupu_" + pid + "_0").text("亮了(" + light + ")");
    $("#hupu_" + pid + "_1").text("亮了(" + light + ")");
};

var isHidden = function(elementId) {
    event.stopPropagation();
    event.preventDefault();
    var vDiv = document.getElementById(elementId);
    vDiv.style.display = (vDiv.style.display == 'none')?'block':'none';
};


// zepto
var $imageList = [];

var reloadStuff = function() {
    $imageList = [];
    var i = 0;
    $('img').each(function (e) {
                  var $this = $(this);
                  var url = $this.attr('data_url');
                  if (url && url.length > 0) {
                     $this.attr('data-index', i);
                     i++;
                     $this.attr('class', 'normal_img');
                     $imageList.push(url);
                  }
                });
    
    $('.normal_img').off('tap').on('tap', function (e) {
                                   e.stopPropagation();
                                   e.preventDefault();
                                   var index = $(this).attr('data-index'),
                                   param = {
                                   index: parseInt(index),
                                   imgs: $imageList
                                   };
                                   
                                   window.location = "zen://showImage?" + encodeURIComponent(JSON.stringify(param));
                                   });
    
    $('a').off('click').on('click', function (e) {
                         e.stopPropagation();
                         e.preventDefault();
                         var url = $(this).attr('href');
                         if (url && url.length > 0) {
                         var param = {"url":url};
                         window.location = "zen://showURL?" + encodeURIComponent(JSON.stringify(param));
                         }
                         });
    
    $('a').off('tap').on('tap', function (e) {
                            e.stopPropagation();
                            e.preventDefault();
                           
                           });
    
    $('.reply-list').off('tap').on('tap', function (e) {
                                   
                                        e.stopPropagation();
                                        e.preventDefault();
                                        var area = $(this).attr('data-area');
                                        var index = $(this).attr('data-index');
                                        if (area && index) {
                                            var param = {
                                                        "area":area,
                                                        "index":index,
                                                        "x":clientX,
                                                        "y":clientY
                                        
                                                };
                                            window.location = "zen://showMenuBar?" + encodeURIComponent(JSON.stringify(param));
                                        }
                                        
                                        });
    
    $('.user-avatar').off('tap').on('tap', function (e) {
                                        e.stopPropagation();
                                        e.preventDefault();
                                        var url = $(this).attr('data_url');
                                        if (url) {
                                            var param = {"url":url};
                                            window.location = "zen://showURL?" + encodeURIComponent(JSON.stringify(param));
                                        }
                                    });

};

var nightMode = function() {
    $('body').attr('class', 'night');
};

