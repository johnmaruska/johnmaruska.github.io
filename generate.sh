#!/bin/bash
set -euo pipefail

tidy-html () {
    FILE=$1
    echo "Tidying $FILE"
    mv $FILE ${FILE}.raw
    set +e  # tidy fails on warnings
    tidy -indent -quiet -config tidy_config.txt ${FILE}.raw > $FILE
    set -e
    rm ${FILE}.raw
}

post-html () {
    FILE=$1
    TITLE=$(head -n 1 $FILE | sed 's/^# //')
    OUTFILE=$(echo $FILE | sed 's/\.md$/.html/')
    echo "Generating docs/$OUTFILE"
    pandoc \
        --standalone --quiet \
        --from markdown --to html \
        --metadata title="$TITLE" --css docs/blogpost.css \
        --variable "lang=en-US" \
        --output docs/$OUTFILE $FILE
}

clojure -M:generate

# for POST in posts/*
# do
#     post-html $POST
# done


for HTML in docs/**.html
do
    tidy-html $HTML
done
