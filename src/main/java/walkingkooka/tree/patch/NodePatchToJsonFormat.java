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

import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.json.map.ToJsonNodeContext;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Optional;

enum NodePatchToJsonFormat {

    JSON_NODE_CONTEXT {
        @Override
        JsonObjectNode setPathNameType(final JsonObjectNode object,
                                       final NodePointer<?, ?> path,
                                       final ToJsonNodeContext context) {
            return this.setPathNameType0(object,
                    NodePatchToJsonFormatNodePointerVisitor.pathNameType(path, context));
        }

        @Override
        JsonObjectNode setPathNameType(final JsonObjectNode object,
                                       final NodePointer<?, ?> from,
                                       final NodePointer<?, ?> path,
                                       final ToJsonNodeContext context) {
            Optional<JsonStringNode> type = NodePatchToJsonFormatNodePointerVisitor.pathNameType(from, context);
            if (!type.isPresent()) {
                type = NodePatchToJsonFormatNodePointerVisitor.pathNameType(path, context);
            }
            return this.setPathNameType0(object, type);
        }

        /**
         * Adds the path component type properties if necessary to the given object.
         */
        private JsonObjectNode setPathNameType0(final JsonObjectNode object,
                                                final Optional<JsonStringNode> pathNameType) {
            return pathNameType
                    .map(t -> object.set(NodePatch.PATH_NAME_TYPE_PROPERTY, t))
                    .orElse(object);
        }

        @Override
        JsonObjectNode setValueType(final JsonObjectNode object,
                                    final Object value,
                                    final ToJsonNodeContext context) {
            return object.set(NodePatch.VALUE_TYPE_PROPERTY,
                    typeOrFail(value, context));
        }

        /**
         * Accepts a value such as a path or value and returns a {@link JsonStringNode} with the type name.
         */
        private JsonStringNode typeOrFail(final Object value,
                                          final ToJsonNodeContext context) {
            return context.typeName(value.getClass())
                    .orElseThrow(() -> new IllegalArgumentException("Type not registered as supporting json: " + value));
        }
    },
    JSON_PATCH {
        @Override
        JsonObjectNode setPathNameType(final JsonObjectNode object,
                                       final NodePointer<?, ?> path,
                                       final ToJsonNodeContext context) {
            return object;
        }

        @Override
        JsonObjectNode setPathNameType(final JsonObjectNode object,
                                       final NodePointer<?, ?> from,
                                       final NodePointer<?, ?> path,
                                       final ToJsonNodeContext context) {
            return object;
        }

        @Override
        JsonObjectNode setValueType(final JsonObjectNode object,
                                    final Object value,
                                    final ToJsonNodeContext context) {
            return object;
        }
    };

    abstract JsonObjectNode setPathNameType(final JsonObjectNode object,
                                            final NodePointer<?, ?> path,
                                            final ToJsonNodeContext context);

    abstract JsonObjectNode setPathNameType(final JsonObjectNode object,
                                            final NodePointer<?, ?> from,
                                            final NodePointer<?, ?> path,
                                            final ToJsonNodeContext context);

    abstract JsonObjectNode setValueType(final JsonObjectNode object,
                                         final Object value,
                                         final ToJsonNodeContext context);
}
