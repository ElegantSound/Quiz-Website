
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import classes.Quiz;
import database.MySQLUtil;
import factory.ClassFactory;

public class QuizDAOImpl implements QuizDAO {

	/*
	 * NOTE: mysql commands do not use column name or table name constants.
	 * And that is ok.
	 */
	
	private DataSource dataSource;
	private ClassFactory modelFactory;
	
	public QuizDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
		modelFactory = new ClassFactory(); 
	}

	@Override
	public Quiz getQuiz(int quizId) {
		Quiz quiz = null;
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"SELECT "
							+ "quizzes.id, username, name, quizzes.description, is_random, is_one_page, immediate_correction, practice_mode, creation_time, time, max_score, category "
							+ "FROM quizzes "
							+ "LEFT JOIN users "
							+ "ON users.id = quizzes.creator_id "
							+ "LEFT JOIN categories "
							+ "ON categories.id = quizzes.category_id "
							+ "WHERE quizzes.id = ?;"
							);
			preparedStatement.setInt(1, quizId);
			ResultSet rs = preparedStatement.executeQuery();
			
			// we need only one row (there should not be more)
			if(rs.next()) quiz = loadIntoQuiz(rs);
			
			preparedStatement.close();
			rs.close();
			con.close();
		} catch (SQLException e) {	e.printStackTrace();}
		return quiz;
	}

	private Quiz loadIntoQuiz(ResultSet rs) throws SQLException {
		Quiz quiz = modelFactory.getQuiz(
				rs.getString("username"), 
				rs.getString("name"), 
				rs.getString("description")
				);
		quiz.setId(rs.getInt("id"));
		quiz.setRandom(rs.getBoolean("is_random"));
		quiz.setOnePage(rs.getBoolean("is_one_page"));
		quiz.setImmediatelyCorrected(rs.getBoolean("immediate_correction"));
		quiz.setHasPracticeMode(rs.getBoolean("practice_mode"));
		quiz.setDateCreated(rs.getLong("creation_time"));
		quiz.setQuizTime(rs.getInt("time"));
		quiz.setMaxScore(rs.getInt("max_score"));
		quiz.setCategory(rs.getString("category"));
		return quiz;
	}

	@Override
	public Quiz updateQuiz(Quiz quiz) {
		Quiz oldQuiz = null;
		oldQuiz = getQuiz(quiz.getId());
		if(oldQuiz == null) return null;
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"UPDATE quizzes "
							+ "SET "
							+ "creator_id = (SELECT id FROM users WHERE username LIKE ?), "
							+ "name = ?, "
							+ "description = ?, "
							+ "is_random = ?, "
							+ "is_one_page = ?, "
							+ "immediate_correction = ?, "
							+ "practice_mode = ?, "
							+ "creation_time = ?, "
							+ "time = ?, "
							+ "max_score = ?, "
							+ "category_id = (SELECT id FROM categories WHERE category LIKE ?) "
							+ "WHERE id = ?");
			preparedStatement.setString(1, quiz.getUserName());
			preparedStatement.setString(2, quiz.getQuizName());
			preparedStatement.setString(3, quiz.getDescription());
			preparedStatement.setBoolean(4, quiz.isRandom());
			preparedStatement.setBoolean(5, quiz.isOnePage());
			preparedStatement.setBoolean(6, quiz.isImmediatelyCorrected());
			preparedStatement.setBoolean(7, quiz.hasPracticeMode());
			preparedStatement.setLong(8, quiz.getDateCreated());
			preparedStatement.setInt(9, quiz.getQuizTime());
			preparedStatement.setInt(10, quiz.getMaxScore());
			preparedStatement.setString(11, quiz.getCategory());
			preparedStatement.setInt(12, quiz.getId());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			con.close();
		} catch (SQLException e) {e.printStackTrace();	}
		return oldQuiz;
	}

	@Override
	public int addQuiz(Quiz quiz) {
		int id = 0;
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"INSERT INTO quizzes "
							+ "("
							+ "creator_id, "
							+ "name, "
							+ "description, "
							+ "is_random, "
							+ "is_one_page, "
							+ "immediate_correction, "
							+ "practice_mode, "
							+ "creation_time, "
							+ "time, "
							+ "max_score,"
							+ "category_id "
							+ ") VALUES ( "
							+ "(SELECT id FROM users WHERE username LIKE ?), "
							+ "?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ "(SELECT id FROM categories WHERE category LIKE ?)"
							+ ");"
							);
			preparedStatement.setString(1, quiz.getUserName());
			preparedStatement.setString(2, quiz.getQuizName());
			preparedStatement.setString(3, quiz.getDescription());
			preparedStatement.setBoolean(4, quiz.isRandom());
			preparedStatement.setBoolean(5, quiz.isOnePage());
			preparedStatement.setBoolean(6, quiz.isImmediatelyCorrected());
			preparedStatement.setBoolean(7, quiz.hasPracticeMode());
			preparedStatement.setLong(8, quiz.getDateCreated());
			preparedStatement.setInt(9, quiz.getQuizTime());
			preparedStatement.setInt(10, quiz.getMaxScore());
			preparedStatement.setString(11, quiz.getCategory());
			preparedStatement.executeUpdate();
			preparedStatement.close();
			id = MySQLUtil.getLastInsertId(con);
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public Quiz deleteQuiz(int quizId) {
		Quiz oldQuiz = null;
		oldQuiz = getQuiz(quizId);
		if(oldQuiz == null) return null;
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"DELETE FROM quizzes WHERE id = ?;"
							);
			preparedStatement.setInt(1, quizId);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			con.close();
		} catch (SQLException e) {e.printStackTrace();}
		return oldQuiz;
	}

	@Override
	public List<Quiz> getRecentQuizzes(int n) {
		List<Quiz> recentQuizzes = new ArrayList<Quiz>();
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"SELECT "
							+ "quizzes.id, username, name, quizzes.description, is_random, is_one_page, immediate_correction, practice_mode, creation_time, time, max_score, category "
							+ "FROM quizzes "
							+ "LEFT JOIN users "
							+ "ON users.id = quizzes.creator_id "
							+ "LEFT JOIN categories "
							+ "ON categories.id = quizzes.category_id "
							+ "ORDER BY creation_time DESC "
							+ "LIMIT ?;"
							);
			preparedStatement.setInt(1, n);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				Quiz quiz = loadIntoQuiz(rs);
				recentQuizzes.add(quiz);
			}
			preparedStatement.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recentQuizzes;
	}

	@Override
	public List<Quiz> getCreatedQuizzes(String userName) {
		List<Quiz> userQuizes = new ArrayList<Quiz>();
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"SELECT "
							+ "quizzes.id, username, name, quizzes.description, is_random, is_one_page, immediate_correction, practice_mode, creation_time, time, max_score, category_id, is_random, is_one_page, immediate_correction, practice_mode, creation_time, category, time, max_score "
							+ "FROM quizzes "
							+ "LEFT JOIN users "
							+ "ON quizzes.creator_id = users.id "
							+ "LEFT JOIN categories "
							+ "ON quizzes.category_id = categories.id "
							+ "WHERE username LIKE ? "
							+ "ORDER BY creation_time DESC;"
							);
			preparedStatement.setString(1, userName);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				Quiz quiz = loadIntoQuiz(rs);
				userQuizes.add(quiz);
			}
			preparedStatement.close();
			rs.close();
			con.close();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		return userQuizes;
	}

	@Override
	public List<Quiz> getSeatchedQuizzes(int numResults, String parameter) {
		List<Quiz> userQuizes = new ArrayList<Quiz>();
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"SELECT "
							+ "quizzes.id, username, name, quizzes.description, is_random, is_one_page, immediate_correction, practice_mode, creation_time, time, max_score, category is_random, is_one_page, immediate_correction, practice_mode, creation_time, category, time, max_score "
							+ "FROM quizzes "
							+ "LEFT JOIN users "
							+ "ON users.id = quizzes.creator_id "
							+ "LEFT JOIN categories "
							+ "ON categories.id = quizzes.category_id "
							+ "LEFT JOIN tags "
							+ "ON tags.quiz_id = quizzes.id "
							+ "WHERE name LIKE ? "
							+ "OR category LIKE ? "
							+ "OR quizzes.description LIKE ? "
							+ "OR username LIKE ? "
							+ "OR tag LIKE ? "
							+ "ORDER BY creation_time DESC "
							+ "LIMIT ?;"
							);
			preparedStatement.setString(1, "%" + parameter + "%");
			preparedStatement.setString(2, "%" + parameter + "%");
			preparedStatement.setString(3, "%" + parameter + "%");
			preparedStatement.setString(4, "%" + parameter + "%");
			preparedStatement.setString(5, "%" + parameter + "%");
			preparedStatement.setInt(6, numResults);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) {
				Quiz quiz = loadIntoQuiz(rs);
				userQuizes.add(quiz);
			}
			preparedStatement.close();
			rs.close();
			con.close();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		return userQuizes;
	}

	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void addTag(int quizId, String tag) {
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"INSERT INTO tags(quiz_id, tag) "
							+ "VALUES(?, ?);"
							);
			preparedStatement.setInt(1, quizId);
			preparedStatement.setString(2, tag);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> getTag(int quizId) {
		Set<String> tags = new HashSet<String> ();
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"SELECT tag "
							+ "FROM tags "
							+ "WHERE quiz_id = ?"
							);
			preparedStatement.setInt(1, quizId);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) tags.add(rs.getString("tag"));
			preparedStatement.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tags;
	}

	@Override
	public void removeTag(int quizId, String tag) {
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"DELETE FROM tags "
							+ "WHERE quiz_id = ? "
							+ " AND "
							+ "tag LIKE ?;"
							);
			preparedStatement.setInt(1, quizId);
			preparedStatement.setString(2, tag);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public Set<String> getCategories() {
		Set<String> categories = new HashSet<String> ();
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"SELECT category "
							+ "FROM categories;"
							);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()) categories.add(rs.getString("category"));
			preparedStatement.close();
			rs.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return categories;
	}

	@Override
	public void addCategory(String category) {
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"INSERT INTO categories (category) "
							+ "values(?);"
							);
			preparedStatement.setString(1, category);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void removeCategory(String category) {
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(
							"DELETE FROM categories "
							+ "WHERE category LIKE ?;"
							);
			preparedStatement.setString(1, category);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}
