package dao;

import java.util.List;
import java.util.Set;

import classes.Quiz; 

public interface QuizDAO {

	/**
	 * Returns Quiz object for given id.
	 * 
	 * @param quizId - int quiz id
	 * @return quiz - Quiz object, null if not found
	 */
	public Quiz getQuiz(int quizId);
	
	/**
	 * Updates quiz and returns overwritten one.
	 * 
	 * @param quiz - new Quiz object (includes own id)
	 * @return oldQuiz - overwritten Quiz object
	 */
	public Quiz updateQuiz(Quiz quiz);
	
	/**
	 * Adds given quiz into quizzes.
	 * 
	 * @param quiz - Quiz object
	 * @return id - id of added quiz
	 */
	public int addQuiz(Quiz quiz);
	
	/**
	 * Deletes quiz with given quiz id.
	 * 
	 * @param quizId - int quiz id to delete
	 * @return oldQuiz - deleted Quiz object, null if does not exist
	 */
	public Quiz deleteQuiz(int quizId);
	
	/**
	 * Returns a list with Quizzes in it sorted by creation date.
	 * 
	 * @param n - number of quizzes
	 * @return list with Quizzes sorted by creation date
	 */
	public List<Quiz> getRecentQuizzes(int n);
	
	/**
	 * Returns a list with recent Quizzes created by user.
	 * 
	 * @return list with recent Quizzes created by user
	 */
	public List<Quiz> getCreatedQuizzes(String userName);	
	
	/**
	 * Returns a list with Quizzes returned by a search.
	 * 
	 * @param parameter - parameter user entered to search for it
	 * @param numResults - size of the list
	 * @return list with  with Quizzes returned by a search
	 */
	public List<Quiz> getSeatchedQuizzes(int numResults, String parameter);
	
	
	
	
	
	
	
	
	
	/**
	 * Adds tag to given quiz. 
	 * 
	 * @param quizId - int quiz id
	 * @param tag - String tag
	 */
	public void addTag(int quizId, String tag);
	
	/**
	 * Returns a list of tags of given quiz.
	 * 
	 * @param quizId - int quiz id
	 * @return set of String tags
	 */
	public Set<String> getTag(int quizId);
	
	/**
	 * Removes tag of given quiz.
	 * 
	 * @param quizId - int quiz id
	 * @param tag - String
	 */
	public void removeTag(int quizId, String tag);
	
	/**
	 * Returns list of existing categories.
	 * 
	 * @return String category
	 */
	public Set<String> getCategories();
	
	/**
	 * Add category to available categories.
	 * 
	 * @param category - String
	 */
	public void addCategory(String category);
	
	/**
	 * Removes category from available categories if it exists.
	 * 
	 * @param category - String
	 */
	public void removeCategory(String category);
	
}
