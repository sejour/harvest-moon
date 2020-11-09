package github.sejour.harvestmoon.parser.xml;

import java.util.Map;
import java.util.Set;

import github.sejour.harvestmoon.path.xml.XPath;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Request {
    boolean wantText;
    Set<String> attributeRequests;
    Map<XPath, Request> childRequests;
}
