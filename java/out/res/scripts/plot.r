args <- commandArgs(trailingOnly=T)

f.folder <- args[1]

f.resultsN <- paste("processed/", f.folder, "/dict-count.tsv", sep="")
f.resultsF <- paste("processed/", f.folder, "/filtered-count.tsv", sep="")
f.graphics <- paste("processed/", f.folder, "/plot.pdf", sep="")

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