package p1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Bobby's implementation of P1. Probably won't be used because of efficiency issues.
 * @author Bobby Signor
 */
class P1_Bobby extends ArrayList<String> {
	private static final long serialVersionUID = 1L;

	private boolean isDecimalPoint(String s, int pos) {
        if (s.charAt(pos) != '.' || pos <= 0 || pos >= s.length() - 1)
            return false;
        return (isNumber(s.charAt(pos - 1)) && isNumber(s.charAt(pos + 1)));
    }

    private boolean isNumber(char c) {
        return (c >= (int) '0' && c <= '9');
    }

    private boolean isLegalApostrophe(String s, int pos) {
        if (s.charAt(pos) != '\'' || pos <= 0 || pos >= s.length() - 1)
            return false;
        return (isLetter(s.charAt(pos - 1)) && isLetter(s.charAt(pos + 1)));
    }

    private boolean isLetter(char c) {
        return (c >= (int) 'a' && c <= (int) 'z');
    }

    private boolean isLegal(String s, int pos) {
        if (isLetter(s.charAt(pos)) || isNumber(s.charAt(pos)))
            return true;
        return isDecimalPoint(s, pos) || isLegalApostrophe(s, pos);
    }

    /**
     * Parses the file into an ArrayList.
     */
    public void parseFile(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            String curToken;
            StringBuilder builder;
            boolean inTag = false;

            while (scanner.hasNext()) {
                //Retry number 4. Here goes nothing...
                curToken = scanner.next().toLowerCase();
                builder = new StringBuilder();
                for (int i = 0; i < curToken.length(); ++i) {
                    if (curToken.charAt(i) == '<')
                        inTag = true;
                    if (!inTag && isLegal(curToken, i))
                        builder.append(curToken.charAt(i));
                    if (inTag && curToken.charAt(i) == '>')
                        inTag = false;
                }
                if (builder.length() > 0)
                    addWord(builder.toString());
            }

            scanner.close();
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Method that adds a word to the list using a binary sort.
     */
    private void addWord(String word) {
        int start = 0, end = this.size() - 1, middle = start + ((end - start) / 2);

        //The binary sort.
        while (end >= start) {
            if (this.get(middle).compareTo(word) < 0) {
                //If we're not far enough into the alphabet, set the next zone to start at middle + 1.
                start = middle + 1;
            } else if (this.get(middle).compareTo(word) > 0) {
                //If we're too far into the alphabet, set the next zone to end at middle - 1.
                end = middle - 1;
            } else {
                //If the word is already in the list, then we can leave. So leave.
                return;
                //What are you still doing here?
                //No, seriously, you should leave.
                //Word's already been dealt with. There's nothing left to do in this section.
                //Wait, you're just reading straight through my code, aren't you?
                //God, that really sucks for you, doesn't it?
                //Well, go on. There's nothing important in these comments. You can ignore them.
                //I'm serious. I'm just doing typing these to get some practice on a new keyboard.
                //Okay, see, I wasn't kidding in the last line. I'm 100% just doing this to get some more practice.
                //Alright, fine. I'm done. See? Next line isn't a comment. You're free. Congrats.
            }

            middle = start + ((end - start) / 2);
        }

        this.add(middle, word);
    }

    public void print() {
        System.out.println("WORDS");
        for (String s : this) {
            System.out.println(s);
        }

        System.out.println("\nNUMBER OF WORDS: " + this.size());
        System.out.close();
    }

    public static void main(String args[]) {
        if (args.length != 1) {
            System.err.println("Error: Wrong number of input parameters (1 parameter required)");
            return;
        }

        P1_Bobby a = new P1_Bobby();
        a.parseFile(args[0]);
        a.print();
    }
}