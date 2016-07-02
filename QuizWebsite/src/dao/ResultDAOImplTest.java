package dao;

import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import classes.Result;
import classes.User;
import classes.Answer;
import classes.Quiz;
import factory.ClassFactory;

public class ResultDAOImplTest {

	static ResultDAO resultDAO;
	static UserDAO userDAO;
	static QuizDAO quizDAO;
	static ClassFactory classFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DAOInstances daoFactory = new DAOInstances();
		daoFactory.init();
		resultDAO = daoFactory.getResultDAO();
		userDAO = daoFactory.getUserDAO();
		quizDAO = daoFactory.getQuizDAO();
		classFactory = new ClassFactory();
	}

	// this is scratching surface barely
	// needs better check
	@Test
	public void test() {
		
		userDAO.addUser("vaja", "vajaspass", "vajassalt");
		userDAO.addUser("vajab", "vajaspass", "vajassalt");
		userDAO.addUser("vajabe", "vajaspass", "vajassalt");
		userDAO.addUser("vajaber", "vajaspass", "vajassalt");
		userDAO.addUser("vajabere", "vajaspass", "vajassalt");
		userDAO.addUser("vajaberej", "vajaspass", "vajassalt");
		userDAO.addUser("vajabereja", "vajaspass", "vajassalt");
		
		Result result1 = buildResult("vaja", 1, 90, 2005, 1);
		Result result2 = buildResult("vajab", 2, 91, 2006, 2);
		Result result21 = buildResult("vajabe", 2, 71, 2034, 9);
		Result result3 = buildResult("vajabe", 3, 92, 2007, 3);
		Result result4 = buildResult("vajaber", 4, 93, 2008, 4);
		Result result41 = buildResult("vajaber", 2, 46, 201, 9);
		Result result5 = buildResult("vajabere", 5, 94, 2008, 5);
		Result result6 = buildResult("vajaberej", 2, 95, 2009, 6);
		Result result7 = buildResult("vajabereja", 1, 96, 2010, 7);
		
		resultDAO.insertResult(result1);
		resultDAO.insertResult(result2);
		resultDAO.insertResult(result21);
		resultDAO.insertResult(result3);
		resultDAO.insertResult(result4);
		resultDAO.insertResult(result41);
		resultDAO.insertResult(result5);
		resultDAO.insertResult(result6);
		resultDAO.insertResult(result7);
		
		System.out.println("best by quiz id: " + resultDAO.getBestResults(2, 3, 2000));
		System.out.println("best by user	:" + resultDAO.getBestResults("vajaber", 5, 202));

		System.out.println("recent q_id n	:" + resultDAO.getRecentResults(2, 2));
		System.out.println("recent u_nm n	:" + resultDAO.getRecentResults("vajaber", 3));
		
		System.out.println("pop quizes	202	:" + resultDAO.getPopularQuizzes(5, 202));
	}

	// result construction wrapper
	private Result buildResult(String u, int q_id, int grade, int start, int duration) {
		Result result = classFactory.getResult(u, q_id);
		result.setFinalGrade(grade);
		result.setTimeStarted(start);
		result.setTimeTaken(duration);
		return result;
	}

	/*
	@Test
	public void test2() {
		Set<String> friends = new HashSet<String>(Arrays.asList("b", "c"));
		User testUser = new User("username", "a1234", "a123");
//		testUser.setFriends(friends);
		userDAO.deleteUser("username");
		userDAO.addUser("username", "a123", "a12");	
		userDAO.updateUser(testUser);  // Aq test user maq ukve setiani.
	
		int lastId = getNewQuizId();
		
		Result result1 = classFactory.getResult("username", lastId);
		result1.setFinalGrade(98);
		result1.setTimeStarted(2005);
		result1.setTimeTaken(5);
		
		List<Answer> answers1 = new ArrayList<Answer> ();
		Answer answer11 = classFactory.getAnswer(Arrays.asList("drogba", "gadavida", "chinetshi"));
		Answer answer12 = classFactory.getAnswer(Arrays.asList("messi", "aris", "sauketeso"));
		
		answers1.add(answer11);
		answers1.add(answer12);
		
		result1.setAnswers(answers1);
		resultDAO.insertResult(result1);
		
		Result result2 = classFactory.getResult("username", lastId);
		result2.setFinalGrade(98);
		result2.setTimeStarted(2005);
		result2.setTimeTaken(5);
		
		List<Answer> answers2 = new ArrayList<Answer> ();
		Answer answer21 = classFactory.getAnswer(Arrays.asList("2", "3", "4"));
		Answer answer22 = classFactory.getAnswer(Arrays.asList("5", "6", "7"));
		
		answers2.add(answer21);
		answers2.add(answer22);
		
		result2.setAnswers(answers2);
		resultDAO.insertResult(result2);
		
		Result result3 = classFactory.getResult("username", lastId);
		result3.setFinalGrade(98);
		result3.setTimeStarted(2005);
		result3.setTimeTaken(5);
		
		List<Answer> answers3 = new ArrayList<Answer> ();
		Answer answer31 = classFactory.getAnswer(Arrays.asList("z", "zz", "zzz"));
		Answer answer32 = classFactory.getAnswer(Arrays.asList("x", "xx", "xxx"));
		
		answers3.add(answer31);
		answers3.add(answer32);
		
		result3.setAnswers(answers3);
		resultDAO.insertResult(result3);
		
		List<Result> results = resultDAO.getResult("username", lastId);
		
		System.out.println(results.toString());
		System.out.println("Number of results: " + results.size());
	}
	*/
	
	private int getNewQuizId()
	{
		Quiz quiz = new Quiz("vaja", "Future Vulture", "quiz about 2034");
		quiz.setDateCreated(20);
		quiz.setDescription("desc");
		quiz.setQuizName("name");
		quiz.setQuizTime(10);
		quiz.setMaxScore(100);
		quiz.setRandom(true);
		quiz.setOnePage(true);
		quiz.setImmediatelyCorrected(false);
		quiz.setHasPracticeMode(false);
		quiz.setCategory("Phys");
		return quizDAO.addQuiz(quiz);
	}
}
