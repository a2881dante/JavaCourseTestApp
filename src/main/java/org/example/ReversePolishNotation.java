package org.example;


import java.util.Arrays;
import java.util.Stack;
import java.util.Iterator;

public class ReversePolishNotation {

    private Stack<String> postfixStack;

    public Double run(String expr) {
        if( this.infixToPostfix(expr) ) {
            return this.calculateResult();
        } else {
            return null;
        }
    }

    private boolean infixToPostfix(String expr) {
        StringBuilder numTemp = new StringBuilder();
        Stack<Character> operandStack = new Stack<>();
        Character lastSign;
        int braceCount = 0;
        int signCount = 0;
        boolean isUni;
        this.postfixStack = new Stack<>();

        if( !Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '+', '(').contains(expr.charAt(0))
                || !Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ')')
                        .contains(expr.charAt(expr.length()-1))) {
            System.out.println("ERROR: Invalid sequence of mathematical operations");
            return false;
        }

        for(int i=0; i < expr.length(); i++) {
            char sign = expr.charAt(i);
            switch( sign ) {
                case '+':
                    signCount++;
                    isUni = (i == 0);
                    if (signCount > 1) {
                        System.out.println("ERROR: Invalid sequence of mathematical operations");
                        return false;
                    }
                    if (!numTemp.toString().equals("")) {
                        this.postfixStack.push(numTemp.toString());
                        numTemp = new StringBuilder();
                    }
                    if (!isUni && !operandStack.empty()) {
                        lastSign = operandStack.peek();
                        if (lastSign == '-' || lastSign == '+'
                                || lastSign == '/' || lastSign == '*'
                                || lastSign == '!' || lastSign == '#') {
                            this.postfixStack.push(operandStack.pop().toString());
                        }
                    }
                    operandStack.push(isUni ? '#' : sign);
                case '-':
                    signCount++;
                    if (signCount > 2) {
                        System.out.println("ERROR: Invalid sequence of mathematical operations\n");
                        return false;
                    }
                    isUni = (i == 0 || expr.charAt(i - 1) == '(' || signCount > 1);
                    if (!numTemp.toString().equals("")) {
                        this.postfixStack.push(numTemp.toString());
                        numTemp = new StringBuilder();
                    }
                    if (!isUni && !operandStack.empty()) {
                        lastSign = operandStack.peek();
                        if (lastSign == '-' || lastSign == '+'
                                || lastSign == '/' || lastSign == '*'
                                || lastSign == '!' || lastSign == '#') {
                            this.postfixStack.push(operandStack.pop().toString());
                        }
                    }
                    operandStack.push(isUni ? '!' : sign);
                    break;
                case '/':
                case '*':
                    signCount++;
                    if (signCount > 1) {
                        System.out.println("ERROR: Invalid sequence of mathematical operations\n");
                        return false;
                    }
                    if (!numTemp.toString().equals("")) {
                        this.postfixStack.push(numTemp.toString());
                        numTemp = new StringBuilder();
                    }
                    if (!operandStack.empty()) {
                        lastSign = operandStack.peek();
                        if (lastSign == '/' || lastSign == '*' || lastSign == '!' || lastSign == '#') {
                            this.postfixStack.push(operandStack.pop().toString());
                        }
                    }
                    operandStack.push(sign);
                    break;
                case ',':
                case '.':
                    numTemp.append(sign);
                    break;
                case '(':
                    ++braceCount;
                    operandStack.push(sign);
                    signCount = 0;
                    break;
                case ')':
                    braceCount--;
                    if (braceCount < 0) {
                        System.out.println("ERROR: Check your placement of round brackets\n");
                        return false;
                    }
                    if (expr.charAt(i - 1) == '(') {
                        System.out.println("ERROR: Round brackets cannot be empty\n");
                        return false;
                    }
                    if (!numTemp.toString().equals("")) {
                        this.postfixStack.push(numTemp.toString());
                        numTemp = new StringBuilder();
                    }
                    lastSign = operandStack.pop();
                    while (lastSign != '(') {
                        this.postfixStack.push(lastSign.toString());
                        lastSign = operandStack.pop();
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    signCount = 0;
                    numTemp.append(sign);
                    if (i == expr.length() - 1) {
                        this.postfixStack.push(numTemp.toString());
                    }
                    break;
                default:
                    System.out.printf("ERROR: Unknown character on position %d", i);
                    return false;
            }
        }
        if(braceCount != 0) {
            System.out.println("ERROR: Check your placement of round brackets");
            return false;
        }

        Iterator<Character> itr = operandStack.iterator();
        while(itr.hasNext()){
            this.postfixStack.push(operandStack.pop().toString());
        }
        Stack<String> tempStack = new Stack<>();
        Iterator<String> itrPostfix = this.postfixStack.iterator();
        while(itrPostfix.hasNext()){
            tempStack.push(this.postfixStack.pop());
        }
        this.postfixStack = tempStack;
        return true;
    }

    private double calculateResult() {
        System.out.println(postfixStack);
        Stack<Double> numStack = new Stack<>();
        Iterator<String> itr = this.postfixStack.iterator();
        while(itr.hasNext()){
            String curr = this.postfixStack.pop();
            try {
                double num = Double.parseDouble(curr);
                numStack.push(num);
            } catch(NumberFormatException e){
                double a1;
                double a2;
                double res = 0.0;
                switch(curr) {
                    case "+":
                        a1 = numStack.pop();
                        a2 = numStack.pop();
                        res = a2 + a1;
                        break;
                    case "-":
                        a1 = numStack.pop();
                        a2 = numStack.pop();
                        res = a2 - a1;
                        break;
                    case "*":
                        a1 = numStack.pop();
                        a2 = numStack.pop();
                        res = a2 * a1;
                        break;
                    case "/":
                        a1 = numStack.pop();
                        a2 = numStack.pop();
                        res = a2 / a1;
                        break;
                    case "!":
                        a1 = numStack.pop();
                        res = -a1;
                    case "#":
                }
                numStack.push(res);
            }
        }

        double finalResult = 0;
        Iterator<Double> itrNum = numStack.iterator();
        while(itrNum.hasNext()){
            finalResult += numStack.pop();
        }
        return finalResult;
    }
}
