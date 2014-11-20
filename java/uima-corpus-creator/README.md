Corpus Creator
==============

To run, the program requires some options:

    java/uima-corpus-creator$ java -Djava.util.logging.config.file=Logger.properties -cp target/uima-corpus-creator-1.0.0-SNAPSHOT-fatjar.jar eu.crydee.readability.uima.corpuscreator.DictCreationPipeline -u postgres -d jdbc:postgresql://localhost:5432/readability -p postgres-user-password

the output is the xml file `out/res/corpus.xml`
