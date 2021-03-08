import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class Main
{
	private static boolean validParentheses(String string)
	{
		Stack<Character> stack = new Stack<>();
		for(char token: string.replaceAll("[\\d\\s\\-+*/.^]", "").toCharArray())
		{
			if (token == '(')  stack.push(token);
			else
			{
				if (stack.empty()) return false;
				stack.pop();
			}
		}
		return stack.empty();
	}

	private static boolean verify(String input)
	{
		return validParentheses(input) && !input.matches("(.*[^\\d\\s\\-+*/().^].*)|(.*^[.+*/^)].*)|(.*[.\\-+*/(^]$.*)|(.*[.+*/^]{2,}.*)|(.*[(-][.+*/^)].*)|(.*\\d+\\(.*)|(.*/0.*)|(.*0+[\\.1-9].*)");
	}

	private static String[] tokenize(String string)
	{
		return Pattern.compile("((?<=^|[+\\-/^*])-?[\\d.]+)|([+\\-/^*)(])|([\\d.]+)", Pattern.MULTILINE).matcher(string).replaceAll("$0 ").split("\\s");
	}

	private static String[] reversePolish(String[] tokenStream)
	{
		Stack<String> operators = new Stack<>();
		ArrayList<String> result = new ArrayList<>();
		Map<String, Integer> map = Map.of("(", 0, "+", 1, "-", 1, "*", 2, "/", 2, "^", 3);
		for(String token: tokenStream)
		{
			if(token.matches(".*\\d.*")) result.add(token);
			else if(token.equals("(")) operators.push(token);
			else if (token.equals(")"))
			{
				while(!operators.empty() && !operators.peek().matches("\\(")) result.add(operators.pop());
				operators.pop();
			}
			else
			{
				while(!operators.empty() && map.get(token) <= map.get(operators.peek())) result.add(operators.pop());
				operators.push(token);
			}
		}
		while(!operators.empty()) result.add(operators.pop());
		return result.toArray(String[]::new);
	}

	private static double calculate(double a, double b, String c)
	{
		if(c.equals("+")) return b + a;
		else if(c.equals("-")) return b - a;
		else if(c.equals("*")) return b * a;
		else if(c.equals("/"))
		{
			if (a == 0) throw new ArithmeticException();
			return b / a;
		}
		return Math.pow(b, a);
	}

	private static String evaluate(String input)
	{
		Stack<String> result = new Stack<>();
		for(String token: reversePolish(tokenize(input)))
		{
			if(token.matches(".*\\d.*")) result.push(token);
			else result.push(String.valueOf(calculate(Double.parseDouble(result.pop()), Double.parseDouble(result.pop()), token)));
		}
		return result.pop();
	}

	public static void main(String[] args)
	{
		final Scanner console = new Scanner(System.in);
		System.out.print("Enter the expression to be evaluated:\n>> ");
		while(true)
		{
			String userInput;
			try
			{
				System.out.print((verify((userInput = console.nextLine().replaceAll(" ", "")))) ? "= " + evaluate(userInput) + "\n>> " : "Invalid expression. Try again.\n>> ");
			}
			catch (ArithmeticException e)
			{
				System.out.print("Invalid expression. Try again.\n>> ");
			}
		}
	}
}