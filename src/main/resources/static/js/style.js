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



});
