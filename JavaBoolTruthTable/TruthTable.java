import java.util.HashMap;

public class TruthTable {
	public static void main(String[] args) {
		String   input = "p || q && !r && q";
		Tree.Node expr = Tree.parse(input);
		String[]  vars = Tree.variables(input);
		Seq     values = evaluations(expr, vars); 
		String   table = tabulate(expr, vars, values);

		System.out.print(table);
	}

	public static HashMap<String, Boolean> makeEnv(String[] keys, boolean[] vals) {
		HashMap<String, Boolean> env = new HashMap<>();
		for (int i = 0; i < keys.length; i++) {
			env.put(keys[i], vals[i]);
		}
		return env;
	}
	public static boolean eval(Tree.Node n, HashMap<String, Boolean> env) {
		if (n.isVar)               return env.get(n.val);
		if (n.val.equals("true"))  return true;
		if (n.val.equals("false")) return false;
		if (n.val.equals("!"))     return !eval(n.left, env);
		if (n.val.equals("||"))    return eval(n.left, env) || eval(n.right, env);
		if (n.val.equals("&&"))    return eval(n.left, env) && eval(n.right, env);
		System.out.println("unrecognized token in evaluator");
		System.exit(1);
		return false;
	}
	public static int twoToThe(int x) { return 1 << x ;}
	public static Seq evaluations(Tree.Node expr, String[] vars) {
 		int outputSize = (vars.length+1) * twoToThe(vars.length);
		Seq output = new Seq(new String[outputSize]);
		output.rowLength = vars.length + 1;
		generate(expr, vars, new boolean[vars.length], output, 0);
		output.resetIndex();
		return output;
	}
	public static void generate(Tree.Node expr, String[] vars, boolean[] bools, Seq s, int n) {
		if (n == vars.length) {
			for (boolean b : bools) { s.puts("" + b); }
			boolean evaluated = eval(expr, makeEnv(vars, bools));
			s.puts("" + evaluated);
			return;
		}
		bools[n] = false;
		generate(expr, vars, bools, s, n+1);
		bools[n] = true;
		generate(expr, vars, bools, s, n+1);
	}
	public static int lengthiest(String[] vars) {
		return java.util.Arrays.stream(vars).map(x -> x.length())
		             .reduce("false".length(), (x, y) -> (x > y) ? x : y);
	}
	public static StringBuilder pad(StringBuilder sb, String a, int l) {
		return repeat(sb, " ", (l - a.length()) + 1);
	}
	public static StringBuilder repeat(StringBuilder sb, String s, int n) {
		for (int i = 0; i < n; i++) { sb.append(s); }
		return sb;
	}
	public static String tabulate(Tree.Node expr, String[] vars, Seq grid) {
		StringBuilder sb = new StringBuilder();
		if (vars.length == 0) {
			boolean single = eval(expr, new HashMap<String, Boolean>());
			return sb.append(single).append("\n").toString();
		}
		String form = "| formula";
		int longest = lengthiest(vars);
		int width = vars.length * (longest + 1) + form.length();
		sb.append("\n");
		for (String a : vars) { pad(sb.append(a), a, longest); }
		sb.append(form).append("\n");
		repeat(sb, "-", width).append("\n");
		while (grid.more()) {
			String b = grid.next();
			pad(sb.append(b), b, longest);
			if (grid.endOfVars()) sb.append("| " + grid.next() + "\n");
			
		}
		repeat(sb, "-", width).append("\n");
		return sb.toString(); 
	}
	static class Seq {
		int index = 0;
		int rowLength;
		String[] array;
		public Seq(String[] ts)     { array = ts; }
		public String next()        { return array[index++]; }
		public void puts(String s)  { array[index++] = s; } 
		public boolean more()       { return (array.length - index) > 0; }
		public void resetIndex()    { index = 0;} 
		public boolean endOfVars()  { return (rowLength-1) == index%rowLength; } 
	}
}