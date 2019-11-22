# Tool to import a Wikimedia dump into a pgsql database.

This software requires a PostgreSQL database containing the mediawiki dump to work.

To install a postgreSQL database on your system, use the method recommended by your OS documentation.
For example, on Ubuntu 18.04, you could do:

    sudo apt install postgresql pgadmin3

Then you could setup the password of the `postgres` database role (used to administrate the database) as follows (from the [Ubuntu doc][doc]):

- Launch the postgres prompt as the `postgres` user with sudo:

  ```console
  $ sudo -u postgres psql postgres
  ```

- Set a password for the "postgres" database role using the command:

  ```
  \password postgres
  ```

  and give your password when prompted.
  The password text will be hidden from the console for security purposes.

Type Control+D to exit the posgreSQL prompt.

[doc]: https://help.ubuntu.com/community/PostgreSQL

## Database creation

The SQL script `create-db.sql` allows you to easily create a database with the required table and view for this software to run.
For example, on a default installation of postgresql 10.10 in Ubuntu 18.04, the following works:

```console
$ sudo -u postgres createdb -T template0 readability
$ sudo -u postgres psql readability < create-db.sql
```

## Dump import

Usage is as follows:

```console
$ java \
    -cp target/mediawiki-importer-1.0.0-SNAPSHOT-fatjar.jar \
    eu.crydee.readability.mediawiki.Importer \
    -d jdbc:postgresql://localhost:5432/readability \
    -u postgres \
    -p postgres-db-role-password
    ~/downloads/simplewiki-20140927-pages-meta-history.xml
```

When the importer runs, the number in the console is the current page being imported.
At the time of this writing there are 521,853 pages, so you can get an idea of the progress this way.
Note that the first pages are way slower to import than the last ones, because they have more history.
See the help for details on how to import only parts of the dump if you feel like doing it in several times because it is slow.
