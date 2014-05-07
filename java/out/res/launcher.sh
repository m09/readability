#!/usr/bin/env bash

xmllint \
    --format dict.xml \
    > processed/indented.xml

xsltproc \
    -o processed/sorted.xml \
    scripts/sort.xsl \
    dict.xml

xsltproc \
    -o processed/dict.tsv \
    scripts/tsv.xsl \
    dict.xml

xsltproc \
    -o processed/dict-summary.tsv \
    scripts/summary.xsl \
    dict.xml

xsltproc -o processed/filtered.xml \
    scripts/filter-long-revised.xsl \
    dict.xml

xsltproc \
    -o processed/filtered-sorted.xml \
    scripts/sort.xsl \
    processed/filtered.xml

xsltproc -o processed/filtered.tsv \
    scripts/tsv.xsl \
    processed/filtered.xml

xsltproc -o processed/filtered-summary.tsv \
    scripts/summary.xsl \
    processed/filtered.xml

./scripts/count.sh "dict.xml" > "processed/dict-count.tsv"
./scripts/count.sh "processed/filtered.xml" > "processed/filtered-count.tsv"

Rscript \
    --vanilla \
    'scripts/plot.r'
