Learning how to write through Simple Wikipedia contributors
===========================================================

The aim of this project is to investigate how to use
[Simple English Wikipedia][sw] to learn readability guidelines.

The idea used is to retrieve revision changes where contributors
expressed explicitly in the revision comment their intent to improve
the readability of the revised article.

Those changes are compiled into a resource then used to learn
readability guidelines.

Resource creation and usage pipelines
-------------------------------------

The `java` folder contains a Maven 3 compliant program to build and
use such a resource. The program is split into many sub-modules. Here
is a fast description so that you can get around more easily:

- uima-core, utils, corpus and model-* folders are just internal
  dependencies to make the Maven build smooth. They are not really
  applications;

- mediawiki-importer allows to import a mediawiki dump into a
  PostgreSQL database that is usable by the other programs;

- uima-corpus-creator creates a corpus from the database data;

- uima-scorer allows to score this corpus. At this point the scoring
  is not good;

- uima-server is a way to expose the corpus to other applications;

- uima-evaluator is work in progress.

Play server
-----------

The `server` folder contains a [Play Framework][play] webservice that
exposes the corpus throught JSON requests to other applications

Web client
----------

The `client` folder contains a [React][react] application to test the
system on the web.

A demo is running at http://readability.crydee.eu (first run is very
slow, it's hosted on Heroku and the application needs to start).

[play]: http://www.playframework.com/

[react]: https://facebook.github.io/react/

[sw]: https://simple.wikipedia.org/wiki/Main_Page
