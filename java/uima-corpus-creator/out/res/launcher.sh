#!/usr/bin/env bash

rm -rf processed
bd="processed/$t"
bf="corpus.xml"

mkdir -p "$bd"

xmllint \
    --format "$bf" \
    > "$bd/indented.xml"

xsltproc \
    -o "$bd/sorted.xml" \
    scripts/sort.xsl \
    "$bf"

xsltproc \
    -o "$bd/dict.tsv" \
    scripts/tsv.xsl \
    "$bf"

xsltproc \
    -o "$bd/dict-summary.tsv" \
    scripts/summary.xsl \
    "$bf"

# ./scripts/count.sh "$d" > "processed/$d/dict-count.tsv"
# ./scripts/count.sh "processed/$d/filtered.xml" > "processed/$d/filtered-count.tsv"

# Rscript \
#     --vanilla \
#     'scripts/plot.r' \
#     "$d"
