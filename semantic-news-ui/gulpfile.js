var gulp = require('gulp');
var inject = require('gulp-inject');
var to5 = require('gulp-6to5');
var annotate = require('gulp-ng-annotate');

gulp.task('scripts', function () {
    var javascripts = gulp.src('./src/**/*.js')
        .pipe(to5())
        .pipe(annotate())
        .pipe(gulp.dest('./build'));
});

gulp.task('styles', function() {
    return gulp.src('./src/**/*.css')
        .pipe(gulp.dest('./build'));
});

gulp.task('templates', function() {
    return gulp.src('./src/**/*.tpl.html')
        .pipe(gulp.dest('./build'));
});

gulp.task('index', ['scripts', 'styles', 'templates'], function() {

    var target = gulp.src('./src/main/resources/static/index.html');

    var js = gulp.src([
        '../webjars/angularjs/1.5.2/angular.min.js',
        'js/angular/register.js',
        'js/angular/app.module.js',
        '**/!(app.module.js)'
        ], {read: false, cwd: './build/'});

    var css = gulp.src([
        'styles/main.css'
        ], {read: false, cwd: './build/'});

    target
        .pipe(inject(js, { addRootSlash: false }))
        .pipe(inject(css, { addRootSlash: false }))
        .pipe(gulp.dest('./build'));
});

gulp.task('watch', ['index'], function () {
    gulp.watch(['./src/**/*.js'], ['scripts']);
    gulp.watch('./src/**/*.css', ['styles']);
    gulp.watch('./src/**/*.tpl.html', ['templates']);
    gulp.watch('./src/index.html', ['index']);
});

gulp.task('default', ['index']);