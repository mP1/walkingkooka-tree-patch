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
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.json.JsonNode;

import java.util.function.Function;

public final class NodePatchFromJsonFormatJsonPatchTest extends NodePatchTestCase4<NodePatchFromJsonFormatJsonPatch<TestNode, StringName>> {

    @Test
    public void testToString() {
        final Function<String, StringName> nameFactory = Names::string;
        final Function<JsonNode, TestNode> valueFactory = (ignored) -> null;

        this.toStringAndCheck(NodePatchFromJsonFormatJsonPatch.with(nameFactory, valueFactory),
                nameFactory + " " + valueFactory);
    }

    @Override
    public Class<NodePatchFromJsonFormatJsonPatch<TestNode, StringName>> type() {
        return Cast.to(NodePatchFromJsonFormatJsonPatch.class);
    }
}
