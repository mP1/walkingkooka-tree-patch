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

import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.JsonString;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Optional;

enum NodePatchToJsonFormat {

    JSON_NODE_CONTEXT {
        @Override
        JsonObject setPathNameType(final JsonObject object,
                                   final NodePointer<?, ?> path,
                                   final JsonNodeMarshallContext context) {
            return this.setPathNameType0(object,
                NodePatchToJsonFormatNodePointerVisitor.pathNameType(path, context));
        }

        @Override
        JsonObject setPathNameType(final JsonObject object,
                                   final NodePointer<?, ?> from,
                                   final NodePointer<?, ?> path,
                                   final JsonNodeMarshallContext context) {
            Optional<JsonString> type = NodePatchToJsonFormatNodePointerVisitor.pathNameType(from, context);
            if (!type.isPresent()) {
                type = NodePatchToJsonFormatNodePointerVisitor.pathNameType(path, context);
            }
            return this.setPathNameType0(object, type);
        }

        /**
         * Adds the path component type properties if necessary to the given object.
         */
        private JsonObject setPathNameType0(final JsonObject object,
                                            final Optional<JsonString> pathNameType) {
            return pathNameType
                .map(t -> object.set(NodePatch.PATH_NAME_TYPE_PROPERTY, t))
                .orElse(object);
        }

        @Override
        JsonObject setValueType(final JsonObject object,
                                final Object value,
                                final JsonNodeMarshallContext context) {
            return object.set(NodePatch.VALUE_TYPE_PROPERTY,
                typeOrFail(value, context));
        }

        /**
         * Accepts a value such as a path or value and returns a {@link JsonString} with the type name.
         */
        private JsonString typeOrFail(final Object value,
                                      final JsonNodeMarshallContext context) {
            return context.typeName(value.getClass())
                .orElseThrow(() -> new IllegalArgumentException("Type not registered as supporting json: " + value));
        }
    },
    JSON_PATCH {
        @Override
        JsonObject setPathNameType(final JsonObject object,
                                   final NodePointer<?, ?> path,
                                   final JsonNodeMarshallContext context) {
            return object;
        }

        @Override
        JsonObject setPathNameType(final JsonObject object,
                                   final NodePointer<?, ?> from,
                                   final NodePointer<?, ?> path,
                                   final JsonNodeMarshallContext context) {
            return object;
        }

        @Override
        JsonObject setValueType(final JsonObject object,
                                final Object value,
                                final JsonNodeMarshallContext context) {
            return object;
        }
    };

    abstract JsonObject setPathNameType(final JsonObject object,
                                        final NodePointer<?, ?> path,
                                        final JsonNodeMarshallContext context);

    abstract JsonObject setPathNameType(final JsonObject object,
                                        final NodePointer<?, ?> from,
                                        final NodePointer<?, ?> path,
                                        final JsonNodeMarshallContext context);

    abstract JsonObject setValueType(final JsonObject object,
                                     final Object value,
                                     final JsonNodeMarshallContext context);
}
