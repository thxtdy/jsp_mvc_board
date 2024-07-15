package com.tenco.tboard.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public BoardController() {

    }

    public void init() throws ServletException {
    	// BoardRepository 추가 예정
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		
		switch (action) {
		case "/create":
			// TODO - 게시글 생성 페이지 이동 처리
			break;
		case "/list":
			// TODO - 게시글 목록 페이지 이동 처리
			handleListBoards(request, response);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void handleListBoards(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 게시글 목록 조회 기능
		int page = 1; // 기본 페이지 번호
		int pageSize = 10; // 한 페이지 당 조회 가능한 글 개수
		
		try {
			String pageStr = request.getParameter("page");
			if(pageStr != null) {
				page = Integer.parseInt(pageStr);
			}
		} catch (Exception e) {
			page = 1;
		}
		
		int offset = (page -1 ) * pageSize;
		
		request.getRequestDispatcher("/WEB-INF/views/board/list.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
