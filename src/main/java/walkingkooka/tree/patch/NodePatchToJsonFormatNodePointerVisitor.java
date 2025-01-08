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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonString;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.tree.pointer.NodePointerVisitor;
import walkingkooka.visit.Visiting;

import java.util.Optional;

/**
 * Walks a {@link NodePointerVisitor} returning a {@link JsonString} holding the path {@link Name} type if one is found.
 */
final class NodePatchToJsonFormatNodePointerVisitor<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePointerVisitor<N, NAME> {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> Optional<JsonString> pathNameType(final NodePointer<N, NAME> path,
                                                                                                final JsonNodeMarshallContext context) {
        final NodePatchToJsonFormatNodePointerVisitor<N, NAME> visitor = new NodePatchToJsonFormatNodePointerVisitor<>(context);
        visitor.accept(path);
        return visitor.pathNameType;
    }

    // VisibleForTesting
    NodePatchToJsonFormatNodePointerVisitor(final JsonNodeMarshallContext context) {
        super();
        this.context = context;
    }

    @Override
    protected Visiting startVisitNamedChild(final NodePointer<N, NAME> node,
                                            final NAME name) {
        this.pathNameType = this.context.typeName(name.getClass());
        return Visiting.SKIP;
    }

    private final JsonNodeMarshallContext context;

    // VisibleForTesting
    Optional<JsonString> pathNameType = Optional.empty();

    @Override
    public String toString() {
        return this.pathNameType.map(JsonNode::toString)
            .orElse("");
    }
}
