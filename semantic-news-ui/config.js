System.config({
  baseURL: "/",
  defaultJSExtensions: true,
  transpiler: "babel",
  babelOptions: {
    "optional": [
      "runtime"
    ]
  },
  paths: {
    "app/*": "js/angular/*",
    "github:*": "jspm_packages/github/*",
    "npm:*": "jspm_packages/npm/*"
  },

  map: {
    "angular": "github:angular/bower-angular@1.5.2",
    "angular-animate": "github:angular/bower-angular-animate@1.5.2",
    "angular-aria": "github:angular/bower-angular-aria@1.5.2",
    "angular-local-storage": "npm:angular-local-storage@0.2.7",
    "angular-material": "github:angular/bower-material@1.0.7",
    "angular-material-icons": "npm:angular-material-icons@0.7.0",
    "angular-messages": "github:angular/bower-angular-messages@1.5.2",
    "angular-resizable": "npm:angular-resizable@1.2.0",
    "angular-ui-bootstrap": "npm:angular-ui-bootstrap@1.3.2",
    "angular-ui-router": "github:angular-ui/ui-router@0.2.18",
    "babel": "npm:babel-core@5.8.38",
    "babel-runtime": "npm:babel-runtime@5.8.38",
    "bootstrap": "github:twbs/bootstrap@3.3.6",
    "bootstrap-material": "github:fezVrasta/bootstrap-material-design@0.5.9",
    "core-js": "npm:core-js@1.2.6",
    "css": "github:systemjs/plugin-css@0.1.21",
    "d3": "npm:d3@3.5.17",
    "font-awesome": "npm:font-awesome@4.6.1",
    "jquery": "github:components/jquery@2.2.0",
    "json": "github:systemjs/plugin-json@0.1.2",
    "lodash": "npm:lodash@4.11.1",
    "text": "github:systemjs/plugin-text@0.0.7",
    "github:angular-ui/ui-router@0.2.18": {
      "angular": "github:angular/bower-angular@1.5.2"
    },
    "github:angular/bower-angular-animate@1.5.2": {
      "angular": "github:angular/bower-angular@1.5.2"
    },
    "github:angular/bower-angular-aria@1.5.2": {
      "angular": "github:angular/bower-angular@1.5.2"
    },
    "github:angular/bower-angular-messages@1.5.2": {
      "angular": "github:angular/bower-angular@1.5.2"
    },
    "github:angular/bower-material@1.0.7": {
      "angular": "github:angular/bower-angular@1.5.2",
      "angular-animate": "github:angular/bower-angular-animate@1.5.2",
      "angular-aria": "github:angular/bower-angular-aria@1.5.2",
      "css": "github:systemjs/plugin-css@0.1.21"
    },
    "github:jspm/nodelibs-assert@0.1.0": {
      "assert": "npm:assert@1.4.0"
    },
    "github:jspm/nodelibs-buffer@0.1.0": {
      "buffer": "npm:buffer@3.6.0"
    },
    "github:jspm/nodelibs-path@0.1.0": {
      "path-browserify": "npm:path-browserify@0.0.0"
    },
    "github:jspm/nodelibs-process@0.1.2": {
      "process": "npm:process@0.11.3"
    },
    "github:jspm/nodelibs-util@0.1.0": {
      "util": "npm:util@0.10.3"
    },
    "github:twbs/bootstrap@3.3.6": {
      "jquery": "npm:jquery@2.2.3"
    },
    "npm:angular-material-icons@0.7.0": {
      "angular": "npm:angular@1.5.2"
    },
    "npm:assert@1.4.0": {
      "assert": "github:jspm/nodelibs-assert@0.1.0",
      "buffer": "github:jspm/nodelibs-buffer@0.1.0",
      "buffer-shims": "npm:buffer-shims@1.0.0",
      "process": "github:jspm/nodelibs-process@0.1.2",
      "util": "npm:util@0.10.3"
    },
    "npm:babel-runtime@5.8.38": {
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:buffer-shims@1.0.0": {
      "buffer": "github:jspm/nodelibs-buffer@0.1.0"
    },
    "npm:buffer@3.6.0": {
      "base64-js": "npm:base64-js@0.0.8",
      "child_process": "github:jspm/nodelibs-child_process@0.1.0",
      "fs": "github:jspm/nodelibs-fs@0.1.2",
      "ieee754": "npm:ieee754@1.1.6",
      "isarray": "npm:isarray@1.0.0",
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:core-js@1.2.6": {
      "fs": "github:jspm/nodelibs-fs@0.1.2",
      "path": "github:jspm/nodelibs-path@0.1.0",
      "process": "github:jspm/nodelibs-process@0.1.2",
      "systemjs-json": "github:systemjs/plugin-json@0.1.2"
    },
    "npm:font-awesome@4.6.1": {
      "css": "github:systemjs/plugin-css@0.1.21"
    },
    "npm:inherits@2.0.1": {
      "util": "github:jspm/nodelibs-util@0.1.0"
    },
    "npm:lodash@4.11.1": {
      "buffer": "github:jspm/nodelibs-buffer@0.1.0",
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:path-browserify@0.0.0": {
      "process": "github:jspm/nodelibs-process@0.1.2"
    },
    "npm:process@0.11.3": {
      "assert": "github:jspm/nodelibs-assert@0.1.0"
    },
    "npm:util@0.10.3": {
      "inherits": "npm:inherits@2.0.1",
      "process": "github:jspm/nodelibs-process@0.1.2"
    }
  }
});
