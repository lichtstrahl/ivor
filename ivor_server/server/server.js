/* eslint-disable max-len */
'use strict';

var loopback = require('loopback');
var boot = require('loopback-boot');
var SQL = require('mysql');
var sqlConnection = SQL.createConnection(
  {
    'host': 'localhost',
    'port': '3306',
    'user': 'root',
    'password': 'i1g9o9r7',
    'database': 'ivor_db',
  }
);

var app = module.exports = loopback();

app.start = function() {
  // start the web server
  return app.listen(function() {
    app.emit('started');
    var baseUrl = app.get('url').replace(/\/$/, '');
    console.log('Web server listening at: %s', baseUrl);
    if (app.get('loopback-component-explorer')) {
      var explorerPath = app.get('loopback-component-explorer').mountPath;
      console.log('Browse your REST API at %s%s', baseUrl, explorerPath);
    }
  });
};

app.get('/igor', function(req, res) {

  sqlConnection.connect();
  sqlConnection.query('SELECT * FROM client', function(error, results, fields) {
    if (error) throw error;
    console.log('The solution is: ', results.length);
    console.log(results);
    res.writeHead(200, {'Content-Type': 'text/html'});
    res.end();
  });
  sqlConnection.end();
});

// Bootstrap the application, configure models, datasources and middleware.
// Sub-apps like REST API are mounted via boot scripts.
boot(app, __dirname, function(err) {
  if (err) throw err;

  // start the server if `$ node server.js`
  if (require.main === module)
    app.start();
});
