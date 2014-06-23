/** @jsx React.DOM */
var InputPane = React.createClass({
  render: function() {
    return (<section className={this.props.active
                                ? "tab-pane active"
                                : "tab-pane"}
            contentEditable="true" style={{minHeight: "200px"}}>
            this, and that!</section>);
  }
});
