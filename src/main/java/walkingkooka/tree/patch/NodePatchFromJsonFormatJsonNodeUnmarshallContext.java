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
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.function.Function;

/**
 * Creates {@link NodePatch} taking the name and value types from properties.
 */
final class NodePatchFromJsonFormatJsonNodeUnmarshallContext extends NodePatchFromJsonFormat {

    static final NodePatchFromJsonFormatJsonNodeUnmarshallContext INSTANCE = new NodePatchFromJsonFormatJsonNodeUnmarshallContext();

    private NodePatchFromJsonFormatJsonNodeUnmarshallContext() {
        super();
    }

    @Override
    void accept(final NodePatchNotEmptyNodePatchVisitor visitor,
                final JsonObject node) {
        visitor.acceptNodePatch(node);
    }

    @Override
    Function<String, Name> nameFactory(final NodePatchNotEmptyNodePatchVisitor visitor,
                                       final JsonNodeUnmarshallContext context) {
        return (string) -> visitor.pathNameFactory(context)
                .apply(JsonNode.string(string), context);
    }

    @Override
    Node<?, ?, ?, ?> valueOrFail(final NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor visitor,
                                 final JsonNodeUnmarshallContext context) {
        return Cast.to(visitor.valueFactory(context)
                .apply(visitor.valueOrFail(visitor.value), context));
    }

    @Override
    public String toString() {
        return JsonNodeUnmarshallContext.class.getSimpleName();
    }
}
