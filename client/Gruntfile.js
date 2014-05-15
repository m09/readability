module.exports = function(grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        react: {
            combined_file_output: {
                files: {
                    'build/js/widgets.js': [
                        'src/js/Mapping.jsx',
                        'src/js/InputPane.jsx',
                        'src/js/OutputPane.jsx',
                        'src/js/Annotator.jsx'
                    ]
                }
            }
        },
        concat: {
            options: {
                separator: ';'
            },
            dist: {
                src: [
                    'src/js/lib/*.js',
                    'build/js/widgets.js'
                ],
                dest: 'dist/js/<%= pkg.name %>.js'
            }
        },
        uglify: {
            options: {
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> */\n'
            },
            dist: {
                files: {
                    'dist/js/<%= pkg.name %>.min.js': ['<%= concat.dist.dest %>']
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-react');

    grunt.registerTask('default', ['react', 'concat', 'uglify']);

};
