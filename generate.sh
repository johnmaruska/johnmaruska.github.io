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

clojure -M:generate
tidy-html docs/resume.html
tidy-html docs/links.html
