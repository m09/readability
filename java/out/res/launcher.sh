#!/usr/bin/env bash

rm -rf processed

mkdir processed

for d in 'dictTxt.xml' 'dictPos.xml'; do

    mkdir "processed/$d"

    xmllint \
        --format "$d" \
        > "processed/$d/indented.xml"

    xsltproc \
        -o "processed/$d/sorted.xml" \
        scripts/sort.xsl \
        "$d"

    xsltproc \
        -o "processed/$d/dict.tsv" \
        scripts/tsv.xsl \
        "$d"

    xsltproc \
        -o "processed/$d/dict-summary.tsv" \
        scripts/summary.xsl \
        "$d"

    xsltproc -o "processed/$d/filtered.xml" \
        scripts/filter-long-revised.xsl \
        "$d"

    xsltproc \
        -o "processed/$d/filtered-sorted.xml" \
        scripts/sort.xsl \
        "processed/$d/filtered.xml"

    xsltproc -o "processed/$d/filtered.tsv" \
        scripts/tsv.xsl \
        "processed/$d/filtered.xml"

    xsltproc -o "processed/$d/filtered-summary.tsv" \
        scripts/summary.xsl \
        "processed/$d/filtered.xml"

    ./scripts/count.sh "$d" > "processed/$d/dict-count.tsv"
    ./scripts/count.sh "processed/$d/filtered.xml" > "processed/$d/filtered-count.tsv"

    Rscript \
        --vanilla \
        'scripts/plot.r' \
        "$d"
done
