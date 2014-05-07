f.resultsN <- "processed/dict-count.tsv"
f.resultsF <- "processed/filtered-count.tsv"
f.graphics <- "processed/plot.pdf"

resN <- read.delim(f.resultsN,
                  header=T,
                  sep='\t')
resF <- read.delim(f.resultsF,
                  header=T,
                  sep='\t')

pdf(f.graphics)

plot(x=resN[,1],
     y=resN[,2],
     xlab="Number of revisions",
     ylab="Number of mappings",
     type="h",
     col="blue",
     log="xy")

points(x=resF[,1],
      y=resF[,2],
      type="h",
      col="red")

legend('topright',
      c("normal", "filtered"), 
      lty=1,
      col=c('blue', 'red'),
      bty='n',
      cex=.75)