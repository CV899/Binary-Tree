package project3;

/* 
* Author: Christian Vincent
* Last edited: 3-29-2019
* COP3530 - Peyman Faizan
*/

import java.util.*;

public class BET {
    //-----------Nested BinaryNode class-----------
    private static class BinaryNode<E> {
        private E element;
        private BinaryNode<E> parent;
        private BinaryNode<E> left;
        private BinaryNode<E> right;
        
        //constructor given an element and its neighbors
        public BinaryNode(E e, BinaryNode<E> above, BinaryNode<E> leftChild, BinaryNode<E> rightChild) {
            element = e;
            parent = above;
            left = leftChild;
            right = rightChild;
        }
        
        //accessor methods
        public E getElement() {
            return element;
        }
        
        public BinaryNode<E> getParent() {
            return parent;
        }
        
        public BinaryNode<E> getLeft() {
            return left;
        }
        
        public BinaryNode<E> getRight() {
            return right;
        }
        
        //update methods
        public void setElement(E e) {
            element = e;
        }
        
        public void setParent(BinaryNode<E> parentNode) {
            parent = parentNode;
        }
        
        public void setLeft(BinaryNode<E> leftChild) {
            left = leftChild;
        }
        
        public void setRight(BinaryNode<E> rightChild) {
            right = rightChild;
        }
        
    }//--------------end of nested BinaryNode class-----------------
    
    private BinaryNode<Character> root = null;
    private int size = 0;
    
    //constructors
    public BET() {
        
    }
    
    public BET(String expr, char mode) throws IllegalStateException {
      
        boolean status = false;
        
        if(mode == 'p') {
            status = buildFromPostfix(expr);
        }
        else if(mode == 'i') {
            status = buildFromInfix(expr);
        }
        
        if(!status) {
            throw new IllegalStateException("Tree build failed");
        }
        
    }
    
    //methods
    public boolean buildFromPostfix(String postfix) {
        
        Stack<BinaryNode> stack = new Stack();
        BinaryNode temp1, temp2, temp3;
        
        if(root != null)
           makeEmpty(root);
        
        String[] postfixTokened = postfix.split(" ");
        
        for(String postfix1 : postfixTokened) {
            if(!"+".equals(postfix1) && !"-".equals(postfix1) && !"/".equals(postfix1)
                    && !"*".equals(postfix1)) {
                
                temp1 = new BinaryNode(postfix1, null, null, null);
                stack.push(temp1);
            }
            else
            {
               
                temp1 = new BinaryNode(postfix1, null, null, null);
                temp2 = stack.pop();
                temp3 = stack.pop();
                
                temp1.setRight(temp2);
                temp1.setLeft(temp3);
                
                stack.push(temp1);
                
            }
            
          root = stack.peek();
          
        }
        //remove completed tree from stack; if built correctly, stack will be
        //empty
        stack.pop();
        
        return stack.isEmpty();
   
    }
    
    public boolean buildFromInfix(String infix) {
     
       Stack<String> output = new Stack();
       Stack<String> operands = new Stack();
       StringBuilder outputString = new StringBuilder();
       
       //check if all parentheses (if any) match correctly
       boolean status = isMatched(infix);
       if(!status)
           return false;
       
       if(root != null)
           makeEmpty(root);
       
       String[] infixTokened = infix.split(" ");
       
       for(String infixToken : infixTokened) {
           
           if(!"+".equals(infixToken) && !"-".equals(infixToken) && !"/".equals(infixToken)
                    && !"*".equals(infixToken) && !"(".equals(infixToken) && !")".equals(infixToken)) 
           {
               output.push(infixToken);
           }
           else 
           {
               
               switch (infixToken) {
                   case "+":
                   case "-":
                       if(operands.peek().equals("+") || operands.peek().equals("-") ||
                               operands.peek().equals("*") || operands.peek().equals("/"))
                       {
                           output.push(operands.pop());
                           operands.push(infixToken);
                       }
                       else
                       {
                           operands.push(infixToken);
                       }   break;
                   case "/":
                   case "*":
                       if(operands.isEmpty())
                           operands.push(infixToken);
                       else if(operands.peek().equals("/") || operands.peek().equals("*"))
                       {
                           output.push(operands.pop());
                           operands.push(infixToken);
                       }
                       else
                       {
                           operands.push(infixToken);
                       }   break;
                   case ")":
                       while(!(operands.peek().equals("(")))
                           output.push(operands.pop());
                        operands.pop();                         //pop opening parenthesis
                        break;
                   //infixToken must be "("
                   default:
                       operands.push(infixToken);
                       break;
               }
               
           }
           
       }
       //push the remaining operands into output stack
       while(!(operands.isEmpty()))
           output.push(operands.pop());
       
       //transfer output stack into outputSting, reverse outputString, 
       //then call buildFromPostfix(String outputString.toString())
       while(!(output.isEmpty())) {
           outputString.insert(0, output.pop());
           if(!output.isEmpty())          //prevents a space being added to the end of the string
              outputString.insert(0," ");   
       }
    
       buildFromPostfix(outputString.toString());
       
       return output.isEmpty();
    }
    
    public void printInfixExpression() {
        
        if(root != null)
            printInfixExpression(root);
        
    }
    
    public void printPostfixExpression() {
        printPostfixExpression(root);
    }
    
    public int size() {
        return size(root);
    }
    
    public boolean isEmpty() {
        return root == null;
    }
    
    public int leafNodes() {
        return leafNodes(root);
    }
    
    //------------private methods----------------
    private void printInfixExpression(BinaryNode n) {
        
        if(n.getLeft() == null && n.getRight() == null)  //external node
             System.out.print(n.getElement() + "");
        else                                             //internal node (subtree)
        {
             System.out.print("( ");
             printInfixExpression(n.getLeft());
             System.out.print(" " + n.getElement() + " ");
             printInfixExpression(n.getRight());
             System.out.print(" )");
        }
      
    }
    
    private void makeEmpty(BinaryNode t) {
        root = null;
    }
    
    private void printPostfixExpression(BinaryNode n) {
        
        if(n == null)
            return;
        
        printPostfixExpression(n.getLeft());
        printPostfixExpression(n.getRight());
        
        System.out.print(n.getElement() + " ");
        
    }
    
   private int size(BinaryNode t) {
       
       if(t == null)
           return 0;
     
       return (size(t.getLeft()) + size(t.getRight()) + 1);
    }
    
    private int leafNodes(BinaryNode t) {
        
        if(t == null)
            return 0;
        else if(t.getLeft() == null && t.getRight() == null)
            return 1;
        else
            return leafNodes(t.getLeft()) + leafNodes(t.getRight());
        
    }
    
    private boolean isMatched(String expression) {
        
        Stack<Character> buffer = new Stack();
        final String opening = "(";
        final String closing = ")";
        for(char c : expression.toCharArray()) {
            if(opening.indexOf(c) != -1)
                buffer.push(c);
            else if(closing.indexOf(c) != -1) {
                if(buffer.isEmpty())
                    return false;
                if(closing.indexOf(c) != opening.indexOf(buffer.pop()))
                    return false;
            }
        }
        return buffer.isEmpty();
    }
    
    
}
