/** @jsx React.DOM */
var ControlPane = React.createClass({
  render: function() {
    var optionsWeight = _.map(this.props.scores, function(s, i) {
        return <option selected={this.props.weight === i}>{s}</option>;
    }.bind(this));
    var optionsSR = _.map(this.props.monoids, function(s, i) {
        return <option selected={this.props.monoid === i}>{s}</option>;
    }.bind(this));
    return (
        <section className="settings">
        <div className="row">
        <div className="col-sm-4">
        Settings:
      </div>
        <div className="col-sm-4">
        Weight:
        <select onChange={this.props.callbackWeight} autocomplete="off">
        {optionsWeight}
      </select>
        </div>
        <div className="col-sm-4">
        Monoid:
        <select onChange={this.props.callbackMonoid} autocomplete="off">
        {optionsSR}
      </select>
        </div>
        </div>
        </section>
    );
  }
});
