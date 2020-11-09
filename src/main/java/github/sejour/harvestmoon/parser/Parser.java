package github.sejour.harvestmoon.parser;

import java.io.InputStream;

import github.sejour.harvestmoon.exception.ParseException;
import github.sejour.harvestmoon.node.Node;
import github.sejour.harvestmoon.path.Path;

import io.reactivex.rxjava3.core.Observable;

public interface Parser<P1 extends Path, P2 extends Path, R> {
    Observable<Node<P2>> parse(InputStream in, P1 itemRootPath, R nodeRequest) throws ParseException;
}
