$(function () {
  $("#loginButton").click(function () {
    var form = {};
    form['username'] = $('.username input').val();
    form['password'] = $('.password input').val();
    var data={
      data:JSON.stringify(form)
    }
    console.log(data);

    $.ajax({
        url: "/api/login",
        type: "POST",
        data: data,
        // contentType: 'application/j  son',
        cache: false,
        success: function (data) {
          console.log(data);
          if(data == 'success'){
            window.location.href = "/index";
          }else {
            $('.loginForm div:last-child p').html("用户名或者密码错误。。。")
          }
        }
    });
  });

    $('#tag').tokenfield()


    editormd("test-editormd", {
        width   : "90%",
        height  : 640,
        syncScrolling : "single",
        //你的lib目录的路径，我这边用JSP做测试的
        path    : "<%=request.getContextPath()%>/resources/editormd/lib/",
        //这个配置在simple.html中并没有，但是为了能够提交表单，使用这个配置可以让构造出来的HTML代码直接在第二个隐藏的textarea域中，方便post提交表单。
        saveHTMLToTextarea : true
    });

    $('#tagCloud a').tagcloud();

});

$.fn.tagcloud.defaults = {
    size: {start: 10, end: 18, unit: 'pt'},
    color: {start: '#aaa', end: '#555'}
};