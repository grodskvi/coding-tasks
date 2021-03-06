package task.searchengine.server.domain;

import java.util.Objects;

public class Token {
    private final String token;

    public Token(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Empty tokens are illegal");
        }
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return Objects.equals(token, token1.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return "Token{" +
                "token='" + token + '\'' +
                '}';
    }

    public static Token normalizedToken(String token) {
        return new Token(token.toLowerCase());
    }
}
