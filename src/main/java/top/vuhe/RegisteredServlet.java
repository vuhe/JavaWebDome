package top.vuhe;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhuhe
 */
public class RegisteredServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static SqlSession sqlSession = null;

    static {
        try {
            InputStream input = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
            sqlSession = sqlSessionFactory.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 获取用户名，用于检测是否存在用户
        String username = request.getParameter("user");
        withSqlSessionRun(() -> {
            List<Object> list = sqlSession.selectList("UserMapper.getUserByName", username);
            try (var out = new OutputStreamWriter(
                    response.getOutputStream(), StandardCharsets.UTF_8)) {
                out.write(list.isEmpty() ? "true" : "false");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = new User()
                .setUsername(request.getParameter("user"))
                .setPassword(request.getParameter("password"))
                .setBirthday(request.getParameter("birthday"))
                .setEmail(request.getParameter("email"));
        withSqlSessionRun(() -> sqlSession.insert("UserMapper.insertUser", user));
        session.setAttribute("user", user);
        response.sendRedirect(request.getContextPath() + "/success.jsp");
    }

    private void withSqlSessionRun(NullSafeDo function) {
        if (sqlSession != null) {
            function.run();
            sqlSession.commit();
        } else {
            throw new NullPointerException("Can't find resources file!");
        }
    }
}
