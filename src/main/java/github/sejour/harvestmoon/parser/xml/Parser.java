package github.sejour.harvestmoon.parser.xml;

import static github.sejour.harvestmoon.util.StreamUtils.safeStream;

import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.common.collect.ImmutableList;

import github.sejour.harvestmoon.exception.ParseException;
import github.sejour.harvestmoon.node.Node;
import github.sejour.harvestmoon.node.ParsedNode;
import github.sejour.harvestmoon.path.xml.ItemRootXPath;
import github.sejour.harvestmoon.path.xml.XPath;
import github.sejour.harvestmoon.path.xml.XPathStreamReader;

import io.reactivex.rxjava3.core.Observable;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Parser implements github.sejour.harvestmoon.parser.Parser<ItemRootXPath, XPath, Request> {
    private final XMLInputFactory xmlInputFactory;
    private final XPathFactory xPathFactory;
    private final DocumentBuilderFactory documentBuilderFactory;

    @Override
    public Observable<Node<XPath>> parse(InputStream in, ItemRootXPath itemRootPath, Request nodeRequest)
            throws ParseException {
        XMLEventReader reader;
        DocumentBuilder builder;
        final var xPath = xPathFactory.newXPath();

        try {
            reader = xmlInputFactory.createXMLEventReader(in);
        } catch (XMLStreamException e) {
            throw new ParseException("failed to create XMLEventReader", e);
        }

        try {
            builder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ParseException("failed to create DocumentBuilderFactory", e);
        }

        return XPathStreamReader
                .readAll(reader, itemRootPath.getIterator())
                .map(str -> {
                    // TODO: Documentへのパースは XPathStreamReader 側で行っても良いかも
                    final var document = builder.parse(new InputSource(new StringReader(str)));
                    return parse(xPath, document.getDocumentElement(), nodeRequest);
                });
    }

    private static Node<XPath> parse(javax.xml.xpath.XPath xPath, Element element, Request request) {
        final var attributes = safeStream(request.getAttributeRequests())
                .map(key -> Map.entry(key, element.getAttribute(key)))
                .collect(Collectors.toMap(Map.Entry<String, String>::getKey,
                                          Map.Entry<String, String>::getValue));

        final var child = Optional
                .ofNullable(request.getChildRequests())
                .map(Map::entrySet)
                .stream()
                .flatMap(Set::stream)
                .map(req -> {
                    try {
                        final var nodes = (NodeList) xPath
                                .evaluate(req.getKey().getExpression(), element, XPathConstants.NODESET);
                        final var nodesLen = nodes.getLength();
                        final var builder = ImmutableList.<Node<XPath>>builder();
                        for (int i = 0; i < nodesLen; ++i) {
                            builder.add(parse(xPath, (Element) nodes.item(i), req.getValue()));
                        }
                        return Map.<XPath, List<Node<XPath>>>entry(req.getKey(), builder.build());
                    } catch (XPathExpressionException e) {
                        throw new RuntimeException("failed to evaluate xpath expression in parse method", e);
                    }
                })
                .collect(Collectors.toMap(Map.Entry<XPath, List<Node<XPath>>>::getKey,
                                          Map.Entry<XPath, List<Node<XPath>>>::getValue));

        return ParsedNode
                .<XPath>builder()
                .text(request.isWantText() ? element.getTextContent() : null)
                .attributes(attributes)
                .child(child)
                .build();
    }
}
