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
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.marshall.FromJsonNodeContext;

import java.util.function.Function;

/**
 * Creates {@link NodePatch} taking the name and value types from properties.
 */
final class NodePatchFromJsonFormatFromJsonNodeContext extends NodePatchFromJsonFormat {

    static final NodePatchFromJsonFormatFromJsonNodeContext INSTANCE = new NodePatchFromJsonFormatFromJsonNodeContext();

    private NodePatchFromJsonFormatFromJsonNodeContext() {
        super();
    }

    @Override
    void accept(final NodePatchNotEmptyNodePatchVisitor visitor,
                final JsonObjectNode node) {
        visitor.acceptNodePatch(node);
    }

    @Override
    Function<String, Name> nameFactory(final NodePatchNotEmptyNodePatchVisitor visitor,
                                       final FromJsonNodeContext context) {
        return (string) -> visitor.pathNameFactory(context)
                .apply(JsonNode.string(string), context);
    }

    @Override
    Node<?, ?, ?, ?> valueOrFail(final NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor visitor,
                                 final FromJsonNodeContext context) {
        return Cast.to(visitor.valueFactory(context)
                .apply(visitor.propertyOrFail(visitor.value, NodePatch.VALUE_PROPERTY), context));
    }

    @Override
    public String toString() {
        return FromJsonNodeContext.class.getSimpleName();
    }
}
