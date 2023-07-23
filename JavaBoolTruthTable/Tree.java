import java.util.Arrays;

public class Tree {
	/*  Exports
	 *
	 */
	public static class Node {
		String val;
		Node left;
		Node right;
		boolean isVar = false;
		public Node(String s, Node l, Node r) {
			val = s;
			left = l;
			right = r;
		}
	}
	public static Node parse(String input) {
		return parseExpr(tokenize(input));
	}
	public static String[] variables(String input) {
		return getVars(tokenize(input), keywords);
	}
	/* Internals
	 *
	 */
	private static String[] keywords  = {"true", "false", "&&", "||", "!", "(", ")"};
	
	private static boolean member(String s, String[] els) {
		for (String e : els) { if (s.equals(e)) return true; }
		return false;
	}
	private static String[] getVars(String[] tokens, String[] keywords) {
    	return Arrays.stream(tokens).distinct()
    		.filter(x -> !member(x, keywords)).toArray(String[]::new);
    }
    private static String[] tokenize(String input) {
	    String s = input.replace("(", " ( ").replace(")", " ) ").replace("!", " ! ");
	    String[] sa = s.replace("&&", " && ").replace("||", " || ").split("\\s+");
	    return Arrays.stream(sa).filter(x -> x.length() > 0).toArray(String[]::new);
	}
	private static Node parseExpr(String[] tokens) {
		Seq toks = new Seq(tokens);
		return parseInfix(toks, 0);
	}
	private static int[] bindingPower(String s) {
		if (s.equals("||")) { return new int[]{1, 2};}
		if (s.equals("&&")) { return new int[]{3, 4};} 
		return new int[]{-1, 5}; // case for !
	}
	private static Node parseInfix(Seq toks, int minbp) {
		Node lhs = parsePrefix(toks);
		while (toks.more()) {
			String op = toks.peek();
			if (op.equals(")")) {
				break;
			} 
			if (!(op.equals("&&") || op.equals("||"))) {
				error("bad operator");
			}
			int lbp = bindingPower(op)[0];
			int rbp = bindingPower(op)[1];
			if (lbp < minbp) {
				break;
			}
			toks.take();
			Node rhs = parseInfix(toks, rbp);
			lhs = new Node(op, lhs, rhs);
		}
		return lhs;
	}
	private static Node parsePrefix(Seq toks) {
		if (!toks.more()) {
			error("unexpected end of line");
		}
		String tok = toks.take();
		if (tok.equals("true") || tok.equals("false")) {
			return new Node(tok, null, null);
		}
		if (tok.equals("!")) {
			int rbp = bindingPower(tok)[1];
			return new Node("!", parseInfix(toks, rbp), null);
		}
		if (tok.equals("(")) {
			Node node = parseInfix(toks, 0);
			if (!toks.peek().equals(")")) {
				error("unbalanced paren");
			}
			toks.take();
			return node;
		}
		if (tok.equals("&&") || tok.equals("||") || tok.equals(")")) {
			error("bad operand");
		}
		Node node = new Node(tok, null, null);
		node.isVar = true;
		return node;
	}
	private static void error(String s) {
		System.out.println(s);
		System.exit(1);
	}
	private static class Seq {
		int index;
		String[] array;
		public Seq(String[] s) { array = s;}
		public boolean more()  { return (array.length - index) > 0; }
		public String take()   { return array[index++]; }
		public String peek() { 
			if (!more()) {
				return "";
			}
			return array[index];
		}
	}
}