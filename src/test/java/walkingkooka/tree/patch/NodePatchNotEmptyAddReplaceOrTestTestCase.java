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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.pointer.NodePointer;

public abstract class NodePatchNotEmptyAddReplaceOrTestTestCase<P extends NodePatchNotEmptyAddReplaceOrTest<JsonNode, JsonPropertyName>> extends NodePatchNonEmptyTestCase<P> {

    NodePatchNotEmptyAddReplaceOrTestTestCase() {
        super();
    }

    @Test
    public final void testEqualsDifferentValue() {
        this.checkNotEquals(this.createPatch(this.path1(), JsonNode.string("different")));
    }

    @Test
    public final void testJsonNodeUnmarshallFromPropertyFails() {
        this.unmarshallFails2("[{\"op\": \"$OP\", \"from\": \"/A1\", \"path\": \"/a1\", \"value-type\": \"json\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testJsonNodeUnmarshallPathNameTypeMissingFails() {
        this.unmarshallFails2("[{\"op\": \"$OP\", \"path\": \"/a1\", \"value-type\": \"json\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testJsonNodeUnmarshallPathMissingFails() {
        this.unmarshallFails2("[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"value-type\": \"json\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testJsonNodeUnmarshallPathInvalidFails() {
        this.unmarshallFails2("[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"!!!\", \"value-type\": \"json\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testJsonNodeUnmarshallValueTypeMissingFails() {
        this.unmarshallFails2("[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"/a1\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testJsonNodeUnmarshallValueMissingFails() {
        this.unmarshallFails2("[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"/a1\", \"value-type\": \"json\"}]");
    }

    @Test
    public final void testJsonNodeUnmarshall() {
        this.unmarshallAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/a1\",\n" +
                        "  \"value-type\": \"json\",\n" +
                        "  \"value\": \"value1\"\n" +
                        "}]",
                this.createPatch());
    }

    @Test
    public final void testJsonNodeUnmarshall2() {
        this.unmarshallAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/b2\",\n" +
                        "  \"value-type\": \"json\",\n" +
                        "  \"value\": \"value2\"\n" +
                        "}]",
                this.createPatch(this.path2(), this.value2()));
    }

    @Test
    public final void testJsonNodeUnmarshall3() {
        this.unmarshallAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/b2\",\n" +
                        "  \"value\": \"value2\",\n" +
                        "  \"value-type\": \"json\"\n" +
                        "}]",
                this.createPatch(this.path2(), this.value2()));
    }

    @Test
    public final void testJsonNodeUnmarshallMissingPathNameType() {
        this.unmarshallAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/123\",\n" +
                        "  \"value-type\": \"json\",\n" +
                        "  \"value\": \"value1\"\n" +
                        "}]",
                this.createPatch(NodePointer.indexed(123, JsonNode.class)));
    }

    @Test
    public final void testJsonNodeMarshall() {
        this.marshallAndCheck2(this.createPatch(),
                "[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"/a1\", \"value-type\": \"json\", \"value\": \"value1\"}]");
    }

    @Test
    public final void testJsonNodeMarshall2() {
        this.marshallAndCheck2(this.createPatch(this.path2(), this.value2()),
                "[{\"op\": \"$OP\", \"path-name-type\": \"json-property-name\", \"path\": \"/b2\", \"value-type\": \"json\", \"value\": \"value2\"}]");
    }

    @Test
    public final void testJsonNodeMarshallRoundtrip() {
        this.marshallWithTypeRoundTripTwiceAndCheck(this.createPatch()
                .add(this.path2(), this.value2()));
    }

    @Test
    public final void testJsonNodeMarshallRoundtrip2() {
        this.marshallWithTypeRoundTripTwiceAndCheck(this.createPatch()
                .add(this.path2(), this.value2())
                .add(this.path3(), this.value3()));
    }

    // fromJsonPatch/toJsonPatch..........................................................................................

    @Test
    public final void testFromJsonPatch() {
        this.fromJsonPatchAndCheck2("[{\"op\": \"$OP\", \"path\": \"/a1\", \"value\": \"value1\"}]",
                this.createPatch());
    }

    @Test
    public final void testToJsonPatch() {
        this.toJsonPatchAndCheck2(this.createPatch(),
                "[{\"op\": \"$OP\", \"path\": \"/a1\", \"value\": \"value1\"}]");
    }

    @Override
    final P createPatch(final NodePointer<JsonNode, JsonPropertyName> path) {
        return this.createPatch(path, this.value1());
    }

    abstract P createPatch(final NodePointer<JsonNode, JsonPropertyName> path, final JsonNode value);
}
