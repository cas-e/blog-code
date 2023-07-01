import java.util.Arrays;

public class BoolEval {
    public static void main(String[] args) {
        String testInput = "(or false (and true (and (not false) true)))";
        String[]  tokens = tokenize(testInput);
        boolean    value = evaluate(tokens);
        System.out.println(value);
    }
    public static String[] tokenize(String input) {
        String[] s = input.replace("(", "  ").replace(")", "  ").split("\\s+");
        return Arrays.stream(s).filter(x -> x.length() > 0).toArray(String[]::new);
    }
	public static boolean evaluate(String[] formula) {
		Seq ops = new Seq(formula);
		boolean value = eval(ops);	
		return value;
	}
	public static boolean eval(Seq ops) {
		String op = ops.take();
		if (op.equals("true"))    return true;
		if (op.equals("false"))   return false;
		if (op.equals("not"))     return !eval(ops);
		if (op.equals("or"))      return eval(ops) || eval(ops);
		if (op.equals("and"))     return eval(ops) && eval(ops);

		System.out.println("unrecognized token: " + op);
		System.exit(1);
		return false;
	}
}
class Seq {
	int index;
	String[] array;
	public Seq(String[] sa) { 
		this.array = sa;
	}
	public String take() { 
		return this.array[this.index++]; 
	}
}