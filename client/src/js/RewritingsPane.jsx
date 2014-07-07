/** @jsx React.DOM */
var RewritingsPane = React.createClass({
  toRows: function(tops, revs, text) {
    var output = [];
    _.map(tops, function(top) {
      var p = 0;
      var rewritten = [];
      _.each(top.revisions, function(r) {
        var rev = revs[r.revisionsId][r.revisionsIndex];
        if (r.begin > p) {
          rewritten.push(
              <span>{htmlForTextWithEmbeddedNewlines(
                text.substring(p, r.begin))}</span>);
        }
        rewritten.push(
            <span className="text-primary"
          style={{textDecoration: "underline"}}>
            {htmlForTextWithEmbeddedNewlines(rev.text)}
          </span>);
        p = r.end;
      }.bind(this));
      if (p < text.length - 1) {
        rewritten = rewritten.concat(htmlForTextWithEmbeddedNewlines(
          text.substring(p, text.length)));
      }
      output.push(<tr><td>{top.score}</td><td>{rewritten}</td></tr>);
    }.bind(this));
    return output;
  },
  render: function() {
    if (_.isEmpty(this.props.data.rewritings)) {
      return <section></section>;
    }
    var revs = this.props.data.revisions;
    var rewritings = this.props.data.rewritings[this.props.weight][this.props.monoid];
    var text = this.props.data.text;
    var rows = this.toRows(rewritings, revs, text);
    return (<section id={this.props.id} className={this.props.active
                                                   ? "tab-pane active"
                                                   : "tab-pane"}>
            <table className="table table-striped table-condensed">
            <thead>
            <tr>
            <th>Score</th>
            <th>Rewriting</th>
            </tr>
            </thead>
            <tbody>
            {rows}
            </tbody>
            </table>
            </section>);
  }
});
