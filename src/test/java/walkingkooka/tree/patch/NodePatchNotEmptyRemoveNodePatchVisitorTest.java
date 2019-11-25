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

public final class NodePatchNotEmptyRemoveNodePatchVisitorTest extends NodePatchNotEmptyNodePatchVisitorTestCase<NodePatchNotEmptyRemoveNodePatchVisitor> {
    @Override
    NodePatchNotEmptyRemoveNodePatchVisitor createVisitor(final JsonObject patch) {
        return new NodePatchNotEmptyRemoveNodePatchVisitor(patch, null, null);
    }

    @Override
    public String typeNamePrefix() {
        return NodePatchNotEmptyRemove.class.getSimpleName();
    }

    @Override
    public Class<NodePatchNotEmptyRemoveNodePatchVisitor> type() {
        return NodePatchNotEmptyRemoveNodePatchVisitor.class;
    }
}
