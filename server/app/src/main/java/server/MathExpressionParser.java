package server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathExpressionParser {
    private static double sum(String arg) {
        double sum = 0.0;
        String[] plus = arg.split("\\+");
        for (int i = 0; i < plus.length; i++) {
            String[] minus = plus[i].split("-");
            if (!minus[0].isBlank())
                sum += Double.parseDouble(minus[0]);
            if (minus.length > 1) {
                for (int j = 1; j < minus.length; j++) {
                    sum -= Double.parseDouble(minus[j]);
                }
            }
        }
        return sum;
    }

    private static double divide(String arg) {
        String[] tmp = arg.split("\\/");
        double res = Double.parseDouble(tmp[0]);
        for (int i = 1; i < tmp.length; i++) {
            res /= Double.parseDouble(tmp[i]);
        }
        return res;
    }

    private static double multiply(String arg) {
        String[] tmp = arg.split("\\*");
        double res = Double.parseDouble(tmp[0]);
        for (int i = 1; i < tmp.length; i++) {
            res *= Double.parseDouble(tmp[i]);
        }
        return res;
    }

    private static double calculate(String arg) {
        // Check for multiplication
        String regex = "[\\.0-9]+\\*[\\.0-9]+";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(arg);
        while (matcher.find()) {
            String grp = matcher.group();
            System.out.println("Matched group: " + grp);
            double res = multiply(grp);
            arg = arg.replace(grp, Double.toString(res));
            matcher = pattern.matcher(arg);
        }

        // Check for division
        regex = "[\\.0-9]+/[\\.0-9]+";
        pattern = Pattern.compile(regex);

        matcher = pattern.matcher(arg);
        while (matcher.find()) {
            String grp = matcher.group();
            System.out.println("Matched group: " + grp);
            double res = divide(grp);
            arg = arg.replace(grp, Double.toString(res));
            matcher = pattern.matcher(arg);
        }

        return sum(arg);
    }

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

    public static double parse(String expression) throws NumberFormatException {
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

        // Check for parentheses
        String regex = "\\([\\-\\+\\*\\/0-9\\.]*\\)";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            String grp = matcher.group();
            double res = calculate(grp.substring(1, grp.length() - 1));
            expression = expression.replace(grp, Double.toString(res));
            matcher = pattern.matcher(expression);
        }
        System.out.println("After parenthesis: " + expression);

        // Then calculate the now clean expression and return
        return calculate(expression);
    }
}
