package server;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MathExpressionParser {
    // Save previous result so we can recall it later
    private String ans;
    
     // Operators
    static DoubleBinaryOperator sum = (a, b) -> a + b;
    static DoubleBinaryOperator div = (a, b) -> a / b;
    static DoubleBinaryOperator mul = (a, b) -> a * b;
    static DoubleBinaryOperator sub = (a, b) -> a - b;
    static DoubleBinaryOperator pow = (a, b) -> Math.pow(a, b);

     // Functions
    static DoubleUnaryOperator root = a -> Math.sqrt(a);

    public MathExpressionParser() {
        this.ans = "";
    }

    // TODO: Something broke this..
    private static String evaluateFunctions(String arg) {
        String regex = "sqrt\\(\\d+\\.?\\d*\\)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(arg);
        while (matcher.find()) {
            String grp = matcher.group();
            System.out.println("Matched group: " + grp);
            String num = (String) grp.subSequence(5, grp.length() - 1);
            double res = Math.sqrt(Double.parseDouble(num));
            arg = arg.replace(grp, Double.toString(res));
            matcher = pattern.matcher(arg);
        }

        return arg;
    }

    //private static String evaluateFun

    private static String evaluateGroup(String delimiter, DoubleBinaryOperator op, String arg) {
        System.out.println("In evaluateGroup()");
        String regex = "\\d+\\.?\\d*+" + delimiter + "\\d+\\.?\\d*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(arg);
        while (matcher.find()) {
            String grp = matcher.group();
            System.out.println("Matched group: " + grp);
            double a = Double.parseDouble(grp.split(delimiter)[0]);
            double b = Double.parseDouble(grp.split(delimiter)[1]);
            double res = op.applyAsDouble(a, b);
            arg = arg.replace(grp, Double.toString(res));
            matcher = pattern.matcher(arg);
        }
        System.out.println("Out of evaluateGroup()");
        return arg;
    }

    private static String extractParentheses(String arg) {
        Pattern pattern = Pattern.compile("\\(\\d+\\.?\\d*\\)");
        Matcher matcher = pattern.matcher(arg);
        while (matcher.find()) {
            String grp = matcher.group();
            System.out.println("Matched group: " + grp);
            arg = arg.replace(grp, grp.substring(1, grp.length() - 1));
            matcher = pattern.matcher(arg);
        }
        return arg;
    }

    /**
     * Take a mathematical expression as input and compute it.
     *
     * As unrecognised symbols are ignored, it actually provides some sort of
     * (very crude) support for symbolic expressions. The next step should maybe
     * be to extend this to actually solve simple equations/algebraic
     * expressions.
     *
     * Works fairly well now, though probably full of bugs.
     */
    public String parse(String expression) throws NumberFormatException {
        // Strip whitespace
        expression = expression.strip();
        System.out.println("Expression: " + expression);

        // TODO: Add implicit '*' as in 2(1+1) -> 2*(1+1)

        // Replace special sequences
        //String regex_pi = Pattern.compile("((?<=\W)");
        expression = expression.replace("pi", Double.toString(Math.PI));
        expression = expression.replaceAll("e", Double.toString(Math.E));
        expression = expression.replaceAll("ans", this.ans);
        System.out.println("After replacement: " + expression);

        // Evaluate groups
        String previous;
        do {
            previous = expression;
            
            // Evaluate functions
            expression = evaluateFunctions(expression);

            // Evaluate primitive operators
            expression = evaluateGroup("\\^", pow, expression);
            expression = evaluateGroup("\\*", mul, expression);
            expression = evaluateGroup("\\/", div, expression);
            expression = evaluateGroup("\\+", sum, expression);
            expression = evaluateGroup("\\-", sub, expression);

            // Remove parentheses
            expression = extractParentheses(expression);
        }

        /*
         * Repeat until the previous block does not change the expression
         * or we are left with a single number.        
         */
        while (!expression.equals(previous) && !expression.matches("\\d+\\.?\\d*"));

        // TODO: Round number to x digits
        
        System.out.println("After evaluation: " + expression);
        this.ans = expression;
        return this.ans;
    }
}
