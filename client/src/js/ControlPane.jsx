/** @jsx React.DOM */
var ControlPane = React.createClass({
  render: function() {
    var optionsWeight = _.map(this.props.scores, function(s) {
        return <option>{s}</option>;
    }.bind(this));
    var optionsSR = _.map(this.props.semirings, function(s) {
        return <option>{s}</option>;
    }.bind(this));
    return (
        <section className="settings">
        <div className="row">
        <div className="col-sm-3">
        Settings:
      </div>
        <div className="col-sm-3">
        Corpus:&nbsp;
        <select onChange={this.props.callbackCorpus}>
        <option value="filtered">Filtered</option>
        <option value="noisy">Noisy</option>
        </select>
        </div>
        <div className="col-sm-3">
        {(_.isEmpty(optionsWeight) || this.props.tab === 'input') ? "" : [
          "Weight: ",
            <select onChange={this.props.callbackWeight}>,
          {optionsWeight}
          </select>]}
        </div>
        <div className="col-sm-3">
        {(this.props.tab !== 'rewritings' || _.isEmpty(optionsSR)) ? "" : [
          "Semiring: ",
            <select onChange={this.props.callbackSemiring}>,
          {optionsSR}
          </select>]}
        </div>
        </div>
        </section>
    );
  }
});
