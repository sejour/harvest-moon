package github.sejour.harvestmoon.path.xml;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.stream.events.StartElement;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PathIterator {
    private static final Pattern NODE_PATTERN = Pattern
            .compile("^(?<name>[^\\[]+)(?<conditions>\\[.+])?$");
    public static final PathIterator MATCHED = builder().build();

    String name;
    AttributeCondition attributeCondition;
    PathIterator child;
    boolean fallThrowable;

    public static PathIterator fromAbsoluteXPath(String expression) {
        if (StringUtils.isBlank(expression)) {
            throw new RuntimeException("blank");
        }

        final var div = expression.split("/", 2);
        if (StringUtils.isNotBlank(div[0])) {
            return parse(expression);
        }

        if (div.length > 1) {
            if (StringUtils.isBlank(div[1]) || div[1].equals("*")) {
                return builder().child(MATCHED).fallThrowable(true).build();
            }
            return parse(div[1]);
        }

        throw new RuntimeException("invalid");
    }

    private static PathIterator parse(String expression) {
        var name = "";
        AttributeCondition attributeCondition = null;
        PathIterator child = MATCHED;

        var div = new String[] { "", expression };
        int pathThrows = -1;
        while (div.length > 1 && (StringUtils.isBlank(div[0]) || div[0].equals("*"))) {
            div = div[1].split("/", 2);
            pathThrows += 1;
        }

        if (StringUtils.isBlank(div[0])) {
            throw new RuntimeException("invalid");
        }

        final var matcher = NODE_PATTERN.matcher(div[0]);
        if (!matcher.find()) {
            // TODO
            throw new RuntimeException("no match");
        }

        name = matcher.group("name");
        final var condition = matcher.group("conditions");
        if (StringUtils.isNotBlank(condition)) {
            attributeCondition = AttributeCondition.parse(condition);
        }

        if (div.length > 1 && !div[1].isEmpty()) {
            child = parse(div[1]);
        }

        return PathIterator
                .builder()
                .name(name)
                .attributeCondition(attributeCondition)
                .child(child)
                .fallThrowable(pathThrows > 0)
                .build();
    }

    public PathIterator next(StartElement element) {
        if (this == MATCHED) {
            return MATCHED;
        }

        if (StringUtils.isEmpty(name) || element.getName().toString().equals(name)) {
            if (attributeCondition == null) {
                return child;
            }

            final var attrIterator = element.getAttributes();
            final var mapBuilder = ImmutableMap.<String, String>builder();
            while (attrIterator.hasNext()) {
                final var attr = attrIterator.next();
                mapBuilder.put(attr.getName().toString(), attr.getValue());
            }

            if (attributeCondition.match(mapBuilder.build())) {
                return child;
            }
        }

        if (fallThrowable) {
            return this;
        }

        return null;
    }
}

interface AttributeCondition {
    boolean match(Map<String, String> attribute);

    Pattern EQUALS_PATTERN = Pattern
            .compile("^@(?<name>.+)=(('(?<single>.+)')|(\"(?<double>.+)\"))$");

    static AttributeCondition parse(String expression) {
        final List<AttributeCondition> conditions = Arrays
                .stream(StringUtils.substringsBetween(expression, "[", "]"))
                .map(exp -> {
                    final var expTrimmed = StringUtils.trim(exp);
                    // TODO: Support 'and', 'or'
                    final var equalsMatcher = EQUALS_PATTERN.matcher(expTrimmed);
                    if (equalsMatcher.find()) {
                        return AttributeEqualsCondition
                                .builder()
                                .name(equalsMatcher.group("name"))
                                .value(Optional.ofNullable(equalsMatcher.group("single"))
                                               .orElseGet(() -> equalsMatcher.group("double")))
                                .build();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new AttributeAndCondition(conditions);
    }
}

@Value
class AttributeAndCondition implements AttributeCondition {
    List<AttributeCondition> conditions;

    @Override
    public boolean match(Map<String, String> attribute) {
        return conditions.stream().allMatch(cnd -> cnd.match(attribute));
    }
}

@Value
@Builder
class AttributeEqualsCondition implements AttributeCondition {
    String name;
    String value;

    @Override
    public boolean match(Map<String, String> attribute) {
        final var value = attribute.get(name);
        if (value == null) {
            return false;
        }
        return value.equals(this.value);
    }
}

