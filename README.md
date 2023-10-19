# John's GitHub Pages Website

Right now it's just a static resume page generated by Clojure but I'm
hoping to expand in the future.


## Commands

### Requirements

Need a working install of Clojure and Java 11, so `clojure` CLI works.

Also need the following packages:

    brew install tidy-html5 pandoc

### Generate new files

    ./generate.sh


## TODO
- add something to give life to home page. Photos, emoji, etc
- Move introduction out of raw HTML to a data file
- create a footer section
- add RSS feed support
- consistent header across home, links, etc. Maybe resume?
- Switch to a static site generator when this gets unwieldy
- Look into printing resume to PDF. Check scanner/auto-loader

## Acknowledgements

Design of the `resume` page commissioned from Kat Garnier aka
[katsinkskyd](https://github.com/katsinskyd)

Structure based off [agzam/resume](https://github.com/agzam/resume)
and changed to suit my personal taste.

Links page inspired by [ChaelCodes' blogpost](https://www.chael.codes/2023/10/04/jekyll-links-page.html)
