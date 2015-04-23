# wikery

Simple web service that queries wikipedia abstracts files. The files are first converted and stored into json format (keeping only title, abstract and url).

## Installation

clone the repo + lein deps

## Usage

### Running
On first query, the project will download and convert from http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-abstract23.xml, so it will take time.

Having the file downloaded in resources and changing the hardcoded url of 'wikery.api' to the local file would speed up this process.

    $ lein run
    or
    $ lein ring server

### Testing
    $ lein test
    $ lein autotest
    
### Docs
    $ lein marg
http://sfalcon.github.io/wikery/

## Examples

### 
browser => localhost:port/search?q=Holsmund

### 
    $ curl -X GET http://localhost:port/search?q=Holsmund

### Bugs

...

### Improvements? / TODO
#### Selectable input
Use parameters or input forms to select what file to parse, it's hardcoded right now
#### Memoizing
Queries?
#### Validating
Inputs, creating error page

## License

Copyright © 2015

Distributed under the Common Development and Distribution Public License 1.0
