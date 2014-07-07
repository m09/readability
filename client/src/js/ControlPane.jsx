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
        <div className="col-sm-3">
        Settings:
      </div>
        <div className="col-sm-3">
        Corpus:&nbsp;
        <select onChange={this.props.callbackCorpus} autocomplete="off">
        <option value="filtered">Filtered</option>
        <option value="noisy">Noisy</option>
        </select>
        </div>
        <div className="col-sm-3">
        Weight:
        <select onChange={this.props.callbackWeight} autocomplete="off">
        {optionsWeight}
      </select>
        </div>
        <div className="col-sm-3">
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
