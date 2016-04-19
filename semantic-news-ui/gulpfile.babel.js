var gulp = require('gulp');
var inject = require('gulp-inject');
var to5 = require('gulp-babel');
var annotate = require('gulp-ng-annotate');

var OUTPUT_DIR = './target/classes';

gulp.task('scripts', function () {
    var javascripts = gulp.src('./src/**/*.js')
        .pipe(to5())
        .pipe(annotate())
        .pipe(gulp.dest(OUTPUT_DIR));
});

gulp.task('styles', function() {
    return gulp.src('./src/**/*.css')
        .pipe(gulp.dest(OUTPUT_DIR));
});

gulp.task('templates', function() {
    return gulp.src('./src/**/*.tpl.html')
        .pipe(gulp.dest(OUTPUT_DIR));
});

gulp.task('index', ['scripts', 'styles', 'templates'], function() {

    var target = gulp.src('./src/main/resources/static/index.html');

    var js = gulp.src([
        'webjars/angularjs/1.5.2/angular.js',
        'main/resources/static/js/angular/utils/register.js',
        'main/resources/static/js/angular/services/services.module.js',
        'main/resources/static/js/angular/controllers/controllers.module.js',
        'main/resources/static/js/angular/app.module.js',
        '**/!(app.module.js)'
        ], {read: false, cwd: OUTPUT_DIR});

    var css = gulp.src([
        'main/resources/static/css/app.css'
        ], {read: false, cwd: OUTPUT_DIR});

    target
        .pipe(inject(js, { addRootSlash: false }))
        .pipe(inject(css, { addRootSlash: false }))
        .pipe(gulp.dest(OUTPUT_DIR));
});

gulp.task('watch', ['index'], function () {
    gulp.watch(['./src/**/*.js'], ['scripts']);
    gulp.watch('./src/**/*.css', ['styles']);
    gulp.watch('./src/**/*.tpl.html', ['templates']);
    gulp.watch('./src/main/resources/static/index.html', ['index']);
});

gulp.task('default', ['index']);