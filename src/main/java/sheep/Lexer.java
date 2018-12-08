package sheep;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    // string used by matching methods. backslash must be escaped.
    // this matches comment(begin with "//"), number, string(wrapped by ") and identifier.
    public static String regexPat
        = "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
        + "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    private Pattern pattern = Pattern.compile(regexPat);
    private ArrayList<Token> queue = new ArrayList<>();
    private boolean hasMore;
    private LineNumberReader reader;

    public Lexer(Reader r) {
        this.hasMore = true;
        this.reader = new LineNumberReader(r);
    }

    /**
     * 先頭のトークンをDequeueする。
     */
    public Token read() throws ParseException {
        if (canDequeueElement(0)) {
            return this.queue.remove(0);
        } else {
            return Token.EOF;
        }
    }

    /**
     * 任意のインデックスのトークンを返す。queueから削除はしない。
     */
    public Token peek(int i) throws ParseException {
        if (canDequeueElement(i)) {
            return this.queue.get(i);
        } else {
            return Token.EOF;
        }
    }

    /**
     * i番目のqueueが用意できるなら、queueに値を詰めてからtrueを返す
     */
    private boolean canDequeueElement(int i) throws ParseException {
        while (i >= this.queue.size()) {
            if (this.hasMore) {
                readLine();
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 正規表現マッチを行い、パースされたトークンをqueueに入れる
     */
    private void fillQueue(String line) throws ParseException{
        int lineNo = this.reader.getLineNumber();
        Matcher matcher = this.pattern.matcher(line);
        matcher.useTransparentBounds(true).useAnchoringBounds(false);
        int pos = 0;
        int endPos = line.length();
        while (pos < endPos) {
            matcher.region(pos, endPos);
            if (matcher.lookingAt()) {
                addToken(lineNo, matcher);
                pos = matcher.end();
            } else {
                throw new ParseException("bad token at the line " + lineNo);
            }
        }
        this.queue.add(new IdToken(lineNo, Token.EOL));
    }

    /**
     * トークンを生成しqueueに入れる。
     * @param lineNo
     * @param matcher
     */
    protected void addToken(int lineNo, Matcher matcher) {
        // group(0):entire pattern
        String m = matcher.group(1);
        // space or comment
        if (m == null || matcher.group(2) != null)
            return;

        Token token;
        if (matcher.group(3) != null) {
            token = new NumToken(lineNo, Integer.parseInt(m));
        } else if (matcher.group(4) != null) {
            token = new StrToken(lineNo, toStringLiteral(m));
        } else {
            token = new IdToken(lineNo, m);
        }
        this.queue.add(token);
    }

    /**
     * 1行読み込み
     * @throws ParseException
     */
    protected void readLine() throws ParseException {
        String line;
        try {
            line = this.reader.readLine();
        } catch (IOException e) {
            throw new ParseException(e);
        }
        if (line == null) {
            this.hasMore = false;
            return;
        }
        fillQueue(line);
    }

    protected String toStringLiteral(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length() - 1;
        for (int i=1; i<len; i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < len) {
                int c2 = s.charAt(i + 1);
                if (c2 == '"' || c2 == '\\') {
                    i++;
                    c = s.charAt(i);
                } else if (c2 == 'n') {
                    i++;
                    c = '\n';
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    protected static class NumToken extends Token {
        private int value;

        protected NumToken(int lineNo, int v) {
            super(lineNo);
            this.value = v;
        }

        public boolean isNumber() { return true; }

        public String getText() {
             return Integer.toString(this.value);
        }

        public int getNumber() { return this.value; }
    }

    protected class IdToken extends Token {
        private String text;

        protected IdToken(int lineNo, String identifier) {
            super(lineNo);
            this.text = identifier;
        }

        public boolean isIdentifier() { return true; }

        public String getText() { return this.text; }
    }

    protected static class StrToken extends Token {
        private String literal;
        StrToken(int lineNo, String str) {
            super(lineNo);
            this.literal = str;
        }

        public boolean isString() { return true; }

        public String getText() { return this.literal; }
    }
}
