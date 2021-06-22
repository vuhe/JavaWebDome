<%--
  Created by IntelliJ IDEA.
  User: zhuhe
  Date: 2021/5/30
  Time: 14:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.js"></script>
    <title>Title</title>
</head>
<body>
<form action="<%=request.getContextPath()%>/register"
      onsubmit="return check()" method="post" id="register">
    <label>用户名：<input id="user" type="text" name="user"/></label><br>
    <label>密码：<input type="password" name="password"/></label><br>
    <label>生日：<input type="text" name="birthday"/></label><br>
    <label>邮箱：<input type="text" name="email"/></label><br>
    <div>
        <input type="submit" value="登录"/>
        <span style="color: red" id="hint"/>
    </div>
</form>
<form action="<%=request.getContextPath()%>/register"
      style="display:none" method="get" id="check">
    <input type="submit">
</form>
<script>
    function check() {
        let flag = false;
        const username = $('#user').val();
        $.ajax({
            url: "http://127.0.0.1:8080<%=request.getContextPath()%>/register",
            type: 'GET',
            data: {user: username},
            async: false,
            success: function (data) {
                if (data === 'false') {
                    $('#hint').text('用户名重复')
                } else {
                    flag = true;
                }
            }
        });
        return flag;
    }
</script>
</body>
</html>
