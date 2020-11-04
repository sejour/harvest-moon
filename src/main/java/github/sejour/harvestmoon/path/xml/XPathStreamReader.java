package github.sejour.harvestmoon.path.xml;

import static github.sejour.harvestmoon.path.xml.PathIterator.MATCHED;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class XPathStreamReader {
    private final XMLEventReader reader;
    private PathIterator iterator;

    public String read() throws XMLStreamException {
        if (!reader.hasNext()) {
            return null;
        }

        final var builder = new StringBuilder();
        final var result = goDown(reader, iterator, builder);
        if (result == null) {
            return read();
        }
        iterator = result;

        return builder.toString();
    }

    private static PathIterator goDown(XMLEventReader reader, PathIterator iterator, StringBuilder builder) throws XMLStreamException {
        while (reader.hasNext()) {
            final var event = reader.nextEvent();

            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    if (iterator == null) {
                        goDown(reader, null, null);
                        continue;
                    }

                    final var element = event.asStartElement();

                    final var nextIterator = iterator.next(element);
                    if (nextIterator == MATCHED) {
                        builder.append(element.toString());
                    }

                    final var result = goDown(reader, nextIterator, builder);
                    if (result != null) {
                        if (result.getChild() == MATCHED) {
                            return result;
                        }
                        if (iterator != MATCHED && nextIterator == MATCHED) {
                            return iterator;
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (iterator == MATCHED) {
                        builder.append(event.asEndElement().toString());
                    }
                    if (builder != null && builder.length() > 0) {
                        return iterator;
                    }
                    return null;
                case XMLStreamConstants.CHARACTERS:
                    if (iterator == MATCHED) {
                        final var isCData = event.asCharacters().isCData();
                        if (isCData) {
                            builder.append("<![CDATA[");
                        }
                        builder.append(event.asCharacters().getData().trim());
                        if (isCData) {
                            builder.append("]]>");
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return null;
    }
}
