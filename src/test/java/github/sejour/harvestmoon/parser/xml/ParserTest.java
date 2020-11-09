package github.sejour.harvestmoon.parser.xml;

import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.xpath.XPathFactory;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import github.sejour.harvestmoon.node.Node;
import github.sejour.harvestmoon.node.ParsedNode;
import github.sejour.harvestmoon.path.xml.ItemRootXPath;
import github.sejour.harvestmoon.path.xml.XPath;

import lombok.Builder;
import lombok.Value;

@RunWith(Theories.class)
public class ParserTest {

    private static final String TEST_XML_FILE = "/xml/parser_test.xml";

    private Parser parser;

    @Before
    public void setup() {
        parser = new Parser(XMLInputFactory.newInstance(),
                            XPathFactory.newInstance(),
                            DocumentBuilderFactory.newInstance());
    }

    @Value
    @Builder
    static class Fixture {
        ItemRootXPath itemRootXPath;
        Request nodeRequest;
        List<Node<XPath>> expects;
    }

    @DataPoints
    public static final Fixture[] FIXTURES = new Fixture[] {
            Fixture.builder()
                   .itemRootXPath(new ItemRootXPath("//entry"))
                   .nodeRequest(Request.builder()
                                       .attributeRequests(ImmutableSet.of("name"))
                                       .childRequests(ImmutableMap.<XPath, Request>builder()
                                                              .put(new XPath("title"),
                                                                   Request.builder()
                                                                          .wantText(true)
                                                                          .build())
                                                              .put(new XPath("description"),
                                                                   Request.builder()
                                                                          .wantText(true)
                                                                          .build())
                                                              .put(new XPath("img"),
                                                                   Request.builder()
                                                                          .attributeRequests(
                                                                                  ImmutableSet.of("src"))
                                                                          .build())
                                                              .build())
                                       .build())
                   .expects(ImmutableList.of(
                           ParsedNode.<XPath>builder()
                                   .attributes(ImmutableMap.of("name", "orange"))
                                   .child(ImmutableMap.<XPath, List<Node<XPath>>>builder()
                                                  .put(new XPath("title"), ImmutableList
                                                          .of(ParsedNode.<XPath>builder()
                                                                      .text("ORANGE")
                                                                      .attributes(Collections.emptyMap())
                                                                      .child(Collections.emptyMap())
                                                                      .build()))
                                                  .put(new XPath("description"), ImmutableList
                                                          .of(ParsedNode.<XPath>builder()
                                                                      .text("みかんみかんみかんみかん\n"
                                                                            + "オレンジオレンジオレンジ\n"
                                                                            + "ORANGEORANGEORANGE")
                                                                      .attributes(Collections.emptyMap())
                                                                      .child(Collections.emptyMap())
                                                                      .build()))
                                                  .put(new XPath("img"), ImmutableList
                                                          .of(ParsedNode.<XPath>builder()
                                                                      .attributes(ImmutableMap.of("src",
                                                                                                  "http://www.example.com/orange.jpg"))
                                                                      .child(Collections.emptyMap())
                                                                      .build()))
                                                  .build())
                                   .build(),
                           ParsedNode.<XPath>builder()
                                   .attributes(ImmutableMap.of("name", "apple"))
                                   .child(ImmutableMap.<XPath, List<Node<XPath>>>builder()
                                                  .put(new XPath("title"), ImmutableList
                                                          .of(ParsedNode.<XPath>builder()
                                                                      .text("APPLE")
                                                                      .attributes(Collections.emptyMap())
                                                                      .child(Collections.emptyMap())
                                                                      .build()))
                                                  .put(new XPath("description"), ImmutableList
                                                          .of(ParsedNode.<XPath>builder()
                                                                      .text("りんごりんごりんごりんご\n"
                                                                            + "アップルアップルアップル\n"
                                                                            + "APPLEAPPLEAPPLE")
                                                                      .attributes(Collections.emptyMap())
                                                                      .child(Collections.emptyMap())
                                                                      .build()))
                                                  .put(new XPath("img"), ImmutableList
                                                          .of(ParsedNode.<XPath>builder()
                                                                      .attributes(ImmutableMap.of("src",
                                                                                                  "http://www.example.com/apple.jpg"))
                                                                      .child(Collections.emptyMap())
                                                                      .build()))
                                                  .build())
                                   .build(),
                           ParsedNode.<XPath>builder()
                                   .attributes(ImmutableMap.of("name", "strawberry"))
                                   .child(ImmutableMap.<XPath, List<Node<XPath>>>builder()
                                                  .put(new XPath("title"), ImmutableList
                                                          .of(ParsedNode.<XPath>builder()
                                                                      .text("STRAWBERRY")
                                                                      .attributes(Collections.emptyMap())
                                                                      .child(Collections.emptyMap())
                                                                      .build()))
                                                  .put(new XPath("description"), ImmutableList
                                                          .of(ParsedNode.<XPath>builder()
                                                                      .text("いちごいちごいちご\n"
                                                                            + "ストロベリーストロベリーストロベリー\n"
                                                                            + "STRAWBERRY")
                                                                      .attributes(Collections.emptyMap())
                                                                      .child(Collections.emptyMap())
                                                                      .build()))
                                                  .put(new XPath("img"), ImmutableList
                                                          .of(ParsedNode.<XPath>builder()
                                                                      .attributes(ImmutableMap.of("src",
                                                                                                  "http://www.example.com/strawberry.jpg"))
                                                                      .child(Collections.emptyMap())
                                                                      .build()))
                                                  .build())
                                   .build()
                   ))
                    .build()
            // TODO: add test cases
    };

    @Theory
    public void test(Fixture fixture) throws Exception {
        try (final var in = getClass().getResourceAsStream(TEST_XML_FILE)) {
            parser.parse(in, fixture.itemRootXPath, fixture.nodeRequest)
                  .test()
                  .assertValueSequence(fixture.expects);
        }
    }

}
