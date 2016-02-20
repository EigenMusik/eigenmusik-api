# EigenMusik API
[![Build Status](https://travis-ci.org/EigenMusik/eigenmusik-api.svg?branch=master)](https://travis-ci.org/EigenMusik/eigenmusik-api)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/33437442f5854b6a9184644c28ba3424)](https://www.codacy.com/app/app44509279/eigenmusik-api)

EigenMusik is a mobile and web app that seeks to aggregate various music sources into a sexy common web player.
## Installation
- Clone the repo:
```
git clone https://github.com/EigenMusik/eigenmusik-api.git
```
- Setup a [PostGres](http://www.postgresql.org/) DB instance.
- Configure your environment, using ```.env.dev``` as a template.
- Run ```mvn spring-boot:run``` to start the API server.

## Usage

To use the API server with your client, either use one of the client repos or develop your own using the [Swagger Docs](http://api.eigenmusik.com/swagger/index.html).

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some sick new feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D
