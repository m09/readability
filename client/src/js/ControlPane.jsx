/** @jsx React.DOM */
var ControlPane = React.createClass({
  render: function() {
    var buttons = _.map(this.props.scores, function(s, i, l) {
        return <button className={this.props.weight === i
                           ? 'btn btn-default active'
                           : 'btn btn-default'}
      onClick={this.props.callbackWeight.bind(this, i)}>
        {l[i]}
      </button>;
    }.bind(this));
    return (
        <section className="settings">
        <div className="row">
        <div className="col-sm-2">
        Settings:
      </div>
        <div className="col-sm-4">
        Corpus:&nbsp;
        <div className="btn-group btn-group-xs">
        <button className={this.props.corpus === 'filtered'
                           ? 'btn btn-default active'
                           : 'btn btn-default'}
      onClick={this.props.callbackCorpus.bind(null, 'filtered')}>
        Filtered
      </button>
        <button className={this.props.corpus === 'noisy'
                           ? 'btn btn-default active'
                           : 'btn btn-default'}
      onClick={this.props.callbackCorpus.bind(null, 'noisy')}>
        Noisy
      </button>
        </div>
        </div>
        <div className="col-sm-6">
        {_.isEmpty(buttons) ? "" : "Weight: "}
        <div className="btn-group btn-group-xs">
        {buttons}
        </div>
        </div>
        </div>
        </section>
    );
  }
});
