/** @jsx React.DOM */
var InputPane = React.createClass({
  render: function() {
    return (<section className={this.props.active
                                ? "tab-pane active"
                                : "tab-pane"}
                     style={{height: "300px"}}>
            <textarea id="input"
                      style={{width: "100%",
                              height: "100%"}}>
            this, and that!
            </textarea>
            </section>);
  }
});
