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

See which terms have the most translations
------------------------------------------

    xsltproc scripts/top.xsl dict.xml | less

Compute top translations for each score
---------------------------------------

    for s in LM LMc DLM DLMc DLMw DLMcw; do
        xsltproc --stringparam "score" "$s" \
            scripts/flattener.xsl \
            filteredTxt.xml | \
            sort -gr | \
            head -n 20 \
            > "$s"
    done
