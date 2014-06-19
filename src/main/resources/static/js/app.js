/*
 * Copyright IBM Corp. 2014
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
$( document ).ready(function() {
  var services = [];
  var envVars = {};
  var buildpacks = ['https://github.com/cloudfoundry/java-buildpack', 'https://github.com/cloudfoundry/cf-buildpack-ruby',
                    'https://github.com/cloudfoundry/heroku-buildpack-nodejs', 'https://github.com/cloudfoundry/ibm-websphere-liberty-buildpack',
                    'https://github.com/jmcc0nn3ll/jetty-buildpack', 'https://github.com/dmikusa-pivotal/cf-php-build-pack',
                    'https://github.com/mstine/heroku-buildpack-clojure','https://github.com/BrianMMcClain/heroku-buildpack-haskell',
                    'https://github.com/michaljemala/cloudfoundry-buildpack-go', 'https://github.com/hmalphettes/heroku-buildpack-go',
                    'https://github.com/cloudfoundry-community/nginx-buildpack', 'https://github.com/cloudfoundry-community/.net-buildpack',
                    'https://github.com/ephoning/heroku-buildpack-python', 'https://github.com/joshuamckenty/heroku-buildpack-python',
                    'https://github.com/friism/heroku-buildpack-mono', 'https://github.com/davidl-zend/zend-server-mysql-buildpack-dev',
                    'https://github.com/tsl0922/java-buildpack', 'https://github.com/glyn/virgo-buildpack', 'https://github.com/heroku/heroku-buildpack-php',
                    'https://github.com/iphoting/heroku-buildpack-php-tyler', 'https://github.com/heroku/heroku-buildpack-scala'];
  var domains = ['mybluemix.net', 'cfapps.io'];
  
  //***************************************************
  // U T I L I T Y  F U N C T I O N S
  //***************************************************
  
  function htmlEncode(value){
    //create a in-memory div, set it's inner text(which jQuery automatically encodes)
    //then grab the encoded contents back out.  The div never exists on the page.
    return $('<div/>').text(value).html();
  };
  
  function createTag(name, closeFunc) {
    var close = $('<a><i class="remove glyphicon glyphicon-remove-sign glyphicon-white"></i></a>');
    var tag = $('<span class="tag label label-info"><span>' + name + '</span></span>').append(close);
    close.click(function(e) {
      tag.remove();
      closeFunc(e);
    });
    return tag;
  };
  
  function numericValidator(alertId) {
    return function(e) {
      if(!$.isNumeric($(this).val()) && $(this).val() !== '') {
        $(alertId).show();
        $(this).parents('.form-group').addClass('has-error');
        $('#generateBtn').attr('disabled', 'disabled');
      } else {
        $(alertId).hide();
        $(this).parents('.form-group').removeClass('has-error');
        $('#generateBtn').removeAttr('disabled');
      }
    }
  };
  
  var substringMatcher = function(strs) {
    return function findMatches(q, cb) {
      var matches, substringRegex;
   
      // an array that will be populated with substring matches
      matches = [];
   
      // regex used to determine if a string contains the substring `q`
      substrRegex = new RegExp(q, 'i');
   
      // iterate through the pool of strings and for any string that
      // contains the substring `q`, add it to the `matches` array
      $.each(strs, function(i, str) {
        if (substrRegex.test(str)) {
          // the typeahead jQuery plugin expects suggestions to a
          // JavaScript object, refer to typeahead docs for more info
          matches.push({ value: str });
        }
      });
   
      cb(matches);
    };
  };
  
  //***************************************************
  // K E Y  L I S T E N E R S
  //***************************************************
  
  $('#inputServiceName').keyup(function(e) {
    if($(this).val() !== '') {
      $('#serviceAddBtn').removeAttr('disabled');
    } else {
      $('#serviceAddBtn').attr('disabled', 'disabled');
    }
  });
  
  function envKeyUp(e) {
    if($('#inputEnvName').val() !== '' && $('#inputEnvValue').val() !== '') {
      $('#envAddBtn').removeAttr('disabled'); 
    } else {
      $('#envAddBtn').attr('disabled', 'disabled');
    }
  };
  
  $('#inputAppName').keyup(function(e) {
    if($(this).val() !== '') {
      $('#generateBtn').removeAttr('disabled');
      $('#downloadBtn').removeAttr('disabled');
    } else {
      $('#generateBtn').attr('disabled', 'disabled');
      $('#downloadBtn').attr('disabled', 'disabled');
    }
  });
  
  $('#inputMemory').keyup(numericValidator('#memoryAlert'));
  $('#inputInstances').keyup(numericValidator('#instancesAlert'));
  $('#inputTimeout').keyup(numericValidator('#timeoutAlert'));
  
  $('#inputEnvName').keyup(envKeyUp);
  $('#inputEnvValue').keyup(envKeyUp);
  
  //***************************************************
  // C L I C K  L I S T E N E R S
  //***************************************************
  
  $('#serviceAddBtn').click(function() {
    var serviceName = htmlEncode($('#inputServiceName').val());
    $('#inputServiceName').val('');
    $('#serviceAddBtn').attr('disabled', 'disabled');
    var index = services.push(serviceName) - 1;
    var tag = createTag(serviceName, function(e) {
      services.splice(index, 1);
      if(services.length == 0) {
        $('#servicesFormGroup').hide();
      }
    });
    $('#services').append(tag);
    $('#servicesFormGroup').show();
    return false;
  });
  
  $('#envAddBtn').click(function() {
    var envName = htmlEncode($('#inputEnvName').val());
    var envValue = htmlEncode($('#inputEnvValue').val());
    $('#inputEnvName').val('');
    $('#inputEnvValue').val('');
    $('#envAddBtn').attr('disabled', 'disabled');
    envVars[envName] = envValue;
    var tag = createTag(envName + ': ' + envValue, function(e) {
      delete envVars[envName];
      if(Object.keys(envVars).length == 0) {
        $('#envVarFormGroup').hide();
      }
    });
    $('#envVars').append(tag);
    $('#envVarFormGroup').show();
    return false;
  });
  
  $('#generateBtn').click(function() {
    $.ajax({
      type: "POST",
      url: "/generate",
      data: JSON.stringify({
        "appName" : $('#inputAppName').val().trim(),
        "memory": $('#inputMemory').val(),
        "instances": $('#inputInstances').val().trim(),
        "buildpack": $('#inputBuildpack').val().trim(),
        "host": $('#appAsHost').is(':checked') ? $('#inputAppName').val().trim() : $('#inputHost').val().trim(),
        "domain": $('#inputDomain').val().trim(),
        "path": $('#inputPath').val().trim(),
        "timeout": $('#inputTimeout').val().trim(),
        "command": $('#inputCommand').val().trim(),
        "services": services,
        "envVars": envVars,
        "noRoute": $('#route').is(':checked')}),
      contentType: "application/json"
    }).done(function(data) {
      $('#yaml').text(data);
      $('#yamlContainer').show();
    });
    return false;
  });
  
  $('#downloadBtn').click(function() {
    $('#inputServicesFinal').val(services.join());
    $('#inputEnvValFinal').val(JSON.stringify(envVars));
  })
  
  $('#appAsHost').change(function() {
    if($(this).is(':checked')) {
      $('#hostFormGroup').hide();
    } else {
      $('#hostFormGroup').show();
    }
  });
  
  if($('#appAsHost').is(':checked')) {
    $('#hostFormGroup').hide();
  } else {
    $('#hostFormGroup').show();
  }
  
  //***************************************************
  // T Y P E A H E A D
  //***************************************************
  
  $('#inputDomain').typeahead({
    hint: true,
    highlight: true,
    minLength: 1
  },
  {
    name: 'domains',
    displayKey: 'value',
    source: substringMatcher(domains)
  });
  
  
  $('#inputBuildpack').typeahead({
    hint: true,
    highlight: true,
    minLength: 1
  },
  {
    name: 'buildpacks',
    displayKey: 'value',
    source: substringMatcher(buildpacks)
  });
  
  $('#inputAppName').popover({});
  $('#inputMemory').popover({});
  $('#inputInstances').popover({});
  $('#inputBuildpack').popover({});
  $('#inputHost').popover({});
  $('#inputDomain').popover({});
  $('#inputPath').popover({});
  $('#inputTimeout').popover({});
  $('#inputCommand').popover({});
  $('#inputServiceName').popover({});
  $('#inputEnvValue').popover({});
  $('#route').popover({});
  $('#appAsHost').popover({});
});