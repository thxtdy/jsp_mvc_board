package com.tenco.tboard.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import com.mysql.cj.Session;
import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.UserRepositoryImpl;
import com.tenco.tboard.repository.interfaces.UserRepository;
@WebServlet("/user/*")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private UserRepository userRepository;
	
    public UserController() {
    	userRepository = new UserRepositoryImpl();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		
		switch (action) {
		case "/signup":
			request.getRequestDispatcher("/WEB-INF/views/user/signup.jsp").forward(request, response);
			break;
		case "/signin":
			// TODO 로그 인 추가 예정
			request.getRequestDispatcher("/WEB-INF/views/user/signin.jsp").forward(request, response);
			break;
		case "/logout":
			// TODO 로그 아웃 추가 예정
			handleLogout(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		session.invalidate(); // 세션 얻고 세션 무효화 처리 => 로그인 세션에서 튕구는거
		
		response.sendRedirect(request.getContextPath() + "/user/signin"); // 튕구고 다시 로그인 페이지로 보내기
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		
		
		
		switch (action) {
		case "/signup": // 회원가입 기능
			handleSignup(request, response);
			break;
		case "/signin":
			handleSignin(request, response);
		break;
		default:
			break;
		}
		
	}


	private void handleSignup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		System.out.println(username);
		// 데이터 유효성 검사 생략.... 왜?
		
		User user = User.builder()
				.username(username)
				.password(password)
				.email(email)
				.build();
		int result =  userRepository.addUser(user);
		if(result != 0) {
			// 스크립트 처리도 가능하달까?
			response.sendRedirect(request.getContextPath() + "/user/signin");
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('중복된 ID 입니다.'); history.back(); </script>");
		}
	}

	private void handleSignin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		User principal =  userRepository.getUserByusernameAndPassword(username, password);
		if(principal != null && principal.getUsername().equals(principal.getUsername()) && principal.getPassword().equals(principal.getPassword()) ) {
			
			HttpSession session = request.getSession();
			session.setAttribute("principal", principal);
			
			// 302(브라우저) => 바로 서블릿 클래스 (BoardController) .. (JSP 내부 이동)
			System.out.println("로그인 완료");
			response.sendRedirect(request.getContextPath() + "/board/list");
		} else {
			request.setAttribute("errorMessage", "ID/PW가 일치하지 않습니다.");
			request.getRequestDispatcher("/WEB-INF/views/user/signin.jsp").forward(request, response);
		}
		
	}
}
