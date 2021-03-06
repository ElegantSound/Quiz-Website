package Tests.Classes.Question;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import classes.question.QuestionQR;

public class QuestionQRTest {

	private HashSet<String> answers;
	private String description;
	private int grade;
	private List<String> userAnswer;
	private QuestionQR question;
	
	@Before
	public void init()
	{
		userAnswer = new ArrayList<String>(); 	
		answers = new HashSet<String>();
		
		description = "Problem";
		grade = 5;
		
		answers.add("a");
		answers.add("b");
		
		question = new QuestionQR(description, grade, answers);
		System.out.println(question.toString());
	}
	
	@Test
	public void testCorrectAnswers() {
		assertEquals((int)question.getGrade(question.getCorrectAnswers()), (int)question.getMaxGrade());
	}
	
	@Test
	public void testBasic() {
		assertEquals(1, question.getCorrectAnswers().size());
		assertTrue(answers.contains(question.getCorrectAnswers().get(0))); // Correct answers is in the answers Set.
		assertEquals(grade, question.getGrade());	
		assertEquals(grade, question.getMaxGrade());
		assertEquals(description, question.getProblem());
		assertTrue(question.getAnswers().equals(answers));
	}
	
	// Checking user's wrong answer.
	@Test
	public void testGetGrade() { 
		userAnswer.add("S");
		assertEquals(0, (int)question.getGrade(userAnswer));
		userAnswer.add("a");
		assertEquals(0, (int)question.getGrade(userAnswer));
	}
	
	@Test
	public void testGetGrade2() { 	// Checking user's right answer.
		List<String> userAnswer = new ArrayList<String>();
		userAnswer.add("a");
		assertEquals(grade, (int)question.getGrade(userAnswer));
		userAnswer.add(0, "b");
		assertEquals(grade, (int)question.getGrade(userAnswer));
		userAnswer.add("c");
		assertEquals(grade, (int)question.getGrade(userAnswer));
	}
	
	// passing 0 grade.
	@Test(expected=IllegalArgumentException.class)
	public void illegalArgumentTest1() { 	
		grade = 0;
		new QuestionQR(description, grade, answers);
	}

	// passing negative grade.
	@Test(expected=IllegalArgumentException.class)
	public void illegalArgumentTest2() { 	
		grade = -1;
		new QuestionQR(description, grade, answers);
	}

	// Null description.
	@Test(expected=IllegalArgumentException.class)
	public void illegalArgumentTest3() { 	
		description = null;
		new QuestionQR(description, grade, answers);
	}

	// Null answers.
	@Test(expected=IllegalArgumentException.class)
	public void illegalArgumentTest4() { 	
		answers = null;
		new QuestionQR(description, grade, answers);
	}

	// answers with null.
	@Test(expected=IllegalArgumentException.class)
	public void illegalArgumentTest5() { 	
		answers.add(null);
		new QuestionQR(description, grade, answers);
	}
	
	// Empty answers.
	@Test(expected=IllegalArgumentException.class)
	public void illegalArgumentTest6() { 
		answers.clear();
		new QuestionQR(description, grade, answers);
	}

	// null userAnswers.
	@Test(expected=IllegalArgumentException.class)
	public void illegalArgumentTest7() {
		userAnswer = null;
		question.getGrade(userAnswer);
	}

	// empty userAnswers.
	@Test(expected=IllegalArgumentException.class)
	public void illegalArgumentTest8() {
		userAnswer.clear();
		question.getGrade(userAnswer);
	}
	
	// userAnswers with null.
	@Test(expected=IllegalArgumentException.class)
	public void illegalArgumentTest9() {
		userAnswer.add(null);
		question.getGrade(userAnswer);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
