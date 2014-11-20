Building the software
=====================

First, to use the fixed version of OpenNLP that is required by this
software, you need to build it from source:

    svn co https://svn.apache.org/repos/asf/opennlp/trunk/ opennlp
    cd opennlp/opennlp
    mvn install

Then, once this dependency is properly installed, you can build the
software with:

    mvn install -P fatjar
