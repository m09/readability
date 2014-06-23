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
                        <span>{text.substring(p, r.begin)}</span>);
                }
                rewritten.push(
                    <span className="text-primary"
                          style={{textDecoration: "underline"}}>
                        {rev.text}
                    </span>);
                p = r.end;
            }.bind(this));
            if (p < text.length - 1) {
                rewritten.push(text.substring(p, text.length));
            }
            output.push(<tr><td>{top.score}</td><td>{rewritten}</td></tr>);
        }.bind(this));
        return output;
    },
    render: function() {
        var revs = this.props.data.revisions.text;
        var rewritings = this.props.data.rewritings[this.props.weight];
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
