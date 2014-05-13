/** @jsx React.DOM */
var Mapping = React.createClass({
    render: function() {
        var lis = [];
        _.each(this.props.data, function(d) {
            lis.push(<li role="presentation" className="dropdown-header">
                    {d.original.text}
                    </li>);
            var revs = _.sortBy(d.revised, function(r) { return -r.count;});
            _.each(_.take(revs, 10), function(r) {
                lis.push(<li role="presentation">
                         {r.text}
                         <span className="badge pull-right">
                         {r.count}
                         </span>
                         </li>);
            });
        });
        return (<span className="dropdown">
                <span className="dropdown-toggle" data-toggle="dropdown"
                style={{color: this.props.style,
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
