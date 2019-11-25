/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.tree.patch;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.JsonString;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.tree.pointer.NodePointerVisitorTesting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NodePatchToJsonFormatNodePointerVisitorTest extends NodePatchTestCase4<NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonPropertyName>>
        implements NodePointerVisitorTesting<NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonPropertyName>, JsonNode, JsonPropertyName> {

    @Test
    public void testPathNameTypeNameAbsentFromPath() {
        this.pathNameTypeAndCheck("/1/2/3/-", null);
    }

    @Test
    public void testPathNameTypeNamePresentInPath() {
        this.pathNameTypeAndCheck("/abc", "json-property-name");
    }

    @Test
    public void testPathNameTypeNamePresentInPath2() {
        this.pathNameTypeAndCheck("/1/abc", "json-property-name");
    }

    private void pathNameTypeAndCheck(final String path, final String typeName) {
        assertEquals(Optional.ofNullable(typeName).map(JsonNode::string),
                NodePatchToJsonFormatNodePointerVisitor.pathNameType(NodePointer.parse(path, JsonPropertyName::with, JsonNode.class), this.marshallContext()),
                () -> "path: " + CharSequences.quoteAndEscape(path));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(new NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonPropertyName>(null), "");
    }

    @Test
    public void testToString2() {
        final JsonString type = JsonNode.string(this.getClass().getName());

        final NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonPropertyName> visitor = new NodePatchToJsonFormatNodePointerVisitor<>(this.marshallContext());
        visitor.pathNameType = Optional.of(type);
        this.toStringAndCheck(visitor, type.toString());
    }

    @Override
    public NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonPropertyName> createVisitor() {
        return new NodePatchToJsonFormatNodePointerVisitor<>(this.marshallContext());
    }

    private JsonNodeMarshallContext marshallContext() {
        return JsonNodeMarshallContexts.basic();
    }

    @Override
    public String typeNamePrefix() {
        return NodePatchToJsonFormat.class.getSimpleName();
    }

    @Override
    public Class<NodePatchToJsonFormatNodePointerVisitor<JsonNode, JsonPropertyName>> type() {
        return Cast.to(NodePatchToJsonFormatNodePointerVisitor.class);
    }
}
