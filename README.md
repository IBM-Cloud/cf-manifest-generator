## About

Web app that will generate a Cloud Foundry manifest file to simplify the cf push command for your application.

## Usage

Fill out the form and click the generate button.  Copy and past the resulting manifest text into a file called
manifest.yml and save it in the root of your application.  Your cf push command will automatically
find the manifest file and use the values within it to deploy your application.

## License

See LICENSE file in the root of the repository.

## Dependencies

For sever side dependencies see the pom.xml file in the root of the repository.

Client side dependencies can be found below.

* [Bootstrap 3.1.1](http://getbootstrap.com/)
* [JQuery 1.11](http://jquery.com/)
* [Typeahead.js 10.2](https://github.com/twitter/typeahead.js/)
* [typeahead.js-bootstrap3.less](https://github.com/hyspace/typeahead.js-bootstrap3.less)