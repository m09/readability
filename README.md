Learning how to write through Simple Wikipedia contributors
===========================================================

The aim of this project is to investigate whether or not
[Simple English Wikipedia][sw] is an interesting resource to learn
readability guidelines.

The idea used is to retrieve revision changes where contributors
expressed explicitly in the revision comment their intent to improve
the readability of the revised article.

Those changes are compiled into a resource then used to learn
readability guidelines.

Resource creation and usage pipelines
-------------------------------------

The `java` folder contains a Maven 3 compliant program to build and
use such a resource.

Simple server & client
----------------------

The `server` folder contains a [Play Framework][play] webservice and a
client using it.

Presentations
-------------

A bibliography introduction is available in the `slides` folder as a
[reveal.js][reveal] presentation.

[play]: http://www.playframework.com/

[reveal]: https://github.com/hakimel/reveal.js/

[sw]: https://simple.wikipedia.org/wiki/Main_Page
