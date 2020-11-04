package github.sejour.harvestmoon.path.xml;

import github.sejour.harvestmoon.path.Path;

import lombok.Getter;

public class ItemRootXPath implements Path {
    @Getter
    private final PathIterator iterator;

    @Getter
    private final String expression;

    public ItemRootXPath(String expression) {
        this.expression = expression;
        iterator = PathIterator.fromAbsoluteXPath(expression);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ItemRootXPath) {
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
