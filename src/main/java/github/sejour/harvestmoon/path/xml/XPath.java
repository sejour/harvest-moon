package github.sejour.harvestmoon.path.xml;

import github.sejour.harvestmoon.path.Path;

import lombok.Value;

@Value
public class XPath implements Path {
    String expression;

    @Override
    public boolean equals(Object other) {
        if (other instanceof XPath) {
            return expression.equals(other.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

    @Override
    public String toString() {
        return expression;
    }
}
