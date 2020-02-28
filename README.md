[![Build Status](https://travis-ci.com/mP1/walkingkooka-tree-patch.svg?branch=master)](https://travis-ci.com/mP1/walkingkooka-tree-patch.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-tree-patch/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-tree-patch?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A `NodePatch` is a `Node` equivalent as what `json-patch` is for json objects or diff patch is for files, namely a set
of discrete operations to test and apply changes to some artifact. The advantage over json-patch implementations is that
any failed patch leaves the original source unmodified and successful patches return a new tree.

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
