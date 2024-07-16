package com.tenco.tboard.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tenco.tboard.model.Board;
import com.tenco.tboard.repository.interfaces.BoardRepository;
import com.tenco.tboard.util.DBUtil;

public class BoardRepositoryImpl implements BoardRepository {

	private static final String SELECT_ALL_BOARDS = " SELECT * FROM board ORDER BY created_at DESC LIMIT ? OFFSET ? ";
	private static final String COUNT_ALL_BOARDS = " SELECT COUNT(*) FROM board ";
	private static final String INSERT_BOARD_SQL = " INSERT INTO board(user_id, title, content) VALUES(?, ?, ?) ";
	private static final String DELETE_BOARD_SQL = " DELETE FROM board where id = ? ";
	private static final String SELECT_BOARD_BY_ID = " SELECT * FROM board WHERE id = ? ";
	private static final String UPDATE_BOARD_SQL = " UPDATE board SET title = ?, content = ? WHERE id = ? ";
	
	@Override
	public void addBoard(Board board) {
		// 논리적인 하나의 작업의 단위
		try (Connection conn = DBUtil.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement ptmt = conn.prepareStatement(INSERT_BOARD_SQL)) {
				ptmt.setInt(1, board.getUserId());
				ptmt.setString(2, board.getTitle());
				ptmt.setString(3, board.getContent());
				ptmt.executeUpdate();

				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void updateBoard(Board board) {
		try (Connection conn = DBUtil.getConnection()){
			conn.setAutoCommit(false);
			try (PreparedStatement ptmt = conn.prepareStatement(UPDATE_BOARD_SQL)){
				ptmt.setString(1, board.getTitle());
				ptmt.setString(2, board.getContent());
				ptmt.setInt(3, board.getId());
				ptmt.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteBoard(int id) {
		try (Connection conn = DBUtil.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement ptmt = conn.prepareStatement(DELETE_BOARD_SQL)) {
				ptmt.setInt(1, id);
				ptmt.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Board getBoardById(int id) {
		Board board = null;

		try (Connection conn = DBUtil.getConnection()) {
			PreparedStatement ptmt = conn.prepareStatement(SELECT_BOARD_BY_ID);
			ptmt.setInt(1, id);

			try (ResultSet rs = ptmt.executeQuery()) {
				if (rs.next()) {
					board = Board.builder()
							.id(rs.getInt("id"))
							.userId(rs.getInt("user_id"))
							.title(rs.getString("title"))
							.content(rs.getString("content"))
							.createdAt(rs.getTimestamp("created_at"))
							.build();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return board;
	}

	@Override
	public List<Board> getAllBoards(int limit, int offset) {
		List<Board> boardList = new ArrayList<Board>();

		try (Connection conn = DBUtil.getConnection()) {
			PreparedStatement ptmt = conn.prepareStatement(SELECT_ALL_BOARDS);
			ptmt.setInt(1, limit);
			ptmt.setInt(2, offset);
			ResultSet rs = ptmt.executeQuery();
			while (rs.next()) {
				boardList.add(
						Board.builder().id(rs.getInt("id")).userId(rs.getInt("user_id")).title(rs.getString("title"))
								.content(rs.getString("content")).createdAt(rs.getTimestamp("created_at")).build());
			}
			System.out.println("BoardRepostitoryImpl - 로깅 : Count : " + boardList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return boardList;
	}

	@Override
	public int getTotalBoardCount() {
		int count = 0;

		try (Connection conn = DBUtil.getConnection()) {
			PreparedStatement ptmt = conn.prepareStatement(COUNT_ALL_BOARDS);
			ResultSet rs = ptmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("로깅 TotalCount : " + count);

		return count;
	}

}
