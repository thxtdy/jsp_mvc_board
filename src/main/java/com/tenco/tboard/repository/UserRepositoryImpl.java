package com.tenco.tboard.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.tenco.tboard.model.User;
import com.tenco.tboard.repository.interfaces.UserRepository;
import com.tenco.tboard.util.DBUtil;

public class UserRepositoryImpl implements UserRepository{
	
	private static final String INSERT_USER_SQL = " INSERT INTO users(username, password, email) VALUES(?, ?, ?) ";
	private static final String DELETE_USER_SQL = " DELETE FROM users WHERE id = ?";
	private static final String SELECT_USER_BY_USERNAME = " SELECT * FROM users WHERE username = ? ";
	private static final String SELECT_USER_BY_USERNAME_AND_PASSWORD = " SELECT * FROM users WHERE username = ? AND password = ?";
	private static final String SELECT_ALL_USERS = " SELECT * FROM users ";
	private static final String SELECT_USER_BY_USERNAME_AND_EMAIL = " SELECT * FROM users WHERE username = ? AND email = ? ";
	
	@Override
	public int addUser(User user) {
		int rowCount = 0;
		try (Connection conn = DBUtil.getConnection()){
			// 트랜잭션 시작
			conn.setAutoCommit(false);
			// username 중복 확인 필요
			// email 중복 확인 필요
			try (PreparedStatement ptmt = conn.prepareStatement(INSERT_USER_SQL)) {
				ptmt.setString(1, user.getUsername());
				ptmt.setString(2, user.getPassword());
				ptmt.setString(3, user.getEmail());
				rowCount = ptmt.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowCount;
	}

	@Override
	public void deleteUser(int id) {
		try (Connection conn = DBUtil.getConnection()){
			// 트랜잭션 시작
			conn.setAutoCommit(false);
			// username 중복 확인 필요
			// email 중복 확인 필요
			try (PreparedStatement ptmt = conn.prepareStatement(DELETE_USER_SQL)) {
				ptmt.setInt(1, id);
				int rowCount = ptmt.executeUpdate();
				if(rowCount > 0) {
					conn.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public User getUserByusername(String username) {
		User user = null;
	
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement ptmt = conn.prepareStatement(SELECT_USER_BY_USERNAME)){
			ptmt.setString(1, username);
			ResultSet rs =  ptmt.executeQuery();
			if(rs.next()) { // 단일행이 떨어짐 = username 이 unique값
				user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.password(rs.getString("password"))
						.email(rs.getString("email"))
						.createdAt(rs.getTimestamp("created_at"))
						.build();
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
	}
		return user;
}

	@Override
	public User getUserByusernameAndPassword(String username, String password) {
		User user = null;
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement ptmt = conn.prepareStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD)){
			ptmt.setString(1, username);
			ptmt.setString(2, password);
			ResultSet rs =  ptmt.executeQuery();
			if(rs.next()) { 
				user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.password(rs.getString("password"))
						.email(rs.getString("email"))
						.createdAt(rs.getTimestamp("created_at"))
						.build();
				return user;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		return user;
	}
	@Override
	public List<User> getAllUsers() {
		// 자료 구조 생성 먼저
		List<User> list = new ArrayList<User>();
		try (Connection conn = DBUtil.getConnection();
			 PreparedStatement ptmt = conn.prepareStatement(SELECT_ALL_USERS)){
			ResultSet rs =  ptmt.executeQuery();
			while(rs.next()) { 
			User user = User.builder()
						.id(rs.getInt("id"))
						.username(rs.getString("username"))
						.password(rs.getString("password"))
						.email(rs.getString("email"))
						.createdAt(rs.getTimestamp("created_at"))
						.build();
				list.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
