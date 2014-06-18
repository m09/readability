/** @jsx React.DOM */
var RewritingsPane = React.createClass({
    toRows: function(tops) {
        var output = [];

        var row = null;
        output.push(<tr><td>0.0</td><td>Hai</td></tr>);
        return _.map(tops, function(top) {
            return <tr><td>{top.score}</td><td>{top.text}</td></tr>;
        });
    },
    render: function() {
        var rows = this.toRows(this.props.data.rewritings);
        return (<section id={this.props.id} className="tab-pane">
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
