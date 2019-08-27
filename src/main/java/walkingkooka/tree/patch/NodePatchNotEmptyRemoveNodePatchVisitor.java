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

import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.map.FromJsonNodeContext;

final class NodePatchNotEmptyRemoveNodePatchVisitor extends NodePatchNotEmptyNodePatchVisitor {

    static NodePatchNotEmptyRemove<?, ?> remove(final JsonObjectNode patch,
                                                final NodePatchFromJsonFormat format,
                                                final FromJsonNodeContext context) {
        final NodePatchNotEmptyRemoveNodePatchVisitor visitor = new NodePatchNotEmptyRemoveNodePatchVisitor(patch,
                format,
                context);
        visitor.accept(patch);
        return NodePatchNotEmptyRemove.with(visitor.path());
    }

    // VisibleForTesting
    NodePatchNotEmptyRemoveNodePatchVisitor(final JsonObjectNode node,
                                            final NodePatchFromJsonFormat format,
                                            final FromJsonNodeContext context) {
        super(node, format, context);
    }

    @Override
    void visitFrom(final String from) {
        this.unknownPropertyPresent(NodePatch.FROM_PROPERTY);
    }

    @Override
    void visitValueType(final JsonNode valueType) {
        this.unknownPropertyPresent(NodePatch.VALUE_TYPE_PROPERTY);
    }

    @Override
    void visitValue(final JsonNode value) {
        this.unknownPropertyPresent(NodePatch.VALUE_PROPERTY);
    }
}
