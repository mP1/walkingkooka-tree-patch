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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.expression.ExpressionNumberContext;
import walkingkooka.tree.expression.ExpressionNumberContexts;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;

import java.math.MathContext;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NodePatchTest extends NodePatchTestCase2<NodePatch<JsonNode, JsonPropertyName>> {

    private final static ExpressionNumberKind KIND = ExpressionNumberKind.DEFAULT;
    private final static MathContext CONTEXT = MathContext.DECIMAL32;

    @Test
    public void testFromJsonPatchNullJsonNodeFails() {
        assertThrows(
                NullPointerException.class,
                () -> NodePatch.fromJsonPatch(
                        null,
                        this.nameFactory(),
                        this.valueFactory(),
                        KIND,
                        CONTEXT
                )
        );
    }

    @Test
    public void testFromJsonPatchNullNameFactoryFails() {
        assertThrows(
                NullPointerException.class,
                () -> NodePatch.fromJsonPatch(
                        JsonNode.object(),
                        null,
                        this.valueFactory(),
                        KIND,
                        CONTEXT
                )
        );
    }

    @Test
    public void testFromJsonPatchNullValueFactoryFails() {
        assertThrows(
                NullPointerException.class,
                () -> NodePatch.fromJsonPatch(
                        JsonNode.object(),
                        this.nameFactory(),
                        null,
                        KIND,
                        CONTEXT
                )
        );
    }

    @Test
    public void testFromJsonPatchNullKindFails() {
        assertThrows(
                NullPointerException.class,
                () -> NodePatch.fromJsonPatch(
                        JsonNode.object(),
                        this.nameFactory(),
                        this.valueFactory(),
                        null,
                        CONTEXT
                )
        );
    }

    @Test
    public void testFromJsonPatchNullMathContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> NodePatch.fromJsonPatch(
                        JsonNode.object(),
                        this.nameFactory(),
                        this.valueFactory(),
                        KIND,
                        null
                )
        );
    }
    
    private Function<String, JsonPropertyName> nameFactory() {
        return JsonPropertyName::with;
    }

    private Function<JsonNode, JsonNode> valueFactory() {
        return Function.identity();
    }

    @Test
    public void testTest() {
        final JsonNode node = JsonNode.parse("{\"a1\": \"value1\"}");

        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .test(this.pointer("/a1"), this.value1()),
                node,
                node);
    }

    @Test
    public void testTestTest() {
        final JsonNode node = JsonNode.parse("{\"a1\": \"value1\", \"b2\": \"value2\"}");

        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .test(this.pointer("/a1"), this.value1())
                        .test(this.pointer("/b2"), this.value2()),
                node,
                node);
    }

    @Test
    public void testTestAdd() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .test(this.pointer("/a1"), this.value1())
                        .add(this.pointer("/b2"), this.value2()),
                "{\"a1\": \"value1\"}",
                "{\"a1\": \"value1\", \"b2\": \"value2\"}");
    }

    @Test
    public void testAddAdd() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .add(this.pointer("/a1"), this.value1())
                        .add(this.pointer("/b2"), this.value2()),
                "{\"c3\": \"value3\"}",
                "{\"a1\": \"value1\", \"b2\": \"value2\", \"c3\": \"value3\"}");
    }

    @Test
    public void testRemoveAdd() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .remove(this.pointer("/a1"))
                        .add(this.pointer("/b2"), this.value2()),
                "{\"a1\": \"value1\", \"c3\": \"value3\"}",
                "{\"b2\": \"value2\", \"c3\": \"value3\"}");
    }

    @Test
    public void testRemoveAddRemove() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .remove(this.pointer("/a1"))
                        .add(this.pointer("/b2"), this.value2())
                        .remove(this.pointer("/c3")),
                "{\"a1\": \"value1\", \"c3\": \"value3\"}",
                "{\"b2\": \"value2\"}");
    }

    @Test
    public void testAddAddRemove() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .add(this.pointer("/a1"), this.value1())
                        .add(this.pointer("/b2"), this.value2())
                        .remove(this.pointer("/a1")),
                "{\"c3\": \"value3\"}",
                "{\"b2\": \"value2\", \"c3\": \"value3\"}");
    }

    @Test
    public void testCopyTestRemove() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .copy(this.pointer("/a1"), this.pointer("/b2/c3"))
                        .test(this.pointer("/b2/c3"), this.string("COPIED"))
                        .remove(this.pointer("/a1")),
                "{\"a1\": \"COPIED\", \"b2\": {\"c3\": \"value3\"}}",
                "{\"b2\": {\"c3\": \"COPIED\"}}");
    }

    @Test
    public void testMoveTest() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .move(this.pointer("/a1"), this.pointer("/b2/c3"))
                        .test(this.pointer("/b2/c3"), this.string("MOVED")),
                "{\"a1\": \"MOVED\", \"b2\": {\"c3\": \"value3\"}}",
                "{\"b2\": {\"c3\": \"MOVED\"}}");
    }

    /**
     * <a href="http://jsonpatch.com/"></a>
     * <pre>
     * The original document
     * {
     *   "baz": "qux",
     *   "foo": "bar"
     * }
     * The patch
     * [
     *   { "op": "replace", "path": "/baz", "value": "boo" },
     *   { "op": "add", "path": "/hello", "value": ["world"] },
     *   { "op": "remove", "path": "/foo" }
     * ]
     * The result
     * {
     *   "baz": "boo",
     *   "hello": ["world"]
     * }
     * </pre>
     */
    @Test
    public void testJsonPatchExample() {
        this.applyAndCheck(NodePatch.empty(JsonNode.class)
                        .replace(this.pointer("/baz"), this.string("boo"))
                        .add(this.pointer("/hello"), JsonNode.array().appendChild(this.string("world")))
                        .remove(this.pointer("/foo")),
                "{\"baz\": \"qux\", \"foo\": \"bar\"}",
                "{\"baz\": \"boo\", \"hello\": [\"world\"]}");
    }

    @Test
    public void testToStringAddAdd() {
        this.toStringAndCheck(NodePatch.empty(JsonNode.class)
                        .add(this.pointer("/a1"), this.value1())
                        .add(this.pointer("/b2"), this.value2()),
                "add path=\"/a1\" value=\"value1\", add path=\"/b2\" value=\"value2\"");
    }

    @Test
    public void testToStringAddReplaceRemove() {
        this.toStringAndCheck(NodePatch.empty(JsonNode.class)
                        .add(this.pointer("/a1"), this.value1())
                        .replace(this.pointer("/b2"), this.value2())
                        .remove(this.pointer("/c3")),
                "add path=\"/a1\" value=\"value1\", replace path=\"/b2\" value=\"value2\", remove path=\"/c3\"");
    }

    @Test
    public void testToStringCopyMove() {
        this.toStringAndCheck(NodePatch.empty(JsonNode.class)
                        .copy(this.pointer("/a1"), this.pointer("/b2"))
                        .move(this.pointer("/c3"), this.pointer("/d4/e5")),
                "copy from=\"/a1\" path=\"/b2\", move from=\"/c3\" path=\"/d4/e5\"");
    }

    private JsonNode string(final String string) {
        return JsonNode.string(string);
    }

    // ClassTesting2....................................................................................................

    @Override
    public Class<NodePatch<JsonNode, JsonPropertyName>> type() {
        return Cast.to(NodePatch.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
