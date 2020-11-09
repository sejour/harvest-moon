package github.sejour.harvestmoon.path.xml;

import javax.xml.stream.XMLInputFactory;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import lombok.Builder;
import lombok.Value;

@RunWith(Theories.class)
public class XPathStreamReaderTest {

    private static final String TEST_XML_FILE = "/xml/xpath_stream_navigator_test.xml";

    private XMLInputFactory factory;

    @Before
    public void setup() {
        factory = XMLInputFactory.newInstance();
    }

    @Value
    @Builder
    static class Fixture {
        String path;
        String[] expects;
    }

    @DataPoints
    public static final Fixture[] FIXTURES = new Fixture[] {
            Fixture.builder()
                   .path("/unknonw")
                   .expects(new String[] {})
                    .build(),
            Fixture.builder()
                   .path("/test/entries/entry")
                   .expects(new String[] {
                           "<entry name='orange' id='entry_1'><title>ORANGE</title><description>みかんみかんみかんみかん\n"
                           + "オレンジオレンジオレンジ\n"
                           + "ORANGEORANGEORANGE</description><img src='http://www.example.com/orange.jpg'></img></entry>",
                           "<entry name='apple' id='entry_2'><title>APPLE</title><description>りんごりんごりんごりんご\n"
                           + "アップルアップルアップル\n"
                           + "APPLEAPPLEAPPLE</description><img src='http://www.example.com/apple.jpg'></img></entry>",
                           "<entry name='strawberry' id='entry_1'><title>STRAWBERRY</title><description>いちごいちごいちご\n"
                           + "ストロベリーストロベリーストロベリー\n"
                           + "STRAWBERRY</description><img src='http://www.example.com/strawberry.jpg'></img></entry>"
                   })
                    .build(),
            Fixture.builder()
                   .path("//entry")
                   .expects(new String[] {
                           "<entry name='orange' id='entry_1'><title>ORANGE</title><description>みかんみかんみかんみかん\n"
                           + "オレンジオレンジオレンジ\n"
                           + "ORANGEORANGEORANGE</description><img src='http://www.example.com/orange.jpg'></img></entry>",
                           "<entry name='apple' id='entry_2'><title>APPLE</title><description>りんごりんごりんごりんご\n"
                           + "アップルアップルアップル\n"
                           + "APPLEAPPLEAPPLE</description><img src='http://www.example.com/apple.jpg'></img></entry>",
                           "<entry name='strawberry' id='entry_1'><title>STRAWBERRY</title><description>いちごいちごいちご\n"
                           + "ストロベリーストロベリーストロベリー\n"
                           + "STRAWBERRY</description><img src='http://www.example.com/strawberry.jpg'></img></entry>"
                   })
                    .build(),
            Fixture.builder()
                   .path("//item")
                   .expects(new String[] {
                           "<item color='green'><title>ITEM-1</title></item>",
                           "<item color='red'><title>ITEM-2</title></item>",
                           "<item color='blue'></item>"
                   })
                    .build(),
            Fixture.builder()
                   .path("/test/data/foods/food[@genre='japanese']")
                   .expects(new String[] {
                           "<food genre='japanese'>蕎麦</food>",
                           "<food genre='japanese'>寿司</food>",
                           "<food genre='japanese'>しゃぶしゃぶ</food>",
                           "<food genre='japanese'>天ぷら</food>"
                   })
                    .build(),
            Fixture.builder()
                   .path("/test//foods/food[@genre='japanese']")
                   .expects(new String[] {
                           "<food genre='japanese'>蕎麦</food>",
                           "<food genre='japanese'>寿司</food>",
                           "<food genre='japanese'>しゃぶしゃぶ</food>",
                           "<food genre='japanese'>天ぷら</food>"
                   })
                    .build(),
            Fixture.builder()
                   .path("/test//food[@genre='japanese']")
                   .expects(new String[] {
                           "<food genre='japanese'>蕎麦</food>",
                           "<food genre='japanese'>寿司</food>",
                           "<food genre='japanese'>しゃぶしゃぶ</food>",
                           "<food genre='japanese'>天ぷら</food>"
                   })
                    .build(),
            Fixture.builder()
                   .path("//food[@genre='japanese']")
                   .expects(new String[] {
                           "<food genre='japanese'>蕎麦</food>",
                           "<food genre='japanese'>寿司</food>",
                           "<food genre='japanese'>しゃぶしゃぶ</food>",
                           "<food genre='japanese'>天ぷら</food>"
                   })
                    .build(),
            Fixture.builder()
                   .path("//content[@class='bar']")
                   .expects(new String[] {
                           "<content class='bar'><data type='A'><header>bar-A-1-header</header><body>bar-A-1-body</body><footer>bar-A-1-footer</footer></data><data type='B'><header>bar-B-1-header</header><body>bar-B-1-body</body><footer>bar-B-1-footer</footer></data></content>",
                           "<content class='bar'><data type='A'><header>yyy:bar-A-header</header><body>yyy:bar-A-body</body><footer>yyy:bar-A-footer</footer></data><data type='B'><header>yyy:bar-B-header</header><body>yyy:bar-B-body</body><footer>yyy:bar-B-footer</footer></data></content>",
                           "<content class='bar'><data type='A'><header>zzz:bar-A-header</header><body>zzz:bar-A-body</body><footer>zzz:bar-A-footer</footer></data><data type='B'><header>zzz:bar-B-header</header><body>zzz:bar-B-body</body><footer>zzz:bar-B-footer</footer></data></content>",
                           "<content class='bar'><data type='A'><header>bar-A-2-header</header><body>bar-A-2-body</body><footer>bar-A-2-footer</footer></data><data type='B'><header>bar-B-2-header</header><body>bar-B-2-body</body><footer>bar-B-2-footer</footer></data></content>"
                   })
                    .build(),
            Fixture.builder()
                   .path("//contents//data[@type='A']")
                   .expects(new String[] {
                           "<data type='A'><header>foo-A-1-header</header><body>foo-A-1-body</body><footer>foo-A-1-footer</footer></data>",
                           "<data type='A'><header>xxx:foo-A-header</header><body>xxx:foo-A-body</body><footer>xxx:foo-A-footer</footer></data>",
                           "<data type='A'><header>bar-A-1-header</header><body>bar-A-1-body</body><footer>bar-A-1-footer</footer></data>",
                           "<data type='A'><header>yyy:bar-A-header</header><body>yyy:bar-A-body</body><footer>yyy:bar-A-footer</footer></data>",
                           "<data type='A'><header>foo-A-2-header</header><body>foo-A-2-body</body><footer>foo-A-2-footer</footer></data>",
                           "<data type='A'><header>zzz:bar-A-header</header><body>zzz:bar-A-body</body><footer>zzz:bar-A-footer</footer></data>",
                           "<data type='A'><header>bar-A-2-header</header><body>bar-A-2-body</body><footer>bar-A-2-footer</footer></data>"
                   })
                    .build(),
            Fixture.builder()
                   .path("//contents/group[@domain='yyy']/content/data[@type='B']")
                   .expects(new String[] {
                           "<data type='B'><header>yyy:bar-B-header</header><body>yyy:bar-B-body</body><footer>yyy:bar-B-footer</footer></data>"
                   })
                    .build(),
            Fixture.builder()
                   .path("//contents/group[@domain='yyy']//data[@type='B']")
                   .expects(new String[] {
                           "<data type='B'><header>yyy:bar-B-header</header><body>yyy:bar-B-body</body><footer>yyy:bar-B-footer</footer></data>"
                   })
                    .build(),
            Fixture.builder()
                   .path("/test")
                   .expects(new String[] {
                           "<test><entries><entry name='orange' id='entry_1'><title>ORANGE</title><description>みかんみかんみかんみかん\n"
                           + "オレンジオレンジオレンジ\n"
                           + "ORANGEORANGEORANGE</description><img src='http://www.example.com/orange.jpg'></img></entry><entry name='apple' id='entry_2'><title>APPLE</title><description>りんごりんごりんごりんご\n"
                           + "アップルアップルアップル\n"
                           + "APPLEAPPLEAPPLE</description><img src='http://www.example.com/apple.jpg'></img></entry><entry name='strawberry' id='entry_1'><title>STRAWBERRY</title><description>いちごいちごいちご\n"
                           + "ストロベリーストロベリーストロベリー\n"
                           + "STRAWBERRY</description><img src='http://www.example.com/strawberry.jpg'></img></entry></entries><item color='green'><title>ITEM-1</title></item><data><foods><food genre='japanese'>蕎麦</food><food genre='korean'>カルビクッパ</food><food genre='chinese'>ラーメン</food><food genre='chinese'>棒棒鶏</food><food genre='western'>ハンバーグ</food><food genre='japanese'>寿司</food><food genre='japanese'>しゃぶしゃぶ</food><food genre='korean'>ビビンバ</food><food genre='western'>パスタ</food><food genre='chinese'>青椒肉絲</food><food genre='japanese'>天ぷら</food><food genre='korean'>サムギョプサル</food><food genre='chinese'>麻婆豆腐</food></foods><venus><venu><name>ZEPP Tokyo</name><station>東京テレポート</station><station>青海</station></venu><venu><name>STUDIO COAST</name><station>新木場</station></venu><venu><name>WWW</name><station>渋谷</station></venu><venu><name>O-EAST</name><station>渋谷</station><station>神泉</station></venu></venus></data><item color='red'><title>ITEM-2</title></item><refs><ref src='http://www.example.com/ref1' title='REF1'></ref><ref src='http://www.example.com/ref2' title='REF2'></ref><ref src='http://www.example.com/ref3' title='REF3'></ref></refs><item color='blue'></item><contents><content class='foo'><data type='A'><header>foo-A-1-header</header><body>foo-A-1-body</body><footer>foo-A-1-footer</footer></data><data type='B'><header>foo-B-1-header</header><body>foo-B-1-body</body><footer>foo-B-1-footer</footer></data></content><group domain='xxx'><content class='foo'><data type='A'><header>xxx:foo-A-header</header><body>xxx:foo-A-body</body><footer>xxx:foo-A-footer</footer></data><data type='B'><header>xxx:foo-B-header</header><body>xxx:foo-B-body</body><footer>xxx:foo-B-footer</footer></data></content></group><content class='bar'><data type='A'><header>bar-A-1-header</header><body>bar-A-1-body</body><footer>bar-A-1-footer</footer></data><data type='B'><header>bar-B-1-header</header><body>bar-B-1-body</body><footer>bar-B-1-footer</footer></data></content><group domain='yyy'><content class='bar'><data type='A'><header>yyy:bar-A-header</header><body>yyy:bar-A-body</body><footer>yyy:bar-A-footer</footer></data><data type='B'><header>yyy:bar-B-header</header><body>yyy:bar-B-body</body><footer>yyy:bar-B-footer</footer></data></content></group><content class='foo'><data type='A'><header>foo-A-2-header</header><body>foo-A-2-body</body><footer>foo-A-2-footer</footer></data><data type='B'><header>foo-B-2-header</header><body>foo-B-2-body</body><footer>foo-B-2-footer</footer></data></content><group domain='zzz'><content class='bar'><data type='A'><header>zzz:bar-A-header</header><body>zzz:bar-A-body</body><footer>zzz:bar-A-footer</footer></data><data type='B'><header>zzz:bar-B-header</header><body>zzz:bar-B-body</body><footer>zzz:bar-B-footer</footer></data></content></group><content class='bar'><data type='A'><header>bar-A-2-header</header><body>bar-A-2-body</body><footer>bar-A-2-footer</footer></data><data type='B'><header>bar-B-2-header</header><body>bar-B-2-body</body><footer>bar-B-2-footer</footer></data></content></contents></test>"
                   })
                    .build(),
            Fixture.builder()
                   .path("/")
                   .expects(new String[] {
                           "<test><entries><entry name='orange' id='entry_1'><title>ORANGE</title><description>みかんみかんみかんみかん\n"
                           + "オレンジオレンジオレンジ\n"
                           + "ORANGEORANGEORANGE</description><img src='http://www.example.com/orange.jpg'></img></entry><entry name='apple' id='entry_2'><title>APPLE</title><description>りんごりんごりんごりんご\n"
                           + "アップルアップルアップル\n"
                           + "APPLEAPPLEAPPLE</description><img src='http://www.example.com/apple.jpg'></img></entry><entry name='strawberry' id='entry_1'><title>STRAWBERRY</title><description>いちごいちごいちご\n"
                           + "ストロベリーストロベリーストロベリー\n"
                           + "STRAWBERRY</description><img src='http://www.example.com/strawberry.jpg'></img></entry></entries><item color='green'><title>ITEM-1</title></item><data><foods><food genre='japanese'>蕎麦</food><food genre='korean'>カルビクッパ</food><food genre='chinese'>ラーメン</food><food genre='chinese'>棒棒鶏</food><food genre='western'>ハンバーグ</food><food genre='japanese'>寿司</food><food genre='japanese'>しゃぶしゃぶ</food><food genre='korean'>ビビンバ</food><food genre='western'>パスタ</food><food genre='chinese'>青椒肉絲</food><food genre='japanese'>天ぷら</food><food genre='korean'>サムギョプサル</food><food genre='chinese'>麻婆豆腐</food></foods><venus><venu><name>ZEPP Tokyo</name><station>東京テレポート</station><station>青海</station></venu><venu><name>STUDIO COAST</name><station>新木場</station></venu><venu><name>WWW</name><station>渋谷</station></venu><venu><name>O-EAST</name><station>渋谷</station><station>神泉</station></venu></venus></data><item color='red'><title>ITEM-2</title></item><refs><ref src='http://www.example.com/ref1' title='REF1'></ref><ref src='http://www.example.com/ref2' title='REF2'></ref><ref src='http://www.example.com/ref3' title='REF3'></ref></refs><item color='blue'></item><contents><content class='foo'><data type='A'><header>foo-A-1-header</header><body>foo-A-1-body</body><footer>foo-A-1-footer</footer></data><data type='B'><header>foo-B-1-header</header><body>foo-B-1-body</body><footer>foo-B-1-footer</footer></data></content><group domain='xxx'><content class='foo'><data type='A'><header>xxx:foo-A-header</header><body>xxx:foo-A-body</body><footer>xxx:foo-A-footer</footer></data><data type='B'><header>xxx:foo-B-header</header><body>xxx:foo-B-body</body><footer>xxx:foo-B-footer</footer></data></content></group><content class='bar'><data type='A'><header>bar-A-1-header</header><body>bar-A-1-body</body><footer>bar-A-1-footer</footer></data><data type='B'><header>bar-B-1-header</header><body>bar-B-1-body</body><footer>bar-B-1-footer</footer></data></content><group domain='yyy'><content class='bar'><data type='A'><header>yyy:bar-A-header</header><body>yyy:bar-A-body</body><footer>yyy:bar-A-footer</footer></data><data type='B'><header>yyy:bar-B-header</header><body>yyy:bar-B-body</body><footer>yyy:bar-B-footer</footer></data></content></group><content class='foo'><data type='A'><header>foo-A-2-header</header><body>foo-A-2-body</body><footer>foo-A-2-footer</footer></data><data type='B'><header>foo-B-2-header</header><body>foo-B-2-body</body><footer>foo-B-2-footer</footer></data></content><group domain='zzz'><content class='bar'><data type='A'><header>zzz:bar-A-header</header><body>zzz:bar-A-body</body><footer>zzz:bar-A-footer</footer></data><data type='B'><header>zzz:bar-B-header</header><body>zzz:bar-B-body</body><footer>zzz:bar-B-footer</footer></data></content></group><content class='bar'><data type='A'><header>bar-A-2-header</header><body>bar-A-2-body</body><footer>bar-A-2-footer</footer></data><data type='B'><header>bar-B-2-header</header><body>bar-B-2-body</body><footer>bar-B-2-footer</footer></data></content></contents></test>"
                   })
                    .build(),
            };

    @Theory
    public void test(Fixture fixture) throws Exception {
        final var iterator = PathIterator.fromAbsoluteXPath(fixture.path);

        try (final var in = getClass().getResourceAsStream(TEST_XML_FILE)) {
            final var eventReader = factory.createXMLEventReader(in);
            XPathStreamReader.readAll(eventReader, iterator)
                             .test()
                             .assertComplete()
                             .assertValues(fixture.expects);
        }
    }

}
