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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.pointer.NodePointer;

final class NodePatchNotEmptyCopyOrMoveNodePatchVisitor extends NodePatchNotEmptyNodePatchVisitor {

    static NodePatchNotEmptyCopyOrMoveCopy<?, ?> copy(final JsonObject patch,
                                                      final NodePatchFromJsonFormat format,
                                                      final JsonNodeUnmarshallContext context) {
        final NodePatchNotEmptyCopyOrMoveNodePatchVisitor visitor = new NodePatchNotEmptyCopyOrMoveNodePatchVisitor(patch,
            format,
            context);
        visitor.accept(patch);
        return NodePatchNotEmptyCopyOrMoveCopy.with(Cast.to(visitor.from()), visitor.path());
    }

    static NodePatchNotEmptyCopyOrMoveMove<?, ?> move(final JsonObject patch,
                                                      final NodePatchFromJsonFormat format,
                                                      final JsonNodeUnmarshallContext context) {
        final NodePatchNotEmptyCopyOrMoveNodePatchVisitor visitor = new NodePatchNotEmptyCopyOrMoveNodePatchVisitor(patch,
            format,
            context);
        visitor.accept(patch);
        return NodePatchNotEmptyCopyOrMoveMove.with(Cast.to(visitor.from()), visitor.path());
    }

    // VisibleForTesting
    NodePatchNotEmptyCopyOrMoveNodePatchVisitor(final JsonObject patch,
                                                final NodePatchFromJsonFormat format,
                                                final JsonNodeUnmarshallContext context) {
        super(patch, format, context);
    }

    // FROM ........................................................................................

    void visitFrom(final String from) {
        this.from = from;
    }

    private NodePointer<?, ?> from() {
        return this.pathOrFail(this.from, NodePatch.FROM_PROPERTY);
    }

    /**
     * Once all properties are visited this will be converted into a {@link NodePointer}
     */
    private String from;

    @Override
    void visitValueType(final JsonNode valueType) {
        this.unknownPropertyPresent(NodePatch.VALUE_TYPE_PROPERTY);
    }

    @Override
    void visitValue(final JsonNode value) {
        this.unknownPropertyPresent(NodePatch.VALUE_PROPERTY);
    }
}
