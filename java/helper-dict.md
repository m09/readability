Install a tool to work on XML files from the CLI
------------------------------------------------

On a debian system you can for example aim for `xmllint` which you can
install by installing the package `libxml2-utils`

Produce an indented xml file from a non-idented one
---------------------------------------------------

    xmllint --format dict.xml > dict-indented.xml

Retrieve all the terms that have more than one translation
----------------------------------------------------------

    xmllint dict-indented.xml \
        --xpath "/dict/original[count(revised-list/revised)>1]"

Filter out the long revisions (edit longer than original)
---------------------------------------------------------

    xsltproc -o filtered.xml filter-long-revised.xsl dict.xml
