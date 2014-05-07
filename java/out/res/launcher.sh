#!/usr/bin/env bash

xmllint \
    --format dict.xml \
    > processed/indented.xml

xsltproc -o processed/filtered.xml \
    scripts/filter-long-revised.xsl \
    dict.xml

xsltproc \
    -o processed/filtered-sorted.xml \
    scripts/sort.xsl \
    processed/filtered.xml

xsltproc \
    -o processed/sorted.xml \
    scripts/sort.xsl \
    dict.xml

./scripts/count.sh \
    processed/filtered.xml \
    > processed/filtered-count.tsv

./scripts/count.sh \
    dict.xml \
    > processed/normal-count.tsv

Rscript \
    --vanilla \
    'scripts/plot.r'
