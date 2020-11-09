package github.sejour.harvestmoon.node;

import java.util.List;

import github.sejour.harvestmoon.path.Path;

public interface Node<P extends Path> {
    String text();
    String attribute(String name);
    List<Node<P>> find(P path);
}
