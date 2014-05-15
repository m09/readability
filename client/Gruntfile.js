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
                src: ['build/js/widgets.js'],
                dest: 'build/js/<%= pkg.name %>.js'
            }
        },
        uglify: {
            dist: {
                files: {
                    'dist/js/<%= pkg.name %>.min.js': [
                        '<%= concat.dist.dest %>'
                    ]
                }
            }
        },
        cssmin: {
            combine: {
                files: {
                    'dist/css/<%= pkg.name %>.min.css': [
                        'src/css/<%= pkg.name %>.css'
                    ]
                }
            }
        } 
    });

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-react');
    grunt.loadNpmTasks('grunt-contrib-cssmin');

    grunt.registerTask('default', ['react', 'concat', 'uglify', 'cssmin']);

};
