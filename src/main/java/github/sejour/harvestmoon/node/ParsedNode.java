package github.sejour.harvestmoon.node;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import github.sejour.harvestmoon.path.Path;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParsedNode<P extends Path> implements Node<P> {
    String text;
    Map<String, String> attributes;
    Map<P, List<Node<P>>> child;

    @Override
    public String text() {
        return text;
    }

    @Override
    public String attribute(String name) {
        if (attributes == null) {
            return null;
        }
        return attributes.get(name);
    }

    @Override
    public List<Node<P>> find(P path) {
        if (child == null) {
            return Collections.emptyList();
        }
        return child.get(path);
    }
}
