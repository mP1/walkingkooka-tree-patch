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

import walkingkooka.Cast;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.pointer.NodePointer;

import java.util.function.BiFunction;

final class NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor extends NodePatchNotEmptyNodePatchVisitor {

    static NodePatchNotEmptyAddReplaceOrTestAdd<?, ?> add(final JsonObject patch,
                                                          final NodePatchFromJsonFormat format,
                                                          final JsonNodeUnmarshallContext context) {
        final NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor visitor = new NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor(patch, format, context);
        visitor.accept(patch);
        return NodePatchNotEmptyAddReplaceOrTestAdd.with(visitor.path(), Cast.to(visitor.value()));
    }

    static NodePatchNotEmptyAddReplaceOrTestReplace<?, ?> replace(final JsonObject patch,
                                                                  final NodePatchFromJsonFormat format,
                                                                  final JsonNodeUnmarshallContext context) {
        final NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor visitor = new NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor(patch, format, context);
        visitor.accept(patch);
        return NodePatchNotEmptyAddReplaceOrTestReplace.with(visitor.path(), Cast.to(format.valueOrFail(visitor, context)));
    }

    static NodePatchNotEmptyAddReplaceOrTestTest<?, ?> test(final JsonObject patch,
                                                            final NodePatchFromJsonFormat format,
                                                            final JsonNodeUnmarshallContext context) {
        final NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor visitor = new NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor(patch, format, context);
        visitor.accept(patch);
        return NodePatchNotEmptyAddReplaceOrTestTest.with(visitor.path(), Cast.to(format.valueOrFail(visitor, context)));
    }

    NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor(final JsonObject patch,
                                                      final NodePatchFromJsonFormat format,
                                                      final JsonNodeUnmarshallContext context) {
        super(patch, format, context);
    }

    @Override
    void visitFrom(final String from) {
        this.unknownPropertyPresent(NodePatch.FROM_PROPERTY);
    }

    // VALUE TYPE .............................................................................................

    void visitValueType(final JsonNode valueType) {
    }

    // VALUE ........................................................................................................

    void visitValue(final JsonNode value) {
        this.value = value;
    }

    private Node<?, ?, ?, ?> value() {
        return this.format.valueOrFail(this, this.context);
    }

    /**
     * Once all properties are visited this will be converted into a {@link NodePointer}
     */
    JsonNode value;

    /**
     * Returns a factory that uses the {@link NodePatch#VALUE_TYPE_PROPERTY} when creating values from json.
     */
    final BiFunction<JsonNode, JsonNodeUnmarshallContext, Node<?, ?, ?, ?>> valueFactory(final JsonNodeUnmarshallContext context) {
        if (null == this.valueFactory) {
            this.valueFactory = context.unmarshallWithType(NodePatch.VALUE_TYPE_PROPERTY, this.patch, NODE_TYPE);
        }
        return this.valueFactory;
    }

    private final static Class<Node<?, ?, ?, ?>> NODE_TYPE = Cast.to(Node.class);

    private BiFunction<JsonNode, JsonNodeUnmarshallContext, Node<?, ?, ?, ?>> valueFactory;
}
