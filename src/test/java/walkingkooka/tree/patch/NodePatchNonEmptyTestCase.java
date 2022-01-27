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
import walkingkooka.tree.expression.ExpressionNumberContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.pointer.NodePointer;

import java.math.MathContext;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodePatchNonEmptyTestCase<P extends NodePatchNonEmpty<JsonNode, JsonPropertyName>> extends NodePatchTestCase3<P> {

    NodePatchNonEmptyTestCase() {
        super();
    }

    @Test
    public final void testEqualsEmpty() {
        this.checkNotEquals(NodePatch.empty(JsonNode.class));
    }

    @Test
    public final void testEqualsDifferentPath() {
        this.checkNotEquals(this.createPatch(this.path3()));
    }

    @Test
    public final void testEqualsWithNext() {
        final NodePointer<JsonNode, JsonPropertyName> path2 = this.path3();
        final JsonNode value2 = this.value2();

        this.checkEquals(this.createPatch().add(path2, value2),
                this.createPatch().add(path2, value2));
    }

    @Test
    public final void testEqualsWithDifferentNext() {
        final NodePointer<JsonNode, JsonPropertyName> path2 = this.path3();
        this.checkNotEquals(this.createPatch().add(path2, this.value2()),
                this.createPatch().add(path2, this.value3()));
    }

    @Test
    public final void testFromJsonUnknownPropertyFails() {
        this.unmarshallFails2("[{\n" +
                "  \"op\": \"$OP\",\n" +
                "  \"unexpected-property\": \"fromJson must fail!\"\n" +
                "}]");
    }

    @Test
    public final void testToJsonPatchRoundtrip() {
        this.toJsonPatchRoundtripAndCheck(this.createPatch());
    }

    @Override final P createPatch() {
        return this.createPatch(this.path1());
    }

    abstract P createPatch(final NodePointer<JsonNode, JsonPropertyName> path);

    final void unmarshallAndCheck2(final String json,
                                   final NodePatch<JsonNode, JsonPropertyName> patch) {
        this.unmarshallAndCheck(json.replace("$OP", this.operation()),
                patch);
    }

    final void unmarshallFails2(final String json) {
        this.unmarshallFails(json.replace("$OP", this.operation()));
    }

    final void marshallAndCheck2(final NodePatch<JsonNode, JsonPropertyName> patch,
                                 final String json) {
        this.marshallAndCheck(patch, json.replace("$OP", this.operation()));
    }

    final void fromJsonPatchAndCheck2(final String json,
                                      final NodePatch<JsonNode, JsonPropertyName> patch) {
        this.fromJsonPatchAndCheck(json.replace("$OP", this.operation()),
                patch);
    }

    final void fromJsonPatchAndCheck(final String from,
                                     final NodePatch<JsonNode, JsonPropertyName> value) {
        this.fromJsonPatchAndCheck(JsonNode.parse(from), value);
    }

    final void fromJsonPatchAndCheck(final JsonNode from,
                                     final NodePatch<JsonNode, JsonPropertyName> value) {
        this.checkEquals(value,
                this.fromJsonPatch(from),
                () -> "fromJsonPatch failed " + from);
    }

    final void fromJsonPatchFails(final String from) {
        this.fromJsonPatchFails(JsonNode.parse(from));
    }

    final void fromJsonPatchFails(final JsonNode from) {
        assertThrows(IllegalArgumentException.class, () -> this.fromJsonPatch(from));
    }

    final NodePatch<JsonNode, JsonPropertyName> fromJsonPatch(final JsonNode node) {
        return NodePatch.fromJsonPatch(
                node,
                JsonPropertyName::with,
                Function.identity(),
                ExpressionNumberKind.DEFAULT,
                MathContext.DECIMAL32
        );
    }

    final void toJsonPatchAndCheck2(final NodePatch<JsonNode, JsonPropertyName> patch,
                                    final String json) {
        this.toJsonPatchAndCheck(patch,
                json.replace("$OP", this.operation()));
    }

    final void toJsonPatchRoundtripAndCheck(final NodePatch<JsonNode, JsonPropertyName> patch) {
        final JsonNode json = patch.toJsonPatch();
        this.fromJsonPatchAndCheck(json, patch);
    }

    abstract String operation();
}
