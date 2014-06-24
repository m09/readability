/** @jsx React.DOM */
var Mapping = React.createClass({
  // https://stackoverflow.com/questions/5560248
  color: function(min, max, value) {
    var p = (value - min) / (max - min),
        c0 = "#000000",
        c1 = "#FF0000",
        f = parseInt(c0.slice(1), 16),
        t = parseInt(c1.slice(1), 16),
        R1 = f >> 16,
        G1 = f >> 8 & 0x00FF,
        B1 = f & 0x0000FF,
        R2 = t >> 16,
        G2 = t >> 8 & 0x00FF,
        B2 = t & 0x0000FF;
    return (
      "#" + (0x1000000 + (Math.round((R2 - R1) * p) + R1) * 0x10000
             + (Math.round((G2 - G1) * p) + G1) * 0x100
             + (Math.round((B2 - B1) * p) + B1))
        .toString(16)
        .slice(1));
  },
  render: function() {
    if (_.isEmpty(this.props.anns)) {
      return;
    }
    var scoreIndex = this.props.weight;
    var revisions = {};
    _.each(this.props.revs, function(v, k) {
      revisions[k] = _.sortBy(v, function(e) {
        return -e.score[scoreIndex];
      });
    });
    var sortedSpan = _.sortBy(this.props.anns, function(ann) {
      return -revisions[ann.revisionsId][0].score[scoreIndex];
    }.bind(this));
    var lis = [];
    var spanScore = revisions[sortedSpan[0].revisionsId][0]
          .score[scoreIndex];
    _.each(sortedSpan, function(ann) {
      var revs = revisions[ann.revisionsId];
      lis.push(<li role="presentation"
               className="dropdown-header"
               style={{
                 color: this.color(
                   this.props.minScore,
                   this.props.maxScore,
                   _.head(revs).score[scoreIndex]
                 )
               }}>
               {this.props.wholeText.substring(ann.begin, ann.end)}
               </li>);
      _.each(_.take(revs, 10), function(r) {
        lis.push(<li role="presentation"
                 style={{
                   color: this.color(
                     this.props.minScore,
                     this.props.maxScore,
                     r.score[scoreIndex]
                   )
                 }}>
                 {r.text.length > 15
                  ? <span title={r.text}>
                  {r.text.substring(0, 13) + '…'}
                  </span>
                  : r.text}
                 <span className="badge pull-right">
                 {Math.round(r.score[scoreIndex] * 100) / 100}
                 </span>
                 </li>);
                }.bind(this));
            }.bind(this));
           return (<span className="dropdown">
                   <span className="dropdown-toggle" data-toggle="dropdown"
                   style={{color: this.color(
                     this.props.minScore,
                     this.props.maxScore,
                     spanScore),
                           textDecoration: 'underline',
                           cursor: 'help'}}>
                   {this.props.text}
                   </span>
                   <ul className="dropdown-menu" role="menu">
                   {lis}
                   </ul>
                   </span>);
          }
  });
