package com.tenco.tboard.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import com.tenco.tboard.model.Board;
import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.BoardRepositoryImpl;
import com.tenco.tboard.repository.interfaces.BoardRepository;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardRepository boardRepository;

	public void init() throws ServletException {
		boardRepository = new BoardRepositoryImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		HttpSession session = request.getSession(false);
		if (session == null && session.getAttribute("principal") == null) {
			response.sendRedirect(request.getContextPath() + "/user/signin");
			return;
		}

		switch (action) {
		case "/create":
			showCreateBoardForm(request, response);
			break;
		case "/list":
			handleListBoards(request, response, session);
			break;
		case "/delete":
			handleDeleteBoards(request, response, session);
			break;
		case "/update":
			showUpdateBoardForm(request, response, session);
			break;
		case "/view":
			showViewBoard(request, response, session);
			break;
		
		case "/deleteComment":
			handleDeleteComment(request, response, session);
			break;
			
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}
	/**
	 * 댓글 삭제 기능 (GET 방식 처리)
	 * @param request
	 * @param response
	 * @param session
	 */
	private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		
	}
	/**
	 * 상세보기 화면 이동 (GET 방식 처리)
	 * @param request
	 * @param response
	 * @param session
	 */
	private void showViewBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			Board board = boardRepository.getBoardById(id);
			if(board == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			request.setAttribute("board", board);
			
			// 현재 로그인한 사용자의 ID
			User user = (User) session.getAttribute("principal");
			if(user != null) {
				request.setAttribute("userId", user.getId());
			}
			
			// TODO - 댓글 조회 및 권한 확인 추가 예정
			request.getRequestDispatcher("/WEB-INF/views/board/view.jsp").forward(request, response);
			
		} catch (Exception e) {

		}
	}

	/**
	 * 업데이트 폼으로 전송 (인증 검사 필수)
	 * @param request
	 * @param response
	 * @param session
	 */
	private void showUpdateBoardForm(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		
	}

	/**
	 * Board 삭제
	 * @param request
	 * @param response
	 * @param session
	 */
	private void handleDeleteBoards(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		
	}

	/**
	 * 게시글 생성 화면 이동
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void showCreateBoardForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("create");
		request.getRequestDispatcher("/WEB-INF/views/board/create.jsp").forward(request, response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void handleListBoards(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws ServletException, IOException {
		// 게시글 목록 조회 기능
		int page = 1; // 기본 페이지 번호
		int pageSize = 5; // 한 페이지 당 조회 가능한 글 개수

		try {
			String pageStr = request.getParameter("page");
			if (pageStr != null) {
				page = Integer.parseInt(pageStr);
			}
		} catch (Exception e) {
			page = 1;
		}

		int offset = (page - 1) * pageSize;

		List<Board> boardList = boardRepository.getAllBoards(pageSize, offset);
		request.setAttribute("boardList", boardList);

		// 페이징 처리 1단계 (현재 페이지 번호, pageSize, offset)

		// 전체 게시글 수 조회
		int totalBoards = boardRepository.getTotalBoardCount();

		int totalPages = (int) Math.ceil((double) totalBoards / pageSize);

		request.setAttribute("boardList", boardList);
		request.setAttribute("totalPages", totalPages);
		System.out.println("총 페이지 블록 수 " + totalPages);
		request.setAttribute("currentPage", page);

		// 현재 로그인한 사용자 ID 설정이랄까?ㅋ
		if (session != null) {
			User user = (User) session.getAttribute("principal");
			if (user != null) {
				request.setAttribute("userId", user.getId());
			}
		}

		request.getRequestDispatcher("/WEB-INF/views/board/list.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		String action = request.getPathInfo();
		HttpSession session = request.getSession(false);
		if (session == null && session.getAttribute("principal") == null) {
			response.sendRedirect(request.getContextPath() + "/user/signin");
			return;
		}

		switch (action) {
		case "/create":
			handleCreateBoard(request, response, session);
			break;
			
		case "/edit":
			break;
			
		case "/addComment":
			break;

		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}
	/**
	 * 게시글 생성 처리
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException 
	 */
	private void handleCreateBoard(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		User user = (User) session.getAttribute("principal"); // signIn에서 설정한 속성 가져와서 Board.builder에 넣기
		
		Board board = Board.builder()
				.userId(user.getId())
				.title(title)
				.content(content)
				.build();
		boardRepository.addBoard(board);
		response.sendRedirect(request.getContextPath() + "/board/list?page=1");
	}

}
