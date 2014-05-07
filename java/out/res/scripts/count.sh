#!/usr/bin/env bash

if [[ $# < 1 ]]; then
    echo "please give the filename of the xml to consider as argument."
    exit 1
fi

file=$1

function countTotal {
    xmllint "$file" --xpath \
        "count(/dict/original/revised-list/revised)"
}

function countWhereNBRevisedIs {
    local nb=$1
    xmllint "$file" --xpath \
        "count( /dict/original/revised-list[ count(revised) = $nb ]/revised )"
}

total="$(countTotal)"
sum=0
nb=1
echo "x	y"
while [[ $sum < $total ]]; do
    count="$(countWhereNBRevisedIs "$nb")"
    echo "$nb	$count"
    ((sum+=count))
    ((nb++))
done
