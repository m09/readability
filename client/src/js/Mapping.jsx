/** @jsx React.DOM */
var Mapping = React.createClass({
    // https://stackoverflow.com/questions/5560248
    color: function(min, max, value) {
        var p = (value - min) / (max - min),
            c0 = "#FF0000",
            c1 = "#00FF00",
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
        if (_.isEmpty(this.props.data)) {
            return;
        }
        var sortedSpan = _.sortBy(
            _.map(this.props.data, function(m) {
                return {
                    original: m.original,
                    revised: _.sortBy(m.revised, function(r) {
                        return -r.count;
                    })
                };
            }),
            function(r) { return -r.revised[0].score; }
        );
        var lis = [];
        var spanScore = sortedSpan[0].revised[0].score;
        _.each(sortedSpan, function(d) {
            var revs = d.revised;
            lis.push(<li role="presentation"
                     className="dropdown-header"
                     style={{
                         color: this.color(
                             this.props.minScore,
                             this.props.maxScore,
                             _.head(revs).score
                         )
                     }}>
                     {d.original.text}
                     </li>);
            _.each(_.take(revs, 10), function(r) {
                lis.push(<li role="presentation"
                         style={{
                             color: this.color(
                                 this.props.minScore,
                                 this.props.maxScore,
                                 r.score
                                 )
                         }}>
                         {r.text.length > 15
                          ? <span title={r.text}>
                          {r.text.substring(0, 13) + '…'}
                          </span>
                          : r.text}
                         <span className="badge pull-right">
                         {Math.round(r.score * 100) / 100}
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
