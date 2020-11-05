package github.sejour.harvestmoon.path.xml;

import static github.sejour.harvestmoon.path.xml.PathIterator.MATCHED;

import java.util.function.Consumer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import io.reactivex.rxjava3.core.Observable;

public class XPathStreamReader {
    public static Observable<String> readAll(XMLEventReader reader, PathIterator iterator) {
        return Observable.create(emitter -> {
            goDown(reader, iterator, emitter::onNext);
            emitter.onComplete();
        });
    }

    private static void goDown(XMLEventReader reader, PathIterator iterator,
                               Consumer<String> itemConsumer) throws XMLStreamException {
        while (reader.hasNext()) {
            final var event = reader.nextEvent();

            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    final var element = event.asStartElement();
                    final var nextIterator = iterator.next(element);
                    if (nextIterator == null) {
                        skipDown(reader);
                        continue;
                    }
                    if (nextIterator == MATCHED) {
                        itemConsumer.accept(getDown(element, reader));
                        continue;
                    }
                    goDown(reader, nextIterator, itemConsumer);
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return;
                default:
                    break;
            }
        }
    }

    private static void skipDown(XMLEventReader reader) throws XMLStreamException {
        int down = 0;
        while (reader.hasNext()) {
            final var event = reader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT -> ++down;
                case XMLStreamConstants.END_ELEMENT -> --down;
            }
            if (down < 0) {
                return;
            }
        }
    }

    private static String getDown(StartElement startElement, XMLEventReader reader) throws XMLStreamException {
        final var builder = new StringBuilder(startElement.toString());
        int down = 0;
        while (reader.hasNext()) {
            final var event = reader.nextEvent();

            if (event.isCharacters()) {
                final var isCData = event.asCharacters().isCData();
                if (isCData) {
                    builder.append("<![CDATA[");
                }
                builder.append(event.asCharacters().getData().trim());
                if (isCData) {
                    builder.append("]]>");
                }
            } else {
                builder.append(event.toString());
            }

            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT -> ++down;
                case XMLStreamConstants.END_ELEMENT -> --down;
            }
            if (down < 0) {
                return builder.toString();
            }
        }

        throw new XMLStreamException();
    }
}
