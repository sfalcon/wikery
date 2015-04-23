# wikery

Simple web service that queries wikipedia abstracts files. The files are first converted and stored into json format (keeping only title, abstract and url).

## Installation

clone the repo + lein deps

## Usage

### Running
    $ lein run
    or
    $ lein ring server

### Testing
    $ lein tests
    $ lein autotest
    
### Docs
    $ lein marg

## Examples

browser => localhost:port/search?q=Holsmund
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

Distributed under the Common Development Public License 1.0
