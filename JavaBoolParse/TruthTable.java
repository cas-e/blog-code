public class TruthTable {
	public static void main(String[] args) {
		String   input = "false || true  && (!false && true)";
		Tree.Node tree = Tree.parse(input);
		boolean answer = eval(tree);

		System.out.println(answer);
	}
	public static boolean eval(Tree.Node n) {
		if (n.val.equals("true"))  return true;
		if (n.val.equals("false")) return false;
		if (n.val.equals("!"))     return !eval(n.left);
		if (n.val.equals("||"))    return eval(n.left) || eval(n.right);
		if (n.val.equals("&&"))    return eval(n.left) && eval(n.right);
		System.out.println("unrecognized token in evaluator");
		System.exit(1);
		return false;
	}
}