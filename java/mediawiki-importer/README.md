Tool to import a Wikimedia dump into a pgsql database.
======================================================

Database creation
-----------------

The SQL script `create-db.sql` allows you to easily create a database with the required table and view for this software to run. For example, on a default installation of postgresql 9.3 in Ubuntu 14.04, the following works:

    $ sudo -u postgres createdb -T template0 readability
    $ sudo -u postgres psql readability < create-db.sql

Dump import
-----------

First, build the software with the dev flag on:

    mog@becca:~/work/readability/java$ mvn install
Then you can use the importer. Usage is as follows:

    mog@becca:~/work/readability/java$ java -cp mediawiki-importer/target/mediawiki-importer-1.0.0-SNAPSHOT.jar eu.crydee.readability.mediawiki.Importer -d jdbc:postgresql://localhost:5432/readability -u postgres -b 5000 mediawiki-importer/simplewiki-20140927-pages-meta-history.xml

When the importer runs, the number in the console is the current page being imported. At the time of this writing there are 335,836 pages, so you can get an idea of the progress this way. Note that the first pages are way slower to import than the last ones, because they have more history. See the help for details on how to import only parts of the dump if you feel like doing it in several times because it is slow.
