package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import classes.User;
import factory.ClassFactory;

public class UserDAOImpl implements UserDAO {

	DataSource dataSource;
	ClassFactory classFactory;
	
	public UserDAOImpl(DataSource dataSource) {
		this.dataSource = dataSource;
		classFactory = new ClassFactory();	// TODO: pass as parameter
	}
	
	@Override
	public User getUser(String userName) {
		User user = null;
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement = 
					con.prepareStatement("SELECT "
							+ "users.username, users.hash_password, "
							+ "users.salt, users.description, users.image, "
							+ "users2.username AS username2 "
							+ "FROM users "
							+ "LEFT JOIN friends "
							+ "ON users.id = friends.first_user_id "
							+ "LEFT JOIN users AS users2 "
							+ "ON users2.id = friends.second_user_id "
							+ "WHERE users.username like ?;");
			preparedStatement.setString(1, userName);
			ResultSet rs = preparedStatement.executeQuery();
			user = loadIntoUser(rs);
			rs.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}


	private User loadIntoUser(ResultSet rs) throws SQLException {
		User user = null;
		if(!rs.next())
			return null; 
		else {
			user = classFactory.getUser(rs.getString("username"), 
					rs.getString("hash_password"), 
					rs.getString("salt"));
			user.setDescription(rs.getString("description"));
			rs.previous();
			}
		Set<String> friends = new HashSet<String> ();
		while(rs.next()) friends.add(rs.getString("username2"));
		user.setFriends(friends);
		return user;
	}

	@Override
	public User deleteUser(String userName) {
		User oldUser = getUser(userName);
		// return null if there is no such user
		if(oldUser == null) return null;
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement = 
					con.prepareStatement("DELETE FROM users WHERE username LIKE ?;");
			preparedStatement.setString(1, userName);
			preparedStatement.executeUpdate();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return oldUser;
	}

	@Override
	public User updateUser(User user) {
		User oldUser = getUser(user.getUserName());
		// return null if there is no such user
		if(oldUser == null) return null;	
		// delete old, put in new and add additional data
		deleteUser(user.getUserName());
		addUser(user.getUserName(),
				user.getHashedPassword(),
				user.getSalt());
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement = 
					con.prepareStatement(updateCommand());
			preparedStatement.setString(4, user.getUserName());
			preparedStatement.setString(1, user.getHashedPassword());
			preparedStatement.setString(2, user.getSalt());
			preparedStatement.setString(3, user.getDescription());
			preparedStatement.executeUpdate();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return oldUser;
	}

	private String updateCommand() {
		return "UPDATE users SET hash_password = ?, salt = ?, description = ? "
				+ "WHERE username LIKE ?;";
	}

	@Override
	public User addUser(String userName, String hexPassword, String salt) {
		User newUser = null;
		newUser = getUser(userName);
		if(newUser != null) return null;	// if there is one already - failed
		try {
			Connection con = dataSource.getConnection();
			PreparedStatement preparedStatement =
					con.prepareStatement(addingCommand());
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, hexPassword);
			preparedStatement.setString(3, salt);
			preparedStatement.executeUpdate();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getUser(userName);
	}

	private String addingCommand() {
		return "INSERT INTO users (username, hash_password, salt, description) VALUES(?, ?, ?);";
	}

}
