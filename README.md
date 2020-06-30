[![Build Status](https://travis-ci.com/mP1/walkingkooka-tree-patch.svg?branch=master)](https://travis-ci.com/mP1/walkingkooka-tree-patch.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-tree-patch/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-tree-patch?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-tree-patch.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree-patch/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-tree-patch.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-tree-patch/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)



A `NodePatch` is a `Node` equivalent as what `json-patch` is for json objects or diff patch is for files, namely a set
of discrete operations to test and apply changes to some artifact. The advantage over json-patch implementations is that
any failed patch leaves the original source unmodified and successful patches return a new tree.

## Json example

The remainder of this section below contains the sample taken from [jsonpatch.com](http://jsonpatch.com/).
The original document

```json
{
  "baz": "qux",
  "foo": "bar"
}
```  

The patch

```json
[
  { "op": "replace", "path": "/baz", "value": "boo" },
  { "op": "add", "path": "/hello", "value": ["world"] },
  { "op": "remove", "path": "/foo" }
]
```

The result
```json
{
  "baz": "boo",
  "hello": ["world"]
}
```

## Java example

```java
public static void main(final String[] args) {
    final JsonNode before = JsonNode.parse("{\"hello\": { \"colour\": \"replaced-1\", \"tree\": \"removed-3\"}}");

    final NodePatch<JsonNode, JsonPropertyName> patch = emptyPatch()
            .add(pointer("/added-1"), JsonNode.number(1))
            .add(pointer("/hello/colour"), JsonNode.string("green-2"))
            .remove(pointer("/hello/tree"));

    final JsonNode after = patch.apply(before);

    final JsonNode expected = JsonNode.parse("{\n" +
            "  \"hello\": {\n" +
            "    \"colour\": \"green-2\"\n" +
            "  },\n" +
            "  \"added-1\": 1\n" +
            "}");

    Assertions.assertEquals(expected, after); // pass!
}

// type safety

private static NodePatch<JsonNode, JsonPropertyName> emptyPatch() {
    return NodePatch.empty(JsonNode.class);
}

private static NodePointer<JsonNode, JsonPropertyName> pointer(final String path) {
    return NodePointer.parse(path, JsonPropertyName::with, JsonNode.class);
}
```
