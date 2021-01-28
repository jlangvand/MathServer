package server;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MathExpressionParser {
     // Operators
    static DoubleBinaryOperator sum = (a, b) -> a + b;
    static DoubleBinaryOperator div = (a, b) -> a / b;
    static DoubleBinaryOperator mul = (a, b) -> a * b;
    static DoubleBinaryOperator sub = (a, b) -> a - b;
    static DoubleBinaryOperator pow = (a, b) -> Math.pow(a, b);

     // Functions
    static DoubleUnaryOperator root = a -> Math.sqrt(a);
    
    private static String evaluateFunctions(String arg) {
        String regex = "sqrt\\([0-9\\.]*\\)";
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
        String regex = "\\d\\.?\\d*+" + delimiter + "\\d+\\.?\\d*";
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
     */
    public static String parse(String expression) throws NumberFormatException {
        // Strip whitespace
        expression = expression.strip();
        System.out.println("Expression: " + expression);

        // TODO: Add implicit '*'

        // Evaluate functions
        expression = evaluateFunctions(expression);

        // Replace special sequences
        expression = expression.replace("pi", "3.14159265");
        expression = expression.replace("e", "2.7182818284");
        System.out.println("After replacement: " + expression);

        // Evaluate groups
        String previous;
        do {
            previous = expression;
            expression = evaluateGroup("\\*", mul, expression);
            expression = evaluateGroup("\\/", div, expression);
            expression = evaluateGroup("\\+", sum, expression);
            expression = evaluateGroup("\\-", sub, expression);
            expression = evaluateGroup("\\*\\*", div, previous);
            expression = extractParentheses(expression);
        } while (!expression.equals(previous) && !expression.matches("\\d+\\.?\\d*"));

        System.out.println("After evaluation: " + expression);
        return expression;
    }
}
