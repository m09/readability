module.exports = function(grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    react: {
      combined_file_output: {
        files: {
          'build/js/widgets.js': [
            'src/js/utils.jsx',
            'src/js/Mapping.jsx',
            'src/js/ControlPane.jsx',
            'src/js/InputPane.jsx',
            'src/js/OutputPane.jsx',
            'src/js/RewritingsPane.jsx',
            'src/js/Annotator.jsx'
          ]
        }
      }
    },
    uglify: {
      dist: {
        files: { 'build/js/widgets.min.js': [ 'build/js/widgets.js' ] }
      }
    },
    cssmin: {
      combine: {
        files: {
          'build/css/<%= pkg.name %>.min.css': [
            'src/css/<%= pkg.name %>.css'
          ]
        }
      }
    }, 
    concat: {
      js: {
        src: [
          'src/js/lib/jquery-1.11.0.min.js',
          'src/js/lib/bootstrap.min.js',
          'src/js/lib/react-0.10.0.min.js',
          'src/js/lib/underscore-min.js',
          'build/js/widgets.js'],
        dest: 'dist/<%= pkg.name %>.js'
      },
      css: {
        src: [
          'src/css/lib/bootstrap.min.css',
          'src/css/lib/bootstrap-theme.min.css',
          'build/css/<%= pkg.name %>.min.css'
        ],
        dest: 'dist/<%= pkg.name %>.css'
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-react');
  grunt.loadNpmTasks('grunt-contrib-cssmin');

  grunt.registerTask('default', ['react', 'uglify', 'cssmin', 'concat']);

};
